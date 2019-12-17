package tech.fullink.credit.demo.dao.mapper;


import tech.fullink.credit.demo.dao.model.CustomerDO;
import tech.fullink.credit.tools.datasource.BaseMapper;

/**
 * @author crow
 */
public interface CustomerMapper extends BaseMapper<CustomerDO> {

    int countCustomer();

}
