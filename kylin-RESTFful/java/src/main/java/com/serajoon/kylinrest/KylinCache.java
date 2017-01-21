package com.serajoon.kylinrest;

public class KylinCache extends KylinHttpBasic {
	/**
	 * 
	 * @param type
	 *            ‘METADATA’ or ‘CUBE’
	 * @param name
	 *            Cache key, e.g the cube name.
	 * @param action
	 *            ‘create’, ‘update’ or ‘drop’
	 * @return
	 */
	public static String wipeCache(String type, String name, String action) {
		String method = "POST";
		String para = "/cache/" + type + "/" + name + "/" + action;
		return excute(para, method, null);
	}
}
