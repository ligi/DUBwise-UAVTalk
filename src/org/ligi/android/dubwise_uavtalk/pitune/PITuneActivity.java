package org.ligi.android.dubwise_uavtalk.pitune;

import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.uavtalk.dubwise.R;

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

public class PITuneActivity extends FragmentActivity {
			
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.pitune_pager);
	        
	        PITuneFragmentPagerAdapter pituneAdapter = new PITuneFragmentPagerAdapter(getSupportFragmentManager());
	        ViewPager awesomePager = (ViewPager) findViewById(R.id.pitune_pager);
	        awesomePager.setAdapter(pituneAdapter);
	   }

	   private class PITuneFragmentPagerAdapter extends FragmentPagerAdapter{
           
           public PITuneFragmentPagerAdapter(FragmentManager fm) {
        	   super(fm);
           }

           @Override
           public int getCount() {
        	   return 2;
           }

			@Override
			public Fragment getItem(int arg0) {
				return PITuneFragment.newInstance(arg0) ;
			}
	   }

	    private final static int MENU_SAVE_VIDEO=0;
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        menu.clear();
	        menu.add(0,MENU_SAVE_VIDEO,0,"Help (Online-Video)").setIcon(android.R.drawable.ic_menu_help);
	        return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch(item.getItemId()) {
	        case MENU_SAVE_VIDEO:
	        	Intent i= new Intent( "android.intent.action.VIEW",
	        			Uri.parse( "http://www.openpilot.org/pid-tuning"));
	        
	        	this.startActivity(i);
	        	break;
	        }
	        return true;
	    }

	 
}
