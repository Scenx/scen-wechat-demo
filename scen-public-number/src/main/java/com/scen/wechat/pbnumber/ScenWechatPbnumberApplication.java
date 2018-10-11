package com.scen.wechat.pbnumber;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author renzhenhua
 */
@SpringBootApplication
@EnableApolloConfig
public class ScenWechatPbnumberApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ScenWechatPbnumberApplication.class, args);
    }
}
