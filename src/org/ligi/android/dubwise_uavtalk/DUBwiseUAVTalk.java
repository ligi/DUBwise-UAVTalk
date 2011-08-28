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

import org.ligi.android.R;
import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.common.intents.IntentHelper.IntentStartOnClick;
import org.ligi.android.dubwise_uavtalk.channelview.CurveEditActivity;
import org.ligi.android.dubwise_uavtalk.connection.ConnectionMenu;
import org.ligi.android.dubwise_uavtalk.connection.StartupConnectionHandler;
import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;
import org.ligi.android.dubwise_uavtalk.outputtest.OutputTestActivity;
import org.ligi.android.dubwise_uavtalk.pitune.PITuneActivity;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferences;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferencesActivity;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoiceTTSFeederThread;
import org.ligi.android.dubwise_uavtalk.uavobject_browser.UAVTalkPrefs;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * MainMenu/Entry/DashBoard Activity for DUBwise UAVTalk
 * 
 * TODO add FragmentPager to page between flight and garage dashboard
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */

public class DUBwiseUAVTalk extends Activity {

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kickstart(this);
        
        this.setTheme(R.style.base_theme);
        this.setContentView(R.layout.dashboard);
        
        new IntentStartOnClick(new Intent(this,OutputTestActivity.class),this)
        	.bind2view(R.id.dashboard_btn_output);
        new IntentStartOnClick(new Intent(this,CurveEditActivity.class),this)
        	.bind2view(R.id.dashboard_btn_channels);
        new IntentStartOnClick(new Intent(this,InstrumentDisplayActivity.class),this)
    		.bind2view(R.id.dashboard_btn_pfd);
        new IntentStartOnClick(new Intent(this,PITuneActivity.class),this)
    		.bind2view(R.id.dashboard_btn_tune);
        new IntentStartOnClick(new Intent(this,ConnectionMenu.class),this)
    		.bind2view(R.id.dashboard_btn_connection);
        new IntentStartOnClick(new Intent(this,StatusVoicePreferencesActivity.class),this)
        	.bind2view(R.id.dashboard_btn_sound);
                           
    }
    
    private final static int MENU_SAVE_EDIT=0;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0,MENU_SAVE_EDIT,0,"Edit UAVObjects").setIcon(android.R.drawable.ic_menu_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case MENU_SAVE_EDIT:
        	IntentHelper.action(this, "EDIT_UAVOBJECT");
        	break;
        }
        return true;
    }

}