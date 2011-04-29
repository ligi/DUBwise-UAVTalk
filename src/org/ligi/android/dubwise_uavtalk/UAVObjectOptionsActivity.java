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

import org.ligi.android.common.adapter.IconTextActionAdapter;
import org.ligi.android.common.intents.IntentHelper;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * menu to enable the user to choose what to do with UAVObjects
 * 
 * TODO implement better workflow for that ( longpress )
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectOptionsActivity extends ListActivity {

    public final static int MENU_EDIT=1;
    public final static int MENU_SHOW_METADATA=2;

    private IconTextActionAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myAdapter = new IconTextActionAdapter(this);
        myAdapter.style(R.layout.icon_and_text, R.id.text, R.id.image);

        myAdapter.add(MENU_EDIT,android.R.drawable.ic_menu_edit, R.string.edit);
        myAdapter.add(MENU_SHOW_METADATA,android.R.drawable.ic_menu_view, R.string.show_metadata);

        this.setListAdapter(myAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(myAdapter.getAction(position)) {
        case MENU_EDIT:
            IntentHelper.action(this, "EDIT_UAVOBJECT");//.startActivityClass(this,UAVObjectsListActivity.class);
            break;
        case MENU_SHOW_METADATA:
            IntentHelper.action(this, "SHOWMETA_UAVOBJECT");
            break;
        }
    }
}