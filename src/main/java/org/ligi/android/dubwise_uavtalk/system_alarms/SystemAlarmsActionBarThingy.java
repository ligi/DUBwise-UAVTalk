package org.ligi.android.dubwise_uavtalk.system_alarms;

import org.ligi.androidhelper.AndroidHelper;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.SystemAlarms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author ligi
 *
 */
public class SystemAlarmsActionBarThingy extends View {

	private boolean uavtalk_ok;
	private Paint mPaint;
	
	public SystemAlarmsActionBarThingy(Context context, AttributeSet attrs) {
		super(context, attrs);
		start();
	}
	
	public SystemAlarmsActionBarThingy(Context context) {
		super(context);
		start();
	}
	
	private void start() {

		mPaint=new Paint();
		mPaint.setFakeBoldText(true);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setAntiAlias(true);
		// check for UAVTalk assumptions to be correct
		uavtalk_ok=SystemAlarms.ALARM_UNINITIALISED<SystemAlarms.ALARM_OK;
		uavtalk_ok&=SystemAlarms.ALARM_OK<SystemAlarms.ALARM_WARNING;
		uavtalk_ok&=SystemAlarms.ALARM_WARNING<SystemAlarms.ALARM_ERROR;
		uavtalk_ok&=SystemAlarms.ALARM_ERROR<SystemAlarms.ALARM_CRITICAL;
		
		if (uavtalk_ok)
			Log.w("assumption about UAVTalk where wrong ( SystemAlarms Enumeration )");
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mPaint.setTextSize(4*h/5);
	}

	/**
	 * class to hold a color and a symbol for error representation
	 * @author ligi
	 *
	 */
	class ColorAndSymbol {
		private int color;
		private String symbol;
		
		public ColorAndSymbol(int color,String symbol) {
			this.color=color;
			this.symbol=symbol;
		}
		
		public int getColor() {
			return color;
		}
		
		public String getSymbol() {
			return symbol;
		}
	}

	private ColorAndSymbol getColorAndSymbolByLevel(byte level) {
		if (!uavtalk_ok)
			return new ColorAndSymbol(Color.RED,"!!");
		
		switch (level) {
			case SystemAlarms.ALARM_UNINITIALISED:
				return new ColorAndSymbol(Color.GRAY,":|"); 
			
			case SystemAlarms.ALARM_OK:
				return new ColorAndSymbol(Color.GREEN,";)"); 
			
			case SystemAlarms.ALARM_WARNING:
				return new ColorAndSymbol(Color.RED,":("); 
	
			case SystemAlarms.ALARM_CRITICAL:
				return new ColorAndSymbol(Color.RED,"!("); 
		}
		
		return new ColorAndSymbol(Color.GRAY,"?|");
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
		ColorAndSymbol cas= getColorAndSymbolByLevel(getMaxErrorLevel());
		mPaint.setColor(cas.getColor());
		canvas.drawText(cas.getSymbol(), this.getWidth()/2,this.getHeight()-mPaint.getFontMetrics().descent, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		AndroidHelper.at(getContext()).startActivityForClass(SystemAlarmsActivity.class);
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

}
