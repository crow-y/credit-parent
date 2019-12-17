package tech.fullink.credit.spring.boot.autoconfigure;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tech.fullink.credit.common.constants.ServiceConstant;

import java.util.Collection;
import java.util.List;

/**
 * @author crow
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(value = ServiceConstant.SWAGGER_ENABLED, havingValue = "true")
@AutoConfigureAfter(SimpleServiceAutoConfiguration.class)
public class ServiceSwaggerAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private Environment env;

    /**
     * @return API 命名规范 必须包含服务名
     */
    @Bean
    public Docket swaggerSpringMvcPlugin(TypeResolver typeResolver) {
        String serviceName = env.getProperty(ServiceConstant.SERVICE_NAME);
        Contact contact = new Contact("Apache Licence 2.0", "http://www.fullink.tech", "");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(serviceName)
                .description(serviceName)
                .termsOfServiceUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .contact(contact)
                .version("0.1")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(
                                typeResolver.resolve(Collection.class, WildcardType.class),
                                typeResolver.resolve(List.class, WildcardType.class))
                )
                .forCodeGeneration(false);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 增加swagger-ui支持
        // 增加 /webjars/* 映射到 classpath:/META-INF/resources/webjars/
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
