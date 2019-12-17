package tech.fullink.credit.common.constants;

/**
 * @author crow
 */
public class Correlation {

    private Correlation() {
    }

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CHAIN_ID_HEADER = "X-Chain-Id";
    public static final String CORRELATION_ID_MDC_KEY = "CORRELATION_ID";
    public static final String CHAIN_ID_MDC_KEY = "CHAIN_ID";
    public static final String SERVICE_GRAY_TAG = "-gray";
    public static final String MDC_CALL_FROM = "call-from";
}
