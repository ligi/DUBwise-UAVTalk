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

package org.ligi.android.dubwise_uavtalk.uavtalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 *                                           
 * Class to handle DUBwise Preferences for the UAVTalk part of DUBwise
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVTalkPrefs {

    private static SharedPreferences shared_prefs;

    public static String KEY_ENABLE_DESCRIPTIONDISPLAY="enable_uavobjdesc_displ";	
    public static String KEY_ENABLE_FLIGHTTELEMETRY="enable_flighttelemetry";	
    public static String KEY_ENABLE_FLIGHTTELEMETRY_UDP="enable_flighttelemetry_udp";	
    public static String KEY_ENABLE_FLIGHTTELEMETRY_BT="enable_flighttelemetry_bt";	
    public static String KEY_ENABLE_UAVTALK_UNITS="enable_uavtalk_units";	
    public static String KEY_ENABLE_UAVOBJECTS_MAINMENU="enable_uavobjects_mainmenu";	

    public static void init(Context context) {
        shared_prefs=PreferenceManager.getDefaultSharedPreferences(context)	;
    }

    public static boolean isUAVTalkUnitDisplayEnabled() {
        return shared_prefs.getBoolean(KEY_ENABLE_UAVTALK_UNITS, true);
    }

    public static boolean isUAVObjectDescriptionDisplayEnabled() {
        return shared_prefs.getBoolean(KEY_ENABLE_DESCRIPTIONDISPLAY, true);
    }

    public static boolean isFlightTelemetryEnabled() {
        return shared_prefs.getBoolean(KEY_ENABLE_FLIGHTTELEMETRY, false);
    }

    public static boolean isFlightTelemetryUDPEnabled() {
        return shared_prefs.getBoolean(KEY_ENABLE_FLIGHTTELEMETRY_UDP, false);
    }

    public static boolean isUAVObjectsMainMenuEnabled() {
        return shared_prefs.getBoolean(KEY_ENABLE_UAVOBJECTS_MAINMENU, false);
    }
}

