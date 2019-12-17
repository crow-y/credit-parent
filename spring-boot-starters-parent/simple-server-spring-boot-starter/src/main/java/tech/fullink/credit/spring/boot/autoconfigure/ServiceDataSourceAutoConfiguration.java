package tech.fullink.credit.spring.boot.autoconfigure;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import tech.fullink.credit.common.MixPropertySourceFactory;

/**
 * @author crow
 */
@Configuration
@ConditionalOnProperty("spring.datasource.url")
@PropertySource(value = {"classpath:/datasource-default.yml"}, factory = MixPropertySourceFactory.class)
public class ServiceDataSourceAutoConfiguration {

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        // 监控配置
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 白名单
        servletRegistrationBean.addInitParameter("allow", "183.129.187.86");
        // 黑名单
        servletRegistrationBean.addInitParameter("deny", "");
        // 登陆的账号信息
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        // 是否可以重置数据
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 不需要忽略的格式信息
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }


}
