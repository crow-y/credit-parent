package tech.fullink.credit.tools.exception;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import tech.fullink.credit.common.exceptions.BaseErrorKeys;
import tech.fullink.credit.common.exceptions.ExceptionResult;
import tech.fullink.credit.common.constants.Correlation;
import tech.fullink.credit.common.exceptions.ServerException;
import tech.fullink.credit.common.utils.ExceptionUtil;
import tech.fullink.credit.common.utils.JacksonUtil;
import tech.fullink.credit.common.utils.NetworkUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @author crow
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseErrorHandler {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    private ErrorPropertiesConverter errorConvert;

    public BaseErrorHandler(ErrorPropertiesConverter errorConvert) {
        this.errorConvert = errorConvert;
    }

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public BaseResponse handleServerException(final ServerException e) {
        log.warn("{}, <uri>: {}, {}\r\n{}", e.getMessage(), request.getRequestURI(), e.toString(), ExceptionUtil.serverExceptionTrack(e));
        return this.errorResponse(e.getErrorKey(), e.getArgs());
    }


    @ExceptionHandler(FeignException.class)
    @ResponseBody
    public BaseResponse handleFeignException(final FeignException e) {
        if (null == e.content()) {
            log.error("发生未知异常, <uri>: {}", request.getRequestURI(), e);
            return this.errorResponse(BaseErrorKeys.INTERNAL_SERVER_ERROR);
        }

        String content = e.contentUTF8();
        try {
            BaseResponse baseResponse = JacksonUtil.jsonToObject(content, BaseResponse.class);
            response.setStatus(baseResponse.getHttpStatus());
            log.warn("微服务method调用异常, <uri>: {}, {}\n\r{}", request.getRequestURI(), baseResponse.getErrorMessage(), ExceptionUtil.feignExceptionTrack(e));
            return baseResponse;
        } catch (Exception e1) {
            log.error("发生未知异常, <uri>: {}", request.getRequestURI(), e);
            return this.errorResponse(BaseErrorKeys.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 运行时异常, 需要打印完整堆栈信息, 并且按照配置输出
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse handleRuntimeException(final RuntimeException e) {
        log.error("发生未知异常, <uri>: {}", request.getRequestURI(), e);
        return this.errorResponse(BaseErrorKeys.INTERNAL_SERVER_ERROR);
    }


    /**
     * 请求超时
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({TimeoutException.class, SocketTimeoutException.class, RetryableException.class})
    public BaseResponse socketTimeoutException(Exception e) {
        log.error("请求超时, <uri>:{}", request.getRequestURI(), e);
        return this.errorResponse(BaseErrorKeys.SOCKET_TIMEOUT);
    }


    /**
     * 请求不存在的接口
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public BaseResponse noHandlerFoundException(NoHandlerFoundException e) {
        log.warn("[{}]请求了不存在的接口: {}", NetworkUtil.getIpAddress(request), e.getRequestURL());
        return this.errorResponse(BaseErrorKeys.NOT_FOUND);
    }


    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public BaseResponse handleRuntimeException(final Throwable e) {
        log.error("发生未知异常, <uri>: {}", request.getRequestURI(), e);
        return this.errorResponse(BaseErrorKeys.INTERNAL_SERVER_ERROR);
    }


    /**
     * 异常返回组装
     *
     * @param key
     * @param args
     * @return
     */
    private BaseResponse errorResponse(String key, String... args) {
        BaseResponse baseResponse = errorConvert.errResponse(key, args);
        baseResponse.setResult(ExceptionResult.builder()
                .correlationId(MDC.get(Correlation.CORRELATION_ID_MDC_KEY))
                .timestamp(new Date())
                .build());
        response.setStatus(baseResponse.getHttpStatus());
        return baseResponse;
    }


}
