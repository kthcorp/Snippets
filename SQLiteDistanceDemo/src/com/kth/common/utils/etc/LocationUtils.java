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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 10. 7.
 * @version 1.0.0
 * @see
 * @linkplain JDK 6.0
 */
public class LocationUtils {

	public static double convertPartialDistanceToKm(double result) {
	    return Math.acos(result) * 6371;
	}
	
	public static double convertKmToPartialDistance(double result) {
	    return Math.cos(result/6371);
	}

	public static double deg2rad(double deg) {
	    return (deg * Math.PI / 180.0);
	}
}
