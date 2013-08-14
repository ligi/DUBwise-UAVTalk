package org.ligi.android.dubwise_uavtalk.pitune;

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.ligi.android.dubwise_uavtalk.connection.UAVTalkGCSThread;
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectLoadHelper;
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectPersistHelper;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVTalkDefinitions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Activity to tune the 
 * @author ligi
 *
 */
public class PITuneActivity extends FragmentActivity {
			
	   private boolean running=true;
	   private PITuneFragmentPagerAdapter pituneAdapter;
	   private FragmentActivity ctx;
	   
	   public class AutoValueApplyer implements Runnable {
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
	   
	   public void setContent() {
			ctx.setContentView(R.layout.pitune_pager);
			
			pituneAdapter = new PITuneFragmentPagerAdapter(getSupportFragmentManager());
			ViewPager awesomePager = (ViewPager) findViewById(R.id.pitune_pager);
			awesomePager.setAdapter(pituneAdapter);
			
			TitlePageIndicator indicator =	(TitlePageIndicator)findViewById( R.id.indicator );

		    if (indicator!=null)
		    	indicator.setViewPager(awesomePager);
	   }
	   
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState); 
		    ctx=this;
		    
		    DUBwiseUAVTalkActivityCommons.before_content(this);
		    
		    UAVTalkGCSThread.getInstance().send_obj(UAVObjects.getStabilizationSettings(),UAVTalkDefinitions.TYPE_OBJ_REQ);
		    Handler ready_handler=new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
				    running=true;
					new Thread(new AutoValueApplyer()).start();
					setContent();
					
				}
		    	
		    };
		    
		    Handler dismiss_handler=new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					setContent();
				}
		    	
		    };
		    
		    UAVObjectLoadHelper.loadWithDialog(this,UAVObjects.getStabilizationSettings(),ready_handler,dismiss_handler);
	      
	   }

	   @Override
	protected void onDestroy() {
		running=false;
		super.onDestroy();
	}

	private class PITuneFragmentPagerAdapter extends FragmentPagerAdapter {
           
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
			public String getPageTitle(int position) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.pitune_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			Intent i = new Intent("android.intent.action.VIEW",
					Uri.parse("http://wiki.openpilot.org/display/Doc/Stabilization"));

			this.startActivity(i);
			break;

		case R.id.menu_save:
			UAVObjectPersistHelper.persistWithDialog(this,UAVObjects.getStabilizationSettings());
			break;

		}
		return true;
	}

	
}
