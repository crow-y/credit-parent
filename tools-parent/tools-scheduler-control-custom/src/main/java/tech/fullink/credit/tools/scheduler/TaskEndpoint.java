package tech.fullink.credit.tools.scheduler;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 自定义task端点
 *
 * @author perdonare 2017/3/24.
 * @author crow 2019/03/08.
 */
@Endpoint(id = "task")
public class TaskEndpoint {
    private final TaskRepository taskRepository;

    public TaskEndpoint(TaskRepository taskRepository) {
        Assert.notNull(taskRepository, "Repository must not be null");
        this.taskRepository = taskRepository;
    }

    @ReadOperation
    public List<TaskStatics> tasks() {
        return taskRepository.findAll();
    }
}
