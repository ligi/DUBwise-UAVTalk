package org.ligi.android.dubwise_uavtalk.system_alarms;

import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.axt.AXT;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.SystemAlarms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author ligi
 *
 */
public class IconicSystemAlarmsActionBarThingy extends View implements Runnable {

	private boolean uavtalk_ok;

	private Drawable icon_uninitialized;
	private Drawable icon_ok;
	private Drawable icon_warning;
	private Drawable icon_error;
	private Drawable icon_critical;

	private boolean running=true;
	private int run=0;

	public IconicSystemAlarmsActionBarThingy(Context context, AttributeSet attrs) {
		super(context, attrs);
		start();
	}
	
	public IconicSystemAlarmsActionBarThingy(Context context) {
		super(context);
		start();
	}
	
	private void start() {

		
		icon_ok=this.getResources().getDrawable(R.drawable.error_green);
		icon_error=this.getResources().getDrawable(R.drawable.error_active);
		icon_critical=this.getResources().getDrawable(R.drawable.error_sel);
		icon_uninitialized=this.getResources().getDrawable(R.drawable.error_inactive);
		icon_warning=this.getResources().getDrawable(R.drawable.error_yellow);
		
		// check for UAVTalk assumptions to be correct
		uavtalk_ok=SystemAlarms.ALARM_UNINITIALISED<SystemAlarms.ALARM_OK;
		uavtalk_ok&=SystemAlarms.ALARM_OK<SystemAlarms.ALARM_WARNING;
		uavtalk_ok&=SystemAlarms.ALARM_WARNING<SystemAlarms.ALARM_ERROR;
		uavtalk_ok&=SystemAlarms.ALARM_ERROR<SystemAlarms.ALARM_CRITICAL;
		
		if (uavtalk_ok)
			Log.w("assumption about UAVTalk where wrong ( SystemAlarms Enumeration )");
		new Thread(this).start();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Rect bounds =new Rect(0,0,w,h);
		icon_ok.setBounds(bounds);
		icon_error.setBounds(bounds);
		icon_critical.setBounds(bounds); 
		icon_warning.setBounds(bounds); 
		icon_uninitialized.setBounds(bounds); 
	}
	
	private byte getMaxErrorLevel() {
		byte max_level=SystemAlarms.ALARM_UNINITIALISED;
		
		for (byte b: UAVObjects.getSystemAlarms().serialize()) 
			if (b>max_level)
				max_level=b;
				
		return max_level;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		switch(getMaxErrorLevel()) {
		case SystemAlarms.ALARM_UNINITIALISED:
			icon_uninitialized.draw(canvas);
			break;
		case SystemAlarms.ALARM_OK:
			icon_ok.draw(canvas);
			break;
		case SystemAlarms.ALARM_WARNING:
			icon_warning.draw(canvas);
			break;
		case SystemAlarms.ALARM_ERROR:
			icon_error.draw(canvas);
			break;
		case SystemAlarms.ALARM_CRITICAL:
			
			if ((run/3)%2==0)
				icon_critical.draw(canvas);
			else
				icon_error.draw(canvas);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		AXT.at(getContext()).startActivityForClass(SystemAlarmsActivity.class);
		return super.onTouchEvent(event);
	}
	
	
	@Override
	public void run() {
		while (running){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {	}
			this.postInvalidate();
			run++;
		}
	}

}
