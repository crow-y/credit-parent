package tech.fullink.credit.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author crow
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "tech.fullink.credit.demo.dao.mapper")
public class MybatisConfig {
}