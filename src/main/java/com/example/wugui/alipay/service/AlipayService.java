package com.example.wugui.alipay.service;

import com.alipay.api.AlipayApiException;

/**
 * 支付宝支付通用接口
 * 官方文档地址：https://opendocs.alipay.com/apis/api_1
 *
 * @author czy
 */
public interface AlipayService {

    /**
     * web端订单支付
     * <p>
     * PC场景下单并支付
     *
     * @param outTradeNo  订单编号（唯一）
     * @param totalAmount 订单价格
     * @param subject     商品名称
     * @return 返回form表单
     * @throws AlipayApiException 支付宝接口异常
     */
    String webPagePay(String outTradeNo, double totalAmount, String subject) throws AlipayApiException;

    /**
     * app端订单支付
     *
     * @param outTradeNo  订单编号
     * @param totalAmount 订单价格
     * @param subject     商品名称
     * @throws AlipayApiException 支付宝接口异常
     */
    String appPagePay(String outTradeNo, Integer totalAmount, String subject) throws AlipayApiException;

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
     * @param outTradeNo   订单支付时传入的商户订单号
     * @param refundAmount 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @param outRequestNo 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     * @throws AlipayApiException 支付宝接口异常
     */
    void refund(String outTradeNo, double refundAmount, String outRequestNo) throws AlipayApiException;


    /**
     * 交易查询
     * <p>
     * 该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
     * 需要调用查询接口的情况： 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
     * 调用支付接口后，返回系统错误或未知交易状态情况；
     * 调用alipay.trade.pay，返回INPROCESS的状态；
     * 调用alipay.trade.cancel之前，需确认支付状态；
     *
     * @param outTradeNo 订单编号（唯一）
     * @throws AlipayApiException 支付宝接口异常
     */
    void payQuery(String outTradeNo) throws AlipayApiException;

    /**
     * 退款查询
     * <p>
     * 商户可使用该接口查询自已通过alipay.trade.refund或alipay.trade.refund.apply提交的退款请求是否执行成功。
     * 该接口的返回码10000，仅代表本次查询操作成功，不代表退款成功。
     * 如果该接口返回了查询数据，且refund_status为空或为REFUND_SUCCESS，则代表退款成功。
     * 如果没有查询到则代表未退款成功，可以调用退款接口进行重试，重试时请务必保证退款请求号一致。
     *
     * @param outTradeNo   订单支付时传入的商户订单号
     * @param outRequestNo 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
     * @throws AlipayApiException 支付宝接口异常
     */
    void refundQuery(String outTradeNo, String outRequestNo) throws AlipayApiException;

    /**
     * 交易关闭
     *
     * @param outTradeNo 订单编号（唯一）
     * @throws AlipayApiException 支付宝接口异常
     */
    void close(String outTradeNo) throws AlipayApiException;


}

