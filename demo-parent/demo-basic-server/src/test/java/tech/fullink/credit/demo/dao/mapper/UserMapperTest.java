package tech.fullink.credit.demo.dao.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.fullink.credit.demo.AbstractTest;
import tech.fullink.credit.demo.dao.model.UserDO;

import java.util.Date;

@Slf4j
public class UserMapperTest extends AbstractTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSave() {
        UserDO user = new UserDO();
        user.setName("俞晨波");
        user.setIdMd5("5A5DD8117D26EE7F39C887B53AA33D2A");
        for (int i = 0; i < 10; i++) {
            Date date = new Date();
            user.setCreatedTime(date);
            user.setUpdatedTime(date);
            userMapper.insert(user);
        }
    }

}
