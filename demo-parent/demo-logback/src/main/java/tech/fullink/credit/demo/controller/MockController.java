package tech.fullink.credit.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author crow
 */
@RestController
@RequestMapping("/mock")
@Slf4j
public class MockController {

    @GetMapping
    public void mdc() {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());
        MDC.put("CHAIN_ID", "0");
        log.info("mdc");
    }

}
