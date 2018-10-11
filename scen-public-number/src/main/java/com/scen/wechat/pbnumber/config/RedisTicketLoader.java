package com.scen.wechat.pbnumber.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.weixin4j.loader.ITicketLoader;
import org.weixin4j.model.js.Ticket;
import org.weixin4j.model.js.TicketType;

import java.util.concurrent.TimeUnit;

/**
 * @author Scen
 * @date 2018/10/11 19:54
 */
@Component
public class RedisTicketLoader implements ITicketLoader {
    private static final Logger LOG = LoggerFactory.getLogger(RedisTicketLoader.class);
    
    @Value("${weixin4j.config.appid}")
    private String appid;
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    public Ticket get(TicketType ticketType) {
        String key = "";
        if (null != ticketType) {
            switch (ticketType) {
                case JSAPI:
                    key = "wechat_ticket_jsapi";
                    break;
                case WX_CARD:
                    key = "wechat_ticket_wxcard";
                    break;
                default:
                    key = "wechat_ticket";
                    break;
            }
        }
        String ticket = (String) redisTemplate.opsForValue().get(key);
        LOG.info("微信 ticket:{}", ticket);
        return JSON.parseObject(ticket, Ticket.class);
    }
    
    @Override
    public void refresh(Ticket ticket) {
        String key = "";
        if (null != ticket.getTicketType()) {
            switch (ticket.getTicketType()) {
                case JSAPI:
                    key = "wechat_ticket_jsapi_" + appid;
                    break;
                case WX_CARD:
                    key = "wechat_ticket_wxcard_" + appid;
                    break;
                default:
                    key = "wechat_ticket_" + appid;
                    break;
            }
        }
        LOG.info("刷新 微信 ticket:{}", ticket.toString());
        String ticketValue = JSON.toJSONString(ticket);
        //ticket.getExpires_in() - 600L，是为了提前10分钟过期
        redisTemplate.opsForValue().set(key, ticketValue, ticket.getExpires_in() - 600L, TimeUnit.SECONDS);
    }
}
