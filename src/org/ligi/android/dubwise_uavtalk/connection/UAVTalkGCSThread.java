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

import java.io.IOException;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.CRC8;
import org.openpilot.uavtalk.UAVObject;
import org.openpilot.uavtalk.UAVObjectChangeListener;
import org.openpilot.uavtalk.UAVObjectMetaData;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.UAVTalkDefinitions;
import org.openpilot.uavtalk.UAVTalkHelper;
import org.openpilot.uavtalk.uavobjects.FlightTelemetryStats;
import org.openpilot.uavtalk.uavobjects.GCSTelemetryStats;

/**
 * Thread to do the GCS Part of the UAVTalk Protocol
 * takes the recent Connection from the ConnectionManager 
 * and the result can be seen in the UAVObjects
 * 
 * TODO fill GCSTelemetryStats datarate fields with data rate
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UAVTalkGCSThread implements Runnable, UAVObjectChangeListener {

    private static UAVTalkGCSThread instance=null;
    public String act_buf=" ";
    public byte[] buf=new byte[255];
    private int rxPackets=0;
    private String last_error="none";


    public String getLastError() {
        return last_error;
    }

    public int getRxPackets() {
        return rxPackets;
    }

    public static UAVTalkGCSThread getInstance() {
        if (instance==null) {
            /* init */
            instance=new UAVTalkGCSThread();
            new Thread(instance).start();
            // register as a changelistener to all objects
            for (UAVObject obj : UAVObjects.getUAVObjectArray()) {
                obj.addChangeListener(instance);
                obj.getMetaData().addChangeListener(instance);
            }
        }
        return instance;
    }

    public void send_obj(UAVObject obj,byte type) {
        try {
            obj.getMetaData().setFlightTelemetryAcked(false);
            obj.getMetaData().setAckPending(type==UAVTalkDefinitions.TYPE_OBJ_ACK);
            obj.getMetaData().setLastSendTime(System.currentTimeMillis());
            //byte type=(do_ack)?UAVTalkDefinitions.TYPE_OBJ_ACK:UAVTalkDefinitions.TYPE_OBJ;
            ConnectionManager.getCommunicationAdapter().write(UAVTalkHelper.generateUAVTalkPackage(type, obj));
        } catch (IOException e) {
            txError("problem sending object " + e);
        }

    }

    private final static int ACK_RESEND_TIME=500;

    /**
     * checks if an ack is pending for longer than than ACK_RESEND_TIME
     * if 
     */
    private void checkForObjectActionPending() {
        UAVObjectMetaData obj_meta;
        for (UAVObject obj : UAVObjects.getUAVObjectArray()) {
            obj_meta=obj.getMetaData();

            if (obj.getMetaData().isAckPending() 
                    &&((System.currentTimeMillis()-obj.getMetaData().getLastSendTime())>ACK_RESEND_TIME )) 
                send_obj(obj,UAVTalkDefinitions.TYPE_ACK);
            if ((obj_meta.getGCSTelemetryUpdateMode()==UAVObjectMetaData.UPDATEMODE_PERIODIC)
                    &&((System.currentTimeMillis()-obj_meta.getLastSendTime())>obj_meta.getGCSTelemetryUpdatePeriod()/10 ))
                send_obj(obj,UAVTalkDefinitions.TYPE_OBJ_ACK);
        }
    }

    public void run() {

        int got=0;
        int act_avail=0;
        byte[] rx_buff=new byte[255];
        int rx_read_count=0;
        int i;

        while(true) {
            try {
                act_avail=ConnectionManager.getCommunicationAdapter().available();
                if (act_avail>0) {
                    // hava data -> process

                    rx_read_count=ConnectionManager.getCommunicationAdapter().read(rx_buff,0, act_avail);
                    for (i=0;i<rx_read_count;i++)
                        process_byte(rx_buff[i]);
                    got++;
                }
                else {
                    checkForObjectActionPending(); // TODO find better place for that
                    // have no new data - sleep longer
                    Thread.sleep(50); 
                }

                // handshaking section - if flight ( master here ) is not connected set gcs 
                if ((UAVObjects.getFlightTelemetryStats().getStatus()!=FlightTelemetryStats.STATUS_CONNECTED)
                        &&(!UAVObjects.getGCSTelemetryStats().getMetaData().isAckPending())) // and not allready waiting for answer
                    switch(UAVObjects.getFlightTelemetryStats().getStatus()) {
                    case FlightTelemetryStats.STATUS_DISCONNECTED: 
                        UAVObjects.getGCSTelemetryStats().setStatus(GCSTelemetryStats.STATUS_HANDSHAKEREQ);
                        break;	

                    case FlightTelemetryStats.STATUS_HANDSHAKEACK:
                    case FlightTelemetryStats.STATUS_CONNECTED:
                        UAVObjects.getGCSTelemetryStats().setStatus(GCSTelemetryStats.STATUS_CONNECTED);
                        break;
                    }

            } catch (Exception e) {
                Log.i("e" + e);
                try {
                    Thread.sleep(1000);
                } catch (Exception e_sleep) {};
            }

        }
    }

    private void rxError(String what) {
        last_error=what;
        Log.w("rx error: " + what);
        UAVObjects.getGCSTelemetryStats().setRxFailures(UAVObjects.getGCSTelemetryStats().getRxFailures()+1);
        state = STATE_SYNC;
    }

    private void txError(String what) {
        Log.w("tx error: " + what);
        UAVObjects.getGCSTelemetryStats().setTxFailures(UAVObjects.getGCSTelemetryStats().getTxFailures()+1);
        ConnectionManager.getCommunicationAdapter().disconnect();
        UAVObjects.getFlightTelemetryStats().setStatus(FlightTelemetryStats.STATUS_DISCONNECTED);
        ConnectionManager.getCommunicationAdapter().connect();
    }

    private final static byte STATE_SYNC=0;
    private final static byte STATE_TYPE=1;
    private final static byte STATE_SIZE=2;
    private final static byte STATE_OBJID=3;
    private final static byte STATE_INSTANCEID=4;
    private final static byte STATE_DATA=5;
    private final static byte STATE_CRC=6;

    private byte state=STATE_SYNC;

    private byte act_crc=0;
    private byte type;
    private int packet_size=0;

    private int state_byte_pos=0;

    private int objId;
    private int data_length;
    private final static int MAX_DATA_LENGTH=255;
    private byte[] data_buff=new byte[MAX_DATA_LENGTH];
    private UAVObject act_uavobject;

    private void process_byte(byte in_byte)  {


        switch (state) {
        case STATE_SYNC:
            if (in_byte != UAVTalkDefinitions.SYNC_VAL)
                break;

            act_crc=0; // init the crc
            act_crc=CRC8.byteUpdate(act_crc, in_byte); // update crc

            //rxPacketLength = 1;
            state = STATE_TYPE;
            break;

        case STATE_TYPE:
            act_crc=CRC8.byteUpdate(act_crc, in_byte); // update crc

            if ((in_byte & UAVTalkDefinitions.TYPE_MASK_VER) != UAVTalkDefinitions.TYPE_VER)  {
                rxError("type error");
                state = STATE_SYNC;
                break;
            }

            type = in_byte;

            packet_size = 0;

            state = STATE_SIZE;
            state_byte_pos = 0;
            break;

        case STATE_SIZE:

            act_crc=CRC8.byteUpdate(act_crc, in_byte); // update crc

            if (state_byte_pos == 0)  {
                packet_size += in_byte;
                state_byte_pos++;
                break;
            }

            packet_size += in_byte << 8;

            /*
            if (packet_size < UAVTALKDefinitions.MIN_HEADER_LENGTH || packet_size > MAX_HEADER_LENGTH + MAX_PAYLOAD_LENGTH)
            {   // incorrect packet size                                                                                                                                         
                    state = STATE_SYNC;
                    break;
            }
             */

            state_byte_pos = 0;
            state = STATE_OBJID;
            objId=0;
            break;

        case STATE_OBJID:

            act_crc=CRC8.byteUpdate(act_crc, in_byte); // update crc

            objId |= (in_byte&0xFF) << (8*state_byte_pos++)	;

            if (state_byte_pos < 4)
                break;

            if (!UAVObjects.hasObjectWithID(objId)) {
                rxError("got unknown object with id  " + String.format("%X",objId) );
                break;
            }

            act_uavobject= UAVObjects.getObjectByID(objId);


            if (type == UAVTalkDefinitions.TYPE_OBJ_REQ || type == UAVTalkDefinitions.TYPE_ACK) {  
                data_length = 0;
                if (type == UAVTalkDefinitions.TYPE_ACK) { // go our ack - notify this
                    act_uavobject.getMetaData().setAckPending(false);
                    act_uavobject.getMetaData().setFlightTelemetryAcked(true);
                }

            }
            else
                data_length = act_uavobject.getDataLength();

            state_byte_pos=0;

            if (data_length==0)
                // no data to process - direct jump to CRC
                state= STATE_CRC;
            else
                // need to process data
                state = STATE_DATA; // TODO implement instanceid stuff but not needed now

            /*obj = UAVObjGetByID(objId);
            if (obj == 0)
            {
                    stats.rxErrors++;

                   break;
            }


            // Check length and determine next state                                                                                                                             
            if (length >= MAX_PAYLOAD_LENGTH)
            {
                    stats.rxErrors++;
                    state = STATE_SYNC;
                    break;
            }

            // Check the lengths match                                                                                                                                          
            if ((rxPacketLength + length) != packet_size)
            {   // packet error - mismatched packet size                                                                                                                         
                    stats.rxErrors++;
                    state = STATE_SYNC;
                    break;
            }
             */

            break;

        case STATE_DATA:
            //Log.i("get data " + in_byte + " state_byte pos " + state_byte_pos + "data len:" + data_length);
            act_crc=CRC8.byteUpdate(act_crc, in_byte); // update crc
            data_buff[state_byte_pos++]=in_byte;

            if (state_byte_pos>=data_length) // last
                state=STATE_CRC;

            break;

        case STATE_INSTANCEID:

            break;
        case STATE_CRC:
            if (in_byte==act_crc) {

                if (type==UAVTalkDefinitions.TYPE_OBJ_ACK)
                    send_obj(act_uavobject,UAVTalkDefinitions.TYPE_ACK);

                if (data_length>0)
                    act_uavobject.deserialize(data_buff);
                rxPackets++;
            } else {
                rxError("CRC ERR" + data_length);
            }

            state=STATE_SYNC;
            break;

        default:
            Log.i("invalid state in rx process byte");

            break;
        }
    }

    public void notifyUAVObjectChange(UAVObject changed_object) {
        Log.i("object change " + changed_object.getObjName());
        if (changed_object.getMetaData().getGCSTelemetryUpdateMode()==UAVObjectMetaData.UPDATEMODE_ONCHANGE) {
            send_obj(changed_object,UAVTalkDefinitions.TYPE_OBJ_ACK);
            Log.i("sending because onchange");
        }
    }
}
