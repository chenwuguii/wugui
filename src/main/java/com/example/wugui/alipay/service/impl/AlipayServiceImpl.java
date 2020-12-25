package com.example.wugui.alipay.service.impl;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.example.wugui.alipay.config.AlipayConfig;
import com.example.wugui.alipay.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author czy
 */
@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    /**
     * 调取支付宝接口 web端支付
     */
    DefaultAlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.GATEWAYURL, AlipayConfig.APP_ID,
            AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
            AlipayConfig.SIGN_TYPE);

    /**
     * 调取支付宝接口app端支付
     */
    AlipayClient alipayClients = new DefaultAlipayClient(AlipayConfig.GATEWAYURL, AlipayConfig.APP_ID,
            AlipayConfig.RSA_PRIVATE_KEY, "json", AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
            AlipayConfig.SIGN_TYPE);

    /**
     * web端订单支付
     * <p>
     * subject：必填，商品的标题/交易标题/订单标题/订单关键字等。 不可使用特殊字符，如 /,=,& 等。
     * product_code：必填，销售产品码，与支付宝签约的产品码名称。目前电脑支付场景下仅支持 FAST_INSTANT_TRADE_PAY。
     * total_amount：必填，订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。
     *
     * @param outTradeNo  订单编号（唯一）
     * @param totalAmount 订单价格
     * @param subject     商品名称
     */
    @Override
    public String webPagePay(String outTradeNo, double totalAmount, String subject) throws Exception {
        if (StringUtils.isNotBlank(outTradeNo) && totalAmount != 0 && StringUtils.isNotBlank(subject)) {
            log.info("调用支付宝下单接口--订单编号：" + outTradeNo + " 订单金额：" + totalAmount + " 商品名称：" + subject);
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            // 支付完成后跳转的页面
            alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
            // 支付完成后异步通知的页面
            alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);
            Map<String, Object> bizContentMap = new HashMap<>(8);
            // 唯一订单编号(必填)
            bizContentMap.put("out_trade_no", outTradeNo);
            // 支付金额，以元为单位，精确到小数点后两位(必填)
            bizContentMap.put("total_amount", totalAmount);
            // 商品名称(必填)
            bizContentMap.put("subject", subject);
            bizContentMap.put("timeout_express", "90m");
            bizContentMap.put("product_code", "FAST_INSTANT_TRADE_PAY");
            alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
            // 返回form表单
            return alipayClient.pageExecute(alipayRequest).getBody();
        }
        return "";
    }

    /**
     * 退款
     * <p>
     * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，支付宝将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退回到买家账号上。
     * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款。
     * 支付宝退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。
     * 一笔退款失败后重新提交，要采用原来的退款单号。
     * 总退款金额不能超过用户实际支付金额。
     * 退款信息以退款接口同步返回或者退款查询接口 alipay.trade.fastpay.refund.query 为准。
     *
     * @param outTradeNo   订单编号  (必填)
     * @param refundReason 退款原因
     * @param refundAmount 退款金额  (必填)
     * @param outRequestNo 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     */
    @Override
    public void refund(String outTradeNo, String refundReason, double refundAmount, String outRequestNo)
            throws AlipayApiException {
        if (StringUtils.isNotBlank(outRequestNo) && refundAmount != 0) {
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            Map<String, Object> bizContentMap = new HashMap<>(8);
            bizContentMap.put("out_trade_no", outTradeNo);
            bizContentMap.put("refund_amount", refundAmount);
            bizContentMap.put("refund_reason", refundReason);
            bizContentMap.put("out_request_no", outRequestNo);
            alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
            if (response.isSuccess()) {
                log.info("退款接口调用成功，返回结果" + response.getBody());
            } else {
                log.info("退款接口调用失败，返回结果 " + response.getBody());
            }
        }
    }

    /**
     * 交易查询
     *
     * @param outTradeNo 订单编号（唯一）
     */
    @Override
    public String query(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        /** 请求接口 */
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"trade_no\":\"" + "" + "\"}");
        /** 转换格式 */
        String result = alipayClient.execute(alipayRequest).getBody();
        return result;
    }

    /**
     * 交易关闭
     *
     * @param outTradeNo 订单编号（唯一）
     */
    @Override
    public String close(String outTradeNo) throws AlipayApiException {
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"trade_no\":\"" + "" + "\"}");

        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }

    /**
     * 退款查询
     *
     * @param outTradeNo   订单编号（唯一）
     * @param outRequestNo 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     */
    @Override
    public String refundQuery(String outTradeNo, String outRequestNo) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();

        /** 请求接口 */
        alipayRequest.setBizContent(
                "{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"out_request_no\":\"" + outRequestNo + "\"}");

        /** 格式转换 */
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }

    /**
     * app端订单支付
     *
     * @param outTradeNo  订单编号
     * @param totalAmount 订单价格
     * @param subject     商品名称
     */
    @Override
    public String appPagePay(String outTradeNo, Integer totalAmount, String subject) throws Exception {
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        /** 同步通知，支付完成后，支付成功页面 */
        alipayRequest.setReturnUrl(AlipayConfig.RETURN_URL);
        /** 异步通知，支付完成后，需要进行的异步处理 */
        alipayRequest.setNotifyUrl(AlipayConfig.NOTIFY_URL);

        /** 销售产品码（固定） */
        String productCode = "QUICK_WAP_WAY";

        /** 进行赋值 */
        AlipayTradeWapPayModel wapPayModel = new AlipayTradeWapPayModel();
        wapPayModel.setOutTradeNo(outTradeNo);
        wapPayModel.setSubject(subject);
        wapPayModel.setTotalAmount(totalAmount.toString());
        wapPayModel.setBody("商品名称");
        wapPayModel.setTimeoutExpress("2m");
        wapPayModel.setProductCode(productCode);
        alipayRequest.setBizModel(wapPayModel);

        /** 格式转换 */
        String result = alipayClients.pageExecute(alipayRequest).getBody().replace('\"', '\'').replace('\n', ' ');
        return result;
    }

}
