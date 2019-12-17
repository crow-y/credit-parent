package tech.fullink.credit.tools.scheduler;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by perdonare on 2017/3/24.
 */
@Data
public class TaskConfig {
    private String jobKey;
    private String address;
    private String addressType;
    private String beanName;
    private String classFullName;
    private String method;
    private String status;
    private String errorMsg;
    private Date startTime;
    private Date endTime;
    private int timeout;
    private Map data;
}
