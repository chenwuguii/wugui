package com.example.wugui.alipay.service.impl;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
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
    public String webPagePay(String outTradeNo, double totalAmount, String subject) throws AlipayApiException {
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
            log.info("调用支付宝统一收单下单并支付页面接口，发送参数：" + bizContentMap);
            alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
            // 返回form表单
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            log.info("调用支付宝统一收单下单并支付页面接口，返回结果：" + form);
            return form;
        }
        return "";
    }

    @Override
    public void refund(String outTradeNo, double refundAmount, String outRequestNo)
            throws AlipayApiException {
        if (StringUtils.isNotBlank(outTradeNo) && refundAmount != 0) {
            AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
            Map<String, Object> bizContentMap = new HashMap<>(8);
            bizContentMap.put("out_trade_no", outTradeNo);
            bizContentMap.put("refund_amount", refundAmount);
            bizContentMap.put("out_request_no", outRequestNo);
            alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
            log.info("调用支付宝退款 alipay.trade.refund 接口，发送参数：" + bizContentMap);
            AlipayTradeRefundResponse response = alipayClient.execute(alipayRequest);
            if (response.isSuccess()) {
                log.info("调用支付宝统一收单交易退款接口成功，返回结果：" + response.getBody());
            } else {
                log.info("调用支付宝统一收单交易退款接口失败，返回结果：" + response.getBody());
            }
        }
    }

    @Override
    public void payQuery(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        Map<String, Object> bizContentMap = new HashMap<>(8);
        bizContentMap.put("out_trade_no", outTradeNo);
        alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
        log.info("调用支付宝统一收单线下交易查询，发送参数：" + bizContentMap);
        AlipayTradeQueryResponse response = alipayClient.execute(alipayRequest);
        if (response.isSuccess()) {
            log.info("调用支付宝统一收单线下交易查询接口成功，返回结果：" + response.getBody());
        } else {
            log.info("调用支付宝统一收单线下交易查询接口失败，返回结果：" + response.getBody());
        }
    }

    @Override
    public void refundQuery(String outTradeNo, String outRequestNo) throws AlipayApiException {
        if (StringUtils.isNotBlank(outTradeNo) && StringUtils.isNotBlank(outRequestNo)) {
            AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
            Map<String, Object> bizContentMap = new HashMap<>(8);
            bizContentMap.put("out_trade_no", outTradeNo);
            bizContentMap.put("out_request_no", outRequestNo);
            alipayRequest.setBizContent(JSON.toJSONString(bizContentMap));
            log.info("调用支付宝统一收单交易退款查询接口，发送参数：" + bizContentMap);
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(alipayRequest);
            if (response.isSuccess()) {
                log.info("调用支付宝统一收单交易退款查询接口成功，返回结果：" + response.getBody());
            } else {
                log.info("调用支付宝统一收单交易退款查询接口失败，返回结果：" + response.getBody());
            }
        }
    }

    /**
     * 交易关闭
     *
     * @param outTradeNo 订单编号（唯一）
     */
    @Override
    public void close(String outTradeNo) throws AlipayApiException {
        AlipayTradeCloseRequest alipayRequest = new AlipayTradeCloseRequest();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\"," + "\"trade_no\":\"" + "" + "\"}");

        String result = alipayClient.execute(alipayRequest).getBody();

    }

    /**
     * app端订单支付
     *
     * @param outTradeNo  订单编号
     * @param totalAmount 订单价格
     * @param subject     商品名称
     */
    @Override
    public String appPagePay(String outTradeNo, Integer totalAmount, String subject) throws AlipayApiException {
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
