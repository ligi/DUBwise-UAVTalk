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
import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity to edit a curve
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

        this.setContentView(R.layout.curve_edit);
        cev=((CurveEditView)this.findViewById(R.id.curve_edit));

        ((Button)this.findViewById(R.id.reset_btn)).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				cev.setCurve(new float[] { 0.0f,0.25f,0.5f,0.75f,1f });
				cev.invalidate();
			}
        	
        });        
        val_tv=new TextView[] { ((TextView)this.findViewById(R.id.curve_val1)),
						         ((TextView)this.findViewById(R.id.curve_val2)),
						         ((TextView)this.findViewById(R.id.curve_val3)),
						         ((TextView)this.findViewById(R.id.curve_val4)),
						         ((TextView)this.findViewById(R.id.curve_val5)) };
        
        cev.setOnChangeListener(this);
        
        float[] f=UAVObjects.getMixerSettings().getThrottleCurve1();
        cev.setCurve(f);
    }

	public void notifyChange() {
		for (int i=0;i<5;i++)
			val_tv[i].setText(String.format("%.3f", cev.getCurvePoint(i)));
		
		UAVObjects.getMixerSettings().setThrottleCurve1(cev.getCurve());
	}
}
