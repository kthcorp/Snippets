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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * String을 처리한다.
 * 
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 9. 7.
 * @version 1.0.0
 * @see
 * @linkplain JDK 6.0
 */
public class StringUtils {

	private static final String EMPTY = "";

	public static String toString(String str) {
		return toString(str, EMPTY);
	}

	public static String toString(String str, String defaultValue) {
		return str == null ? EMPTY : str;
	}

	public static String toString(Object obj) {
		return toString(obj, EMPTY);
	}

	public static String toString(Object obj, String defaultValue) {
		try {
			return obj == null ? EMPTY : (String) obj;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String appVersion(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			if (LogUtil.isWarnEnabled()) {
				LogUtil.w(StringUtils.class, e);
			}
			return null;
		}
	}
}
