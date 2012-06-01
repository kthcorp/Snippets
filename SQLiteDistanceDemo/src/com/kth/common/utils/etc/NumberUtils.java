/**
 * KTH Developed by Java
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with KTH.
 */

package com.kth.common.utils.etc;

/**
 * String을 Integer 또는 Long 형으루 형변환한다.
 * 
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 9. 7.
 * @version 1.0.0
 * @see
 * @linkplain JDK 6.0
 */
public class NumberUtils {

	public static int toInt(String str) {
		return toInt(str, 0);
	}

	public static int toInt(String str, int defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int toInt(Object obj) {
		return toInt(obj, 0);
	}

	public static int toInt(Object obj, int defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return (Integer) obj;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long toLong(String str) {
		return toLong(str, 0L);
	}

	public static long toLong(String str, long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long toLong(Object obj) {
		return toLong(obj, 0L);
	}

	public static long toLong(Object obj, long defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		try {
			return (Long) obj;
		} catch (Exception nfe) {
			return defaultValue;
		}
	}

	public static float toFloat(String str) {
		return toFloat(str, 0f);
	}

	public static float toFloat(String str, float defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 랜덤 int 구하기
	 * @param min 최소값
	 * @param max 최대값
	 * @return
	 */
	public static int getRandomNumber(int $min, int $max)
	{
	    return $min + (int) (Math.random() * ($max - $min));
	}
	 
	/**
	 * 랜덤 float 구하기
	 * @param min 최소값
	 * @param max 최대값
	 * @return
	 */
	public static float getRandomNumber(float $min, float $max)
	{
	    return (float) ($min + (Math.random() * ($max - $min)));
	}
	
	/**
	 * 랜덤 double 구하기
	 * @param min 최소값
	 * @param max 최대값
	 * @return
	 */
	public static double getRandomNumber(double $min, double $max)
	{
	    return (double) ($min + (Math.random() * ($max - $min)));
	}
}
