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
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectLoadHelper;
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectPersistHelper;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private UAVObjectFieldBaseActivity ctx;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	ctx=this;
        super.onCreate(savedInstanceState);
        
        DUBwiseUAVTalkActivityCommons.before_content(this);
    }
	
    abstract void notifyContentChange() ;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.uavobjectedit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.menu_save:
		UAVObjectPersistHelper.persistWithDialog(this,UAVObjects.getObjectByID(objid));
		return true;
	case R.id.menu_apply:
		UAVObjectPersistHelper.apply(UAVObjects.getObjectByID(objid));
		return true;
	case R.id.menu_load:
		Handler h=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				ctx.notifyContentChange();
			}
			
		};
		UAVObjectLoadHelper.loadWithDialog(this, UAVObjects.getObjectByID(objid), h);
	}
	return false;
	}
}
