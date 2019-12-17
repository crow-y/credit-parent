package tech.fullink.credit.tools.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import tech.fullink.credit.common.utils.DateUtil;

import java.util.*;

/**
 * 本地任务执行记录
 *
 * @author perdonare 2017/3/24
 */
@Slf4j
public class TaskRepository {
    private int capacity = 100;
    private final List<TaskStatics> taskStatics = new LinkedList<>();
    private static final String SUCCESS_FLAG = "success";
    private static final String FAIL_FLAG = "fail";
    private boolean reverse = true;

    public void setCapacity(int capacity) {
        synchronized (this.taskStatics) {
            this.capacity = capacity;
        }
    }

    public List<TaskStatics> findAll() {
        synchronized (this.taskStatics) {
            if (!CollectionUtils.isEmpty(this.taskStatics)) {
                Iterator<TaskStatics> itr = this.taskStatics.iterator();
                while (itr.hasNext()) {
                    TaskStatics taskStatic = itr.next();
                    if (isTimeout(taskStatic.getStartTime(), taskStatic.getEndTime(), taskStatic.getTimeout())) {
                        taskStatic.setStatus(FAIL_FLAG);
                        taskStatic.setFailReason("task execute timeout");
                        if (taskStatic.getStatus().equalsIgnoreCase(SUCCESS_FLAG) || taskStatic.getStatus().equalsIgnoreCase(FAIL_FLAG)) {
                            itr.remove();
                        }
                    }
                }
            }
            return Collections.unmodifiableList(new ArrayList(this.taskStatics));
        }
    }


    public void add(TaskStatics taskStatics) {
        log.debug("add taskStatic:{}", taskStatics);
        synchronized (this.taskStatics) {
            //remove timeout job
            if (!CollectionUtils.isEmpty(this.taskStatics)) {
                Iterator<TaskStatics> itr = this.taskStatics.iterator();
                while (itr.hasNext()) {
                    TaskStatics taskStatic = itr.next();
                    if (isTimeout(taskStatic.getStartTime(), taskStatic.getEndTime(), taskStatic.getTimeout())) {
                        itr.remove();
                    }
                }
            }
            while (this.taskStatics.size() >= this.capacity) {
                this.taskStatics.remove(this.reverse ? this.capacity - 1 : 0);
            }

            if (this.reverse) {
                this.taskStatics.add(0, taskStatics);
            } else {
                this.taskStatics.add(taskStatics);
            }

        }
    }

    private boolean isTimeout(Date startDate, Date endDate, long timeout) {
        endDate = endDate == null ? DateUtil.getCurrentDate() : endDate;
        long timeMilli = endDate.getTime() - startDate.getTime();
        return timeMilli > 0 && timeMilli >= timeout;
    }

    public void update(TaskStatics taskStatic) {
        if (this.taskStatics.contains(taskStatic)) {
            log.debug("update taskStatic:{}", taskStatics);
            int index = taskStatics.indexOf(taskStatic);
            taskStatic.setEndTime(new Date());
            taskStatics.add(index, taskStatic);
        }
    }
}
