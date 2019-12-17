package tech.fullink.credit.tools.common;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import tech.fullink.credit.common.constants.Correlation;

import java.util.UUID;

/**
 * @author crow
 */
@Slf4j
public class CorrelationTool {

    /**
     * 加入一个跟踪号
     *
     * @param correlationId
     */
    public static void mdcPut(String correlationId) {
        MDC.put(Correlation.CORRELATION_ID_MDC_KEY, correlationId);
        MDC.put(Correlation.CHAIN_ID_MDC_KEY, "0");
    }

    /**
     * 获取当前线程跟踪号
     *
     * @return
     */
    public static String mdcGet() {
        return MDC.get(Correlation.CORRELATION_ID_MDC_KEY);
    }

    /**
     * 产生新的跟踪号
     */
    public static void mdcPutAlwaysNew() {
        MDC.put(Correlation.CORRELATION_ID_MDC_KEY, UUID.randomUUID().toString().replace("-", ""));
        MDC.put(Correlation.CHAIN_ID_HEADER, "0");
    }

}
