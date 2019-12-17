package tech.fullink.credit.common.model.topic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author crow
 */
@Data
public abstract class AbstractTopic implements Serializable {
    private static final long serialVersionUID = 6956359503647994636L;

    /**
     * 类型
     */
    protected String type;
    /**
     * 状态
     */
    protected String status;
    /**
     * 业务流水号
     */
    protected String bizSeqNo;

    /**
     * 请求流水号
     */
    protected String requestSerial;

    /**
     * 日志跟踪号
     */
    protected String traceLogId;
}
