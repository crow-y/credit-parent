package tech.fullink.credit.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

/**
 * @author crow
 */
@Slf4j
public class JacksonUtil {

    private JacksonUtil() {}

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 反序列化时碰到没有的属性选择跳过
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * object -> json string
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("jackson序列化错误", e);
            throw new RuntimeException("jackson序列化错误");
        }
    }


    /**
     * json string -> object
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("jackson反序列化错误", e);
            throw new RuntimeException("jackson反序列化错误");
        }
    }

    /**
     * json string -> object
     *
     * @param json
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("jackson反序列化错误", e);
            throw new RuntimeException("jackson反序列化错误");
        }
    }


    /**
     * inputStream -> object
     *
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(InputStream inputStream, Class<T> clazz) {
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("jackson反序列化错误", e);
            throw new RuntimeException("jackson反序列化错误");
        }
    }

}
