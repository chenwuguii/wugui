package com.example.wugui.alipay.controller;

import com.example.wugui.alipay.config.AlipayConfig;
import com.example.wugui.alipay.service.AlipayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 支付宝支付配置类
 *
 * @author czy
 */
@RestController()
@RequestMapping("/alipay")
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    @GetMapping("/webPagePay")
    public void webPagePay(HttpServletResponse response) {
        // 模拟从数据库查询商品
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        double totalAmount = 100.00;
        String subject = "商品名称";
        try {
            String form = alipayService.webPagePay(outTradeNo, totalAmount, subject);
            if (StringUtils.isNotBlank(form)) {
                // 渲染form表单到页面
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().write(form);
                response.getWriter().flush();
                response.getWriter().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/refund")
    public void refund() {
        try {
            alipayService.refund("58f885c3aca2417fb378b5fffbc87e66", 100.00, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/payQuery")
    public void payQuery() {
        try {
            alipayService.payQuery("c0482e1ee7fd45b49fc5d4183f138087");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/refundQuery")
    public void refundQuery() {
        try {
            alipayService.refundQuery("c0482e1ee7fd45b49fc5d4183f138087","2020122822001453880503160577");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
