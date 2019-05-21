package com.iteedu.ssoboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.iteedu.ssoboot.modules.sys.dao.MenuDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

	@Autowired
	MenuDao menuDao;
	@Test
	public void contextLoads() {
		int i=menuDao.queryList(null).size();
		System.out.println(i);
	}

}
