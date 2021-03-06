/**
 * OrcFormat.java
 * com.hainiuxy.mr.run.util
 * Copyright (c) 2019, 海牛版权所有.
 * @author   潘牛                      
*/

package utils;

/**
 * 
 * @author   潘牛                      
 * @Date	 2019年6月5日 	 
 */
public class OrcFormat {

	public static String SCHEMA = "struct<aid:string,pkgname:string,uptime:bigint,type:int,country:string,gpcategory:string>";
	public static String COUNTRY = "country";
	public static String OUT_SCHEMA = "struct<code:string,name:string>";
}

