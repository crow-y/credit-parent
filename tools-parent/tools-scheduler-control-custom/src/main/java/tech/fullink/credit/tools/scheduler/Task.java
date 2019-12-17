package tech.fullink.credit.tools.scheduler;

import java.util.Map;

/**
 * Created by perdonare on 2017/3/24.
 */
public interface Task {
    default TaskStatics notifyTask() {
        return null;
    }
    void run(Map map) throws Exception;
}
