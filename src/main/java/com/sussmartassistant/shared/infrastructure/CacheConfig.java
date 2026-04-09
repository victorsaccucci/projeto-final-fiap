package com.sussmartassistant.shared.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * Configuração de cache Redis com TTLs diferenciados por cache name.
 * Ativa apenas quando spring.cache.type=redis (produção).
 * No profile dev, spring.cache.type=simple é usado automaticamente.
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class CacheConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    public static final String CACHE_PACIENTE = "paciente";
    public static final String CACHE_ALERGIAS = "alergias";
    public static final String CACHE_MEDICAMENTOS = "medicamentos";
    public static final String CACHE_PRONTUARIO = "prontuario";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var jsonSerializer = RedisSerializer.json();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = Map.of(
                CACHE_PACIENTE, defaultConfig.entryTtl(Duration.ofMinutes(30)),
                CACHE_ALERGIAS, defaultConfig.entryTtl(Duration.ofMinutes(15)),
                CACHE_MEDICAMENTOS, defaultConfig.entryTtl(Duration.ofMinutes(15)),
                CACHE_PRONTUARIO, defaultConfig.entryTtl(Duration.ofMinutes(10))
        );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig.entryTtl(Duration.ofMinutes(15)))
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }

    /**
     * Fallback: quando Redis está indisponível, loga o erro e continua sem cache.
     */
    static class RedisCacheErrorHandler extends SimpleCacheErrorHandler {

        private static final Logger log = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

        @Override
        public void handleCacheGetError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
            log.warn("Redis cache GET falhou para cache '{}', key '{}': {}", cache.getName(), key, exception.getMessage());
        }

        @Override
        public void handleCachePutError(RuntimeException exception, org.springframework.cache.Cache cache, Object key, Object value) {
            log.warn("Redis cache PUT falhou para cache '{}', key '{}': {}", cache.getName(), key, exception.getMessage());
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, org.springframework.cache.Cache cache, Object key) {
            log.warn("Redis cache EVICT falhou para cache '{}', key '{}': {}", cache.getName(), key, exception.getMessage());
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, org.springframework.cache.Cache cache) {
            log.warn("Redis cache CLEAR falhou para cache '{}': {}", cache.getName(), exception.getMessage());
        }
    }
}
