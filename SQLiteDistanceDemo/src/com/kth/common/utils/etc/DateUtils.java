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
public class DateUtils {

	/**
	 * 현재시간을 synchronized를 선택해서 "yyyy-MM-dd HH:mm:ss" Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(false);
	 * </pre>
	 * 
	 * @return String.
	 */
	public static String toDateString() {
		return toDateString("yyyy-MM-dd HH:mm:ss", new Date());
	}

	/**
	 * 시간을 원하는 String Format으로 변환해서 반환한다.
	 * 
	 * <pre>
	 * DateUtil.toDateString(true, &quot;yyyy-MM-dd HH:mm:ss mmmm&quot;, str, new Date());
	 * </pre>
	 * 
	 * @param fmt Format.
	 * @param date Date
	 * @return String
	 */
	public static String toDateString(String fmt, Date date) {
		return new SimpleDateFormat(fmt).format(date);
	}

	public static final int gmtTimeZone() {
		try {
			TimeZone tz = TimeZone.getDefault();
			LogUtil.v(DateUtils.class,
					tz.getDisplayName() + " : " + (tz.getRawOffset() / 60 / 60 / 1000));
			return tz.getRawOffset() / 60 / 60 / 1000;
		} catch (Exception e) {
			LogUtil.w(DateUtils.class, e);
		}
		return 0;
	}

	/**
	 * <PRE>
	 * @Method Name : getUnixTime
	 * @Date     : 2012. 3. 13.
	 * @Author   : basagee
	 * @Revision :
	 * @Description : 아래의 두 Method 는 서버에서 받은 아이템의 시간이 현재로부터 언제 이전에 생성된 것인지 비교하는 함수이다.
	 * </PRE>
	 * @return Long
	 * @return
	 */
    public static Long getUnixTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long now = calendar.getTimeInMillis();

        return (now);

    }
    
    public static final Long getElapsedTimeMilliseconds(Long time) {
        return getUnixTime() - time;
    }

    public static final Long getElapsedTimeSeconds(Long timeSeconds) {
        return getUnixTime() / 1000 - timeSeconds;
    }
}
