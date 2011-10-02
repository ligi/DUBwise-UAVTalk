package org.ligi.android.dubwise_uavtalk.pitune;

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.ligi.android.dubwise_uavtalk.connection.UAVTalkGCSThread;
import org.ligi.android.dubwise_uavtalk.uavobject_browser.UAVObjectPersistHelper;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVTalkDefinitions;

import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;
import com.jakewharton.android.viewpagerindicator.TitleProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class PITuneActivity extends FragmentActivity implements Runnable{
			
	   private boolean running=true;
		
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState); 
		    
		    DUBwiseUAVTalkActivityCommons.before_content(this);
		    
		    UAVTalkGCSThread.getInstance().send_obj(UAVObjects.getStabilizationSettings(),UAVTalkDefinitions.TYPE_OBJ_REQ);
	    
		    running=true;
		    new Thread(this).start();
	        this.setContentView(R.layout.pitune_pager);
	        
	        PITuneFragmentPagerAdapter pituneAdapter = new PITuneFragmentPagerAdapter(getSupportFragmentManager());
	        ViewPager awesomePager = (ViewPager) findViewById(R.id.pitune_pager);
	        awesomePager.setAdapter(pituneAdapter);
	        
	        TitlePageIndicator indicator =
	                (TitlePageIndicator)findViewById( R.id.indicator );

	        if (indicator!=null)
	        	indicator.setViewPager(awesomePager);
	   }

	   @Override
	protected void onDestroy() {
		running=false;
		super.onDestroy();
	}

	private class PITuneFragmentPagerAdapter extends FragmentPagerAdapter implements TitleProvider{
           
           public PITuneFragmentPagerAdapter(FragmentManager fm) {
        	   super(fm);
           }

           @Override
           public int getCount() {
        	   return 3;
           }

			@Override
			public Fragment getItem(int arg0) {
				return PITuneFragment.newInstance(arg0) ;
			}

			@Override
			public String getTitle(int position) {
				switch (position) {
				case 0:
					return "inner loop";
				case 1:
					return "outer loop";
				case 2:
					return "stick limits";
				}
				return "null";
			}
	   }

	   private final static int MENU_HELP=0;
	   private final static int MENU_SAVE=1;
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        menu.clear();
	        menu.add(0,MENU_HELP,0,"Help (wiki)").setIcon(android.R.drawable.ic_menu_help);
	        menu.add(0,MENU_SAVE,0,"Save").setIcon(android.R.drawable.ic_menu_save);
	        return super.onCreateOptionsMenu(menu);
	    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_HELP:
			Intent i = new Intent("android.intent.action.VIEW",
					Uri.parse("http://wiki.openpilot.org/display/Doc/Stabilization"));

			this.startActivity(i);
			break;

		case MENU_SAVE:
			UAVObjectPersistHelper.persistWithDialog(this,UAVObjects.getStabilizationSettings());
			break;

		}
		return true;
	}

	public void run() {
		while(running) {
			try {
				UAVObjectPersistHelper.apply(UAVObjects.getStabilizationSettings());
				Thread.sleep(400);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}


}
