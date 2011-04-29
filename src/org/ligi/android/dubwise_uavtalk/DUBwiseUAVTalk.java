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

import org.ligi.android.common.adapter.IconTextActionAdapter;
import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.dubwise_uavtalk.channelview.ChannelViewActivity;
import org.ligi.android.dubwise_uavtalk.connection.ConnectionMenu;
import org.ligi.android.dubwise_uavtalk.connection.StartupConnectionHandler;
import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;
import org.ligi.android.dubwise_uavtalk.outputtest.OutputTestActivity;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferences;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoiceTTSFeederThread;
import org.ligi.android.dubwise_uavtalk.uavtalk.UAVTalkPrefs;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * MainMenu/Entry Activity for DUBwise UAVTalk
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */

public class DUBwiseUAVTalk extends ListActivity {

    public final static int MENU_CONNECT=1;
    public final static int MENU_BROWSE_SETTINGS=2;
    public final static int MENU_BROWSE_UAVOBJECTS=3;
    public final static int MENU_VIEW_CHANNELS=4;
    public final static int MENU_VIEW_INSTRUMENTS=5;
    public final static int MENU_OUTPUTEST=6;

    
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

        myAdapter = new IconTextActionAdapter(this);
        myAdapter.style(R.layout.icon_and_text, R.id.text, R.id.image);

        myAdapter.add(MENU_CONNECT,android.R.drawable.ic_menu_share, R.string.connection);
        myAdapter.add(MENU_BROWSE_SETTINGS,android.R.drawable.ic_menu_preferences, R.string.settings);
        myAdapter.add(MENU_BROWSE_UAVOBJECTS,android.R.drawable.ic_menu_agenda, R.string.uavobjects);
        myAdapter.add(MENU_VIEW_CHANNELS,android.R.drawable.ic_menu_directions, R.string.channels);
        myAdapter.add(MENU_VIEW_INSTRUMENTS,android.R.drawable.ic_menu_view, R.string.view_instruments);
        myAdapter.add(MENU_OUTPUTEST,android.R.drawable.ic_menu_edit, R.string.output_test);

        this.setListAdapter(myAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(myAdapter.getAction(position)) {
        case MENU_CONNECT:
            IntentHelper.startActivityClass(this, ConnectionMenu.class);
            break;
        case MENU_BROWSE_UAVOBJECTS:
            //IntentHelper.action(this, "PICK_UAVOBJECT");//.startActivityClass(this,UAVObjectsListActivity.class);
            IntentHelper.startActivityClass(this,UAVObjectOptionsActivity.class);
            break;
        case MENU_BROWSE_SETTINGS:
            IntentHelper.startActivityClass(this,SettingsListActivity.class);
            break;
        case MENU_VIEW_CHANNELS:
            IntentHelper.startActivityClass(this,ChannelViewActivity.class);
            break;
        case MENU_VIEW_INSTRUMENTS:
            IntentHelper.startActivityClass(this,InstrumentDisplayActivity.class);
            break;
        case MENU_OUTPUTEST:
            IntentHelper.startActivityClass(this,OutputTestActivity.class);
            break;
        }
    }

}