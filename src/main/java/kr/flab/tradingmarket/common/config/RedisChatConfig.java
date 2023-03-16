package kr.flab.tradingmarket.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisChatConfig {

    private final LettuceClientConfiguration lettuceClientConfiguration;
    @Value("${spring.redis.chat.cluster.password}")
    public String password;
    @Value("${spring.redis.chat.cluster.nodes}")
    private List<String> clusterNodes;

    @Bean(name = "redisChatTemplate")
    public StringRedisTemplate redisTemplate(
        @Qualifier("redisChatFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setEnableTransactionSupport(false);
        return stringRedisTemplate;
    }

    @Bean(name = "redisChatFactory")
    public RedisConnectionFactory redisChatFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
        redisClusterConfiguration.setPassword(password);

        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }

    @Bean
    public RedisScript<Void> createRoomScript() {
        Resource script = new ClassPathResource("scripts/createRoom.lua");
        return RedisScript.of(script);
    }
}