package com.gzt.o2o.dao;

import java.util.List;

import com.gzt.o2o.entity.Area;

public interface AreaDao {
	/**
	 * 查询area表中的所有区域
	 * @return
	 */
	List<Area> queryArea();
}
