/**************************************************************************
 *
 * License:
 *
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent context! 
 *  This explicitly includes that lethal weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *  
 *  The program is provided AS IS with NO WARRANTY OF ANY KIND, 
 *  INCLUDING THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS 
 *  FOR A PARTICULAR PURPOSE.
 *
 *  If you have questions please write the author marcus bueschleb 
 *  a mail to ligi at ligi dot de
 *  
 *  enjoy life!
 *  ligi
 *
 **************************************************************************/

package org.ligi.android.dubwise_uavtalk.map;

import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

/**
 *  Activity to show a Map with annotations from the UAVObjects
 *  
 * @author ligi
 *
 */
public class DUBwiseMapActivity extends MapActivity implements LocationListener {
	
	
	private MapView mapView;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		if (isDebugBuild())
			this.setContentView(R.layout.map_debug);
		else
			this.setContentView(R.layout.map_release);
		 
		mapView=(MapView)findViewById(R.id.mapview);

		mapView.setSatellite(true);
		 
		LinearLayout zoomView = (LinearLayout) mapView.getZoomControls();

		zoomView.setLayoutParams(new ViewGroup.LayoutParams
		   (ViewGroup.LayoutParams.WRAP_CONTENT,
		    ViewGroup.LayoutParams.WRAP_CONTENT)); 

		zoomView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		mapView.addView(zoomView);
	
		mapView.getController().setZoom(19);
		 
		centerToUAV();
		
		//mapView.getZoomButtonsController().setVisible(true);

		DUBwiseMapOverlay overlay=new DUBwiseMapOverlay(this);
		mapView.getOverlays().add(overlay);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5.0f, this);
		
		if ((UAVObjects.getGPSPosition().getLatitude()==0)&&
			(UAVObjects.getGPSPosition().getLongitude()==0))
		(new AlertDialog.Builder(this)).setTitle(R.string.no_gps_title) 
			.setMessage(R.string.no_gps_msg)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(R.string.understood, new DialogDiscarder())
			.setNeutralButton("Fake position", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					
					UAVObjects.getGPSPosition().setLatitude(523047070);
					UAVObjects.getGPSPosition().setLongitude(8657280);
					centerToUAV();
				}
				
			})
			.show();
		
	}
	
	public boolean isDebugBuild() {
		try {
			PackageManager pm = this.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);

			return ((pi.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint p = new GeoPoint((int) (lat * 1000000), (int)( lng * 1000000));
			//overlay.phonePoint=p;
			//mapView.getController().animateTo(p);
		}
	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}

	public void centerToUAV() {
		 
		GeoPoint kopterPoint=new GeoPoint( UAVObjects.getGPSPosition().getLatitude()/10
										   ,UAVObjects.getGPSPosition().getLongitude()/10);

		mapView.getController().setCenter(kopterPoint);
		
	}

	public MapView getMapView() {
		return mapView;	
	}
}