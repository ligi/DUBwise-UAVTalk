package org.ligi.android.dubwise_uavtalk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class DUBwiseUAVTalkStartupActivity extends Activity{

	private Activity ctx;
	private boolean running=true;
	private Thread kickstarter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ctx=this;
		super.onCreate(savedInstanceState);
		ProgressDialog.show(this,"starting","please give me some time to start DUBwise for UAVTalk for you",true,true)
		.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				ctx.finish();
				running=false;
				if (kickstarter!=null)
					kickstarter.stop();
			}
			
		});
		
		class KickStarter implements Runnable {

			@Override
			public void run() {
				Looper.prepare();
				DUBwiseUAVTalk.kickstart(ctx);
				if (running) 
					ctx.startActivity(new Intent(ctx,DUBwiseUAVTalkDashboardActivity.class));	
				ctx.finish();
			}
			
		}
		
		kickstarter=new Thread(new KickStarter());
		kickstarter.start();
	}

}
