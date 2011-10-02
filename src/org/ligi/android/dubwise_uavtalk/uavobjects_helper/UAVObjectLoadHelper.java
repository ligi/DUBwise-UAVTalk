package org.ligi.android.dubwise_uavtalk.uavobjects_helper;

import org.ligi.android.dubwise_uavtalk.connection.UAVTalkGCSThread;
import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVTalkDefinitions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
/**
 * android helper functions around persisting UAVObjects
 * 
 * @author ligi
 *
 */
public class UAVObjectLoadHelper {

	public static void loadWithDialog(Context ctx,final UAVObject obj,final Handler finishing_callback) {
		
		final int DIALOG_DISMISS = 0;

		final AlertDialog dialog = ProgressDialog.show(ctx, "Loading",
				"loading values from your board", true, true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				finishing_callback.sendEmptyMessage(0);
				dialog.dismiss();
			}
		};

		new Thread() {
			public void run() {
				
				int i = 0;
				
				long last_t=obj.getMetaData().getLastDeserialize();
				load(obj);
				while (last_t==obj.getMetaData().getLastDeserialize())
					try {
						Thread.sleep(100);
						i++;

						if ((i % 23) == 0)  // every 2.3s reinitiate loading
							load(obj);

					} catch (InterruptedException e) {
					}
				handler.sendEmptyMessage(DIALOG_DISMISS);
			}
		}.start();
	}
	
	public final static void load(UAVObject obj) {
    	UAVTalkGCSThread.getInstance().send_obj(obj,UAVTalkDefinitions.TYPE_OBJ_REQ);
    }
}
