package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.common.intents.IntentHelper;
import org.ligi.android.dubwise_uavtalk.connection.ConnStatusView;
import org.ligi.android.dubwise_uavtalk.instruments.ArtificialHorizon;
import org.ligi.android.dubwise_uavtalk.system_alarms.SystemAlarmsActionBarThingy;
import org.ligi.android.uavtalk.dubwise.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class DUBwiseUAVTalkActionBar extends LinearLayout {
	private Context context;
	private String profile="default";
	
	
	public void setProfile(String profile) {
		this.profile=profile;
		layout();
	}
	
	public DUBwiseUAVTalkActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		layout();
	}

	private void layout() {
		this.removeAllViews();
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams( (int) getResources().getDimension(R.dimen.actionbar_height),(int) getResources().getDimension(R.dimen.actionbar_height));
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundResource(R.drawable.actionbar_bg);

		ImageView home=new ImageView(context);
		home.setLayoutParams(lp);
		home.setImageResource(R.drawable.icon);
		class MyHomeOnClick implements View.OnClickListener {
			
			private Context ctx;
			
			public MyHomeOnClick(Context ctx) {
					this.ctx=ctx;
			}
			
			public void onClick(View v) {
				IntentHelper.startActivityClass(ctx,DUBwiseUAVTalk.class);	
			}
			
		}
		
		home.setOnClickListener(new MyHomeOnClick(context));
		this.addView(home);
		
		ConnStatusView csv=new ConnStatusView(context);
		csv.setLayoutParams(lp);
		this.addView(csv);
		
		SystemAlarmsActionBarThingy alv=new SystemAlarmsActionBarThingy(context);
		alv.setLayoutParams(lp);
		this.addView(alv);
		
		if (!profile.equals("hori")) {
			ArtificialHorizon instr=new ArtificialHorizon(context);
			instr.setLayoutParams(lp);
			this.addView(instr);
		}
	}

}