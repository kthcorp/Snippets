/**
 * KTH Developed by Java <br>
 *
 * @Copyright 2011 by Service Platform Development Team, KTH, Inc. All rights reserved.
 *
 * This software is the confidential and proprietary information of KTH, Inc. <br>
 * You shall not disclose such Confidential Information and shall use it only <br>
 * in accordance with the terms of the license agreement you entered into with KTH.
 */
package com.paran.sqlitedistancedemo.db;

import java.util.concurrent.CountDownLatch;

import com.kth.common.provider.sqlite.SQLiteContentProvider;
import com.kth.common.utils.etc.LocationUtils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * com.paran.sqlitedistancedemo.db.DemoProvider.java - Creation date: 2012. 1. 12. <br>
 *
 * @author KTH 단말어플리케이션개발팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class DemoProvider extends SQLiteContentProvider {

	private static final String TAG = "DemoProvider";

	private DemoDBHelper mDbHelper;

	public static final String DB_NAME = "demo.db";
	public static final int DB_VERSION = 1;

	private volatile CountDownLatch mAccessLatch;

	private static final int _START = 0;

	private static final int POST = _START							+1;
	private static final int POST_ID = _START						+2;
	private static final int NEARBYTIMELINE = _START				+3;
	private static final int NEARBYTIMELINE_ID = _START				+4;
	private static final int ATTACHMENT = _START					+5;
	private static final int ATTACHMENT_ID = _START					+6;

	private static final UriMatcher mUriMatcher;
	static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(DemoDB.AUTHORITY, DemoDB.Post.PATH, POST);
        mUriMatcher.addURI(DemoDB.AUTHORITY, DemoDB.Post.PATH+"/#", POST_ID);
    }

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		final int match = mUriMatcher.match(uri);
		switch(match) {
		case POST:
			return DemoDB.Post.CONTENT_TYPE;
		case POST_ID:
			return DemoDB.Post.CONTENT_ITEM_TYPE;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        try {
            return initialize();
        } catch (RuntimeException e) {
        	Log.e(TAG, "Cannot start provider:"+ e);
            return false;
        }
	}

	private boolean initialize() {
        final Context context = getContext();
        mDbHelper = (DemoDBHelper)getDatabaseHelper();
        mDb = mDbHelper.getWritableDatabase();

        return (mDb != null);
	}

	/* (non-Javadoc)
	 * @see com.paran.usay.db.SQLiteContentProvider#getDatabaseHelper(android.content.Context)
	 */
	@Override
    protected DemoDBHelper getDatabaseHelper(final Context context) {
        return DemoDBHelper.getInstance(context);
    }

	private String checkUri(Uri uri) {
		String table;
		final int match = mUriMatcher.match(uri);
		
		switch(match) {
		case POST:
		case POST_ID:
			table = DemoDB.Post.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		return table;
	}
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
        return super.insert(uri, values);
	}

	static String getQueryParameter(Uri uri, String parameter) {
        String query = uri.getEncodedQuery();
        if (query == null) {
            return null;
        }

        int queryLength = query.length();
        int parameterLength = parameter.length();

        String value;
        int index = 0;
        while (true) {
            index = query.indexOf(parameter, index);
            if (index == -1) {
                return null;
            }

            index += parameterLength;

            if (queryLength == index) {
                return null;
            }

            if (query.charAt(index) == '=') {
                index++;
                break;
            }
        }

        int ampIndex = query.indexOf('&', index);
        if (ampIndex == -1) {
            value = query.substring(index);
        } else {
            value = query.substring(index, ampIndex);
        }

        return Uri.decode(value);
    }

	public static String buildDistanceQuery(double latitude, double longitude) {
	    final double coslat = Math.cos(LocationUtils.deg2rad(latitude));
	    final double sinlat = Math.sin(LocationUtils.deg2rad(latitude));
	    final double coslng = Math.cos(LocationUtils.deg2rad(longitude));
	    final double sinlng = Math.sin(LocationUtils.deg2rad(longitude));
	    //@formatter:off
	    return "(" + coslat + "*" + DemoDB.Post.COS_LATITUDE
	            + "*(" + DemoDB.Post.COS_LONGITUDE + "*" + coslng
	            + "+" + DemoDB.Post.SIN_LONGITUDE + "*" + sinlng
	            + ")+" + sinlat + "*" + DemoDB.Post.SIN_LATITUDE 
	            + ")";
	    //@formatter:on
	}
	
	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		
		String table = null;
		String _selectionWithId = "";
		String _id = BaseColumns._ID;
						
		String limit = uri.getQueryParameter(DemoDB.COMMON_PARAM_LIMIT);

		long idFromUri = -1;
		try {
			idFromUri = ContentUris.parseId(uri);
		} catch (NumberFormatException e){
		} finally {
		}

		table = checkUri(uri);

		if(table.equalsIgnoreCase(DemoDB.Post.TABLE_NAME)) {
			String latitude = uri.getQueryParameter(DemoDB.Post.PARAM_LATITUDE);
			String longitude = uri.getQueryParameter(DemoDB.Post.PARAM_LONGITUDE);
			
			String partialDistance = uri.getQueryParameter(DemoDB.Post.PARAM_DISTANCE);
			if(partialDistance != null && latitude != null && longitude != null) {
				String sql = "SELECT *"
						+", "+buildDistanceQuery(Double.valueOf(latitude), Double.valueOf(longitude))
						+" AS "+DemoDB.Post.PARTIAL_DISTANCE
						+" FROM "+DemoDB.Post.TABLE_NAME
						+" WHERE "+DemoDB.Post.PARTIAL_DISTANCE+" > "+partialDistance;
				
				if(limit != null && limit.length() > 0) {
					sql = sql + " LIMIT "+limit;
				}
				
				Log.e(TAG, "Sql =>"+sql);
				Cursor rawCusor = db.rawQuery(sql, null);
				
				return rawCusor;
			}
		}
		
		if(idFromUri != -1)
			_selectionWithId = (_id + "=" + idFromUri + " ") + (selection == null ? "" : " AND (" + selection + ")");

		if(_selectionWithId != null && _selectionWithId.length() > 0) {
			Cursor c = db.query(table, projection, _selectionWithId, selectionArgs, null, null, sortOrder, (limit != null)?limit:null);
			return c;
		} else {
			Cursor c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder, (limit != null)?limit:null);
			return c;
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
        return super.update(uri, values, selection, selectionArgs);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return super.delete(uri, selection, selectionArgs);
	}

	/* (non-Javadoc)
	 * @see com.paran.usay.db.SQLiteContentProvider#insertInTransaction(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	protected Uri insertInTransaction(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowid = -1;

		String table = null;
		
		final int match = mUriMatcher.match(uri);
		
		table = checkUri(uri);

		rowid = mDb.insert(table, null, values);
		
		if(rowid < 0) {
			return null;
		}

		return ContentUris.withAppendedId(uri, rowid);
	}

	/* (non-Javadoc)
	 * @see com.paran.usay.db.SQLiteContentProvider#updateInTransaction(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	protected int updateInTransaction(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		
		String table = null;
		String _id = BaseColumns._ID;
		String _selectionWithId = null;
		
		long idFromUri = -1;
		try {
			idFromUri = ContentUris.parseId(uri);
		} catch (NumberFormatException e){
		} finally {
		}

		table = checkUri(uri);

		if(idFromUri != -1)
			_selectionWithId = (_id + "=" + idFromUri + " ") + (selection == null ? "" : " AND (" + selection + ")");


		if(_selectionWithId == null) {
			Cursor c = query(uri, null, selection, selectionArgs, null);

			try {
				c.moveToFirst();
				if(!c.isAfterLast()) {
					do {
						if(values.size() > 0) {
							long id = c.getLong(0);
							String[] selectionArgsTemp = {String.valueOf(id)};
							mDb.update(table, values, _id+" =?", selectionArgsTemp);
							count++;
						}
					} while(c.moveToNext());
				}
			} finally {
				c.close();
			}
		} else {
			mDb.update(table, values, _selectionWithId, selectionArgs);
			count++;
		}

		return count;
	}

	/* (non-Javadoc)
	 * @see com.paran.usay.db.SQLiteContentProvider#deleteInTransaction(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	protected int deleteInTransaction(Uri uri, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		
		String table = null;
		String _id = BaseColumns._ID;
		String _selectionWithId = null;
		
		long idFromUri = -1;
		try {
			idFromUri = ContentUris.parseId(uri);
		} catch (NumberFormatException e){
		} finally {
		}

		if(idFromUri != -1)
			_selectionWithId = (_id + "=" + idFromUri + " ") + (selection == null ? "" : " AND (" + selection + ")");

		table = checkUri(uri);

		if(_selectionWithId == null) {
			if(selection != null) {
				Cursor c = mDb.query(table, null, selection, selectionArgs, null, null, null);
				try {
					while(c.moveToNext()) {
						long id = c.getLong(c.getColumnIndex(_id));
						mDb.delete(table, _id+" = " + String.valueOf(id), null);
						count++;
					}
				} finally {
					c.close();
				}
			} else {
				count = mDb.delete(table, null, null);
			}
		} else {
			mDb.delete(table, _selectionWithId, selectionArgs);
			count++;
		}

		return count;
	}

	/* (non-Javadoc)
	 * @see com.paran.usay.db.SQLiteContentProvider#notifyChange()
	 */
	@Override
	protected void notifyChange() {
		// TODO Auto-generated method stub
		getContext().getContentResolver().notifyChange(DemoDB.AUTHORITY_URI, null);
	}

	@Override
    protected void onBeginTransaction() {
		Log.i(TAG, "onBeginTransaction");

        super.onBeginTransaction();
    }

    private void clearTransactionalChanges() {
    }

    @Override
    protected void beforeTransactionCommit() {
    	Log.i(TAG, "beforeTransactionCommit");

        super.beforeTransactionCommit();
        flushTransactionalChanges();
    }

    private void flushTransactionalChanges() {
    	Log.i(TAG, "flushTransactionChanges");

        clearTransactionalChanges();
    }
}
