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

package org.ligi.android.dubwise_uavtalk.uavtalk;

import org.openpilot.uavtalk.UAVObjects;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Activity to list the elements of an array from an UAVObjects-field
 *  
 * @author ligi the UAVObjects
 *
 */
public class UAVObjectFieldArrayListActivity extends ListActivity {

    private int objid; 
    private int fieldid; 
    private myArrayAdapter ma;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objid=this.getIntent().getIntExtra("objid", 0);
        fieldid=this.getIntent().getIntExtra("fieldid", 0);

        UAVObjects.init();
        ma=new myArrayAdapter(this, android.R.layout.simple_list_item_1, UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getElementNames());
        this.setListAdapter(ma);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        UAVObjectFieldEdit.editField(this,objid, fieldid,(byte)position,ma);
    }

    class myArrayAdapter extends ArrayAdapter<String> {

        private Context context;
        public myArrayAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.context=context;
        }


        public View getView(int position, View convertView, ViewGroup parent) { 
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=vi.inflate(android.R.layout.two_line_list_item, null); 
            TextView label=(TextView)row.findViewById(android.R.id.text1); 

            String txt=UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getElementNames()[position] + " : ";
            txt+=UAVObjectFieldHelper.getFieldValueStr(UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid], (byte)position);
            if (UAVTalkPrefs.isUAVTalkUnitDisplayEnabled())
                txt+=" " +UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid].getUnit();
            label.setText(txt);
            return row;

        }
    }
}
