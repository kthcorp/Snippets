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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpProtocolParams;

import android.os.Environment;
import android.util.Log;

/**
 * Android 어플리케이션의 로그를 기록한다.<br />
 * 단말기의 기본 로그 레벨을 확인한 후 변경한다.<br />
 * <br />
 * <br />
 * 로그 레벨 확인
 * 
 * <pre>
 * Logger.isLoggable(TAG, Logger.VERBOSE) == false
 * Logger.isLoggable(TAG, Logger.DEBUG) == false
 * Logger.isLoggable(TAG, Logger.INFO) == true
 * Logger.isLoggable(TAG, Logger.WARN) == true
 * Logger.isLoggable(TAG, Logger.ERROR) == true
 * </pre>
 * 
 * 로그 레벨을 Shell에서 변경 방법 (TAG : FRIENDING)
 * 
 * <pre>
 * shell> adb shell stop
 * shell> adb shell setprop log.tag.KTH VERBOSE
 * shell> adb shell start
 * shell> adb shell getprop ## 설정값 확인.
 * 
 * shell> adb shell stop && adb shell setprop log.tag.KTH VERBOSE && adb shell start && adb shell getprop
 * </pre>
 * 
 * 버그 리포팅
 * 
 * <pre>
 * shell> adb shell bugreport > test.txt
 * </pre>
 * 
 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
 * @since 2011. 7. 18.
 * @version 1.0.0
 * @linkplain JDK 6.0
 */
public final class LogUtil {

	public static final String TAG = "KTH";
	// 윤석태 추가.
	// 파일 쌓이는거 싫다. ㅎㅎ 
	private static boolean mFileLogEnabled = false;

	// public static final int VERBOSE = Log.VERBOSE;
	// public static final int DEBUG = Log.DEBUG;
	// public static final int INFO = Log.INFO;
	// public static final int WARN = Log.WARN;
	// public static final int ERROR = Log.ERROR;

	/**
	 * VERBOSE 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void v(final Class<?> clazz, final String msg) {
		if (LogUtil.isVerboseEnabled()) {
			Log.println(Log.VERBOSE, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg);

			// 로그를 파일에 기록할 수 있는지 확인한다.
			if (LogUtil.isFileLogEnabled()) {
			    write(Log.VERBOSE, LogUtil.getClassLineNumber(clazz), msg);
			}
		}
	}

	/**
	 * VERBOSE 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param tr Throwable.
	 */
	public static void v(final Class<?> clazz, final Throwable tr) {
		if (LogUtil.isVerboseEnabled()) {
			Log.println(Log.VERBOSE, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.VERBOSE, LogUtil.getClassLineNumber(clazz), tr);
            }
		}
	}

	/**
	 * VERBOSE 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 * @param tr Throwable.
	 */
	public static void v(final Class<?> clazz, final String msg, final Throwable tr) {
		if (LogUtil.isVerboseEnabled()) {
			Log.println(Log.VERBOSE, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg + '\n'
					+ Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.VERBOSE, LogUtil.getClassLineNumber(clazz), msg, tr);
            }
		}
	}

	/**
	 * DEBUG 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void d(final Class<?> clazz, final String msg) {
		if (LogUtil.isDebugEnabled()) {
			Log.println(Log.DEBUG, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg);

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.DEBUG, LogUtil.getClassLineNumber(clazz), msg);
            }
		}
	}

	/**
	 * DEBUG 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param tr Throwable.
	 */
	public static void d(final Class<?> clazz, final Throwable tr) {
		if (LogUtil.isDebugEnabled()) {
			Log.println(Log.DEBUG, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.DEBUG, LogUtil.getClassLineNumber(clazz), tr);
            }
		}
	}

	/**
	 * DEBUG 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 * @param tr Throwable.
	 */
	public static void d(final Class<?> clazz, final String msg, final Throwable tr) {
		if (LogUtil.isDebugEnabled()) {
			Log.println(Log.DEBUG, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg + '\n'
					+ Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.DEBUG, LogUtil.getClassLineNumber(clazz), msg, tr);
            }
		}
	}

	/**
	 * INFO 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void i(final Class<?> clazz, final String msg) {
		if (LogUtil.isInfoEnabled()) {
			Log.println(Log.INFO, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg);

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.INFO, LogUtil.getClassLineNumber(clazz), msg);
            }
		}
	}

	/**
	 * INFO 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param tr Throwable.
	 */
	public static void i(final Class<?> clazz, final Throwable tr) {
		if (LogUtil.isInfoEnabled()) {
			Log.println(Log.INFO, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.INFO, LogUtil.getClassLineNumber(clazz), tr);
            }
		}
	}

	/**
	 * INFO 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 * @param tr Throwable.
	 */
	public static void i(final Class<?> clazz, final String msg, final Throwable tr) {
		if (LogUtil.isInfoEnabled()) {
			Log.println(Log.INFO, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg + '\n'
					+ Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.INFO, LogUtil.getClassLineNumber(clazz), msg, tr);
            }
		}
	}

	/**
	 * WARN 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void w(final Class<?> clazz, final String msg) {
		if (LogUtil.isWarnEnabled()) {
			Log.println(Log.WARN, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg);

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.WARN, LogUtil.getClassLineNumber(clazz), msg);
            }
		}
	}

	/**
	 * WARN 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param tr Throwable.
	 */
	public static void w(final Class<?> clazz, final Throwable tr) {
		if (LogUtil.isWarnEnabled()) {
			Log.println(Log.WARN, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.WARN, LogUtil.getClassLineNumber(clazz), tr);
            }
		}
	}

	/**
	 * WARN 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 * @param tr Throwable.
	 */
	public static void w(final Class<?> clazz, final String msg, final Throwable tr) {
		if (LogUtil.isWarnEnabled()) {
			Log.println(Log.WARN, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg + '\n'
					+ Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.WARN, LogUtil.getClassLineNumber(clazz), msg, tr);
            }
		}
	}

	/**
	 * ERROR 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void e(final Class<?> clazz, final String msg) {
		if (LogUtil.isErrorEnabled()) {
			Log.println(Log.ERROR, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg);

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.ERROR, LogUtil.getClassLineNumber(clazz), msg);
            }
		}
	}

	/**
	 * ERROR 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param tr Throwable.
	 */
	public static void e(final Class<?> clazz, final Throwable tr) {
		if (LogUtil.isErrorEnabled()) {
			Log.println(Log.ERROR, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.ERROR, LogUtil.getClassLineNumber(clazz), tr);
            }
		}
	}

	/**
	 * ERROR 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 * @param tr Throwable.
	 */
	public static void e(final Class<?> clazz, final String msg, final Throwable tr) {
		if (LogUtil.isErrorEnabled()) {
			Log.println(Log.ERROR, TAG, LogUtil.getClassLineNumber(clazz) + " - " + msg + '\n'
					+ Log.getStackTraceString(tr));

            // 로그를 파일에 기록할 수 있는지 확인한다.
            if (LogUtil.isFileLogEnabled()) {
                write(Log.ERROR, LogUtil.getClassLineNumber(clazz), msg, tr);
            }
		}
	}

	/**
	 * Stack Trace 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 */
	public static void stackTrace(final Class<?> clazz) {
		stackTrace(clazz, Log.INFO, null);
	}

	/**
	 * Stack Trace 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param msg 메시지.
	 */
	public static void stackTrace(final Class<?> clazz, final String msg) {
		stackTrace(clazz, Log.INFO, msg);
	}

	/**
	 * Stack Trace 로그를 기록한다.
	 * 
	 * @param clazz 로그 기록을 요청한 Class명.
	 * @param level 로그 레벨.
	 * @param msg 메시지.
	 */
	public static void stackTrace(final Class<?> clazz, final int level, final String msg) {
		if (Log.isLoggable(TAG, level)) {
			Thread th = Thread.currentThread();
			StackTraceElement[] stack = th.getStackTrace();

			StringBuilder sb = new StringBuilder();
			if (msg != null && !"".equals(msg)) {
				sb.append(msg).append("\n");
			}
			for (StackTraceElement element : stack) {
				if (!"getStackTrace".equals(element.getMethodName())
						&& !"stackTrace".equals(element.getMethodName())) {
					sb.append("\tat ").append(element.toString()).append("\n");
				}
			}
			Log.println(level, TAG,
					LogUtil.getClassLineNumber(clazz) + " - " + sb.toString());
		}
	}

	/**
	 * VERBOSE 로그 기록 확인 여부 판단.
	 */
	public static boolean isVerboseEnabled() {
		return Log.isLoggable(TAG, Log.VERBOSE);
	}

	/**
	 * DEBUG 로그 기록 확인 여부 판단.
	 */
	public static boolean isDebugEnabled() {
		return Log.isLoggable(TAG, Log.DEBUG);
	}

	/**
	 * INFO 로그 기록 확인 여부 판단.
	 */
	public static boolean isInfoEnabled() {
		return Log.isLoggable(TAG, Log.INFO);
	}

	/**
	 * WARN 로그 기록 확인 여부 판단.
	 */
	public static boolean isWarnEnabled() {
		return Log.isLoggable(TAG, Log.WARN);
	}

	/**
	 * ERROR 로그 기록 확인 여부 판단.
	 */
	public static boolean isErrorEnabled() {
		return Log.isLoggable(TAG, Log.ERROR);
	}

	/** 클래스명과 메소드명, 라인번호를 기록할 수 있도록한다. */
	private static String getClassLineNumber(final Class<?> clazz) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements != null) {
			for (StackTraceElement e : elements) {
				if ((clazz.getName()).equals(e.getClassName())
						|| (clazz.getSimpleName()).equals(e.getClassName())) {
					return e.getClassName()
							+ "(" + e.getMethodName() + ":" + e.getLineNumber() + ")";
				}
			}
		}
		return "";
	}

	/** 특정 로그를 외부 메모리에 저장한다. */
	private static void write(final int level, final String clazz, final String msg) {
		new Thread(new Write(level, clazz, msg)).start();
	}

	private static void write(final int level, final String clazz, final Throwable tr) {
		new Thread(new Write(level, clazz, tr)).start();
	}

	private static void write(final int level, final String clazz, final String msg,
			final Throwable tr) {
		new Thread(new Write(level, clazz, tr)).start();
	}

//	/**
//	 * 네트워크 Connection 로그를 기록한다.
//	 * 
//	 * @param clazz 로그 기록을 요청한 Class명.
//	 * @param request Request 정보.
//	 * @param response Response 정보.
//	 * @param url HTTP URL정보.
//	 */
//	public static void connection(final Class<?> clazz, final HttpRequest request,
//			final HttpResponse response, final String url, final int method) {
//		connection(clazz, Log.INFO, request, response, url, method);
//	}
//
//	/**
//	 * 네트워크 Connection 로그를 기록한다.
//	 * 
//	 * @param clazz 로그 기록을 요청한 Class명.
//	 * @param level 로그 레벨.
//	 * @param request Request 정보.
//	 * @param response Response 정보.
//	 * @param url HTTP URL정보.
//	 */
//	public static void connection(final Class<?> clazz, final int level, final HttpRequest request,
//			final HttpResponse response, final String url, final int method) {
//		if (Log.isLoggable(TAG, level)) {
//			StringBuilder sb = new StringBuilder();
//
//			sb.append("\n");
//			if (response != null) {
//				sb.append("========================= Request & Response =========================");
//			}
//			else {
//				sb.append("========================= Request =========================");
//			}
//			sb.append("\n");
//
//			switch (method) {
//				case HttpClientManager.HTTP_PUT_METHOD:
//					sb.append("[ HTTP Method      ] PUT").append("\n");
//					break;
//				case HttpClientManager.HTTP_POST_METHOD:
//					sb.append("[ HTTP Method      ] POST").append("\n");
//					break;
//				case HttpClientManager.HTTP_DELETE_METHOD:
//					sb.append("[ HTTP Method      ] DELETE").append("\n");
//					break;
//				default:
//					sb.append("[ HTTP Method      ] GET").append("\n");
//					break;
//			}
//
//			if (request != null) {
//				// Proxy 서버를 확인한다.
//				HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
//				if (proxy != null) {
//					sb.append("[ Proxy Server     ] ").append(proxy).append("\n");
//				}
//
//				// User-Agent를 확인한다.
//				String userAgent = HttpProtocolParams.getUserAgent(request.getParams());
//				if (userAgent != null) {
//					sb.append("[ User-Agent       ] ").append(userAgent).append("\n");
//				}
//
//				// HTTP 요청 URL을 확인한다.
//				if (url != null) {
//					sb.append("[ Request URL      ] ").append(url).append("\n");
//				}
//
//				// Request의 Header 정보를 확인한다.
//				Header[] headers = request.getAllHeaders();
//				if (headers != null) {
//					sb.append("[ Request Header   ] ");
//					int size = headers.length;
//					for (int i = 0; i < size; i++) {
//						Header h = headers[i];
//						sb.append(h.getName()).append("=").append(h.getValue()).append("; ");
//					}
//					sb.append("\n");
//				}
//
//				// HTTP 프로토콜 버전을 확인한다.
//				String protocol = request.getProtocolVersion().toString();
//				if (protocol != null) {
//					sb.append("[ Request Protocol ] ").append(protocol).append("\n");
//				}
//			}
//
//			if (response != null) {
//				// Request의 Header 정보를 확인한다.
//				Header[] headers = response.getAllHeaders();
//				if (headers != null) {
//					sb.append("[ Response Header  ] ");
//					int size = headers.length;
//					for (int i = 0; i < size; i++) {
//						Header h = headers[i];
//						sb.append(h.getName()).append("=").append(h.getValue()).append("; ");
//					}
//					sb.append("\n");
//				}
//
//				// HTTP Responses Status 코드를 확인한다.
//				int statusCode = response.getStatusLine().getStatusCode();
//				sb.append("[ Response Status  ] ").append(statusCode);
//			}
//
//			Log.println(level, TAG, "\n" + Logger.getClassLineNumber(clazz) + sb.toString());
//		}
//	}

	/**
	 * Date를 원하는 String Format으로 변환한다.
	 */
	public static String toDateString(String frm) {
		return new SimpleDateFormat(frm).format(new Date());
	}

	/**
	 * 2011. 11. 22 윤석태 추가
	 * 파일 로그가 기본으로 쌓이는게 싫다. ㅎㅎ 
	 */
	/**
     * @param mFileLogEnabled the mFileLogEnabled to set
     */
    public static void setFileLogEnabled(boolean mFileLogEnabled) {
        LogUtil.mFileLogEnabled = mFileLogEnabled;
    }

    /**
     * @return the mFileLogEnabled
     */
    public static boolean isFileLogEnabled() {
        return mFileLogEnabled;
    }

    /**
	 * 로그를 파일에 기록한다.
	 * 
	 * @author <a href="mailto:sky01126@paran.com"><b>손근양</b></a>
	 * @since 2011. 9. 15.
	 * @version 1.0.0
	 * @see Runnable
	 * @linkplain JDK 6.0
	 */
	static class Write implements Runnable {
		int mLevel;
		String mClazz;
		String mMessage;
		Throwable mThrowable;

		public Write(int level, String clazz, String msg) {
			this.mLevel = level;
			this.mClazz = clazz;
			this.mMessage = msg;
		}

		public Write(int level, String clazz, Throwable tr) {
			this.mLevel = level;
			this.mClazz = clazz;
			this.mThrowable = tr;
		}

		public Write(int level, String clazz, String msg, Throwable tr) {
			this.mLevel = level;
			this.mClazz = clazz;
			this.mMessage = msg;
			this.mThrowable = tr;
		}

		@Override
		public void run() {
			FileChannel out = null;
			FileOutputStream fos = null;
			try {
				// 외부 메모리가 있는지 확인한다.
				File filePath = Environment.getExternalStorageDirectory().getAbsoluteFile();
				long size = StorageUtils.availableExternalStorageSize();

				// 내부/외부 메모리에 10M 이상의 여유공간이 존재하는지 확인한다.
				if (size < (10 * 1024 * 1024)) {
					return;
				}

				String path = filePath + "/logs";

				// 로그를 기록 할 디렉토리가 없으면 생성한다.
				if (!new File(path).exists()) {
					new File(path).mkdirs();
				}

				String fileName = TAG + "_" + toDateString("yyyyMMddHH") + ".log";

				// 파일에 생성한다.
				fos = new FileOutputStream(path + "/" + fileName, true);
				out = fos.getChannel();

				// 로그를 생성한다.
				StringBuilder sb = new StringBuilder();
				sb.append(toDateString("yyyy-MM-dd HH:mm:ss mmmm"));
				switch (mLevel) {
					case Log.DEBUG:
						sb.append(" [DEBUG  ] ");
						break;
					case Log.INFO:
						sb.append(" [INFO   ] ");
						break;
					case Log.WARN:
						sb.append(" [WARN   ] ");
						break;
					case Log.ERROR:
						sb.append(" [ERROR  ] ");
						break;
					case Log.VERBOSE:
					default:
						sb.append(" [VERBOSE] ");
						break;
				}

				sb.append(mClazz).append(" - ");
				if (mMessage != null && !"".equals(mMessage)) {
					sb.append(mMessage);
					if (mThrowable != null) {
						sb.append("\n");
						sb.append(Log.getStackTraceString(mThrowable));
					}
				} else {
					if (mThrowable != null) {
						sb.append(Log.getStackTraceString(mThrowable));
					}
				}

				sb.append("\n\n");
				byte[] b = (sb.toString()).getBytes();
				ByteBuffer buf = ByteBuffer.allocate(b.length);

				// 바이트배열을 버퍼에 넣는다.
				buf.put(b);

				// 버퍼의 위치(position)는 0으로 Limit와 Capacity값과 같게 설정한다.
				buf.clear();
				out.write(buf);
			} catch (Exception e) {
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
			}

		}

	}
}
