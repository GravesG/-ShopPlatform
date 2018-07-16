package com.gzt.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzt.o2o.BaseTest;
import com.gzt.o2o.entity.Area;

public class AreaTest extends BaseTest {
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		List<Area> lists = areaDao.queryArea();
		assertEquals(2,lists.size());
	}
}
