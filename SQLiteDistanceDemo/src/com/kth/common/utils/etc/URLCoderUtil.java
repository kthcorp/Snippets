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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

/**
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 11. 6.
 * @version 1.0.0
 * @see
 * @linkplain JDK 6.0
 */
public class URLCoderUtil {

	/** URL을 URL Decode한다. */
	public static String decode(final String content) {
		return decode(content, HTTP.DEFAULT_CONTENT_CHARSET);
	}

	/** URL을 URL Decode한다. */
	public static String decode(final String content, final String encoding) {
		try {
			return URLDecoder.decode(content,
					encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

	/** URL을 URL encode. */
	public static String encode(final String content) {
		return encode(content, HTTP.DEFAULT_CONTENT_CHARSET);
	}

	/** URL을 URL Encode한다. */
	public static String encode(final String content, final String encoding) {
		try {
			return URLEncoder.encode(content,
					encoding != null ? encoding : HTTP.DEFAULT_CONTENT_CHARSET);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

}
