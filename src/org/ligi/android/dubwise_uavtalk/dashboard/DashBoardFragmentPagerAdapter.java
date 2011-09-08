package org.ligi.android.dubwise_uavtalk.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jakewharton.android.viewpagerindicator.TitleProvider;

public class DashBoardFragmentPagerAdapter extends FragmentPagerAdapter implements TitleProvider{
    
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