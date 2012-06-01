package com.paran.sqlitedistancedemo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.kth.common.utils.etc.LocationUtils;
import com.kth.common.utils.etc.NumberUtils;
import com.paran.sqlitedistancedemo.db.DemoDB;

public class sampleMapLocationOverlayActivity extends MapActivity implements LocationListener {
	String _TAG = "sampleMapLocationOverlayActivity";
	
	LocationManager location = null;
	boolean bGetteringGPS = false; // GPS ���留�諛���ㅻ�濡����
	private LocationManager locationManager = null;

	private MapView mapView = null;
	private MapController mapController;
	private GeoPoint centerGP = null;

	private double currentLat = 0;
	private double currentLng = 0;

	private SeekBar mSeekbar = null;
	private Button btnConfirm = null;
	private Context context = null;
	private EditText editDistance = null;
	InterestingLocations mInterestingLocations;
	private int mNowLocation = 0;
	
	private Drawable marker = null;
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.context = this;

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		 Iterator<String> providers =
		 locationManager.getAllProviders().iterator();

		
		 while(providers.hasNext()) { Log.d("Test", "provider " +
		 providers.next()); }
		 

		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);

		String best = locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(best, 1000, 0, this);

		setContentView(R.layout.map_location_overlay);

		registerEventListener();
		
		if(getCountDummyData() <= 0) {
			makeDummyData();
		}
	}
	
	private void startProgressDialog() {
		if(progress == null)
			progress = new ProgressDialog(this);
		
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setMessage("화면 구성 중입니다.");
		progress.setCancelable(false);
		progress.show();
	}
	
	private void stopProgressDialog() {
		if(progress != null) {
			progress.dismiss();
			progress = null;
		}
	}
	
	private int getCountDummyData() {
		int count = 0;
		
		Cursor c = getContentResolver().query(DemoDB.Post.CONTENT_URI, null, null, null, null);
		
		if(c != null) {
			count = c.getCount();
			
			c.close();
			return count;
		}
		
		return count;
	}

	private void makeDummyData() {
		Toast.makeText(this, "더미 데이터 생성 중입니다.", Toast.LENGTH_SHORT).show();
		
		ArrayList<ContentValues> list = new ArrayList<ContentValues>();
		
		for(int i = 1; i <= 1000 ; i++) {
			ContentValues values = new ContentValues();
			
			String index = Integer.toString(i);
			
			double longitude = NumberUtils.getRandomNumber((double)125.92319053333333f, (double)127.92319053333333f);
			double latitude = NumberUtils.getRandomNumber((double)36.4922565f, (double)38.4922565f);
			
			values.put(DemoDB.Post.LATITUDE, latitude);
			values.put(DemoDB.Post.LONGITUDE, longitude);
			
			values.put(DemoDB.Post.COS_LATITUDE, Math.cos(LocationUtils.deg2rad(latitude)));
			values.put(DemoDB.Post.SIN_LATITUDE, Math.sin(LocationUtils.deg2rad(latitude)));
			values.put(DemoDB.Post.COS_LONGITUDE, Math.cos(LocationUtils.deg2rad(longitude)));
			values.put(DemoDB.Post.SIN_LONGITUDE, Math.sin(LocationUtils.deg2rad(longitude)));
			
			list.add(values);
		}
		
		ContentValues[] result = new ContentValues[list.size()];
		list.toArray(result);
		
		getContentResolver().bulkInsert(DemoDB.Post.CONTENT_URI, result);
		
		Toast.makeText(this, "더미 데이터 생성 완료", Toast.LENGTH_SHORT).show();
	}
	
	private void registerEventListener() {

		mapView = (MapView) findViewById(R.id.map_view);
		mapController = mapView.getController();

		editDistance = (EditText) findViewById(R.id.edit_distance);
		editDistance.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//				double km = Double.valueOf(editDistance.getText().toString());
//				mSeekbar.setProgress((int)(km*10));
			}
		});

		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setLocationFromDistance(Double.valueOf(editDistance.getText().toString()));
			}
		});

		mSeekbar = (SeekBar)findViewById(R.id.seekbar);
		mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				editDistance.setText(String.valueOf((double)seekBar.getProgress()/10.f));
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
//				if(!editDistance.isFocused())
					editDistance.setText(String.valueOf((double)progress/10.f));
			}
		});
		
		Button btnInsertDB = (Button) findViewById(R.id.btn_dbinsert);
		btnInsertDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getCountDummyData() > 0) {
					Toast.makeText(context, "이미 더미 데이터가 있습니다. 삭제 후 눌러주세요.", Toast.LENGTH_SHORT).show();
				} else {
					makeDummyData();
				}
			}
		});
		
		Button btnDeleteDB = (Button) findViewById(R.id.btn_dbdelete);
		btnDeleteDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getContentResolver().delete(DemoDB.Post.CONTENT_URI, null, null);
				
				Toast.makeText(context, "더미 데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 맵초기화
	 */
	private void setMap() {

		centerGP = new GeoPoint((int) (currentLat * 1E6),
				(int) (currentLng * 1E6));

		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapView.setStreetView(true);
		mapController.setZoom(10);
		mapController.setCenter(centerGP);
		mapView.invalidate();

		if(mInterestingLocations==null)
		{
			Drawable marker = getResources().getDrawable(
					android.R.drawable.ic_menu_myplaces);
			mInterestingLocations= new InterestingLocations(marker, (int) (currentLat * 1E6), (int) (currentLng * 1E6));
			mapView.getOverlays().add(mInterestingLocations);
		}
		else{
			placeMarker((int) (currentLat * 1E6), (int) (currentLng * 1E6),android.R.drawable.ic_menu_myplaces,true);
		}
		
		Log.e(_TAG, "currentLat: "+currentLat+" currentLng: "+currentLng);
		
//		placeMarker((int) (currentLat * 1E6)+1400, (int) (currentLng * 1E6)+400,R.drawable.da_marker_red,false);
//		placeMarker((int) (currentLat * 1E6)-500, (int) (currentLng * 1E6)-200,R.drawable.da_marker_red,false);
		
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {

		if (bGetteringGPS == false) {

			currentLat = location.getLatitude();
			currentLng = location.getLongitude();

			Log.d("Test", "location " + currentLat + " " + currentLng);
			locationManager.removeUpdates(this);
			bGetteringGPS = true;

			setMap();
			
			//placeMarker((int) (currentLat * 1E6), (int) (currentLng * 1E6),R.drawable.icon);
			
			//placeMarker((int) (currentLat * 1E6)+30, (int) (currentLng * 1E6+10),R.drawable.icon);
			
			
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	 *        마커설정.
	 * 
	 * @param markerLatitude
	 * @param markerLongitude
	 * @param aRes 마커 이미지 아이디
	 * @param aCenterFlag 가운데표시(현재 위치)이면 true 
	 */
	private void placeMarker(int markerLatitude, int markerLongitude , int aRes,Boolean aCenterFlag) {
		
		Drawable marker = getResources().getDrawable(
				aRes);
	
		if(aCenterFlag){
			
			mInterestingLocations.locations.remove(mNowLocation);
			// 마지막에 현재 아이템이 추가되기 때문에..
			mNowLocation = mInterestingLocations.locations.size();
		}
		
		
		
		GeoPoint myPlace = new GeoPoint( markerLatitude,
				markerLongitude);
		OverlayItem item = new OverlayItem(myPlace, "My Place", "My Place");
		
		
		
		mInterestingLocations.addOverlay(item, marker);
		
	}
	
	private void placeMarker(int markerLatitude, int markerLongitude , int aRes) {
		
		Drawable marker = getResources().getDrawable(
				aRes);
	
		GeoPoint myPlace = new GeoPoint( markerLatitude,
				markerLongitude);
		OverlayItem item = new OverlayItem(myPlace, "My Place", "My Place");
		
		
		
		mInterestingLocations.addOverlay(item, marker);
		
	}

	/**
	 * @deprecated
	 * 		현재 위치 마커에 표시
	 * 
	 * @param centerGeoPoint
	 */
	private void CenterLocation(GeoPoint centerGeoPoint) {
		mapController.animateTo(centerGeoPoint);

		placeMarker(centerGeoPoint.getLatitudeE6(),
				centerGeoPoint.getLongitudeE6(),android.R.drawable.ic_menu_myplaces,true);

		currentLat = (double) centerGeoPoint.getLatitudeE6() / 1000000;
		currentLng = (double) centerGeoPoint.getLongitudeE6() / 1000000;

	};

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;//super.onCreateOptionsMenu(menu);
    }
	
	private void setLocation() {
		SetLocation task = new SetLocation(-1);
		task.execute();
	}
	
	class SetLocation extends AsyncTask {

		private double distance = -1;
		private OverlayItem[] locations = null;
		
		public SetLocation(double distance) {
			this.distance = distance;
			
			if(marker == null)
				marker = getResources().getDrawable(R.drawable.da_marker_red);
		}
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			startProgressDialog();
			
			btnConfirm.setEnabled(false);
			mapView.setEnabled(false);
			
			mInterestingLocations.locations.clear();
		}


		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mInterestingLocations.addOverlays(locations);
			Log.e(_TAG, "Size:"+mInterestingLocations.size());
			mapView.postInvalidate();
			
			btnConfirm.setEnabled(true);
			mapView.setEnabled(true);
			
			stopProgressDialog();
		}


		@Override
		protected void onProgressUpdate(Object... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			
//			locations.addOverlay((OverlayItem)values[0], marker);
//			mInterestingLocations.addOverlay((OverlayItem)values[0], marker);
//			mapView.postInvalidate();
			
			int percent = (Integer)values[0];
			Log.e(_TAG, "Progress:"+percent);
			if(percent != progress.getProgress())
				progress.setProgress(percent);
		}


		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			placeMarker((int) (currentLat * 1E6), (int) (currentLng * 1E6),android.R.drawable.ic_menu_myplaces);
			
			Uri uri = null;
			
			if(distance != -1) {
				double partialDistance = LocationUtils.convertKmToPartialDistance(distance);
				Log.e(_TAG, "partialDistance =>"+partialDistance);
				
				Builder builder = DemoDB.Post.CONTENT_URI.buildUpon();
				builder.appendQueryParameter(DemoDB.Post.PARAM_LATITUDE, Double.toString(currentLat));
				builder.appendQueryParameter(DemoDB.Post.PARAM_LONGITUDE, Double.toString(currentLng));
				builder.appendQueryParameter(DemoDB.Post.PARTIAL_DISTANCE, Double.toString(partialDistance));
				uri = builder.build();
			} else {
				Builder builder = DemoDB.Post.CONTENT_URI.buildUpon();
				uri = builder.build();
			}
			
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			
			try {
				int total = cursor.getCount();
				int current = 0;
				
				if(locations == null) {
					locations = new OverlayItem[total];
				}
				if(cursor.moveToFirst()) {
					if(!cursor.isAfterLast()) {
						do {
							double value4 = cursor.getDouble(cursor.getColumnIndex(DemoDB.Post.LATITUDE));
							double value5 = cursor.getDouble(cursor.getColumnIndex(DemoDB.Post.LONGITUDE));
							
							if(distance != -1) {
								double value6 = cursor.getDouble(cursor.getColumnIndex(DemoDB.Post.PARTIAL_DISTANCE));
								
								double kmResult = LocationUtils.convertPartialDistanceToKm(value6);
								
								int markerLatitude = (int) (value4 * 1E6);
								int markerLongitude = (int) (value5 * 1E6);
								
								if(marker == null)
									marker = getResources().getDrawable(R.drawable.da_marker_red);
							
								GeoPoint myPlace = new GeoPoint( markerLatitude,
										markerLongitude);
								
								locations[current] = new OverlayItem(myPlace, "My Place"+current, "My Place");
								locations[current].setMarker(boundCenterBottom(marker));
								
								current++;
								this.publishProgress(current*100/total);
							} else {
								int markerLatitude = (int) (value4 * 1E6);
								int markerLongitude = (int) (value5 * 1E6);
								
								if(marker == null)
									marker = getResources().getDrawable(R.drawable.da_marker_red);
							
								GeoPoint myPlace = new GeoPoint( markerLatitude,
										markerLongitude);
								
								locations[current] = new OverlayItem(myPlace, "My Place"+current, "My Place");
								locations[current].setMarker(boundCenterBottom(marker));
								
								current++;
								this.publishProgress(current*100/total);
							}
							
							
						} while(cursor.moveToNext());
					}
				}
			} finally {	
				if(cursor != null)
					cursor.close();
			}
			
			return null;
		}
		
	}
	
	private void setLocationFromDistance(double km) {
		SetLocation task = new SetLocation(km);
		task.execute();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.menu_all:
            	setLocation();
                break;
//            case R.id.menu_query:
//            	setLocationFromDistance(30);
//            	break;
            default : 
                break;
        }
        
        return false;
    }
	
	/**
	 * 吏����� ������щ━���대���
	 */

	class InterestingLocations extends ItemizedOverlay<OverlayItem> {

		public List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;
		private OverlayItem myOverlayItem;

		public InterestingLocations(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}
		
		public InterestingLocations(Drawable defaultMarker, int LatitudeE6,
				int LongitudeE6) {
			super(defaultMarker);

			// TODO Auto-generated constructor stub
			this.marker = defaultMarker;
			// create locations of interest
			GeoPoint myPlace = new GeoPoint(LatitudeE6, LongitudeE6);
			myOverlayItem = new OverlayItem(myPlace, "My Place", "My Place");
			locations.add(myOverlayItem);

			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return locations.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return locations.size();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			// TODO Auto-generated method stub
			// shadow 그림자 효과 flag 
			shadow = false;
			super.draw(canvas, mapView, shadow);

//			boundCenterBottom(marker);
		}

		/**
		 * onTouchListener 濡�� touch up, down, move 媛����濡�援ы� ����� bug...
		 */
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			
//			CenterLocation(p);
			
			return true;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			// TODO Auto-generated method stub
			return super.onTouchEvent(event, mapView);
		}

		public synchronized void addOverlays(OverlayItem[] locations2)
		{
			locations.addAll(Arrays.asList(locations2));
			populate();
		}
		
		public synchronized void addOverlay(OverlayItem overlay)
		{
			locations.add(overlay);
			populate();
		}
		
		public synchronized void addOverlay(OverlayItem overlay, Drawable drawable)
		{
			if(drawable!=null)
				overlay.setMarker(boundCenterBottom(drawable));
			addOverlay(overlay);
		}
		
		public Drawable getMarker(Drawable drawable) {
			return boundCenterBottom(drawable);
		}
	}
	
	public static Drawable boundCenterBottom(Drawable balloon) {
        balloon.setBounds(balloon.getIntrinsicWidth() / -2, -balloon.getIntrinsicHeight(),
                        balloon.getIntrinsicWidth() / 2, 0);
        return balloon;
	}
}