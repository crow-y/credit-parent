package tech.fullink.credit.spring.boot.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.fullink.credit.tools.scheduler.SchedulerFilter;
import tech.fullink.credit.tools.scheduler.SchedulerManager;
import tech.fullink.credit.tools.scheduler.TaskEndpoint;
import tech.fullink.credit.tools.scheduler.TaskRepository;


/**
 * 任务调度自动装配
 *
 * @author crow
 */
@Configuration
@ConditionalOnProperty(prefix = "common.schedule", value = "enabled",havingValue = "true")
@ConfigurationProperties(prefix = "common.schedule")
@Slf4j
public class SchedulerAutoConfiguration {

    static {
        log.info("SchedulerAutoConfiguration init ...");
    }

    private static final String DEFAULT_SCHEDULE_PATH = "/schedule";
    private static final String DEFAULT_TASK_PATH = "/task";
    private String path;

    private BeanFactory beanFactory;

    public SchedulerAutoConfiguration(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Bean
    public TaskRepository taskRepository() {
        return new TaskRepository();
    }

    @Bean
    public SchedulerManager schedulerManager() {
        path = path == null ? DEFAULT_SCHEDULE_PATH : path;
        SchedulerManager schedulerManager = new SchedulerManager(beanFactory, taskRepository(), path);
        return schedulerManager;
    }

    @Bean
    public SchedulerFilter schedulerFilter() {
        SchedulerFilter schedulerFilter = new SchedulerFilter(schedulerManager());
        return schedulerFilter;
    }

    @Bean
    public TaskEndpoint taskEndpoint() {
        return new TaskEndpoint(taskRepository());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
