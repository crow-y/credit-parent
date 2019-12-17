package tech.fullink.credit.tools.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import tech.fullink.credit.common.utils.JacksonUtil;

/**
 * @author crow
 */
@Slf4j
public class FeignMessageModem {

    /**
     * 获取异常错误信息, 通用异常解析器(BaseErrorHandler)抛出的错误内容
     *
     * @param e
     * @return
     */
    public static String getMessage(FeignException e) {
        if (null == e || null == e.content()) {
            return "";
        }

        String content = e.contentUTF8();
        try {
            BaseResponse baseResponse = JacksonUtil.jsonToObject(content, BaseResponse.class);
            return baseResponse.getErrorMessage();
        } catch (Exception e1) {
            // 忽略
        }
        return "";
    }

}
