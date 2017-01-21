package com.serajoon;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.serajoon.mapper.KylinSalesMapper;
import com.serajoon.po.KylinSalesPo;

public class KylinTest {
	private static ApplicationContext applicationContext;

	@Before
	public void init() throws IOException {
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}

	@Test
	public void test1() {
		try {
			KylinSalesMapper testMapper = (KylinSalesMapper) applicationContext.getBean("kylinSalesMapper");
			List<KylinSalesPo> u = testMapper.kylinTestFind();
			System.out.println("total_selled"+"\t"+"sellers");
			for(KylinSalesPo t:u){
				System.out.println(t.getTotal_selled()+"\t"+t.getSellers());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
