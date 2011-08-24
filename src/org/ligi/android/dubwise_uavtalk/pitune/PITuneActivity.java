package org.ligi.android.dubwise_uavtalk.pitune;

import org.ligi.android.uavtalk.dubwise.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

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

	 
}
