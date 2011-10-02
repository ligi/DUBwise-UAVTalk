/**************************************************************************
 *                                                                       
 * Author:  Marcus -LiGi- Bueschleb   
 *  http://ligi.de
 *  
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organisations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

package org.ligi.android.dubwise_uavtalk.uavobject_browser;

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.openpilot.uavtalk.UAVObjects;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Base ListActivity for common stuff in editing UAVObjects e.g. save 
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public abstract class UAVObjectFieldBaseActivity extends ListActivity {

	public int objid; 
	private final static int MENU_APPLY=0;
	private final static int MENU_SAVE=1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        DUBwiseUAVTalkActivityCommons.before_content(this);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0,MENU_APPLY,0,"Apply").setIcon(android.R.drawable.ic_menu_set_as);
        menu.add(0,MENU_SAVE,0,"Save").setIcon(android.R.drawable.ic_menu_save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case MENU_SAVE:
		UAVObjectPersistHelper.persistWithDialog(this,UAVObjects.getObjectByID(objid));
		return true;
	case MENU_APPLY:
		UAVObjectPersistHelper.apply(UAVObjects.getObjectByID(objid));
		return true;
	}
	return false;
	}
}
