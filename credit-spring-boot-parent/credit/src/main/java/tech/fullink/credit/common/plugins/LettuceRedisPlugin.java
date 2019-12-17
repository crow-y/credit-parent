package tech.fullink.credit.common.plugins;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author crow
 */
@Slf4j
public class LettuceRedisPlugin implements BaseRedisPlugin {
    private RedisTemplate<String, String> redisTemplate;


    public LettuceRedisPlugin(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setEx(String key, String value, int expireInSecond) {
        redisTemplate.opsForValue().set(key, value, expireInSecond, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long del(String key) {
        return redisTemplate.delete(key) ? 1L : 0L;
    }

    @Override
    public void sAdd(String key, String... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    @Override
    public void zAdd(String key, String member, Double score) {
        redisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Long zScore(String key, String member) {
        Double score = redisTemplate.opsForZSet().score(key, member);
        return null == score ? null : score.longValue();
    }

    @Override
    public Long zRemRangeByScore(String key, Long min, Long max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Set<String> zRangeByScore(String key, Long min, Long max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Long zRem(String key, String... members) {
        return redisTemplate.opsForZSet().remove(key, members);
    }

    @Override
    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Long sRem(String key, String... members) {
        return redisTemplate.opsForSet().remove(key, members);
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public void hmSet(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public String hGet(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public Long hDel(String key, String... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }
}
