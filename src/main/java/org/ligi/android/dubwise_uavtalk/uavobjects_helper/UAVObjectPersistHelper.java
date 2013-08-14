package org.ligi.android.dubwise_uavtalk.uavobjects_helper;

import org.ligi.android.dubwise_uavtalk.connection.UAVTalkGCSThread;
import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVTalkDefinitions;
import org.openpilot.uavtalk.uavobjects.ObjectPersistence;

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
public class UAVObjectPersistHelper {

	public static void persistWithDialog(Context ctx,final UAVObject obj) {
		
		final int DIALOG_DISMISS = 0;

		final AlertDialog dialog = ProgressDialog.show(ctx, "Saving",
				"writing the values to your  board", true, true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
					dialog.dismiss();
			}
		};

		new Thread() {
			public void run() {
				apply(obj);
				save(obj);
				int i = 0;
				while (UAVObjects.getObjectPersistence().getOperation() != ObjectPersistence.OPERATION_COMPLETED)
					try {
						Thread.sleep(100);
						i++;

						if ((i % 23) == 0) { // every 2.3s reinitiate apply/save
							apply(obj);
							save(obj);
						}

					} catch (InterruptedException e) {
					}

				handler.sendEmptyMessage(DIALOG_DISMISS);
			}
		}.start();
	}
	
	public final static void apply(UAVObject obj) {
    	UAVTalkGCSThread.getInstance().send_obj(obj,UAVTalkDefinitions.TYPE_OBJ_ACK);
    }
	
	public final static void save(UAVObject obj) {
    	UAVObjects.getObjectPersistence().setOperation(ObjectPersistence.OPERATION_SAVE);
    	UAVObjects.getObjectPersistence().setObjectID(obj.getObjID());
    	UAVObjects.getObjectPersistence().setSelection(ObjectPersistence.SELECTION_SINGLEOBJECT);
    	UAVTalkGCSThread.getInstance().send_obj(UAVObjects.getObjectPersistence(), UAVTalkDefinitions.TYPE_OBJ_ACK);
    }
 
}
