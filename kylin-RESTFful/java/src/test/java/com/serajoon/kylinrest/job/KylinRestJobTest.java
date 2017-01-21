package com.serajoon.kylinrest.job;

import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.serajoon.kylinrest.KylinRestJob;

public class KylinRestJobTest {
	@Before
	public void setUp() throws Exception {
		KylinRestJob.login("ADMIN", "KYLIN");
	}
	
	@Test
	public void resumeJob(){
		String resumeJob = KylinRestJob.resumeJob("bbd2cfaa-2189-408b-89b5-3f1405eb5912");
		System.out.println(resumeJob);
	}
	
	@Test
	public void discardJob(){
		String resumeJob = KylinRestJob.discardJob("bbd2cfaa-2189-408b-89b5-3f1405eb5912");
		System.out.println(resumeJob);
	}
	
	@Test
	public void getJobStatus(){
		String resumeJob = KylinRestJob.getJobStatus("ca8fe12d-c771-4639-a815-911bc01cd632");
		JSONObject parseObject = JSON.parseObject(resumeJob);
		String status = (String)parseObject.get("job_status");
		System.out.println(status);
	}
	
	@Test
	public void getJobStepOutput(){
		String jobStepOutput = KylinRestJob.getJobStepOutput("7dce9af8-708b-4a45-a67b-3a9064ee1b99","7dce9af8-708b-4a45-a67b-3a9064ee1b99-1");
		System.out.println(jobStepOutput);
	}
}
