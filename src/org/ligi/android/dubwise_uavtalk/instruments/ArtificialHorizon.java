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
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ArtificialHorizon implements InstrumentInterface{

    private Paint mPaint;
    private View mParent;
    private Drawable ground_drawable;
    private Drawable sky_drawable;
    private Drawable nose_drawable;
    private int nick_bar_move;
    
    public ArtificialHorizon(View parent) {
    	mParent=parent;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        UAVObjects.getAttitudeActual().getMetaData().setFlightTelemetryUpdatePeriod(100);
        ground_drawable=mParent.getResources().getDrawable(R.drawable.horizon_earth);
        sky_drawable=mParent.getResources().getDrawable(R.drawable.horizon_sky);
        nose_drawable=mParent.getResources().getDrawable(R.drawable.horizon_nose);
    }
    
    public void draw(Canvas canvas) {


        /*        if (DUBwisePrefs.isArtificialHorizonInverted())
            angle_roll*=-1;
         */

        canvas.rotate(UAVObjects.getAttitudeActual().getRoll()*-1,canvas.getWidth()/2,canvas.getHeight()/2);
        
        mPaint.setARGB(255,177,129,0);

        // roll rect                                                                                                                       
        sky_drawable.setBounds(-canvas.getWidth(),-canvas.getHeight()*2,2*canvas.getWidth(),canvas.getHeight()/2);
        sky_drawable.draw(canvas);
    
        ground_drawable.setBounds(-canvas.getWidth(),canvas.getHeight()/2,2*canvas.getWidth(),(int)(canvas.getHeight()*1.3));
        ground_drawable.draw(canvas);
        int bar_height=20;

        // pitch rect                                                                                                                       
        mPaint.setARGB(200,0,200,0);
        int nick_bar_move=(int)((UAVObjects.getAttitudeActual().getPitch()/90.0*canvas.getHeight()/3.0));
        nose_drawable.setBounds(canvas.getWidth()/3,canvas.getHeight()/2 -bar_height/2 + nick_bar_move ,2*canvas.getWidth()/3, canvas.getHeight()/2+ nick_bar_move+bar_height);
        nose_drawable.draw(canvas);
        canvas.restore();
    }
}
