package tech.fullink.credit.demo.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@NameStyle(Style.camelhumpAndLowercase)
public class UserDO {

    private Long id;

    private String name;

    private String idMd5;

    private Date createdTime;

    private Date updatedTime;

}
