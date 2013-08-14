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

package org.ligi.android.dubwise_uavtalk.connection;

import org.ligi.android.io.BluetoothCommunicationAdapter;
import org.ligi.java.io.CommunicationAdapterInterface;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.FlightTelemetryStats;
import org.openpilot.uavtalk.uavobjects.GCSTelemetryStats;

/**
 * Manages Connections
 *  
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class ConnectionManager {

    private static CommunicationAdapterInterface myCommunicationAdapter=null;
    private static String connection_name="none";
    private static String connection_url="none";

    public final static String BT_PROTO_START="btspp://"; // bluetooth serial port profile
    public final static String UDP_PROTO_START="udp://"; 

    public static CommunicationAdapterInterface getCommunicationAdapter() {
        return myCommunicationAdapter;
    }

    public static boolean reconnect() {
    	if (myCommunicationAdapter==null)
    		return false;
    	
    	myCommunicationAdapter.disconnect();
    	myCommunicationAdapter.connect();
    	
    	return true; 
    }
    
    private static void setCommunicationAdapter(CommunicationAdapterInterface new_ca) {
        if (myCommunicationAdapter!=null)
            myCommunicationAdapter.disconnect();

        // set UAVObjects to disconnect state - undo handshake
        UAVObjects.getGCSTelemetryStats().setStatus(GCSTelemetryStats.STATUS_DISCONNECTED);
        UAVObjects.getFlightTelemetryStats().setStatus(FlightTelemetryStats.STATUS_DISCONNECTED);
        myCommunicationAdapter=new_ca;
        myCommunicationAdapter.connect();
    }

    /**
     * build a communication adapter and connect
     * supported scemes:
     *  btspp://<mac>:<port> - bluetooth serial port profilex - the mac without colons - just plain hex values
     *  
     * 
     * @param name
     * @param url
     */
    public static void connect(String name,String url) {
        connection_name=name;
        connection_url=url;

        if (url.startsWith(BT_PROTO_START)) {

            Log.i("mac" + url);
            String mac=url.replace(BT_PROTO_START, "");
            mac=mac.substring(0, mac.indexOf(":"));
            String mac_with_colons="";

            for (int i=0;i<6;i++)
                mac_with_colons+=((i!=0)?":":"") + mac.substring(i*2,i*2+2);

            BluetoothCommunicationAdapter ba=new BluetoothCommunicationAdapter(mac_with_colons);
            setCommunicationAdapter(ba);
        }
        // TODO implement other protocols
    }

    public static String getConnectionName() {
        return connection_name;
    }

    public static String getConnectionURL() {
        return connection_url;
    }
}
