package org.ligi.android.dubwise_uavtalk;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DUBwiseUAVTalkPreferences {

	public static String getString(Activity activity,int key,String default_val) {
		SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(activity.getBaseContext());
		
		return prefs.getString(activity.getResources().getString(key),default_val);
	}
}
