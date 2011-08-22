package org.ligi.android.dubwise_uavtalk.dobar;

import org.ligi.android.dubwise_uavtalk.connection.HandshakeStatusAlertDialog;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.FlightTelemetryStats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
		new Thread(this).start();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		switch (UAVObjects.getFlightTelemetryStats().getStatus()) {
		case FlightTelemetryStats.STATUS_CONNECTED:
			canvas.drawColor(Color.GREEN);
			break;
		case FlightTelemetryStats.STATUS_DISCONNECTED:
			canvas.drawColor(Color.RED);
			break;
		case FlightTelemetryStats.STATUS_HANDSHAKEACK:
		case FlightTelemetryStats.STATUS_HANDSHAKEREQ:
			canvas.drawColor(Color.YELLOW);
			break;
		}
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
