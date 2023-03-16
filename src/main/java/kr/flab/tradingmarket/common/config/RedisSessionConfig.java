package kr.flab.tradingmarket.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
    @Value("${spring.redis.session.host}")
    public String host;

    @Value("${spring.redis.session.port}")
    public int port;

    @Value("${spring.redis.session.password}")
    public String password;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        @Qualifier("redisSessionFactory") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisSessionTemplate = new RedisTemplate<>();
        redisSessionTemplate.setKeySerializer(new StringRedisSerializer());
        redisSessionTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisSessionTemplate.setConnectionFactory(connectionFactory);
        return redisSessionTemplate;
    }

    @Bean
    @SpringSessionRedisConnectionFactory
    public RedisConnectionFactory redisSessionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}