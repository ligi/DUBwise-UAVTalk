package org.ligi.android.dubwise_uavtalk.dashboard;

import com.viewpagerindicator.TitleProvider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DashBoardFragmentPagerAdapter extends FragmentPagerAdapter implements TitleProvider{
    
    public DashBoardFragmentPagerAdapter(FragmentManager fm) {
 	   super(fm);
    }

    @Override
    public int getCount() {
 	   return 3;
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
			return "FLY";
		case 1:
			return "SETUP";
		case 2:
			return "DEV";
		}
		return null;
	}
}