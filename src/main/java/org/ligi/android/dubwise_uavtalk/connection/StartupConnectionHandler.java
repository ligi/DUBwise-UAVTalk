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

package org.ligi.android.dubwise_uavtalk.connection;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles automatic connecting on startup - like loading and saving connections
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StartupConnectionHandler {

    private static SharedPreferences mySharedPreferences;

    public static void init(Activity activity) {

        mySharedPreferences=activity.getSharedPreferences("default_conn", Activity.MODE_PRIVATE);
        if (mySharedPreferences.getBoolean("saved",false)) {  // if we have a default connection
            String url= mySharedPreferences.getString("url","corrupt settings data");
            String name=mySharedPreferences.getString("name","corrupt settings data");
            if (url.startsWith(ConnectionManager.BT_PROTO_START)) 
                BluetoothAdapter.getDefaultAdapter().enable();
            ConnectionManager.connect(name,url);
            
        }
    }

    public static void save(String name,String url) {
        Editor e=mySharedPreferences.edit();
        e.putBoolean("saved",true);
        e.putString("url",url);
        e.putString("name",name);
        e.commit();
    }

}
