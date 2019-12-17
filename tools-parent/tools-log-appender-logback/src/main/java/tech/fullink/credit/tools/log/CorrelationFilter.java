package tech.fullink.credit.tools.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import tech.fullink.credit.common.constants.Correlation;
import tech.fullink.credit.common.utils.CommonUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * http请求线程跟踪号过滤器
 *
 * @author crow
 */
@Slf4j
public class CorrelationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String correlationId = httpServletRequest.getHeader(Correlation.CORRELATION_ID_HEADER);
            String seqNo = httpServletRequest.getHeader(Correlation.CHAIN_ID_HEADER);
            if (!this.isAsyncDispatcher(httpServletRequest)) {
                if (StringUtils.isEmpty(correlationId)) {
                    correlationId = CommonUtil.getUUID();
                    log.debug("No correlationId found in header. Generated : " + correlationId);
                } else {
                    log.debug("Found correlationId in header : " + correlationId);
                }

                if (!StringUtils.isEmpty(seqNo) && CommonUtil.isDigits(seqNo)) {
                    log.debug("Found chain id in header : " + seqNo);
                    seqNo = Integer.valueOf(seqNo) + 1 + "";
                } else {
                    seqNo = "0";
                    log.debug("No chain id found in header. Generated : " + seqNo);
                }
            }
            MDC.put(Correlation.CORRELATION_ID_MDC_KEY, correlationId);
            MDC.put(Correlation.CHAIN_ID_MDC_KEY, seqNo);
            filterChain.doFilter(httpServletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * 判断是否是异步请求
     * @param httpServletRequest
     * @return
     */
    private boolean isAsyncDispatcher(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
    }
}
