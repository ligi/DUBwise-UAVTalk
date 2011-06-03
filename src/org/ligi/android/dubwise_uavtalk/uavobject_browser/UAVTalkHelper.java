package org.ligi.android.dubwise_uavtalk.uavobject_browser;

import java.util.HashMap;

import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;

public class UAVTalkHelper {
	public static HashMap<Integer,UAVObjectMetaData> meta_data_backup;
	
	public static void saveMetaData() {
		meta_data_backup.clear();
		for (UAVObject obj:UAVObjects.getUAVObjectArray()) 
			meta_data_backup.put(obj.getObjID(), obj.getMetaData());
	}
	
	
	
}
