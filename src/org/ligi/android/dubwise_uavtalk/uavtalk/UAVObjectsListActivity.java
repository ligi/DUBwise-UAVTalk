/**************************************************************************
 *                                                                       
gi * Author:  Marcus -LiGi- Bueschleb   
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

import java.util.Vector;

import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.android.dubwise_uavtalk.R;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
        
        Intent myIntent = getIntent();
        String action_str=myIntent.getAction();

        
        UAVObject[] uavobjects=UAVObjects.getUAVObjectArray();
        
        if (Intent.ACTION_SEARCH.equals(myIntent.getAction())) {
          String query = myIntent.getStringExtra(SearchManager.QUERY);

          Vector<UAVObject> obj_vector =new Vector<UAVObject>();
          Vector<UAVObject> founds_vector =new Vector<UAVObject>();
          for (int i=0;i<uavobjects.length;i++)
        	  obj_vector.add(uavobjects[i]);
          
          for (UAVObject obj:obj_vector)
        	  if (obj.getObjName().toUpperCase().contains(query.toUpperCase()))
        		  founds_vector.add(obj);

          Log.i("found " + founds_vector.size());
          for (UAVObject obj:founds_vector)
        	  Log.i("found " + obj.getObjName());
          uavobjects=new UAVObject[founds_vector.size()];
          founds_vector.copyInto(uavobjects);
        }
        
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
        
        
        UAVObjectsArrayAdapter adapter=new UAVObjectsArrayAdapter(this, android.R.layout.simple_list_item_1, uavobjects);
        this.setListAdapter( adapter);
        //new PeriodicallyInvalidateAdapter(this,adapter);

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

    class UAVObjectsArrayAdapter extends ArrayAdapter<UAVObject> implements OnTouchListener,OnClickListener,OnLongClickListener {

        private Context context;
        private UAVObject[] objects;

        public UAVObjectsArrayAdapter(Context context, int textViewResourceId, UAVObject[] objects) {
            super(context, textViewResourceId, objects);
            this.context=context;
            this.objects=objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) { 
        	

            UAVObject act_obj=(UAVObject)(objects[position]);

            LinearLayout lin=new LinearLayout(context);
            lin.setOrientation(LinearLayout.HORIZONTAL);
            ProgressBar active=new ProgressBar(context);
           
            // TODO make new view showing in and output
            
            TextView tv=new TextView(context);
            
            class ActivityUpdater implements Runnable {
            	
            	private UAVObject uavobject;
            	private View v;
            	Handler h=new Handler();
            	
            	public ActivityUpdater(UAVObject uavobject,View view) {
            		
            		this.v=view;
            		
            		this.uavobject=uavobject;
            		new Thread(this).start();
            	}
            	
				@Override
				public void run() {
					while (v.getVisibility()!=View.GONE) {
						
						class SetVisibilityRunnable implements Runnable {

							public void run() {
								if (uavobject.getMetaData().getLastDeserialize()-System.currentTimeMillis()+500>0)
									v.setVisibility(View.VISIBLE);
								else
									v.setVisibility(View.INVISIBLE);
							}
							
						}
						
						h.post(new SetVisibilityRunnable());
						
						
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
            		}
				}
            	
            }
            

            
            new ActivityUpdater(((UAVObject)(objects[position])),active);
            tv.setText(((UAVObject)(objects[position])).getObjName());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_MM, 15.0f);
            lin.addView(tv);
            lin.addView(active);
            
            /*if (System.currentTimeMillis()-act_obj.getMetaData().getLastDeserialize()<2550)
                row.setBackgroundColor(Color.argb( 255- (int) (System.currentTimeMillis()-act_obj.getMetaData().getLastDeserialize())/10 , 0x23,0x23,0xFF));

            
            row.setOnClickListener(this);
            row.setOnTouchListener(this);
*/
            lin.setTag(act_obj);
            lin.setOnClickListener(this);
         
            lin.setOnLongClickListener(this);
            lin.setOnTouchListener(this);
            
            return lin;

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

		@Override
		public boolean onLongClick(View v) {
			new AlertDialog.Builder(context).setMessage("longclick").show();
			return true;
		}

    }

}
