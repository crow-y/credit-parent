package tech.fullink.credit.common.plugins;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author crow
 */
public interface BaseRedisPlugin {

    /**
     * 设置key-value, 几秒后过期
     * @param key
     * @param value
     * @param expireInSecond
     */
    default void setEx(final String key, final String value, final int expireInSecond) {
    }

    default String get(final String key) {
        return "";
    }

    default Long del(final String key) {
        return 0L;
    }

    default void sAdd(String key, String... value) {
    }

    default void zAdd(String key, String member, Double score) {
    }

    default Long zScore(String key, String member) {
        return 0L;
    }

    default Long zRemRangeByScore(String key, Long min, Long max) {
        return 0L;
    }

    default Set<String> zRangeByScore(String key, Long min, Long max) {
        return Collections.emptySet();
    }

    default Long zRem(String key, String... members) {
        return 0L;
    }

    default Set<String> sMembers(String key) {
        return Collections.emptySet();
    }

    default Long sRem(String key, String... members) {
        return 0L;
    }

    default Boolean sIsMember(String key, String value) {
        return false;
    }


    //===================== Redis Hash  =====================

    default void hmSet(String key, Map<String, String> map) {

    }

    default String hGet(String key, String field) {
        return "";
    }

    default Long hDel(String key, String... fields) {
        return 0L;
    }

}
