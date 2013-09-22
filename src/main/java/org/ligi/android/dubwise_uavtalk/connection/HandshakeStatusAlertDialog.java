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

import org.ligi.axt.helpers.dialog.DialogDiscardingOnClickListener;
import org.ligi.tracedroid.logging.Log;
import org.openpilot.uavtalk.UAVObjects;
import org.openpilot.uavtalk.uavobjects.FlightTelemetryStats;
import org.openpilot.uavtalk.uavobjects.GCSTelemetryStats;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * shows Progress and information on the connection progress
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class HandshakeStatusAlertDialog {

    /**
     * shows the handshake status alert
     * do not autoclose
     * 
     * @param activity
     */
    public static void show(Activity activity) {
        show(activity,false,null);
    }

    /**
     * 
     * @param activity
     * @param autoclose - if the alert should close when connection is established
     * 
     */
    public static void show(Context activity,boolean autoclose,Intent after_connection_intent ) {

        LinearLayout lin=new LinearLayout(activity);
        lin.setOrientation(LinearLayout.VERTICAL);

        ScrollView sv=new ScrollView(activity);
        TextView details_text_view=new TextView(activity);
        //TextView connection_state_text_view=new TextView(activity);

        LinearLayout lin_in_scrollview=new LinearLayout(activity);
        lin_in_scrollview.setOrientation(LinearLayout.VERTICAL);
        sv.addView(lin_in_scrollview);
        lin_in_scrollview.addView(details_text_view);

        details_text_view.setText("no text");

        ProgressBar progress =new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(16);

        lin.addView(progress);
        lin.addView(sv);


        AlertDialog alert=new AlertDialog.Builder(activity)
        .setTitle("Handshake Status")
        .setView(lin)
        .setPositiveButton("OK", new DialogDiscardingOnClickListener())
        .show();



        class AlertDialogUpdater implements Runnable {

            private Handler h=new Handler();
            private TextView myTextView;
            private ProgressBar myProgress;
            private AlertDialog myAutoCloseAlert;
            private boolean running;
            private Intent after_connection_intent;
            private Context myActivity;

            public AlertDialogUpdater(Context myActivity,TextView ab,ProgressBar progress,AlertDialog autoclose_alert,Intent after_connection_intent) {
                this.myActivity=myActivity;
                myTextView=ab;
                myProgress=progress;
                myAutoCloseAlert=autoclose_alert;
                this.after_connection_intent=after_connection_intent;
                running=true;
            }

            public void run() {

                while (running)
                    try {
                        class MsgUpdater implements Runnable {
                            private String myMessage="";

                            private void addMsg(String msg){
                                myMessage+=msg+"\n";
                            }
                            public void run() {
                                myMessage="";

                                try {
                                    addMsg("Name: " +ConnectionManager.getConnectionName());
                                    addMsg("URL: " +ConnectionManager.getConnectionURL());

                                    addMsg("GCS status: " + UAVObjects.getGCSTelemetryStats().getStatus() +"=" +  GCSTelemetryStats.getStatusEnumOptions()[UAVObjects.getGCSTelemetryStats().getStatus()]);
                                    addMsg("Flight status: " + UAVObjects.getFlightTelemetryStats().getStatus() +"=" + FlightTelemetryStats.getStatusEnumOptions()[UAVObjects.getFlightTelemetryStats().getStatus()]);
                                    addMsg("Bytes in: " + UAVTalkGCSThread.getInstance().getRCVByteCount());
                                    addMsg("RX Packets: " + UAVTalkGCSThread.getInstance().getRxPackets());
                                    addMsg("RX Failures: " + UAVObjects.getGCSTelemetryStats().getRxFailures());
                                    

                                    addMsg("last RX Error " + UAVTalkGCSThread.getInstance().getLastError());
                                } catch (Exception e) { }

                                myTextView.setText(myMessage);
                                // TODO better calculate progress - could fail with enum
                                myProgress.setProgress((UAVObjects.getGCSTelemetryStats().getStatus()+1)*(UAVObjects.getFlightTelemetryStats().getStatus()+1));

                            }
                        }
                        h.post(new MsgUpdater());
                        Thread.sleep(199);
                        if ((UAVObjects.getGCSTelemetryStats().getStatus()==GCSTelemetryStats.STATUS_CONNECTED)
                                &&(UAVObjects.getFlightTelemetryStats().getStatus()==FlightTelemetryStats.STATUS_CONNECTED)) {

                            StartupConnectionHandler.save(ConnectionManager.getConnectionName(),ConnectionManager.getConnectionURL());
                            if (myAutoCloseAlert!=null)  {
                                myAutoCloseAlert.dismiss();
                                running=false;
                                
                                if (after_connection_intent!=null)
                                    myActivity.startActivity(after_connection_intent);

                            }
                            
                            
                        }

                    } catch (Exception e) {	Log.w(e.toString());}

            }

        }
        AlertDialog autoclose_alert=null;
        if (autoclose) 
            autoclose_alert=alert;
        new Thread(new AlertDialogUpdater(activity,details_text_view,progress,autoclose_alert,after_connection_intent)).start();
    }
}
