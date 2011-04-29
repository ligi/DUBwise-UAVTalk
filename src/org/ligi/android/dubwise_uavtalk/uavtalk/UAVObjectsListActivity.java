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

package org.ligi.android.dubwise_uavtalk.uavtalk;

import org.ligi.android.common.adapter.PeriodicallyInvalidateAdapter;
import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.android.dubwise_uavtalk.R;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to List all UAVObjecs
 *
 * TODO - implement a better way for marking recently updated ones other tan invalidating the adapter
 *  
 * @author ligi the UAVObjects
 *
 */
public class UAVObjectsListActivity extends ListActivity {

    public final static int ACTION_PICK_UAVOBJECT=0;
    public final static int ACTION_EDIT_UAVOBJECT=1;
    public final static int ACTION_SHOWMETA_UAVOBJECT=2;

    private int myAction=ACTION_SHOWMETA_UAVOBJECT; // default

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action_str=this.getIntent().getAction();
        Log.i("Starting UAVObjectsListActivity with action" + action_str);
        
        if (action_str.equalsIgnoreCase("PICK_UAVOBJECT")) {
            this.setTitle(R.string.title_pick_uavobj);
            myAction=ACTION_PICK_UAVOBJECT;
        } else if (action_str.equalsIgnoreCase("EDIT_UAVOBJECT")) {
            this.setTitle(R.string.title_edit_uavobj);
            myAction=ACTION_EDIT_UAVOBJECT;
        } else if (action_str.equalsIgnoreCase("SHOWMETA_UAVOBJECT")) {
            this.setTitle(R.string.title_show_uavobjmeta);
            myAction=ACTION_SHOWMETA_UAVOBJECT;
        } else Log.w("I do not know that action " + action_str + " using fallback");

        UAVObjectsArrayAdapter adapter=new UAVObjectsArrayAdapter(this, android.R.layout.simple_list_item_1, UAVObjects.getUAVObjectArray());
        this.setListAdapter( adapter);
        new PeriodicallyInvalidateAdapter(this,adapter);

    }
    
    /**
     * just passes the result to the next activity down the line when there was a result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==Activity.RESULT_OK) {
            this.setResult(resultCode, data);
            finish();
        }
    }

    class UAVObjectsArrayAdapter extends ArrayAdapter<UAVObject> implements OnClickListener, OnTouchListener {

        private Context context;
        private UAVObject[] objects;

        public UAVObjectsArrayAdapter(Context context, int textViewResourceId, UAVObject[] objects) {
            super(context, textViewResourceId, objects);
            this.context=context;
            this.objects=objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) { 

            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            UAVObject act_obj=(UAVObject)(objects[position]);
            View row;
            if (UAVTalkPrefs.isUAVObjectDescriptionDisplayEnabled()) {
                row=vi.inflate(android.R.layout.two_line_list_item, null);
                TextView label=(TextView)row.findViewById(android.R.id.text1); 
                label.setText(((UAVObject)(objects[position])).getObjName());

                TextView descr=(TextView)row.findViewById(android.R.id.text2); 
                descr.setText(((UAVObject)(objects[position])).getObjDescription());

            }
            else {
                row=vi.inflate(android.R.layout.simple_list_item_1, null);
                TextView label=(TextView)row.findViewById(android.R.id.text1); 
                label.setText(((UAVObject)(objects[position])).getObjName());
            }

            if (System.currentTimeMillis()-act_obj.getMetaData().getLastDeserialize()<2550)
                row.setBackgroundColor(Color.argb( 255- (int) (System.currentTimeMillis()-act_obj.getMetaData().getLastDeserialize())/10 , 0x23,0x23,0xFF));

            row.setOnClickListener(this);
            row.setOnTouchListener(this);

            row.setTag(act_obj);
            return row;

        }

        public void onClick(View v) {
            //new AlertDialog.Builder(context).setMessage("foo").setSingleChoiceItems(new CharSequence[] { CharSequence .class ("Description"),"Meta Data"},-1, null).show();
            UAVObject obj=((UAVObject)(v.getTag()));
            UAVObjectMetaData obj_meta=obj.getMetaData();
            switch (myAction) {
            case ACTION_EDIT_UAVOBJECT:
            case ACTION_PICK_UAVOBJECT:
                Intent fieldListActivity=new Intent(context,UAVObjectFieldListActivity.class);
                //fieldListActivity.putExtra("action",myAdapter
                fieldListActivity.putExtra("objid", obj.getObjID());
                fieldListActivity.putExtra("action", myAction);
                startActivityForResult(fieldListActivity, 0);
                //startActivity(fieldListActivity);
                break;

            case ACTION_SHOWMETA_UAVOBJECT:
                String msg="";
                msg+="ID " + obj.getObjID()+"\n";

                msg+="gcs Access: " + UAVObjectMetaData.getAccessString(obj_meta.getGCSAccess()) +"\n";
                msg+="gcs Update Mode: " + UAVObjectMetaData.getUpdateModeString(obj_meta.getGCSTelemetryUpdateMode()) +"\n";
                msg+="gcs Update Period: " + obj_meta.getGCSTelemetryUpdatePeriod() +"\n";

                msg+="flight Access: " + UAVObjectMetaData.getAccessString(obj_meta.getFlightAccess()) +"\n";
                msg+="flight Update Mode: " + UAVObjectMetaData.getUpdateModeString(obj_meta.getFlightTelemetryUpdateMode()) +"\n";
                msg+="flight Update Period: " + obj_meta.getFlightTelemetryUpdatePeriod() +"\n";

                msg+="logging update mode: " + UAVObjectMetaData.getUpdateModeString(obj_meta.getLoggingUpdateMode()) + "\n";
                msg+="logging update period: " +obj_meta.getLoggingUpdatePeriod() + "\n";

                new AlertDialog.Builder(context)
                .setTitle("Meta Data for " + obj.getObjName())
                .setMessage(msg)
                .setPositiveButton("OK", new DialogDiscarder())
                .show();

                break;
            }
        }

        public boolean onTouch(View v, MotionEvent arg1) {
            v.setBackgroundResource(android.R.drawable.list_selector_background);
            v.setPadding(10, 0, 0, 0);
            return false;
        }		
    }

}
