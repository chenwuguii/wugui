package com.example.wugui.alipay.controller;

import com.example.wugui.alipay.config.AlipayConfig;
import com.example.wugui.alipay.service.AlipayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 支付宝支付配置类
 *
 * @author czy
 */
@Controller
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    @GetMapping("/webPagePay")
    public void webPagePay(HttpServletResponse response) {
        // 模拟从数据库查询商品
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        double totalAmount = 1.58;
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
    public void refund(HttpServletResponse response) {
        // 模拟从数据库查询商品
        String outTradeNo = UUID.randomUUID().toString().replace("-", "");
        double totalAmount = 1.58;
        try {
            String form = alipayService.refund("1afab9e03d7840abb15299b6c9c47c69", "客户不想要了", 1.00, "1");
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

}
