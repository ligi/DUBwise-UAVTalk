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

package org.ligi.android.dubwise_uavtalk.system_alarms;

import java.util.Vector;

import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.SystemAlarms;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Shows the SystemAlarms
 *  - sort by level
 *  
 * @author ligi
 *
 */
public class SystemAlarmsActivity extends ListActivity {

    private myArrayAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		
        adapter=new myArrayAdapter(this);
        this.setListAdapter(adapter);
        this.setTitle("UAV-SystemAlarms");
		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryUpdateMode(UAVObjectMetaData.UPDATEMODE_ONCHANGE);
		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryUpdatePeriod(100);
		UAVObjects.getActuatorCommand().getMetaData().setFlightTelemetryUpdateMode(UAVObjectMetaData.UPDATEMODE_ONCHANGE);
		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryAcked(false);
		UAVObjects.getActuatorCommand().getMetaData().setFlightAccess(UAVObjectMetaData.ACCESS_READONLY);
		
    }

    public void refreshAlarmVector() {
        myAlarmVector=new Vector<myAlarm>();
        
        byte[] level_order_arr={SystemAlarms.ALARM_CRITICAL,SystemAlarms.ALARM_ERROR,SystemAlarms.ALARM_WARNING,SystemAlarms.ALARM_OK,SystemAlarms.ALARM_UNINITIALISED};
		
        for (int lvl_i=0;lvl_i<level_order_arr.length;lvl_i++)
        	for (int i=0;i<UAVObjects.getSystemAlarms().getAlarm().length;i++)
        		if (level_order_arr[lvl_i]==UAVObjects.getSystemAlarms().getAlarm()[i])
        			myAlarmVector.add(new myAlarm(SystemAlarms.getAlarmElementNames()[i],UAVObjects.getSystemAlarms().getAlarm()[i]));

    }
    private Vector<myAlarm> myAlarmVector;
    
    class myAlarm {
    	private String label;
    	private byte level;
    	public myAlarm(String label,byte level) {
    		this.label=label;
    		this.level=level;
    	}
		
		public String getLabel() {
			return label;
		}
		
		public byte getLevel() {
			return level;
		}
    	
    }
    class myArrayAdapter extends BaseAdapter { 

        private Activity context; 

        public myArrayAdapter(Activity context) {
            super();
            this.context=context;
        } 
        
        public View getView(int position, View convertView, ViewGroup parent) {
        	myAlarm act_alarm=myAlarmVector.get(position);
            LinearLayout lin=new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            TextView label_tv=new TextView(context);
            label_tv.setText(act_alarm.getLabel());
            switch (act_alarm.getLevel()) {
            case SystemAlarms.ALARM_UNINITIALISED:
            	lin.setBackgroundColor(Color.GRAY);
            	label_tv.setTextColor(Color.BLACK);
            	break;
            case SystemAlarms.ALARM_ERROR:
            	label_tv.setTextColor(Color.WHITE);
            	lin.setBackgroundColor(Color.RED);
            case SystemAlarms.ALARM_CRITICAL:
            	label_tv.setText(label_tv.getText()+"(!"); // ;-)
            	break;
            	
            case SystemAlarms.ALARM_WARNING:
            	lin.setBackgroundColor(Color.YELLOW);
            	label_tv.setTextColor(Color.BLACK);
            	break;

            case SystemAlarms.ALARM_OK:
            	lin.setBackgroundColor(Color.GREEN);
            	label_tv.setTextColor(Color.BLACK);
            	break;
            }
            
            label_tv.setTextSize(TypedValue.COMPLEX_UNIT_MM,15.0f);
            lin.addView(label_tv);
            return(lin); 
        }

        public int getCount() {
            return myAlarmVector.size();
        }

        /**
         * do not use - not implemented!
         */
        public Object getItem(int arg0) {
            return null;
        }

        /**
         * do not use - not implemented!
         */
        public long getItemId(int position) {
            return 0;
        }

    }
}
