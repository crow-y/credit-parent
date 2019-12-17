package tech.fullink.credit.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author crow
 */
@Slf4j
public class MDCTest extends AbstractTest{

    @Test
    public void testMDC() {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString().replace("-", ""));
        MDC.put("CHAIN_ID", "0");
        log.info("123");
    }

}
