package com.github.resouce.utils.utils;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;

/**
 * @date 2017年6月1日 下午2:12:55
 */
public class RedisCache {
    private final static Logger             logger = LoggerFactory.getLogger(RedisCache.class);

    private String                          prefix;
    private StringRedisTemplate             redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public RedisCache(String prefix, StringRedisTemplate redisTemplate) {
        super();
        this.prefix = prefix;
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public boolean set(String key, Object obj, int seconds) {
        try {
            String objectJson = JSON.toJSONString(obj);
            valueOperations.set(getRedisKey(key), objectJson, seconds, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            logger.error("缓存异常", e);
            return false;
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            String value = valueOperations.get(getRedisKey(key));
            return JSON.parseObject(value, clazz);
        } catch (Exception e) {
            logger.error("缓存异常", e);
            return null;
        }
    }

    public boolean remove(String key) {
        try {
            redisTemplate.delete(getRedisKey(key));
            return true;
        } catch (Exception e) {
            logger.error("缓存异常", e);
            return false;
        }
    }

    public String getRedisKey(String key) {
        if (StringUtils.isNotBlank(prefix)) {
            return this.prefix + key;
        } else {
            return key;
        }
    }
}
