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

package org.ligi.android.dubwise_uavtalk.dashboard;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.ligi.android.common.dialogs.DialogDiscarder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * shows Progress of downloading Dashboard theme images
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class DownloadDashboardImagesStatusAlertDialog {

	private final static String[] img_lst=new String[] { 
				"out"
				,"chan"
				,"tune"
				,"con"};
	private final static String[] url_lst=new String[] { 
				"http://git.openpilot.org/browse/~raw,r=7195862d77afd72c6bd296c3b5137964fa48059b/OpenPilot/ground/openpilotgcs/src/plugins/config/images/Servo.png"
				,"http://git.openpilot.org/browse/~raw,r=aabe6524516d7a4eb30d6970d862840d1eb83f58/OpenPilot/ground/openpilotgcs/src/plugins/config/images/Transmitter.png"
				,"http://git.openpilot.org/browse/~raw,r=7ce441291601fae3936b12d17253ec94fc003bd1/OpenPilot/ground/openpilotgcs/src/plugins/config/images/gyroscope.png"
				,"http://git.openpilot.org/browse/~raw,r=7195862d77afd72c6bd296c3b5137964fa48059b/OpenPilot/ground/openpilotgcs/src/plugins/config/images/PipXtreme.png"
			};
	
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

        LinearLayout lin_in_scrollview=new LinearLayout(activity);
        lin_in_scrollview.setOrientation(LinearLayout.VERTICAL);
        sv.addView(lin_in_scrollview);
        lin_in_scrollview.addView(details_text_view);

        details_text_view.setText("no text");

        ProgressBar progress =new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
        progress.setMax(img_lst.length);

        lin.addView(progress);
        lin.addView(sv);


        new AlertDialog.Builder(activity)
        .setTitle("Download Status")
        .setView(lin)
        .setPositiveButton("OK", new DialogDiscarder())
        .show();

        class AlertDialogUpdater implements Runnable {

            private Handler h=new Handler();
            private TextView myTextView;
            private ProgressBar myProgress;

            public AlertDialogUpdater(TextView ab,ProgressBar progress) {
                myTextView=ab;
                myProgress=progress;

            }

            public void run() {

                for (int i=0;i<img_lst.length;i++)
                     {
                        class MsgUpdater implements Runnable {
                            
                            private int i;
                            public MsgUpdater(int i) {
                            	this.i=i;
                            }
                            public void run() {
                            	myProgress.setProgress(i+1);
                            	if (i!=img_lst.length-1)
                            		myTextView.setText("Downloading " + img_lst[i]+".png");
                            	else
                            		myTextView.setText("Ready - please restart DUBwise to apply changes!");
                            }
                        }
                        h.post(new MsgUpdater(i));
                        
                        try {
                        URLConnection ucon = new URL(url_lst[i]).openConnection();
                        BufferedInputStream bis = new BufferedInputStream(ucon.getInputStream());
 
                        ByteArrayBuffer baf = new ByteArrayBuffer(50);
                        int current = 0;
                        while ((current = bis.read()) != -1) 
                                baf.append((byte) current);

                        File path=new File(Environment.getExternalStorageDirectory()+"/dubwise/images/dashboard");
                        path.mkdirs();
                        
                        FileOutputStream fos = new FileOutputStream(new File(path.getAbsolutePath() +"/"+img_lst[i]+".png" ));
                        fos.write(baf.toByteArray());
                        fos.close();
                        } catch (Exception e) { } 
                        
                        
                        
                        try {
							Thread.sleep(199);
						} catch (InterruptedException e) {	}

                    } 
            }
        }

        new Thread(new AlertDialogUpdater(details_text_view,progress)).start();
    }
}
