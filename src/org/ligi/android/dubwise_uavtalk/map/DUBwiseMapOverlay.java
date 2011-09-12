package org.ligi.android.dubwise_uavtalk.map;

import org.ligi.android.uavtalk.dubwise.R;
import org.openpilot.uavtalk.UAVObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DUBwiseMapOverlay extends Overlay {

	private Context ctx;
	private Bitmap uas_image;
	private Paint mPaint;
	private Matrix mtx = new Matrix();
	private final static int uas_icon_size=42;
	private Point uas_point=new Point();
	
	public DUBwiseMapOverlay(Context ctx) {
		this.ctx=ctx; 
		mPaint=new Paint();
		uas_image= Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.ctx.getResources(),
                     	R.drawable.uas_icon),uas_icon_size,uas_icon_size,true);
	}


	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

		mtx.setRotate(UAVObjects.getGPSPosition().getHeading()+70,uas_icon_size/2,uas_icon_size/2);
		
		mapView.getProjection().toPixels(new GeoPoint( UAVObjects.getGPSPosition().getLatitude()/10
				   ,UAVObjects.getGPSPosition().getLongitude()/10), uas_point);
		
		mtx.postTranslate(uas_point.x,  uas_point.y);
		
		canvas.drawBitmap(uas_image, mtx, mPaint);

		try {
			Thread.sleep(40);
		} catch (InterruptedException e) {}

		mapView.postInvalidate();
	}
}
