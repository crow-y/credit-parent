package tech.fullink.credit.tools.log;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.fullink.credit.common.constants.Correlation;
import tech.fullink.credit.common.utils.CommonUtil;

import java.util.Collection;
import java.util.Map;

/**
 * feign client http 日志跟踪号拦截器
 *
 * @author crow
 */
public class FeignCorrelationInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Map<String, Collection<String>> headers = requestTemplate.request().headers();

        //add "CORRELATION_ID"
        if (CollectionUtils.isEmpty(headers.get(Correlation.CORRELATION_ID_HEADER))) {
            String correlationId = MDC.get(Correlation.CORRELATION_ID_MDC_KEY);
            if (StringUtils.isEmpty(correlationId)) {
                correlationId = CommonUtil.getUUID();
            }
            requestTemplate.header(Correlation.CORRELATION_ID_HEADER, correlationId);
        }
        //add CHAIN_ID
        if (CollectionUtils.isEmpty(headers.get(Correlation.CHAIN_ID_HEADER))) {
            String chainID = MDC.get(Correlation.CHAIN_ID_MDC_KEY);
            if (StringUtils.isEmpty(chainID)) {
                chainID = "0";
            }
            requestTemplate.header(Correlation.CHAIN_ID_HEADER, chainID);
        }
    }
}
