package com.scen.wechat.pbnumber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.weixin4j.WeixinException;
import org.weixin4j.spi.IMessageHandler;
import org.weixin4j.util.TokenUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信开发者接入
 *
 * @author Scen
 * @date 2018/10/10 13:26
 */
@Controller
@RequestMapping("/weixin/jieru")
public class Scen2WeixinController {
    @Autowired
    private IMessageHandler messageHandler;
    
    @Value("${wx.token}")
    private String token;
    
    /**
     * 开发者接入验证
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息来源可靠性验证
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //1.验证消息真实性
        //http://mp.weixin.qq.com/wiki/index.php?title=验证消息真实性
        //成为开发者验证
        String echostr = request.getParameter("echostr");
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            response.getWriter().write(echostr);
        }
    }
    
    //接收微信消息
    @RequestMapping(method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //消息来源可靠性验证
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //确认此次GET请求来自微信服务器，原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
        if (!TokenUtil.checkSignature(token, signature, timestamp, nonce)) {
            //消息不可靠，直接返回
            response.getWriter().write("");
            return;
        }
        //用户每次向公众号发送消息、或者产生自定义菜单点击事件时，响应URL将得到推送
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
            //获取POST流
            ServletInputStream in = request.getInputStream();
            //处理输入消息，返回结果
            String xml = messageHandler.invoke(in);
            //返回结果
            response.getWriter().write(xml);
        } catch (IOException e) {
            response.getWriter().write("");
        } catch (WeixinException ex) {
            response.getWriter().write("");
        }
    }
}
