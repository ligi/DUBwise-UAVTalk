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

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.SystemAlarms;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        DUBwiseUAVTalkActivityCommons.before_content(this);
        
        this.setContentView(R.layout.list);

        refreshAlarmVector();
		
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
        myAlarmVector=new Vector<SystemAlarmClass>();

        // interesting ones first
        byte[] level_order_arr={SystemAlarms.ALARM_CRITICAL,SystemAlarms.ALARM_ERROR,SystemAlarms.ALARM_WARNING,SystemAlarms.ALARM_OK,SystemAlarms.ALARM_UNINITIALISED};
		
        for (int lvl_i=0;lvl_i<level_order_arr.length;lvl_i++)
        	for (int i=0;i<UAVObjects.getSystemAlarms().getAlarm().length;i++)
        		if (level_order_arr[lvl_i]==UAVObjects.getSystemAlarms().getAlarm()[i])
        			myAlarmVector.add(new SystemAlarmClass(SystemAlarms.getAlarmElementNames()[i],UAVObjects.getSystemAlarms().getAlarm()[i]));

    }
    private Vector<SystemAlarmClass> myAlarmVector;
    
    class SystemAlarmClass {
    	private String label;
    	private byte level;
    	public SystemAlarmClass(String label,byte level) {
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

    	private LayoutInflater mLayoutInflater;

        public myArrayAdapter(Activity context) {
            super();
            mLayoutInflater=LayoutInflater.from(context);
        } 
        
        public View getView(int position, View convertView, ViewGroup parent) {
        	View view=mLayoutInflater.inflate(R.layout.icon_and_text4list, null);
        	
        	SystemAlarmClass act_alarm=myAlarmVector.get(position);
        	
        	
            TextView label_tv=(TextView)view.findViewById(R.id.alarm_txt);
            label_tv.setText(act_alarm.getLabel());
            ImageView alert_img=(ImageView)view.findViewById(R.id.alarm_image);
            switch (act_alarm.getLevel()) {
            case SystemAlarms.ALARM_UNINITIALISED:
            	alert_img.setBackgroundResource(R.drawable.error_inactive);
            	break;
            
            case SystemAlarms.ALARM_CRITICAL:
            	alert_img.setBackgroundResource(R.drawable.error_inactive);
            	label_tv.setText(label_tv.getText()+"(!)"); // ;-)
            	// no break wanted
            case SystemAlarms.ALARM_ERROR:
            	alert_img.setBackgroundResource(R.drawable.error_active);
            	break;
            	
            case SystemAlarms.ALARM_WARNING:
            	alert_img.setBackgroundResource(R.drawable.error_yellow);
            	break;

            case SystemAlarms.ALARM_OK:
            	alert_img.setBackgroundResource(R.drawable.error_green);
            	break;
            }
            
            return(view); 
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
