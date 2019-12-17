package tech.fullink.credit.common.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author crow
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResult {
    /**
     * 日志追踪号
     */
    private String correlationId;

    /**
     * 所属系统
     */
    private String belongSystem;

    /**
     * 时间戳
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss,SSS")
    private Date timestamp;

    private String code;

    private String msg;
}
