package com.iteedu.ssoboot.modules.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 
	 * 指定缓存失效时间
	 * 
	 * @param key
	 *            键
	 * 
	 * @param time
	 *            时间(秒)
	 * 
	 * @return
	 * 
	 */

	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * 根据key 获取过期时间
	 * 
	 * @param key
	 *            键 不能为null
	 * 
	 * @return 时间(秒) 返回0代表为永久有效
	 * 
	 */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);

	}

	/**
	 * 
	 * 判断key是否存在
	 * 
	 * @param key
	 *            键
	 * 
	 * @return true 存在 false不存在
	 * 
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 保存对象到Redis 对象不过期
	 *
	 * @param key
	 *            待缓存的key
	 * @param object
	 *            待缓存的对象
	 * @return 返回是否缓存成功
	 */
	public boolean setObject(String key, Object object) throws Exception {
		return setObject(key, object, -1);
	}

	/**
	 * 保存对象到Redis 并设置超时时间
	 *
	 * @param key
	 *            缓存key
	 * @param object
	 *            缓存对象
	 * @param timeout
	 *            超时时间
	 * @return 返回是否缓存成功
	 * @throws Exception
	 *             异常上抛
	 */
	public boolean setObject(String key, Object object, int timeout) throws Exception {
		 String value = SerializeUtil.serialize(object);
		try {
			// 为-1时不设置超时时间
			if (timeout > 0) {
				setString(key, value, timeout);
			} else {
				setString(key, value);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 从Redis中获取对象
	 *
	 * @param key
	 *            待获取数据的key
	 * @return 返回key对应的对象
	 */
	public Object getObject(String key) throws Exception {
		Object object = null;
        try {
            String serializeObj = getString(key);
            if (null == serializeObj || serializeObj.length() == 0) {
                object = null;
            } else {
                object = SerializeUtil.deserialize(serializeObj);
            }
        }  catch (Exception e) {
            throw e;
        }
        return object;
	}

	/**
	 * 缓存String类型的数据,数据不过期
	 *
	 * @param key
	 *            待缓存数据的key
	 * @param value
	 *            需要缓存的额数据
	 * @return 返回是否缓存成功
	 */
	public boolean setString(String key, String value) throws Exception {
		return setString(key, value, -1);
	}

	/**
	 * 缓存String类型的数据并设置超时时间
	 *
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            超时时间
	 * @return 返回是否缓存成功
	 */
	public boolean setString(String key, String value, int timeout) throws Exception {
		try {
			stringRedisTemplate.opsForValue().set(key, value);
			if (timeout != -1) {
				stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取String类型的数据
	 *
	 * @param key
	 *            需要获取数据的key
	 * @return 返回key对应的数据
	 */
	public String getString(String key) throws Exception {
		try {
			return key == null ? null : stringRedisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除缓存中的数据
	 *
	 * @param key
	 *            需要删除数据的key
	 * @return 返回是否删除成功
	 */
	public boolean del(String key) {
		try {
			return redisTemplate.delete(key);
		} catch (Exception e) {
			throw e;
		}
	}

}
