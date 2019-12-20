package tech.fullink.credit.spring.boot.autoconfigure;

import com.google.common.base.Preconditions;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.fullink.credit.common.MixPropertySourceFactory;
import tech.fullink.credit.common.constants.ServiceConstant;
import tech.fullink.credit.tools.exception.BaseErrorHandler;
import tech.fullink.credit.tools.exception.ErrorPropertiesConverter;
import tech.fullink.credit.tools.log.CorrelationFilter;
import tech.fullink.credit.tools.log.FeignCorrelationInterceptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author crow
 */

@Configuration
@Slf4j
@PropertySource(value = {"classpath:/service-default.yml"}, factory = MixPropertySourceFactory.class)
@EnableConfigurationProperties({WebEndpointProperties.class})
public class BasicServiceAutoConfiguration implements WebMvcConfigurer {
    static {
        log.debug("SimpleAutoConfiguration init ...");
    }

    @Autowired
    private Environment env;
    private String serviceName;

    @PostConstruct
    public void postConstruct() {
        Preconditions.checkArgument(StringUtils.isEmpty(System.getProperty(ServiceConstant.SERVICE_TAG)),
                "缺少启动参数spring.profiles.active");
        Preconditions.checkArgument(StringUtils.isEmpty(System.getProperty(ServiceConstant.SERVICE_PORT)),
                "缺少启动参数server.port");

        this.serviceName = this.env.getProperty(ServiceConstant.SERVICE_NAME);

        log.info("Config Info Setup  : profile=[{}], serviceName =[{}], port = [{}], label=[{}] , configServerName=[{}]"
                , env.getProperty(ServiceConstant.SERVICE_TAG)
                , serviceName
                , env.getProperty(ServiceConstant.SERVICE_PORT)
                , env.getProperty(ServiceConstant.SERVICE_LABEL)
                , env.getProperty(ServiceConstant.SERVICE_DISCOVERY_SERVICE_ID));
    }

    /**
     * controller 通用异常处理
     *
     * @return
     */
    @Bean
    public BaseErrorHandler errorHandler() {
        log.debug("errorHandler init ...");
        return new BaseErrorHandler(new ErrorPropertiesConverter());
    }


    /**
     * http请求日志跟踪号过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean correlationFilterRegistration() {
        log.debug("correlationFilter init ...");
        FilterRegistrationBean registration = new FilterRegistrationBean(new CorrelationFilter());
        registration.addUrlPatterns("/*");
        registration.setName("correlationFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * feign client http 日志跟踪号拦截器
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        log.debug("requestInterceptor init ...");
        return new FeignCorrelationInterceptor();
    }

    @PreDestroy
    private void destroy() {
        log.info("服务[{}]关闭", serviceName);
    }
}
