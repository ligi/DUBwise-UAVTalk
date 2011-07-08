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

package org.ligi.android.dubwise_uavtalk.channelview;

import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.ManualControlCommand;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * activity to view the channels
 * useful for checking if everything is alright with the RC
 * could later be extended to set up/calibrate the RC
 *  
 * @author ligi
 *
 */
public class ChannelViewActivity extends ListActivity implements Runnable {

    private myArrayAdapter adapter;
    private boolean running=true;
    private int old_update_period;
    private AlertDialog no_rc_alert;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTheme(R.style.base_theme);
        this.setContentView(R.layout.list);

        adapter=new myArrayAdapter(this);
        this.setListAdapter(adapter);

        // we want to have the data fast at 10Hz
        UAVObjectMetaData meta=UAVObjects.getManualControlCommand().getMetaData();
        old_update_period=meta.getFlightTelemetryUpdatePeriod();
        meta.setFlightTelemetryUpdatePeriod(100);

        no_rc_alert=new AlertDialog.Builder(this)
	        .setMessage(R.string.no_rc_warning)
	        .setTitle(R.string.no_rc_title)
	        .setOnCancelListener(new OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					finish();
				}
	        	
	        })
	        .create();
	        
        new Thread(this).start();
    }

    @Override
    protected void onStop() {
        UAVObjects.getManualControlCommand().getMetaData().setFlightTelemetryUpdatePeriod(old_update_period);
        super.onStop();
    }

    class myArrayAdapter extends BaseAdapter { 

        private Activity context; 

        public myArrayAdapter(Activity context) {
            super();

            this.context=context;
        } 

        public String channel2String(byte chan) {
            String res="";

            // chan+1 because none comes first
            // TODO find better way to make a localized String out of this enum

            if (chan==UAVObjects.getManualControlSettings().getYaw())
                res+=context.getString(R.string.yaw);
            if (chan==UAVObjects.getManualControlSettings().getPitch())
                res+=context.getString(R.string.pitch);
            if (chan==UAVObjects.getManualControlSettings().getRoll())
                res+=context.getString(R.string.roll);
            if (chan==UAVObjects.getManualControlSettings().getThrottle())
                res+=context.getString(R.string.throttle);
            if (chan==UAVObjects.getManualControlSettings().getAccessory0())
                res+=context.getString(R.string.accessory)+"1";
            if (chan==UAVObjects.getManualControlSettings().getAccessory1())
                res+=context.getString(R.string.accessory)+"2";
            if (chan==UAVObjects.getManualControlSettings().getAccessory2())
                res+=context.getString(R.string.accessory)+"3";

            if (res.equals(""))
                res=context.getString(R.string.none);

            return res;
        }

        public View getView(int position, View convertView, ViewGroup parent) { 

            LinearLayout lin=new LinearLayout(context);
            // ProgressBar p=   new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            lin.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

            FrameLayout frame=new FrameLayout(context);
            frame.setLayoutParams(lp);

            ProgressBar p= new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            p.setMax(256);
            p.setVerticalFadingEdgeEnabled(false);
            p.setPadding(3, 3, 3, 1);
            int max=UAVObjects.getManualControlSettings().getChannelMax()[position];
            int min=UAVObjects.getManualControlSettings().getChannelMin()[position];
            int val=UAVObjects.getManualControlCommand().getChannel()[position];
            p.setMax(max-min);
            p.setProgress(val-min);
            frame.addView(p);

            TextView text = new TextView(context);
            String txt=	channel2String((byte)position)+"="+val; 
            text.setText( txt );
            text.setTextColor(0xFF000000);
            text.setShadowLayer(2, 1, 1, 0xffffffff);
            text.setPadding(17, 3, 3, 1);
            frame.addView(text);

            lin.addView(frame);

            FrameLayout frame_minmax=new FrameLayout(context);
            frame_minmax.setLayoutParams(lp);

            TextView min_tv=new TextView(context);
            min_tv.setText(""+min);
            min_tv.setPadding(7, 0, 0,0);

            TextView max_tv=new TextView(context);
            max_tv.setGravity(Gravity.RIGHT);
            max_tv.setText(""+max);
            max_tv.setPadding(0, 0, 7,0);

            TextView neutral_tv=new TextView(context);
            neutral_tv.setGravity(Gravity.CENTER);
            neutral_tv.setText(""+UAVObjects.getManualControlSettings().getChannelNeutral()[position]);
            neutral_tv.setPadding(7, 0, 0,0);

            frame_minmax.addView(min_tv);
            frame_minmax.addView(max_tv);
            frame_minmax.addView(neutral_tv);
            lin.addView(frame_minmax);

            return(lin); 
        }

        public int getCount() {
            return UAVObjects.getManualControlCommand().getChannel().length;
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

    @Override
    protected void onDestroy() {
        running=false;
        super.onDestroy();
    }
    
    

    final Handler mHandler = new Handler();
    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
        	boolean connected=UAVObjects.getManualControlCommand().getConnected() == ManualControlCommand.CONNECTED_TRUE;
            if ((no_rc_alert.isShowing())&&(connected))
            	no_rc_alert.hide();
            if ((!no_rc_alert.isShowing())&&(!connected))
            	no_rc_alert.show();	
            
            if (connected) 
            	adapter.notifyDataSetChanged();
            
        }
    };

    public int getRefreshSleep() {
        return 100;
    }

    public void run() {
        while (running) {
            mHandler.post(mUpdateResults);
            try {
                Thread.sleep(getRefreshSleep());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
