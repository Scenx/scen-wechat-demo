package com.scen.wechat.pbnumber.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.weixin4j.model.message.OutputMessage;
import org.weixin4j.model.message.normal.*;
import org.weixin4j.model.message.output.TextOutputMessage;
import org.weixin4j.spi.INormalMessageHandler;

/**
 * 自定义普通消息处理器
 *
 * @author Scen
 */
@Component
public class AtsNormalMessageHandler implements INormalMessageHandler {
    
    protected final Logger LOG = LoggerFactory.getLogger(AtsNormalMessageHandler.class);
    
    @Override
    public OutputMessage textTypeMsg(TextInputMessage msg) {
        LOG.debug("文本消息：" + msg.getContent());
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("您发的消息是：" + msg.getContent());
        return out;
    }
    
    @Override
    public OutputMessage imageTypeMsg(ImageInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("图片消息已收到！");
        return out;
    }
    
    @Override
    public OutputMessage voiceTypeMsg(VoiceInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("您发送的语音消息是！" + msg.getRecognition());
        return out;
    }
    
    @Override
    public OutputMessage videoTypeMsg(VideoInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("视频消息已收到！");
        return out;
    }
    
    @Override
    public OutputMessage shortvideoTypeMsg(ShortVideoInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("短视频消息已收到！");
        return out;
    }
    
    @Override
    public OutputMessage locationTypeMsg(LocationInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("地理位置已收到");
        return out;
    }
    
    @Override
    public OutputMessage linkTypeMsg(LinkInputMessage msg) {
        TextOutputMessage out = new TextOutputMessage();
        out.setContent("链接已经收到！");
        return out;
    }
    
}
