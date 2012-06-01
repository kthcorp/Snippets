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

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;

/**
 * Android Device의 정보를 가져온다.
 *
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 9. 7.
 * @version 1.0.0
 * @linkplain JDK 6.0
 */
public class DeviceUtils {

	public static final int INT_ERROR_CODE = -1;

	/**
	 * Mobile Country Code를 추출한다.
	 *
	 * @param context Android Context.
	 * @return Mobile Country Code.
	 */
	public static int mcc(final Context context) {
		try {
			TelephonyManager telephonyManager =
					(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String networkOperator = telephonyManager.getNetworkOperator();
			if (networkOperator != null && !"".equals(networkOperator)) {
				if (LogUtil.isVerboseEnabled()) {
					LogUtil.v(DeviceUtils.class, "Network Operator: " + networkOperator);
				}
				// int mcc = NumberUtils.toInt(networkOperator.substring(0, 3));
				// int mnc = Integer.parseInt(networkOperator.substring(3));
				return NumberUtils.toInt(networkOperator.substring(0, 3));
			}
		} catch (Exception e) {
			if (LogUtil.isErrorEnabled()) {
				LogUtil.e(DeviceUtils.class, e);
			}
		}
		return INT_ERROR_CODE;
	}

	/**
	 * 안드로이드 키보드를 올려준다.
	 *
	 * @param ctx Context
	 * @param view View
	 */
	public static void upKeyboard(Context ctx, View view) {
		InputMethodManager mgr =
				(InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * 안드로이드 키보드를 내려준다.
	 *
	 * @param ctx Context
	 * @param view View
	 */
	public static void downKeyboard(Context ctx, View view) {
		InputMethodManager mgr =
				(InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(view, 0);
	}
	
	// 단말의 해상도를 구한다.
	public static DisplayMetrics getDisplayMetrics(Context ctx) {
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

	    return displayMetrics;
	}

    private static final String EMULATOR_ID = "ffffffffffffffff";

    public static String getCurrentLanguage(Context context) {
        Locale locale = context.getResources( ).getConfiguration( ).locale;
        return locale.getDefault().toString();
    }

    public static CharSequence getApplicationLabel(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return pm.getApplicationLabel(ai);
        } catch (NameNotFoundException e) {
            return "AnonDroid";
        }
    }

    public static String getApplicationVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static boolean isOnEmulator(Context context) {
        if ("sdk".equals(Build.MODEL) && "sdk".equals(Build.PRODUCT)) {
            return true;
        } 
        return getUniqueDeviceID(context).equals(EMULATOR_ID);
    }
    
    public static String getDeviceModel() {
        StringBuilder ret = new StringBuilder();
        if ("sdk".equals(Build.MODEL) && "sdk".equals(Build.PRODUCT)) {
            return "SDK Emulator";
        }
        
        ret.append(Build.MODEL).append(" [");
        ret.append(Build.MANUFACTURER).append(" ");
        ret.append(Build.PRODUCT).append("]");
        
        return ret.toString();
    }
    
    public static String getOSVersion() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.CUR_DEVELOPMENT)
            return "DEV";
        
        return Build.VERSION.RELEASE;
    }
    
    public static String getUniqueDeviceID(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String id = android.provider.Settings.System.getString(contentResolver, 
                                    android.provider.Settings.System.ANDROID_ID);
        if (id == null)
            id = EMULATOR_ID;
        return id;
    }
    
    public static String getDeviceID(Context context) {
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = telManager.getDeviceId();
        if (id == null)
            id = EMULATOR_ID;
        return id;
    }
}
