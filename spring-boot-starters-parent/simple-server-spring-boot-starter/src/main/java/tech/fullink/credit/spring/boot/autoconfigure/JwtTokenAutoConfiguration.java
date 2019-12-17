package tech.fullink.credit.spring.boot.autoconfigure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.fullink.credit.common.plugins.BaseRedisPlugin;
import tech.fullink.credit.common.plugins.LettuceRedisPlugin;
import tech.fullink.credit.tools.security.JwtInterceptor;
import tech.fullink.credit.tools.security.JwtService;

/**
 * @author crow
 */
@Configuration
@ConditionalOnProperty(prefix = "common.authorization", value = "enabled",havingValue = "true")
@ConfigurationProperties(prefix = "common.authorization")
@Slf4j
public class JwtTokenAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean(name = "stringRedisTemplate")
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtService jwtService() {
        return new JwtService();
    }


    @Bean
    @ConditionalOnMissingBean
    public BaseRedisPlugin baseRedisPlugin(RedisTemplate<String, String> stringRedisTemplate) {
        return new LettuceRedisPlugin(stringRedisTemplate);
    }


    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加用户token拦截器，拦截所有请求
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(jwtInterceptor());
        interceptorRegistration.addPathPatterns("/**");
    }
}
