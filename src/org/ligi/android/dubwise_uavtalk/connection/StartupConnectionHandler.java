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

import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.ReadyListener;

import org.ligi.android.dubwise_uavtalk.R;
import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Handles automatic connecting on startup - like loading and saving connections
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class StartupConnectionHandler {

    private static SharedPreferences mySharedPreferences;

    public static void init(Activity activity) {

        mySharedPreferences=activity.getSharedPreferences("default_conn", Activity.MODE_PRIVATE);
        if (mySharedPreferences.getBoolean("saved",false)) {  // if we have a default connection
            String url= mySharedPreferences.getString("url","corrupt settings data");
            String name=mySharedPreferences.getString("name","corrupt settings data");
            if (url.startsWith(ConnectionManager.BT_PROTO_START)) {

                class myReadyListener extends ReadyListener {

                    private String name;
                    private String url;
                    private Activity myActivity;
                    private AlertDialog alert2close;
                    
                    public myReadyListener(Activity context,String name,String url,AlertDialog alert2close) {
                        this.name=name;
                        this.url=url;
                        this.myActivity=context;
                        this.alert2close=alert2close;
                    }
                    
                    @Override
                    public void ready() {
                        ConnectionManager.connect(name,url);
                        alert2close.dismiss();
                        HandshakeStatusAlertDialog.show(myActivity,true,new Intent(myActivity,InstrumentDisplayActivity.class));
                        
                    }
                }
                LinearLayout lin=new LinearLayout(activity);
                ProgressBar progress=new ProgressBar(activity);
                progress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                lin.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv=new TextView(activity);
                tv.setText(R.string.switching_on_bt);
                
                tv.setTextSize(TypedValue.COMPLEX_UNIT_MM, 10);
                lin.addView(progress);
                lin.addView(tv);
                
                
                AlertDialog  switch_alert=new AlertDialog.Builder(activity)
                
                .setView(lin)
                .show();
                
                LocalDevice.getInstance().init(activity.getApplicationContext(), new myReadyListener(activity,name,url,switch_alert));

            }
            else {
                ConnectionManager.connect(name,url);
                HandshakeStatusAlertDialog.show(activity,true,new Intent(activity,InstrumentDisplayActivity.class));
            }


        }
    }

    public static void save(String name,String url) {
        Editor e=mySharedPreferences.edit();
        e.putBoolean("saved",true);
        e.putString("url",url);
        e.putString("name",name);
        e.commit();
    }

}
