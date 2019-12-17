package tech.fullink.credit.demo.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.fullink.credit.common.exceptions.BaseErrorKeys;
import tech.fullink.credit.common.exceptions.ServerException;
import tech.fullink.credit.demo.feign.ExceptionClient;

/**
 * @author crow
 */
@Api("异常测试")
@RestController
@RequestMapping("/exception")
@Slf4j
public class ExceptionController {
    @Autowired
    private ExceptionClient exceptionClient;

    @GetMapping
    public void exception() {
        throw new RuntimeException("系统错误");
    }

    @GetMapping("/illegalArgument")
    public void illegalArgument() {
        throw ServerException.fromKey(BaseErrorKeys.ILLEGAL_ARGUMENT, "地址错误");
    }

    @GetMapping("/feignException")
    public void feignException() {
        exceptionClient.illegalArgument();
    }

    @GetMapping("/readTimeOut")
    public void readTimeOut() throws InterruptedException {
        Thread.sleep(70 * 1000L);
    }

    @GetMapping("/feignTimeoutException")
    public void feignTimeoutException() {
        exceptionClient.readTimeOut();
    }





}
