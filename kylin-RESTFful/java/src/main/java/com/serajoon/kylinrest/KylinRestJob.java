package com.serajoon.kylinrest;

public class KylinRestJob extends KylinHttpBasic {
	/**
	 * 重新开始job
	 * @param job ID
	 * @return
	 */
	public static String resumeJob(String jobId) {

		String method = "PUT";
		String para = "/jobs/" + jobId + "/resume";
		return excute(para, method, null);

	}

	/**
	 * 放弃job
	 * @param jobId
	 * @return
	 */
	public static String discardJob(String jobId) {

		String method = "PUT";
		String para = "/jobs/" + jobId + "/cancel";
		return excute(para, method, null);

	}
	
	/**
	 * 获得job的状态
	 * @param jobId
	 * @return
	 */
	public static String getJobStatus(String jobId) {

		String method = "GET";
		String para = "/jobs/" + jobId;
		return excute(para, method, null);

	}
	
	

	/**
	 * 
	 * @param jobId  7dce9af8-708b-4a45-a67b-3a9064ee1b99
	 * @param stepId jobId-sequence 7dce9af8-708b-4a45-a67b-3a9064ee1b99-3
	 * @return
	 */
	public static String getJobStepOutput(String jobId, String stepId) {
		String method = "GET";
		String para = "/jobs/" + jobId + "/steps/" + stepId + "/output";
		return excute(para, method, null);
	}
}
