package com.key.win.test.rocketmq;

import com.key.win.MqCenter;
import com.key.win.message.dao.BusinssDataMapper;
import com.key.win.message.model.BusinessData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * @SpringBootTest 会加载整个spring容器
 * BusinssDataMapper 需要运行在spring容器环境中
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { MqCenter.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 配置启动类
public class BusinssDataMapperTest {

	@Autowired
	private BusinssDataMapper businssDataMapper ;
	
	
	@Test
	public void testSave() {
		businssDataMapper.save(BusinessData.builder().id(UUID.randomUUID().toString()).username("gitgeek").sex("1").build());
	}
}
