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
package org.ligi.android.dubwise_uavtalk.instruments;

import org.ligi.android.uavtalk.dubwise.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
/**
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class InstrumentDisplayActivity extends Activity {

    private static final int MENU_SETTINGS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instruments);
    }

    /* Creates the menu items */
    public boolean onCreateOptionsMenu(Menu menu) {

        //menu.add(0,MENU_SETTINGS,0,"Settings").setIcon(android.R.drawable.ic_menu_preferences);

        return true;
    }
    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        case MENU_SETTINGS:
            //startActivity(new Intent(this, CockpitPrefsActivity.class));
            return true;
        }
        return false;
    }
}