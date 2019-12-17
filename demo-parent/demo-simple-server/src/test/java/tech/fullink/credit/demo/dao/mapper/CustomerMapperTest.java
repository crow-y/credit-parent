package tech.fullink.credit.demo.dao.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.fullink.credit.demo.dao.mapper.tech.fullink.credit.demo.AbstractTest;
import tech.fullink.credit.demo.dao.model.CustomerDO;

/**
 * @author crow
 */
@Slf4j
public class CustomerMapperTest extends AbstractTest {
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    public void testGetByMd5() {
        String md5 = "ABCDEFG";
        CustomerDO example = CustomerDO.builder().md5(md5).build();
        CustomerDO customer = customerMapper.selectOne(example);

        log.info("{}", customer);
        Assert.assertNull(customer);
    }

}
