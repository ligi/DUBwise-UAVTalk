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

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ArtificialHorizon implements InstrumentInterface{

    private View mParent;
    private Drawable ground_drawable;
    private Drawable sky_drawable;
    private Drawable nose_drawable;
    private int bar_height=20;
    private int nick_bar_move;
    
    public ArtificialHorizon(View parent) {
    	mParent=parent;
    	if (!mParent.isInEditMode())
    		UAVObjects.getAttitudeActual().getMetaData().setFlightTelemetryUpdatePeriod(100);
        ground_drawable=mParent.getResources().getDrawable(R.drawable.horizon_earth);
        sky_drawable=mParent.getResources().getDrawable(R.drawable.horizon_sky);
        nose_drawable=mParent.getResources().getDrawable(R.drawable.horizon_nose);
        
    }
    
    private float getRoll() {
    	if (!mParent.isInEditMode())
    		return UAVObjects.getAttitudeActual().getRoll();
    	
    	return 0.0f;
    }

    private float getPitch() {
    	if (!mParent.isInEditMode())
    		return UAVObjects.getAttitudeActual().getPitch();
    	
    	return 0.0f;
    }

    public void draw(Canvas canvas) {
    	bar_height=mParent.getHeight()/17+1;
    	// TODO check direction
        canvas.rotate(getRoll()*-1,mParent.getWidth()/2,mParent.getHeight()/2);
                                                                                                            
        sky_drawable.setBounds(-mParent.getWidth(),-mParent.getHeight()*2,2*mParent.getWidth(),mParent.getHeight()/2);
        sky_drawable.draw(canvas);
    
        ground_drawable.setBounds(-mParent.getWidth(),mParent.getHeight()/2,2*mParent.getWidth(),(int)(mParent.getHeight()*1.3));
        ground_drawable.draw(canvas);

        // pitch rect                                                                                                                       
        nick_bar_move=(int)((getPitch()/90.0*mParent.getHeight()/3.0));
        nose_drawable.setBounds(mParent.getWidth()/3,mParent.getHeight()/2 -bar_height/2 + nick_bar_move ,2*mParent.getWidth()/3, mParent.getHeight()/2+ nick_bar_move+bar_height);
        nose_drawable.draw(canvas);
        canvas.restore();
    }
}
