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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * com.paran.sqlitedistancedemo.db.DemoDBHelper.java - Creation date: 2012. 1. 12. <br>
 *
 * @author KTH 안드로이드팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public class DemoDBHelper extends SQLiteOpenHelper {

	private static DemoDBHelper sSingleton = null;

	public static synchronized DemoDBHelper getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new DemoDBHelper(context);
        }
        return sSingleton;
    }

	/**
	 * <PRE>
	 * Comment : <br>
	 * @author kth
	 * @version 1.0
	 * @date 2011. 7. 26.
	 * </PRE>
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private DemoDBHelper(Context context) {
		super(context, DemoProvider.DB_NAME, null, DemoProvider.DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE  TABLE IF NOT EXISTS "+DemoDB.Post.TABLE_NAME+" ("
				+DemoDB.Post._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+DemoDB.Post.LATITUDE+" FLOAT(11,7) ,"
				+DemoDB.Post.LONGITUDE+" FLOAT(11,7) ,"
				+DemoDB.Post.COS_LATITUDE+" FLOAT(11,7) ,"
				+DemoDB.Post.SIN_LATITUDE+" FLOAT(11,7) ,"
				+DemoDB.Post.COS_LONGITUDE+" FLOAT(11,7) ,"
				+DemoDB.Post.SIN_LONGITUDE+" FLOAT(11,7)"
				+" );");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+DemoProvider.DB_NAME+"."+DemoDB.Post.TABLE_NAME);
		onCreate(db);
	}

}
