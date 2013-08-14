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
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.androidhelper.adapters.PeriodicallyInvalidateAdapter;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to List the Fields of an UAVObject with unit and value 
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectFieldListActivity extends UAVObjectFieldBaseActivity {
    
    private int action;
    private UAVObjectsFieldDescriptionArrayAdapter my_adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DUBwiseUAVTalkActivityCommons.before_content(this);
            
        this.setContentView(R.layout.list);

        objid=this.getIntent().getIntExtra("objid", 0);
        action=this.getIntent().getIntExtra("action", 0);

        UAVObject act_obj=UAVObjects.getObjectByID(objid);

        switch(action) {
        	case UAVObjectsListActivity.ACTION_EDIT_UAVOBJECT:
                this.setTitle("edit " + act_obj.getObjName());
                break;
        	case UAVObjectsListActivity.ACTION_PICK_UAVOBJECT:
                this.setTitle("pick from " + act_obj.getObjName());
                break;
        }
                    
        my_adapter=new UAVObjectsFieldDescriptionArrayAdapter(this, android.R.layout.simple_list_item_1, act_obj.getFieldDescriptions());
        this.setListAdapter(my_adapter);
        new PeriodicallyInvalidateAdapter(this,my_adapter);

    }

    /**
     * just passes the result to the next activity down the line
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK) {
            this.setResult(resultCode, data);
            finish();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (UAVObjects.getObjectByID(objid).getFieldDescriptions()[position].getElementNames().length>1) {
            Intent fieldArrayListActivity=new Intent(this,UAVObjectFieldArrayListActivity.class);
            fieldArrayListActivity.putExtra("objid", objid);
            fieldArrayListActivity.putExtra("fieldid", position);
            fieldArrayListActivity.putExtra("action",action);
            startActivity(fieldArrayListActivity);
            startActivityForResult(fieldArrayListActivity, 0);
        } else {
            switch(action) {
            case UAVObjectsListActivity.ACTION_EDIT_UAVOBJECT:
                UAVObjectFieldEdit.editField(this,objid, position,(byte)0,my_adapter);
                break;
            case UAVObjectsListActivity.ACTION_PICK_UAVOBJECT:
                Log.i("result field list");
                //UAVObjectFieldEdit.editField(this,objid, position,(byte)0,my_adapter);
                Intent result_intent=new Intent();
                result_intent.putExtra("objid",objid);
                result_intent.putExtra("fieldid",position);
                result_intent.putExtra("arraypos",0);
                this.setResult(Activity.RESULT_OK, result_intent);
                finish();
                break;

            }

        }
    }


    class UAVObjectsFieldDescriptionArrayAdapter extends ArrayAdapter<UAVObjectFieldDescription> {

        private Context context;
        private UAVObjectFieldDescription[] objects;

        public UAVObjectsFieldDescriptionArrayAdapter(Context context, int textViewResourceId, UAVObjectFieldDescription[] objects) {
            super(context, textViewResourceId, objects);
            this.context=context;
            this.objects=objects;
            //this.notifyDataSetChanged()
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { 
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=vi.inflate(android.R.layout.simple_list_item_1, null); 
            TextView label=(TextView)row.findViewById(android.R.id.text1); 
            UAVObjectFieldDescription act_obj=((UAVObjectFieldDescription)(objects[position]));
            String txt=act_obj.getName() + ": ";  

            if (act_obj.getElementNames().length>1) {
                txt+=">>";
            }
            else {
                txt+=UAVObjectFieldHelper.getFieldValueStr(act_obj);
                if (UAVTalkPrefs.isUAVTalkUnitDisplayEnabled())
                    txt+=" " + act_obj.getUnit();
            }

            label.setText(txt  );

            return row;
        }		
    }
    
	@Override
	void notifyContentChange() {
		my_adapter.notifyDataSetChanged();		
	}
}
