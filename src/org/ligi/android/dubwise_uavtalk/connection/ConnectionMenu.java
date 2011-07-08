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
package org.ligi.android.dubwise_uavtalk.connection;

import org.ligi.android.common.adapter.IconTextActionAdapter;
import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * ListActivity to show and handle connection Options
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class ConnectionMenu extends ListActivity {
    public static final int MENU_CONNECT_BT=1;
    public static final int MENU_CONNECT_UDP=2;
    public static final int MENU_CONNECT_HANDSHAKESTATUS=3;

    private final static int INTENT_REQUEST_CODE_BT_CONN=1;

    private IconTextActionAdapter myAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTheme(R.style.base_theme);
        this.setContentView(R.layout.list);

        UAVTalkGCSThread.getInstance();

        this.setTitle(R.string.connection);
        myAdapter = new IconTextActionAdapter(this);
        myAdapter.style(R.layout.icon_and_text, R.id.text, R.id.image);

        myAdapter.add(MENU_CONNECT_BT,R.drawable.bt, R.string.via_bt);
        // TODO implement UDP ConnectionAdapter
        myAdapter.add(MENU_CONNECT_UDP,android.R.drawable.ic_menu_share, R.string.via_udp);
        myAdapter.add(MENU_CONNECT_HANDSHAKESTATUS,android.R.drawable.ic_menu_view, R.string.handshake_status);

        this.setListAdapter(myAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch(myAdapter.getAction(position)) {
        case MENU_CONNECT_BT:
            IntentHelper.action4result(this,"PICK_BLUETOOTH_DEVICE",INTENT_REQUEST_CODE_BT_CONN);
            break;
        case MENU_CONNECT_HANDSHAKESTATUS:
            HandshakeStatusAlertDialog.show(this);
            break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
        case INTENT_REQUEST_CODE_BT_CONN:
            if (resultCode==Activity.RESULT_OK) {
                String addr=data.getStringExtra("ADDR");
                String friendly_name=data.getStringExtra("FRIENDLYNAME");
                Log.i("connecting to bt addr:" + addr);
                HandshakeStatusAlertDialog.show(this);

                // build url
                String url=ConnectionManager.BT_PROTO_START;
                url+=data.getStringExtra("ADDR").replace(":", "");
                url+=":1"; // port
                ConnectionManager.connect(friendly_name, url);
            }
            break;

        default:	
            Log.w("unknown code in onActivityResult code="+requestCode);
        }

    }


}
