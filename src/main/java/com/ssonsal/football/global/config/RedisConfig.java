package com.ssonsal.football.global.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

    @Configuration
    @Slf4j
    public class RedisConfig {

        @Value("${spring.redis.host}")
        private String host;

        @Value("${spring.redis.port}")
        private int port;

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            log.info("[redisConnectionFactory] Lettuce를 사용하여 Redis와 연결 host : {}, port : {}", host, port);
            log.info("Lettuce Redis Client 사용 → RedisTemplate 의 메서드로 Redis 서버에 명령을 수행할 수 있음");
            return new LettuceConnectionFactory(host, port);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            log.info("redisTemplate를 받아와서 set, get, delete를 사용");
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());

            log.info("setKeySerializer, setValueSerializer 설정");
            log.info("redis-cli을 통해 직접 데이터를 조회 시 알아볼 수 없는 형태로 출력되는 것을 방지");
            // 일반적인 key:value의 경우 시리얼라이저(정렬화)
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());

            return redisTemplate;
        }
    }
