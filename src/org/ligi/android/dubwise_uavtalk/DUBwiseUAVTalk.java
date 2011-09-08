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
import org.ligi.android.dubwise_uavtalk.connection.StartupConnectionHandler;
import org.ligi.android.dubwise_uavtalk.gps.LocationUpdater;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferences;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoiceTTSFeederThread;
import org.ligi.android.dubwise_uavtalk.uavobject_browser.UAVTalkPrefs;
import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.logging.Log;
import org.ligi.tracedroid.sending.TraceDroidEmailSender;
import org.openpilot.uavtalk.UAVObjects;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
        new Thread(new LocationUpdater(activity)).start();
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        kickstart(this);
        
        this.setTheme(R.style.base_theme);
        this.setContentView(R.layout.dashboard_pager);
        
        DashBoardFragmentPagerAdapter pituneAdapter = new DashBoardFragmentPagerAdapter(this.getSupportFragmentManager());
        ViewPager awesomePager = (ViewPager) findViewById(R.id.dashboard_pager);
        awesomePager.setAdapter(pituneAdapter);
        
        TitlePageIndicator indicator =
                (TitlePageIndicator)findViewById( R.id.indicator );

        if (indicator!=null)
        	indicator.setViewPager(awesomePager);
        
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
    
    private class DashBoardFragmentPagerAdapter extends FragmentPagerAdapter implements TitleProvider{
        
        public DashBoardFragmentPagerAdapter(FragmentManager fm) {
     	   super(fm);
        }

        @Override
        public int getCount() {
     	   return 2;
        }

		@Override
		public Fragment getItem(int arg0) {
			return DashboardFragment.newInstance(arg0);		
		}

		@Override
		public String getTitle(int position) {
			switch(position) {
			/*case 0:
				return "check"; */
			case 0:
				return "fly";
			case 1:
				return "setup";
			}
			return null;
		}
    }
}