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

import java.util.List;

import com.google.gson.annotations.SerializedName;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * com.paran.sqlitedistancedemo.db.DemoDB.java - Creation date: 2012. 1. 18. <br>
 *
 * @author KTH 안드로이드팀 홍성훈(Email: breadval@kthcorp.com, Ext: 2923) 
 * @version 1.0
 * @tags 
 */
public final class DemoDB {

	public static final String AUTHORITY = "com.paran.sqlitedistancedemo";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	public static final String MIMETYPE_PREFIX = "vnd.demo.Data.cursor";
	public static final String MIMETYPE_DIR = ".dir/";
	public static final String MIMETYPE_ITEM = ".item/";

	public static final String COMMON_PARAM_LIMIT = "limit";
	
	private DemoDB() {

	}

	public static final class Post implements BaseColumns {
		public static final String TABLE_NAME = "post";
		public static final String PATH = "post";
		
		public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+PATH);
		
		public static final String CONTENT_TYPE = MIMETYPE_PREFIX+MIMETYPE_DIR+TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = MIMETYPE_PREFIX+MIMETYPE_ITEM+TABLE_NAME;
		
		public static final String PARAM_LATITUDE = "latitude";
		public static final String PARAM_LONGITUDE = "longitude";
		public static final String PARAM_DISTANCE = "partial_distance";
		
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		
		public static final String COS_LATITUDE = "cos_latitude";
		public static final String SIN_LATITUDE = "sin_latitude";
		public static final String COS_LONGITUDE = "cos_longitude";
		public static final String SIN_LONGITUDE = "sin_longitude";
		
		public static final String PARTIAL_DISTANCE = "partial_distance";
		public static final String CREATED_TIME = "createdTime";
	}
	
}
