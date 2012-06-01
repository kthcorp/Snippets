package com.kth.common.utils.etc;

/**
 * KTH Developed by Java
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with KTH.
 */

/**
 * 한글명에서 초성을 추출한다.
 * 
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 10. 11.
 * @version 1.0.0
 * @see
 * @linkplain JDK 6.0
 */
public class ChosungUtils {

	public static final char HANGUL_BASE_UNIT = 588;// 각자음 마다 가지는 글자수
	public static final char HANGUL_BEGIN_UNICODE = 44032; // 가
	public static final char HANGUL_END_UNICODE = 55203; // 힣

	public static final char[] HANGUL_CHOSUNG = {
			'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
			'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
	};

	public static String getChosung(String value) {
		if (value == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (ch == ' ') { // 공백문자는 무시한다.
				continue;
			}
			if (isHangul(ch)) { // 한글인지 확인한다.
				sb.append(getChosung(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private static char getChosung(char hanChar) {
		int hanBegin = (hanChar - HANGUL_BEGIN_UNICODE);
		int index = hanBegin / HANGUL_BASE_UNIT;
		return HANGUL_CHOSUNG[index];
	}

	private static boolean isHangul(char unicode) {
		return HANGUL_BEGIN_UNICODE <= unicode && unicode <= HANGUL_END_UNICODE;
	}

	// private static boolean containInitialChar(char c) {
	// return Arrays.binarySearch(HANGUL_CHOSUNG, c) != -1;
	// }
	//
	// public static String getHangulInitialSound(String value, String search) {
	// StringBuilder sb = new StringBuilder();
	// int minLen = Math.min(value.length(), search.length());
	// for (int i = 0; i < minLen; i++) {
	// char ch = value.charAt(i);
	// if (isHangul(ch) && containInitialChar(search.charAt(i))) {
	// sb.append(getInitalSound(ch));
	// } else {
	// sb.append(ch);
	// }
	// }
	// return sb.toString();
	// }
	//
	// public static boolean isHangulInitialSound(String value, String sarch) {
	// if (value == null || sarch == null) {
	// if (value != null)
	// return true;
	// return sarch == value;
	// }
	// return sarch.equals(getHangulInitialSound(value, sarch));
	// }

	public static void main(String[] args) {
		// System.out.println(getHangulInitialSound("테스트"));
		System.out.println(getChosung("Test Test Test"));
		System.out.println(getChosung("향수를 ㅋㄸㄸ"));
		System.out.println(getChosung("日 本語"));

	}
}
