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

package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.dubwise_uavtalk.connection.StartupConnectionHandler;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferences;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoiceTTSFeederThread;
import org.ligi.android.dubwise_uavtalk.uavobject_browser.UAVTalkPrefs;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.openpilot.uavtalk.UAVObjects;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * MainMenu/Entry/DashBoard Activity for DUBwise UAVTalk
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */

public class DUBwiseUAVTalk extends FragmentActivity {

    public static boolean kickstarted=false;

    /**
     * sets up environment 
     * 
     * @param activity
     */
    public static void kickstart(Activity activity) {
        if (kickstarted)
            return;
        kickstarted=true;

        UAVObjects.init();
        Log.setTAG("DUBwiseUAVTalk"); // It's all about DUBwise for UAVTalk from here ;-)
        TraceDroid.init(activity);
        TraceDroidEmailSender.sendStackTraces("ligi@ligi.de", activity);

        UAVTalkPrefs.init(activity);
        if (UAVTalkPrefs.isFlightTelemetryUDPEnabled())
            Log.i("TODO - reimplement UDP");

        UAVObjects.init();
        StatusVoicePreferences.init(activity);
        StatusVoiceTTSFeederThread.init(activity);
        StartupConnectionHandler.init(activity);
        
        // TODO Settings definition
        //new Thread(new LocationUpdater(activity)).start();
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.startActivity(new Intent(this,DUBwiseUAVTalkStartupActivity.class));
        finish();
    }
}