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

package org.ligi.android.dubwise_uavtalk.outputtest;


import org.ligi.android.commons.SeekBarMinMax;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Activity to test the outputs of the UAV
 * based on the channelView
 * 
 * @author ligi
 *
 */
public class OutputTestActivity extends ListActivity {

    private myArrayAdapter adapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTheme(R.style.base_theme);
        this.setContentView(R.layout.list);

        adapter=new myArrayAdapter(this);
        this.setListAdapter(adapter);

		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryUpdateMode(UAVObjectMetaData.UPDATEMODE_ONCHANGE);
		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryUpdatePeriod(100);
		UAVObjects.getActuatorCommand().getMetaData().setFlightTelemetryUpdateMode(UAVObjectMetaData.UPDATEMODE_ONCHANGE);
		UAVObjects.getActuatorCommand().getMetaData().setGCSTelemetryAcked(false);
		UAVObjects.getActuatorCommand().getMetaData().setFlightAccess(UAVObjectMetaData.ACCESS_READONLY);
        
        new AlertDialog.Builder(this)
        .setTitle(R.string.special_awareness)
        .setMessage(R.string.outputtest_warning)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton("OK",new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
			}
        })        
        .setNegativeButton("BACK!",new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
        })
        .show();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class myArrayAdapter extends BaseAdapter { 

        private Activity context; 

        public myArrayAdapter(Activity context) {
            super();

            this.context=context;
        } 

        
        public View getView(int position, View convertView, ViewGroup parent) { 
        

            LinearLayout lin=new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

            FrameLayout frame=new FrameLayout(context);
            frame.setLayoutParams(lp);

            int max=UAVObjects.getActuatorSettings().getChannelMax()[position];
            final int min=UAVObjects.getActuatorSettings().getChannelMin()[position];
            int val=UAVObjects.getActuatorCommand().getChannel()[position];
            
            SeekBarMinMax seekbar= new SeekBarMinMax(context,min,max);
            seekbar.setTag(position);
            final TextView value_tv=new TextView(context); 
            
            seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					int[] chan_arr=UAVObjects.getActuatorCommand().getChannel();
					chan_arr[(Integer)seekBar.getTag()]=seekBar.getProgress();
					UAVObjects.getActuatorCommand().setChannel(chan_arr);
					value_tv.setText(""+UAVObjects.getActuatorCommand().getChannel()[(Integer)seekBar.getTag()]);
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
				} });
            
            
            seekbar.setVerticalFadingEdgeEnabled(false);
            seekbar.setPadding(3, 3, 3, 1);
            seekbar.setProgress(val);
            
            frame.addView(seekbar);

            TextView text = new TextView(context);
            String txt=	"channel #" + position; 
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


            value_tv.setGravity(Gravity.CENTER);
            value_tv.setText(""+val);
            value_tv.setPadding(7, 0, 0,0);

            frame_minmax.addView(min_tv);
            frame_minmax.addView(max_tv);
            frame_minmax.addView(value_tv);
            lin.addView(frame_minmax);
            
            return(lin); 
        }

        public int getCount() {
            return UAVObjects.getActuatorCommand().getChannel().length;
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
