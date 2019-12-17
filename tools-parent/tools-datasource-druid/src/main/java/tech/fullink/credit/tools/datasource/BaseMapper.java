package tech.fullink.credit.tools.datasource;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author crow
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
