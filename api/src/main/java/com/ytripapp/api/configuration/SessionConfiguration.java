package com.ytripapp.api.configuration;

import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@Profile("session")
@EnableRedisHttpSession
@Import({
    SessionAutoConfiguration.class,
    RedisAutoConfiguration.class,
    RedisHttpSessionConfiguration.class
})
@Configuration
public class SessionConfiguration {
}
