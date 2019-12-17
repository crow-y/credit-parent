package tech.fullink.credit.demo.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author crow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "flow_customer")
@NameStyle(Style.camelhumpAndLowercase)
public class CustomerDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "Mysql")
    private Long id;

    private String md5;

    private String userName;

    private String userId;

    private String appId;

    private String status;

    private Date bindExpired;

    private Date creditExpired;

    private String reason;

    private String contractNo;

    private String orgCode;

    private Date createdAt;

    private Date updatedAt;


    public enum CustomerStatusEnum {
        /** 初始 */
        INIT,
        /** 已注册 */
        REGISTED,
        /** 禁止借款 */
        BIND,
    }
}
