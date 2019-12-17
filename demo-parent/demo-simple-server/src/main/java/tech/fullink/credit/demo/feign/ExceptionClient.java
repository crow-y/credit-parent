package tech.fullink.credit.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author crow
 */
@FeignClient(value = "exceptionClient", url = "http://localhost:7001/exception")
public interface ExceptionClient {

    /**
     * 参数错误
     */
    @GetMapping("/illegalArgument")
    void illegalArgument();


    /**
     * 参数错误
     */
    @GetMapping("/readTimeOut")
    void readTimeOut();

}
