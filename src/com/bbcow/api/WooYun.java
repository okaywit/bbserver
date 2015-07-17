package com.bbcow.api;

public class WooYun {
	/**
	 * @return 返回结果
	 */
	public static String request() {
		return ApiConnector.request(" http://apis.baidu.com/apistore/wooyun/confirm", "limit=10");
	}
	
	public static void main(String[] args) {
		System.out.println(WooYun.request());
	}

}
