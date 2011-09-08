package org.ligi.android.dubwise_uavtalk.gps;

import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
/**
 * update the phone ( mock ) location from the UAS location
 * 
 * @author ligi
 *
 */
public class LocationUpdater implements Runnable {
	private LocationManager lm;
	private Location loc;
	
	public LocationUpdater(Activity activity) {
		lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		lm.addTestProvider(LocationManager.GPS_PROVIDER, 
				false,//requiresNetwork
				false, //requiresSatellite, 
				false, //requiresCell, 
				false, // hasMonetaryCost, 
				true, //supportsAltitude, 
				true, ///supportsSpeed,
				true, //supportsBearing, 
				Criteria.POWER_LOW, 
				Criteria.ACCURACY_FINE);
		loc=new Location(LocationManager.GPS_PROVIDER);

	}

	
	public void run() {
		
		while (true) {
            loc.setLatitude(UAVObjects.getGPSPosition().getLatitude()/100.0d);
            loc.setLongitude(UAVObjects.getGPSPosition().getLongitude()/100.0d);
            loc.setTime(System.currentTimeMillis()); 
            loc.setSpeed(UAVObjects.getGPSPosition().getGroundspeed());
            loc.setAltitude(UAVObjects.getGPSPosition().getAltitude());
            loc.setBearing(UAVObjects.getGPSPosition().getHeading());
            lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, loc);
            lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER,true);
            //lm.setTestProviderStatus(provider, status, extras, updateTime)
            
            lm.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

            try {
            	Thread.sleep(1000);
            }catch(Exception e) { }
            
		}

	}
	
}