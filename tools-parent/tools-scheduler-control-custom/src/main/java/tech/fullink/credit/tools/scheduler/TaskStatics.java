package tech.fullink.credit.tools.scheduler;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Created by perdonare on 2017/3/24.
 */
@Data@NoArgsConstructor
public class TaskStatics {
    private String key;
    private String status;
    private String failReason;
    private String condition;
    private Date startTime;
    private Date endTime;
    private long timeout;
    private Map extra;

    public TaskStatics(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskStatics)) return false;

        TaskStatics that = (TaskStatics) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }
}
