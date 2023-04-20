package com.opaigc.server.infrastructure.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import com.opaigc.server.infrastructure.common.Constants;

import cn.hutool.core.util.StrUtil;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: Runner.dada
 * @date: 2020/12/12
 * @description: Redis Util, Base on Spring Boot Redis
 **/
@SuppressWarnings(value = {"unchecked"})
@Component
public class RedisUtil {

	private final RedisTemplate redisTemplate;

	@Autowired
	public RedisUtil(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public Collection<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	public Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void set(String key, String value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	public String getAndSet(String key, String value) {
		return (String) redisTemplate.opsForValue().getAndSet(key, value);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public Long delete(Collection collection) {
		return redisTemplate.delete(collection);
	}

	public <T> void setObject(String key, T value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public <T> void setObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
		redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
	}

	public <T> T getObject(String key) {
		ValueOperations<String, T> operation = redisTemplate.opsForValue();
		return operation.get(key);
	}

	public boolean expire(String key, long timeout) {
		return this.expire(key, timeout, TimeUnit.SECONDS);
	}

	public boolean expire(String key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	public Boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	public Long getExpire(String key, TimeUnit unit) {
		return redisTemplate.getExpire(key, unit);
	}

	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	public <T> Long setList(String key, List<T> dataList) {
		Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
		return Objects.isNull(count) ? 0 : count;
	}

	public <T> List<T> getList(String key) {
		return redisTemplate.opsForList().range(key, 0, -1);
	}

	public <T> Long setSet(String key, Set<T> dataSet) {
		Long count = redisTemplate.opsForSet().add(key, dataSet);
		return Objects.isNull(count) ? 0 : count;
	}

	public <T> Set<T> getSet(String key) {
		return redisTemplate.opsForSet().members(key);
	}

	public <T> void setHash(String key, Map<String, T> dataMap) {
		redisTemplate.opsForHash().putAll(key, dataMap);
	}

	public <T> Map<String, T> getHash(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public <T> void setHashValue(String key, String hashKey, T value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	public <T> T getHashValue(String key, String hashKey) {
		HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
		return opsForHash.get(key, hashKey);
	}

	public <T> List<T> getMultiHashValue(String key, Collection<Object> hashKeys) {
		return redisTemplate.opsForHash().multiGet(key, hashKeys);
	}

	public Long incrBy(String key, long increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	public Long increment(String key) {
		return redisTemplate.opsForValue().increment(key);
	}

	public void incrTimesOrLock(String key, Integer maxTimes, Integer lockTime, TimeUnit timeUnit) {
		String value = get(key);
		if (StrUtil.isBlank(value)) {
			this.set(key, "1", 24, TimeUnit.HOURS);
		} else if (!Constants.LOGIN_LOCKED_VALUE.equals(value)) {
			Integer times = Integer.valueOf(value);
			if (times > maxTimes) {
				set(key, Constants.LOGIN_LOCKED_VALUE, lockTime, timeUnit);
			} else {
				set(key, String.valueOf(times), 24, TimeUnit.HOURS);
			}
		}
	}
}
