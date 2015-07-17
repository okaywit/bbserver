package com.bbcow.api;


public class Weather {
	/**
	 * @return 返回结果
	 */
	public static String request(String cityName) {
		return ApiConnector.request("http://apis.baidu.com/apistore/weatherservice/cityname", "cityname="+cityName);
	}
	
}
