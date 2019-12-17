package tech.fullink.credit.tools.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import tech.fullink.credit.common.exceptions.ExceptionResult;
import tech.fullink.credit.common.constants.Correlation;
import tech.fullink.credit.common.utils.DateUtil;
import tech.fullink.credit.common.utils.JacksonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * 任务调度执行类
 *
 * @author perdonareon 2017/3/24.
 * @author crow
 */
@Slf4j
public class SchedulerManager {
    private final BeanFactory beanFactory;
    private final TaskRepository taskRepository;
    private static final long DEFAULT_TIMEOUT = 60 * 60 * 1000;

    /**
     * 目标访问端点
     */
    private String path;

    public SchedulerManager(BeanFactory beanFactory, TaskRepository taskRepository, String path) {
        this.beanFactory = beanFactory;
        this.taskRepository = taskRepository;
        this.path = path;
    }

    public boolean include(String requestPath) {
        return path.equalsIgnoreCase(requestPath);
    }

    /**
     * 执行任务
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void schedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TaskConfig taskConfig = null;
        if (null != request.getInputStream()) {
            try {
                taskConfig = JacksonUtil.jsonToObject(request.getInputStream(), TaskConfig.class);
            } catch (Exception e) {
                log.error("", e);
            }
        }

        if (taskConfig != null && taskConfig.getBeanName() != null) {
            Task bean;
            try {
                bean = beanFactory.getBean(taskConfig.getBeanName(), Task.class);
            } catch (BeansException e) {
                sendError(response, HttpStatus.NOT_FOUND.value(), new ExceptionResult(MDC.get(Correlation.CORRELATION_ID_MDC_KEY), null, DateUtil.getCurrentDate(), "999999", "bean not found"));
                return;
            }
            TaskStatics taskStatics = new TaskStatics();
            taskStatics.setKey(taskConfig.getJobKey());
            taskStatics.setStartTime(new Date());
            long timeout = taskConfig.getTimeout() == 0 ? DEFAULT_TIMEOUT : taskConfig.getTimeout();
            taskStatics.setTimeout(timeout);
            taskStatics.setStatus(TaskExecStatus.INIT.name());
            taskRepository.add(taskStatics);
            final Map data = taskConfig.getData();
            //做异步
            new Thread(() -> {
                try {
                    bean.run(data);
                    taskStatics.setStatus(TaskExecStatus.SUCCESS.name());
                    taskRepository.update(taskStatics);
                } catch (Exception e) {
                    log.error("定时任务执行异常:{}", e);
                    String msg = e.getMessage() == null ? "task execute exception" : e.getMessage();
                    taskStatics.setFailReason(msg);
                    taskStatics.setStatus(TaskExecStatus.FAIL.name());
                    taskRepository.update(taskStatics);
                }
            }).start();

        }
    }


    /**
     * 返回错误信息
     *
     * @param response
     * @param httpStatus
     * @param exceptionResult
     * @throws IOException
     */
    private void sendError(HttpServletResponse response, int httpStatus, ExceptionResult exceptionResult) throws IOException {
        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.setStatus(httpStatus);
        String errorStr = JacksonUtil.objectToJson(exceptionResult);
        response.getOutputStream().write(errorStr.getBytes());
    }

}
