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
import org.ligi.android.dubwise_uavtalk.dashboard.DashBoardFragmentPagerAdapter;
import com.jakewharton.android.viewpagerindicator.TitlePageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * MainMenu/DashBoard Activity for DUBwise UAVTalk
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */

public class DUBwiseUAVTalkDashboardActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        DUBwiseUAVTalkActivityCommons.before_content(this);
    	super.onCreate(savedInstanceState);

        
        this.setContentView(R.layout.dashboard_pager);
        
        DashBoardFragmentPagerAdapter pituneAdapter = new DashBoardFragmentPagerAdapter(this.getSupportFragmentManager());
        ViewPager awesomePager = (ViewPager) findViewById(R.id.dashboard_pager);
        awesomePager.setAdapter(pituneAdapter);
        
        TitlePageIndicator indicator =  (TitlePageIndicator)findViewById( R.id.indicator );

        if (indicator!=null) // if we have a indicator ( layouts without might exist due to screen real estate) 
        	indicator.setViewPager(awesomePager); // connect indicator and pager
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getMenuInflater().inflate(R.menu.dashboard_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private final static int REQUEST_SETTINGS=0;
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_preferneces:
			this.startActivityForResult(new Intent(this,DUBwiseUAVTalkPreferencesActivity.class),REQUEST_SETTINGS);

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
    
    
}