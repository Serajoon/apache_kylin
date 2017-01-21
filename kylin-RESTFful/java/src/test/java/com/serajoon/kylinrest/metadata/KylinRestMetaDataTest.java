package com.serajoon.kylinrest.metadata;

import org.junit.Before;
import org.junit.Test;

import com.serajoon.kylinrest.KylinRestMetaData;

public class KylinRestMetaDataTest {
	@Before
	public void setUp() throws Exception {
		KylinRestMetaData.login("ADMIN", "KYLIN");
	}

	@Test
	public void getHiveTable() {
		String hiveTable = KylinRestMetaData.getHiveTable("fact_fwwb");
		System.out.println(hiveTable);
	}

	@Test
	public void getHiveTableInfo() {
		String hiveTable = KylinRestMetaData.getHiveTableInfo("fact_fwwb");
		System.out.println(hiveTable);
	}

	@Test
	public void getHiveTables() {
		String hiveTable = KylinRestMetaData.getHiveTables("increament",true);
		System.out.println(hiveTable);
	}
	@Test
	public void loadHiveTables() {
		String hiveTable = KylinRestMetaData.loadHiveTables("increament.dim_zone,increament.fact_fwwb","loadtest");
		System.out.println(hiveTable);
	}
}
