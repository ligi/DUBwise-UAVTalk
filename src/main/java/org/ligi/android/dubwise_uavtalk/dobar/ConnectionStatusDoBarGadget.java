package org.ligi.android.dubwise_uavtalk.dobar;

import org.ligi.android.dubwise_uavtalk.connection.HandshakeStatusAlertDialog;
import org.ligi.android.dubwise_uavtalk.connection.UAVTalkGCSThread;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.FlightTelemetryStats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * View to show the connection-status
 * 
 * @author ligi
 *
 */
public class ConnectionStatusDoBarGadget extends View implements Runnable,DoBarGadget {

	private boolean running;
	
	private int draw_round=0;
	private int last_bytes=0;

	private Drawable icon_inactive;
	private Drawable icon_active;
	private Drawable icon_flow1;
	private Drawable icon_flow2;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		HandshakeStatusAlertDialog.show(this.getContext(),false,null);
		
		return super.onTouchEvent(event);
	}

	public ConnectionStatusDoBarGadget(Context context) {
		super(context);
		start();
	}
	
	public ConnectionStatusDoBarGadget(Context context,AttributeSet attrs) {
		super(context,attrs);
		start();
	}

	private void start() {
		running=true;
		icon_inactive=this.getResources().getDrawable(R.drawable.connstate_inactive);
		icon_active=this.getResources().getDrawable(R.drawable.connstate_active);
		icon_flow1=this.getResources().getDrawable(R.drawable.connstate_flow1);
		icon_flow2=this.getResources().getDrawable(R.drawable.connstate_flow2);
		
		new Thread(this).start();
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Rect bounds=new Rect(0,0,w,h);
		icon_inactive.setBounds(bounds);
		icon_active.setBounds(bounds);
		icon_flow1.setBounds(bounds);
		icon_flow2.setBounds(bounds);
		
	}
	
	private Drawable getActDrawable() {
		switch (UAVObjects.getFlightTelemetryStats().getStatus()) {
		case FlightTelemetryStats.STATUS_CONNECTED:
			Drawable res=icon_flow2;
			if ((((draw_round/3)%2)==0)&&(UAVTalkGCSThread.getInstance().getRxPackets()>last_bytes))
				res= icon_flow1;
			
			last_bytes=UAVTalkGCSThread.getInstance().getRxPackets();
			return res;
		case FlightTelemetryStats.STATUS_DISCONNECTED:
			return icon_inactive;

		case FlightTelemetryStats.STATUS_HANDSHAKEACK:
		case FlightTelemetryStats.STATUS_HANDSHAKEREQ:
			return icon_active;
		}
		Log.w("no Icon for status");
		return icon_inactive;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		draw_round++;
		getActDrawable().draw(canvas);
	}


	public void run() {
		while (running) {
			this.postInvalidate();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {	}
		}
	}


	public View getView() {
		return this;
	}

}
