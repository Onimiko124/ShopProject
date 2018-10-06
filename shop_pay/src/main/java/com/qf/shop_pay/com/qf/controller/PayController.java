package com.qf.shop_pay.com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    private IOrderService orderService;

    @RequestMapping("/alipay")
    public String alipay(String orderid, Model model) {

        // 将订单id转发到付款页面
        model.addAttribute("orderid",orderid);
        return "gopay";
    }

    @RequestMapping("/goalipay")
    public void goalipay(String orderid, HttpServletResponse httpResponse) {
        // 查询orderid对应的购物车
        System.out.println("开始跳转到支付宝的支付页面,orderid为-->" + orderid);
        Orders orders = orderService.queryByOrderid(orderid);
        System.out.println("orders-->" + orders);
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016092100559256",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNyFjwzX/VN9OJW62Kn2hGeHwZQVxVWk0/V6IV/ZQ0loEKSqb3UWaPDNSnijoV+Sb9dnly4qb/Oibwvry0PQLrWapc86uT/FOIlrwwxJLNGlaY64ZhzSpfImPW4FjThW8PN7f1T0X4Shcp88pkdux55Rz5JXI/zdP4HymZllx6crrKbNds6uFdNAml4DUbn42t1XBbWv1vDgGAr1wL4+KjADwkT0XsMAbnTYkfovwFHCEHkqj5Z2W6jcUTX9NTLZbSOq/GqF56rzhdOdbByE8RRj3KqiH2w3O5mj6luIwzlu5K6uu+LNdNnee2fq4PLVJyF9SvNCc2+c/eZPRWM58ZAgMBAAECggEAbJOc6Ou2lYYnDO2UPLBOnojuIDkZiJOazINDsSt8L7mCIGuC6asAq1PcFmGYkfM3lKsB3qUL1jCmWX736989ORZ1jLFeF/oHznYAsCgZc9BTuM/FHWiMpkYjxw4PeAybPEAjNEz8/GNg0dPMjps0BTUbtwCiOZJaXY0fwmqPb4T+1TXDrWimndm7HaymmFrV+Y6BawnXLEskXWpnPWVpdfjrmHDd6kXqjd0dfX2QtHm4ivMVQb2/liSEgqYK6tlGp2vRBx9CO0hZLA5o0DhjPT3+tLbH6ceII/dNPq+4nTHyRlnlAWH8DvI48TAZV+K7zjeH+87E3aqH+cCZTubRwQKBgQDXoRLzZGsmEfvKKTIUr0gHX8SZ5FU6dNytbzlCuVBfduvmueypOFMreDDDC3ZGy3/Wikb8JvQsLn9msAbPvvHfnGhhGAs3DriCZw7uWiG7z7cawtCkUQCVNE2Y4nl3RdUM9Z6N/TLLlOfhY5OUvPjqg3FNDVeJfsATxa1i5Lm7fQKBgQCoU95YJwp+hSSTBWdd323009j/y0q2pJGWdSh2CT5VopxAmOb/PAPDEtm2IWvTEIv2kTtFETH9HPWwBp9GX/WFTN1X3nVHcSILh+kP3y/5Q6JLuK1gLquJ2YGwm2oN/Ki5xQvVe+V40CnF6erxdAM3GjHKmWaZnckzx+BWtGsszQKBgBnBb5YK5JGDjGO1T/oQhbX7xmvrThLRapp8soY5xFPizIKeqpn71FNY49ooQFRscjSaB6iOrpOoaidF10pxMErZT85pJM3KwFczICFwOp9ITEKyCmHWGw+3U51zT7bjbHND01B6cdrHSnstfa9Ccyjv0lS1x0LxFNhHwFITp+uxAoGAR7fsLRz9QKjTy3xq7TksLTsx4tKoE6eMjFGAmsIXd/8fmfpLuYXFiJD4h7w3p6cOK1D3sta/uLtlBsnbhO2Pu2rjh/O1fmn6enU+3M9YLlkdxit6W1enKMWulyOwjSMQdI47G4t9Wvs+QSAIRTYLxKd5fXvclij/N/u6XtbiLyUCgYEAuhrS4zoLO4+IH3HnAJrBTG6aMgJ4IVX4Lx5es7dl+Zymbu2Ujgf26MtHeG9iKqiH9DksmtG9AkCHI3c+El+x3uReie6ZvmraJqKUPg0lfJ5j6wG844IdWcKizix0MR2XNUKwheghoneIrTjupElSnxB3OYMGWcNT8/hsg74Y+Xo=",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl78Q/zON/nEVJLvcGrk/bxeUpmnRzbjpTFI1QfN5wfg3QFqOJewcz6mI8ta0cATQem8kp3A5yPLIfk+vuvcc3WslFqVcJMrD+nqRgy/YYGf1jxFfOmep3yazD2jbsNC18CCoUvxcPSJXT0tYH3eSJBOeNoDHnjkACdXVBH8ejXQt9V2mWhd5u538sTxBGYvZjjCkOBKe/5sG2gGLhUeerCmHi34ADFQZa1gF67b+/5YZYYNxjJZhE3hYvYs7r0KwD0JKe/fXTHGtpK3WiOSpQIOYKHADuT3eKyyhosE12TDpMifi//kliq0YqK1cDI+wdyDavPjyVPpEpukuPCnZ4wIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");    // 设置同步响应的URL
        alipayRequest.setNotifyUrl("http://www.baidu.com");//设置异步响应的URL
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\""+ orderid +"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+ orders.getOprice() +"," +
                "    \"subject\":\""+ orders.getOrderid() +"\"," +
                "    \"body\":\""+ orders.getOrderid() +"\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=UTF-8");
        try {
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看订单是否已经支付完毕
     */
    @RequestMapping("/isok")
    public String payisok(String orderid) {
        System.out.println("开始isok方法，根据orderid和私钥查询是否已经支付成功");
        //通过订单号查询支付宝是否支付成功
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016092100559256",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCNyFjwzX/VN9OJW62Kn2hGeHwZQVxVWk0/V6IV/ZQ0loEKSqb3UWaPDNSnijoV+Sb9dnly4qb/Oibwvry0PQLrWapc86uT/FOIlrwwxJLNGlaY64ZhzSpfImPW4FjThW8PN7f1T0X4Shcp88pkdux55Rz5JXI/zdP4HymZllx6crrKbNds6uFdNAml4DUbn42t1XBbWv1vDgGAr1wL4+KjADwkT0XsMAbnTYkfovwFHCEHkqj5Z2W6jcUTX9NTLZbSOq/GqF56rzhdOdbByE8RRj3KqiH2w3O5mj6luIwzlu5K6uu+LNdNnee2fq4PLVJyF9SvNCc2+c/eZPRWM58ZAgMBAAECggEAbJOc6Ou2lYYnDO2UPLBOnojuIDkZiJOazINDsSt8L7mCIGuC6asAq1PcFmGYkfM3lKsB3qUL1jCmWX736989ORZ1jLFeF/oHznYAsCgZc9BTuM/FHWiMpkYjxw4PeAybPEAjNEz8/GNg0dPMjps0BTUbtwCiOZJaXY0fwmqPb4T+1TXDrWimndm7HaymmFrV+Y6BawnXLEskXWpnPWVpdfjrmHDd6kXqjd0dfX2QtHm4ivMVQb2/liSEgqYK6tlGp2vRBx9CO0hZLA5o0DhjPT3+tLbH6ceII/dNPq+4nTHyRlnlAWH8DvI48TAZV+K7zjeH+87E3aqH+cCZTubRwQKBgQDXoRLzZGsmEfvKKTIUr0gHX8SZ5FU6dNytbzlCuVBfduvmueypOFMreDDDC3ZGy3/Wikb8JvQsLn9msAbPvvHfnGhhGAs3DriCZw7uWiG7z7cawtCkUQCVNE2Y4nl3RdUM9Z6N/TLLlOfhY5OUvPjqg3FNDVeJfsATxa1i5Lm7fQKBgQCoU95YJwp+hSSTBWdd323009j/y0q2pJGWdSh2CT5VopxAmOb/PAPDEtm2IWvTEIv2kTtFETH9HPWwBp9GX/WFTN1X3nVHcSILh+kP3y/5Q6JLuK1gLquJ2YGwm2oN/Ki5xQvVe+V40CnF6erxdAM3GjHKmWaZnckzx+BWtGsszQKBgBnBb5YK5JGDjGO1T/oQhbX7xmvrThLRapp8soY5xFPizIKeqpn71FNY49ooQFRscjSaB6iOrpOoaidF10pxMErZT85pJM3KwFczICFwOp9ITEKyCmHWGw+3U51zT7bjbHND01B6cdrHSnstfa9Ccyjv0lS1x0LxFNhHwFITp+uxAoGAR7fsLRz9QKjTy3xq7TksLTsx4tKoE6eMjFGAmsIXd/8fmfpLuYXFiJD4h7w3p6cOK1D3sta/uLtlBsnbhO2Pu2rjh/O1fmn6enU+3M9YLlkdxit6W1enKMWulyOwjSMQdI47G4t9Wvs+QSAIRTYLxKd5fXvclij/N/u6XtbiLyUCgYEAuhrS4zoLO4+IH3HnAJrBTG6aMgJ4IVX4Lx5es7dl+Zymbu2Ujgf26MtHeG9iKqiH9DksmtG9AkCHI3c+El+x3uReie6ZvmraJqKUPg0lfJ5j6wG844IdWcKizix0MR2XNUKwheghoneIrTjupElSnxB3OYMGWcNT8/hsg74Y+Xo=",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl78Q/zON/nEVJLvcGrk/bxeUpmnRzbjpTFI1QfN5wfg3QFqOJewcz6mI8ta0cATQem8kp3A5yPLIfk+vuvcc3WslFqVcJMrD+nqRgy/YYGf1jxFfOmep3yazD2jbsNC18CCoUvxcPSJXT0tYH3eSJBOeNoDHnjkACdXVBH8ejXQt9V2mWhd5u538sTxBGYvZjjCkOBKe/5sG2gGLhUeerCmHi34ADFQZa1gF67b+/5YZYYNxjJZhE3hYvYs7r0KwD0JKe/fXTHGtpK3WiOSpQIOYKHADuT3eKyyhosE12TDpMifi//kliq0YqK1cDI+wdyDavPjyVPpEpukuPCnZ4wIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderid + "\"" +
                "  }");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            String tradeStatus = response.getTradeStatus();
            if(tradeStatus.equals("TRADE_SUCCESS")){
                //支付成功，修改订单的状态为1(已付款)
                System.out.println("交易成功");
                orderService.updateStatusByOrderid(orderid, 1);
            }
        } else {
            System.out.println("调用失败");
        }
        //如果支付成功修改订单状态

        return "redirect:http://localhost:8088/order/orderlist";
    }
}
