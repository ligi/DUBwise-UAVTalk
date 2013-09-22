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

package org.ligi.android.dubwise_uavtalk.statusvoice;

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalkActivityCommons;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.axt.base_activities.RefreshingStringBaseListActivity;


import android.os.Bundle;

/**
 * show some status info for the StatusVoice - to debug what is going wrong
 * when there is something going wrong
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StatusVoiceDebugActivity extends RefreshingStringBaseListActivity {

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DUBwiseUAVTalkActivityCommons.before_content(this);
		this.setContentView(R.layout.list);
    }

	@Override
    public String getStringByPosition(int pos) {
        switch (pos) {
        case 0:
            return "pause remain " + StatusVoiceTTSFeederThread.getInstance().getPause() +"ms";
        case 1:
            return "block " + StatusVoiceTTSFeederThread.getInstance().getActBlock() +"/"+ StatusVoiceBlockProvider.getInstance().getBlockVector().size(); 
        case 2:
            return "last spoken " + StatusVoiceTTSFeederThread.getInstance().getLastSpoken(); 
        }
        return null;
    }

}
