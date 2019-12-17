package tech.fullink.credit.tools.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

/**
 * @author crow
 */
@Slf4j
public class ErrorPropertiesConverter {

    private static final String SPLIT_CHAR = ";";

    private static final String REPLACE_CHAR = "\\{\\}";

    private Properties properties;

    {
        this.properties = this.readProperties("/base-error.properties", false);
        Properties properties1 = this.readProperties("error.properties", true);
        if (properties1 != null) {
            this.properties.putAll(properties1);
        }
    }

    private Properties readProperties(String fileName, boolean classPath) {
        InputStream inputStream;
        if (classPath) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        } else {
            inputStream = this.getClass().getResourceAsStream(fileName);
        }

        if (inputStream == null) {
            return null;
        }

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))) {
            Properties properties = new Properties();
            properties.load(bf);
            inputStream.close();
            return properties;
        } catch (IOException e) {
            log.error("读取文件【{}】内容失败, ", fileName);
        }
        return null;
    }

    /**
     * 获取错误信息
     *
     * @param key
     * @return
     */
    public String getErrorMsg(String key) {
        if (Objects.nonNull(properties) && !StringUtils.isEmpty(key) && properties.containsKey(key)) {
            return properties.getProperty(key);
        }

        return null;
    }

    public BaseResponse errResponse(String key, String... args) {

        String content = this.getErrorMsg(key);

        if (StringUtils.isEmpty(content)) {
            log.warn("未找到对应的error key配置");
            return this.missMappingError();
        }

        String[] contentArr = content.split(SPLIT_CHAR, -1);
        String internalErrorCode = contentArr[0];
        String errorMessage = contentArr.length >= 1 ? contentArr[1] : "";
        int httpCode = contentArr.length >= 2 ? Integer.valueOf(contentArr[2]) : HttpStatus.INTERNAL_SERVER_ERROR.value();

        return BaseResponse.builder()
                .httpStatus(httpCode)
                .errorMessage(this.getErrorMsg(errorMessage, args))
                .errorCode(internalErrorCode)
                .build();
    }


    /**
     * 没有配置error.properties文件对应错误信息
     *
     * @return
     */
    public BaseResponse missMappingError() {
        return BaseResponse.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage("未找到对应的error key配置")
                .errorCode("100001")
                .build();
    }


    /**
     * 错误信息占位符替换
     *
     * @param errorMessage
     * @param args
     * @return
     */
    public String getErrorMsg(String errorMessage, String... args) {
        if (args == null) {
            errorMessage = errorMessage.replaceFirst(REPLACE_CHAR, "");
        }
        for (String arg : args) {
            if (arg != null) {
                errorMessage = errorMessage.replaceFirst(REPLACE_CHAR, arg);
            }
        }
        return errorMessage;
    }

}
