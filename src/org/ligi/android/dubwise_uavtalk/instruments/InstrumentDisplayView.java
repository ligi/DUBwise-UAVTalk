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

import java.util.Vector;

import android.app.Activity;
import android.view.View;
import android.graphics.*;

public class InstrumentDisplayView extends View 
{
    private Vector<InstrumentInterface> instruments=new Vector<InstrumentInterface>();

    public InstrumentDisplayView(Activity context) {
        super(context);

        instruments.add(new ArtificialHorizon());
        // needed to get Key Events
        setFocusable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (InstrumentInterface instrument:instruments)
            instrument.draw(canvas);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        invalidate();
    }
}
