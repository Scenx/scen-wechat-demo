package com.scen.wechat.pbnumber.controller;

import com.scen.wechat.pbnumber.vo.ScenResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;
import org.weixin4j.spring.WeixinTemplate;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Scen
 * @date 2018/10/10 11:00
 */
@RestController
public class TestController {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private WeixinTemplate weixinTemplate;
    
    @Value("${weixin4j.config.oauthUrl}")
    private String oauthUrl;
    
    @Value("${my.openid}")
    private String openId;
    
    
    /**
     * 获取普通access_token
     *
     * @return
     */
    @RequestMapping("/getAccessToken")
    public ScenResult getAccessToken() {
        try {
            String accessToken = weixinTemplate.getToken().getAccess_token();
            int expiresIn = weixinTemplate.getToken().getExpires_in();
            logger.info("当前getAccess_token：" + accessToken + "|时间：" + expiresIn);
            return ScenResult.ok(accessToken);
        } catch (WeixinException e) {
            e.printStackTrace();
            logger.error("获取当前getAccess_token出错");
            return ScenResult.build(500, "当前getAccess_token出错");
        }
    }
    
    
    /**
     * 获取微信服务器ip地址
     *
     * @return
     */
    @RequestMapping("/getcallbackip")
    public ScenResult getcallbackip() {
        try {
            return ScenResult.ok(weixinTemplate.base().getCallbackIp());
        } catch (WeixinException e) {
            logger.error("获取微信服务器ip地址出错");
            e.printStackTrace();
            return ScenResult.build(500, "获取微信服务器ip地址出错");
        }
    }
    
    
    /**
     * 获取用户基本信息
     *
     * @return
     */
    @RequestMapping("/userInfo")
    public ScenResult userInfo(String code, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(code)) {
                response.sendRedirect(weixinTemplate.sns().getOAuth2CodeUserInfoUrl("http://" + oauthUrl + "/userInfo"));
                return null;
            } else {
                return ScenResult.ok(weixinTemplate.sns().getSnsUserByCode(code));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取用户基本信息失败");
            return ScenResult.build(500, "获取用户基本信息失败");
        }
    }
    
    
    /**
     * 客服发送消息
     *
     * @return
     */
    @RequestMapping("/customSendContent")
    public ScenResult customSendContent() {
        try {
            weixinTemplate.message().customSendContent(openId, "你好我是客服");
            return ScenResult.ok();
        } catch (WeixinException e) {
            e.printStackTrace();
            logger.error("客服发送消息失败opendi:" + openId);
            return ScenResult.build(500, "客服发送消息失败opendi:" + openId);
        }
    }
    
    /**
     * 客服群发消息
     *
     * @return
     */
    @RequestMapping("/massSendContent")
    public ScenResult massSendContent() {
        try {
//            群发需要至少两个人关注公众号
//            单人异常信息
//            {"errcode":40130,"errmsg":"invalid openid list size, at least two openid hint: [o5IfUa0803shb1]"}
//            weixinTemplate.message().massSendContent();
            return ScenResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("客服群发消息失败");
            return ScenResult.build(500, "客服群发消息失败");
        }
    }
}
