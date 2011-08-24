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
import org.ligi.android.common.adapter.IconTextActionAdapter;
import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.dubwise_uavtalk.channelview.ChannelViewActivity;
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
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * MainMenu/Entry/DashBoard Activity for DUBwise UAVTalk
 * 
 * TODO add FragmentPager to page between flight and garage dashboard
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */

public class DUBwiseUAVTalk extends Activity {

	private Context ctx;
	
 
    
    private IconTextActionAdapter myAdapter;

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

        ctx=this;
        kickstart(this);
        
        this.setTheme(R.style.base_theme);
        
       /* this.setContentView(R.layout.list);

        myAdapter = new IconTextActionAdapter(this);
        myAdapter.style(R.layout.icon_and_text, R.id.text, R.id.image);

        myAdapter.add(MENU_CONNECT,android.R.drawable.ic_menu_share, R.string.connection);
        myAdapter.add(MENU_BROWSE_SETTINGS,android.R.drawable.ic_menu_preferences, R.string.settings);
        myAdapter.add(MENU_BROWSE_UAVOBJECTS,android.R.drawable.ic_menu_agenda, R.string.uavobjects);
        myAdapter.add(MENU_VIEW_CHANNELS,android.R.drawable.ic_menu_directions, R.string.channels);
        myAdapter.add(MENU_VIEW_INSTRUMENTS,android.R.drawable.ic_menu_view, R.string.view_instruments);
        
        myAdapter.add(MENU_PITUNE,android.R.drawable.ic_dialog_alert, R.string.pitune);
        */
     //   this.setListAdapter(myAdapter);
       
        this.setContentView(R.layout.dashboard);
        
        ((Button)this.findViewById(R.id.dashboard_btn_output)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				  IntentHelper.startActivityClass(ctx,OutputTestActivity.class);
			}
        	
        });

        
        ((Button)this.findViewById(R.id.dashboard_btn_channels)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IntentHelper.startActivityClass(ctx,ChannelViewActivity.class);
			}
        	
        });

        ((Button)this.findViewById(R.id.dashboard_btn_pfd)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IntentHelper.startActivityClass(ctx,InstrumentDisplayActivity.class);
			}
        	
        });

        ((Button)this.findViewById(R.id.dashboard_btn_tune)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
			    IntentHelper.startActivityClass(ctx,PITuneActivity.class);
			}
        	
        });
        
        ((Button)this.findViewById(R.id.dashboard_btn_connection)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IntentHelper.startActivityClass(ctx, ConnectionMenu.class);
			}
        	
        });
        
        ((Button)this.findViewById(R.id.dashboard_btn_sound)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				IntentHelper.startActivityClass(ctx, StatusVoicePreferencesActivity.class);
			}
        	
        });
        
    }
/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(myAdapter.getAction(position)) {
        case MENU_CONNECT:
            IntentHelper.startActivityClass(this, ConnectionMenu.class);
            break;
        case MENU_BROWSE_UAVOBJECTS:
        	// TODO check if good default
        	IntentHelper.action(this, "EDIT_UAVOBJECT");
            break;
        case MENU_BROWSE_SETTINGS:
            IntentHelper.startActivityClass(this,SettingsListActivity.class);
            break;
      
        }
    }
*/
}