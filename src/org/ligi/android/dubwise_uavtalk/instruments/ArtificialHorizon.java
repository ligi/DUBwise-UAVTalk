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
    private final static int bar_height=20;
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
    	// TODO check direction
        canvas.rotate(getRoll()*-1,canvas.getWidth()/2,canvas.getHeight()/2);
                                                                                                            
        sky_drawable.setBounds(-canvas.getWidth(),-canvas.getHeight()*2,2*canvas.getWidth(),canvas.getHeight()/2);
        sky_drawable.draw(canvas);
    
        ground_drawable.setBounds(-canvas.getWidth(),canvas.getHeight()/2,2*canvas.getWidth(),(int)(canvas.getHeight()*1.3));
        ground_drawable.draw(canvas);

        // pitch rect                                                                                                                       
        nick_bar_move=(int)((getPitch()/90.0*canvas.getHeight()/3.0));
        nose_drawable.setBounds(canvas.getWidth()/3,canvas.getHeight()/2 -bar_height/2 + nick_bar_move ,2*canvas.getWidth()/3, canvas.getHeight()/2+ nick_bar_move+bar_height);
        nose_drawable.draw(canvas);
        canvas.restore();
    }
}
