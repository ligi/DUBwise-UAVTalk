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
package org.ligi.android.dubwise_uavtalk.instruments;

import org.ligi.android.dubwise_uavtalk.R;
import org.openpilot.uavtalk.UAVObjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ArtificialHorizon extends View{

    public ArtificialHorizon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	//private View mParent;
    private Drawable ground_drawable;
    private Drawable sky_drawable;
    private int bar_height=20;
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	mWhiteLinePaint.setStrokeWidth(h/400+1);
    	mBlackLinePaint.setStrokeWidth(h/42+1);
    	
        mWhiteLinePaint.setTextSize(h/23);
	}

	private Paint mWhiteLinePaint;
    private Paint mBlackLinePaint;
    
    
    public void init() {
    	if (!isInEditMode())
    		UAVObjects.getAttitudeActual().getMetaData().setFlightTelemetryUpdatePeriod(100);
        ground_drawable=getResources().getDrawable(R.drawable.horizon_earth);
        sky_drawable=getResources().getDrawable(R.drawable.horizon_sky);
        mWhiteLinePaint=new Paint();
        
        mWhiteLinePaint.setStyle(Paint.Style.FILL);
        mWhiteLinePaint.setColor(Color.WHITE);
        mBlackLinePaint=new Paint();
        
        mBlackLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBlackLinePaint.setColor(Color.BLACK);

        mBlackLinePaint.setAntiAlias(true);
        mWhiteLinePaint.setAntiAlias(true);
    }
    
    private float getRoll() {
    	if (!isInEditMode())
    		return UAVObjects.getAttitudeActual().getRoll();
    	
    	return 0.0f;
    }

    private float getPitch() {
    	if (!isInEditMode())
    		return UAVObjects.getAttitudeActual().getPitch();
    	
    	return 0.0f;
    }

    private final static float line_width=3;
    
    public void draw(Canvas canvas) {
    	
    	canvas.save();
    	bar_height=getHeight()/17+1;
    	// TODO check direction
    	
    	float line_dist=getHeight()/20f;
    	canvas.translate(0,getPitch()*line_dist/20);
    	canvas.rotate(getRoll()*-1,getWidth()/2,getHeight()/2);
                                                                                                            
        sky_drawable.setBounds(-getWidth(),-getHeight()*2,2*getWidth(),getHeight()/2);
        sky_drawable.draw(canvas);

        ground_drawable.setBounds(-getWidth(),getHeight()/2,2*getWidth(),(int)(getHeight()*2));
        ground_drawable.draw(canvas);
        //canvas.drawLine(0,(getHeight()-line_width)/2, getWidth(), (getHeight()-line_width)/2, mWhiteLinePaint);
        
        float y,w=0;
        int absi=0;
        for (int i=-20;i<20;i++) {
        	absi=Math.abs(i);
        	y=(getHeight()-line_width)/2+line_dist*i;
        	if (i==0)
        		w=getWidth();
        	else if ((i%2)!=0)
        		w=getWidth()/10;
        	else  if ((absi%4)==2)
        		w=getWidth()/6;
        	else {
        		w=getWidth()/3;
        		mWhiteLinePaint.setTextAlign(Paint.Align.RIGHT);
        		canvas.drawText(""+(absi/4)*10,(getWidth()-w)/2-getWidth()/23-1,y-mWhiteLinePaint.ascent()-mWhiteLinePaint.getTextSize()/2, mWhiteLinePaint);
        		mWhiteLinePaint.setTextAlign(Paint.Align.LEFT);
        		canvas.drawText(""+(absi/4)*10,(getWidth()-w)/2+w+getWidth()/23+1,y-mWhiteLinePaint.ascent()-mWhiteLinePaint.getTextSize()/2, mWhiteLinePaint);
        	}
        	
        	canvas.drawLine((getWidth()-w)/2,y, (getWidth()-w)/2+w,y , mWhiteLinePaint);
        	
    	
        }
        y=getHeight()/2;
    	
        // pitch rect                                                                                                                       
        /*nick_bar_move=(int)((getPitch()/90.0*getHeight()/3.0));
        nose_drawable.setBounds(getWidth()/3,getHeight()/2 -bar_height/2 + nick_bar_move ,2*getWidth()/3, getHeight()/2+ nick_bar_move+bar_height);
        nose_drawable.draw(canvas); */
        canvas.restore();
        
        //canvas.drawArc(arc_rect,0,360,true,mBlackLinePaint);
        
        canvas.drawLine((getWidth())/5,y, 2*(getWidth())/5,y , mBlackLinePaint);
        canvas.drawLine(3*(getWidth())/5,y, 4*(getWidth())/5,y , mBlackLinePaint);
        canvas.drawLine(3*(getWidth())/5+mBlackLinePaint.getStrokeWidth()/2,y, 3*(getWidth())/5+mBlackLinePaint.getStrokeWidth()/2,y+getHeight()/20+1 , mBlackLinePaint);
        canvas.drawLine(2*(getWidth())/5-mBlackLinePaint.getStrokeWidth()/2,y, 2*(getWidth())/5-mBlackLinePaint.getStrokeWidth()/2,y+getHeight()/20+1 , mBlackLinePaint);
        canvas.drawPoint(getWidth()/2,getHeight()/2, mBlackLinePaint);
        
        //drawCircle(getWidth()/2,getHeight()/2, getHeight()/200+1, mBlackLinePaint);
        invalidate();
    }
}
