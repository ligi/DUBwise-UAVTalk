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

import org.ligi.android.dubwise.rc.CurveEditView;
import org.ligi.android.dubwise.rc.CurveEditView.OnChangeListener;
import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectLoadHelper;
import org.ligi.android.dubwise_uavtalk.uavobjects_helper.UAVObjectPersistHelper;
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity to edit a curve ( e.g. ThrottleCurve )
 * 
 * @author ligi
 *
 */
public class CurveEditActivity extends Activity implements OnChangeListener {

	private TextView[] val_tv;
	private CurveEditView cev;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DUBwiseUAVTalkActivityCommons.before_content(this);

        this.setContentView(R.layout.curve_edit);
        cev=((CurveEditView)this.findViewById(R.id.curve_edit));

         val_tv=new TextView[] { ((TextView)this.findViewById(R.id.curve_val1)),
						         ((TextView)this.findViewById(R.id.curve_val2)),
						         ((TextView)this.findViewById(R.id.curve_val3)),
						         ((TextView)this.findViewById(R.id.curve_val4)),
						         ((TextView)this.findViewById(R.id.curve_val5)) };
        
        cev.setOnChangeListener(this);
        
		Handler change_handler=new Handler() {
			public void handleMessage(Message msg) {
				cev.setCurve(UAVObjects.getMixerSettings().getThrottleCurve1());
			}
		};

        UAVObjectLoadHelper.loadWithDialog(this, UAVObjects.getMixerSettings(),change_handler);
        
    }
    
    public void updateTextFields() {
    	for (int i=0;i<val_tv.length;i++)
			val_tv[i].setText(String.format("%.3f", cev.getCurvePoint(i)));
    }

	public void notifyChange() {
		updateTextFields();
		UAVObjects.getMixerSettings().setThrottleCurve1(cev.getCurve());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.curve_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_revert:
			cev.setCurve(new float[] { 0.0f,0.25f,0.5f,0.75f,1f });
			cev.invalidate();
			break;
			
		case R.id.menu_save:
			UAVObjectPersistHelper.persistWithDialog(this,UAVObjects.getMixerSettings());
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
