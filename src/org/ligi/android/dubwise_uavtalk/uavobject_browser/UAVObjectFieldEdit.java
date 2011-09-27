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

package org.ligi.android.dubwise_uavtalk.uavobject_browser;

import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * class to handle editing of a UAVObject Value
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVObjectFieldEdit {

    /**
     * shows a dialog to let the user edit a UAVObject value
     * 
     * @param ctx the context to create the dialog in
     * @param objid
     * @param fieldid
     * @param arr_pos
     * @param adapter2notify an adapter to notify if data has been changed
     */
    public static void editField(Context ctx,int objid,int fieldid,final byte arr_pos,BaseAdapter adapter2notify) {

        UAVObjectFieldDescription descr=UAVObjects.getObjectByID(objid).getFieldDescriptions()[fieldid];
        AlertDialog.Builder alert=(new AlertDialog.Builder(ctx)).setTitle("Edit " + descr.getName());

        switch (descr.getType()) {
        case UAVObjectFieldDescription.FIELDTYPE_ENUM:
            Spinner s=new Spinner(ctx);


            ArrayAdapter<String> enum_adapter=new ArrayAdapter<String>(ctx,R.layout.simple_spinner_item,descr.getEnumOptions());
            enum_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            s.setAdapter(enum_adapter);
            s.setSelection((Byte) UAVObjects.getObjectByID(descr.getObjId()).getField(descr.getFieldId(),arr_pos)); 

            alert.setView(s);
            //s.setOnItemSelectedListener(new foo extends OnItemSelectedListener)
            class myItemSelectedListener implements OnItemSelectedListener {

                private UAVObjectFieldDescription descr;
                private BaseAdapter arr_adapt2notify;

                public myItemSelectedListener(UAVObjectFieldDescription descr,BaseAdapter arr_adapt2nofify) {
                    this.descr=descr;
                    this.arr_adapt2notify= arr_adapt2nofify;
                }

                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Log.d("Setting to " + arg2);
                    UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),arr_pos,(byte)arg2);
                    arr_adapt2notify.notifyDataSetChanged();
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }

            }
            s.setOnItemSelectedListener(new myItemSelectedListener(descr,adapter2notify));

            break;

        case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
            alert.setView(edit_number(ctx,descr,arr_pos,InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED));
            break;

        case UAVObjectFieldDescription.FIELDTYPE_INT8:
        case UAVObjectFieldDescription.FIELDTYPE_INT16:
        case UAVObjectFieldDescription.FIELDTYPE_INT32:
            alert.setView(edit_number(ctx,descr,arr_pos,InputType.TYPE_NUMBER_FLAG_SIGNED));
            break;

        case UAVObjectFieldDescription.FIELDTYPE_UINT8:
        case UAVObjectFieldDescription.FIELDTYPE_UINT16:
        case UAVObjectFieldDescription.FIELDTYPE_UINT32:
            alert.setView(edit_number(ctx,descr,arr_pos,0));
            break;
        }

        alert.setPositiveButton("OK", null).show();

    }

    private static EditText edit_number(Context ctx,UAVObjectFieldDescription descr,final byte pos,int flags) {
        EditText txt=new EditText(ctx);
        txt.setInputType(InputType.TYPE_CLASS_NUMBER | flags);
        txt.setText(UAVObjectFieldHelper.getFieldValueStr(descr,pos));

        class myTxtWatcher implements TextWatcher {

            UAVObjectFieldDescription descr;

            public myTxtWatcher(UAVObjectFieldDescription descr) {
                this.descr=descr;
            }

            public void afterTextChanged(Editable s) {
                try {
                    switch (descr.getType()) {

                    case UAVObjectFieldDescription.FIELDTYPE_FLOAT32:
                        UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),pos, Float.parseFloat(s.toString()));
                        break;

                    case UAVObjectFieldDescription.FIELDTYPE_UINT8:
                    case UAVObjectFieldDescription.FIELDTYPE_UINT16:
                    case UAVObjectFieldDescription.FIELDTYPE_UINT32:
                    case UAVObjectFieldDescription.FIELDTYPE_INT8:
                    case UAVObjectFieldDescription.FIELDTYPE_INT16:
                    case UAVObjectFieldDescription.FIELDTYPE_INT32:
                        UAVObjects.getObjectByID(descr.getObjId()).setField(descr.getFieldId(),pos, Integer.parseInt(s.toString()));
                        break;
                    }
                } catch (java.lang.NumberFormatException e) { } // be robust non valid user input 
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        }

        txt.addTextChangedListener(new myTxtWatcher(descr) );

        return txt;

    }

}
