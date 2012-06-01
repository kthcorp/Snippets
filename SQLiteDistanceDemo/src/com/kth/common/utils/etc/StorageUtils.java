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

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;

/**
 * 안드로이드의 내부/외부 메모리 정보를 가져온다.
 * 
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 9. 22.
 * @version 1.0.0
 * @linkplain JDK 6.0
 */
public class StorageUtils {

    /**
     *  윤석태 2011. 11. 22 </br>
     *  주기적으로 외장메모리에 대한 체크를 하고자 하는 경우에 사용한다. 
     */
    private static BroadcastReceiver mExternalStorageReceiver;
    private static boolean mExternalStorageAvailable = false;
    private static boolean mExternalStorageWriteable = false;

    private static void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public static void startWatchingExternalStorage(Context context) {
        mExternalStorageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LogUtil.i(this.getClass(), "Storage: " + intent.getData());
                updateExternalStorageState();
            }
        };
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        context.registerReceiver(mExternalStorageReceiver, filter);
        updateExternalStorageState();
    }

    public static void stopWatchingExternalStorage(Context context) {
        context.unregisterReceiver(mExternalStorageReceiver);
        mExternalStorageReceiver = null;
    }

    /**
     * @return the mExternalStorageAvailable
     */
    public static boolean isExternalStorageAvailable() {
        if (mExternalStorageReceiver != null)
            return mExternalStorageAvailable;
        else {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                mExternalStorageAvailable = mExternalStorageWriteable = true;
            } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
            } else {
                mExternalStorageAvailable = mExternalStorageWriteable = false;
            }
            
            return mExternalStorageWriteable;
        }
    }

    /**
     * @return the mExternalStorageAvailable
     */
    public static boolean isExternalStorageWritable() {
        return mExternalStorageWriteable;
    }

    
	public static long totalInternalStorageSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	public static long availableInternalStorageSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public static long totalExternalStorageSize() {
		if (isExternalStorageAvailable() == true) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {
			return -1;
		}
	}

	public static long availableExternalStorageSize() {
		if (isExternalStorageAvailable() == true) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		} else {
			return -1;
		}
	}

	public static String FormatSize(long size) {
		String suffix = null;
		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
			}
		}
		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}
		if (suffix != null) {
			resultBuffer.append(suffix);
		}
		return resultBuffer.toString();
	}

}
