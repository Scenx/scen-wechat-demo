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
import org.weixin4j.model.message.template.TemplateData;
import org.weixin4j.model.qrcode.QrcodeType;
import org.weixin4j.spring.WeixinTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Value("${wx.templateid}")
    private String templateId;
    
    
    /**
     * 获取普通access_token
     *
     * @return 普通access_token
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
     * @return 微信服务器ip地址列表
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
     * @return 用户基本信息
     */
    @RequestMapping("/userInfo")
    public ScenResult userInfo(String code, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(code)) {
                response.sendRedirect(weixinTemplate.sns().getOAuth2CodeUserInfoUrl("http://" + oauthUrl + "/userInfo"));
                return null;
            } else {
                Map<String, Object> map = new HashMap<>(2);
                map.put("snsUserInfo", weixinTemplate.sns().getSnsUserByCode(code));
                map.put("userInfo", weixinTemplate.user().info(openId));
                return ScenResult.ok(map);
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
     * @return 成功或失败信息
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
     * @return 成功或失败信息
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
    
    
    /**
     * 发送模板消息
     *
     * @return 成功或失败信息
     */
    @RequestMapping("/sendTemplateMessage")
    public ScenResult sendTemplateMessage() {
        try {
            TemplateData templateData = new TemplateData();
            templateData.setKey("这是key");
            templateData.setValue("这是value");
            templateData.setColor("red");
            List<TemplateData> templateDataList = new ArrayList<>();
            templateDataList.add(templateData);
            weixinTemplate.message().sendTemplateMessage(openId, templateId, templateDataList);
            return ScenResult.ok();
        } catch (WeixinException e) {
            logger.error("发送模板消息失败templateId：" + templateId);
            e.printStackTrace();
            return ScenResult.build(500, "发送模板消息失败templateId：" + templateId);
        }
    }
    
    /**
     * 设置用户备注名
     *
     * @return 成功或失败信息
     */
    @RequestMapping("/updateRemark")
    public ScenResult updateRemark() {
        try {
            weixinTemplate.user().updateRemark(openId, "华子");
            return ScenResult.ok();
        } catch (WeixinException e) {
            logger.error("设置用户备注名失败openid:" + openId);
            e.printStackTrace();
            return ScenResult.build(500, "设置用户备注名失败openid:" + openId);
        }
    }
    
    /**
     * 获取所有用户列表
     *
     * @return 用户列表
     */
    @RequestMapping("/getAll")
    public ScenResult getAll() {
        try {
            return ScenResult.ok(weixinTemplate.user().getAll());
        } catch (WeixinException e) {
            logger.error("获取所有用户列表失败");
            e.printStackTrace();
            return ScenResult.build(500, "获取所有用户列表失败");
        }
    }
    
    /**
     * 创建二维码
     *
     * @return 图片（二维码地址）
     */
    @RequestMapping("/createQrcode")
    public ScenResult createQrcode() {
        try {
            return ScenResult.ok(
                    weixinTemplate.
                            qrcode().
                            showQrcode(
                                    weixinTemplate.qrcode().create(
                                            QrcodeType.QR_SCENE, 1, 1800
                                    ).getTicket()
                            )
            );
        } catch (WeixinException e) {
            logger.error("创建二维码失败");
            e.printStackTrace();
            return ScenResult.build(500, "创建二维码失败");
        }
    }
}
