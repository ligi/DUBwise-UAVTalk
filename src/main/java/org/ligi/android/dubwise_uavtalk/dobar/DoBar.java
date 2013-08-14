package org.ligi.android.dubwise_uavtalk.dobar;

import org.ligi.android.dubwise_uavtalk.DUBwiseUAVTalk;
import org.ligi.android.dubwise_uavtalk.system_alarms.IconicSystemAlarmsActionBarThingy;
import org.ligi.android.uavtalk.dubwise.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class DoBar extends LinearLayout  {
	private Context context;
	private LayoutParams layoutParams;
	
	public DoBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		layoutParams =this.generateLayoutParams(attrs);
		layout();
	}

	private void layout() {
		this.removeAllViews();
		
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams( layoutParams.height, layoutParams.height);
		this.setOrientation(LinearLayout.HORIZONTAL);
		//this.setBackgroundResource(R.drawable.dashboard_metal_top_bg);
		//this.setBackgroundColor(Color.TRANSPARENT);
		this.setBackgroundColor(Color.argb(20, 0, 0, 0));
		ImageView home=new ImageView(context);
		home.setLayoutParams(lp);
		home.setImageResource(R.drawable.icon);
		class MyHomeOnClick implements View.OnClickListener {
			
			private Context ctx;
			
			public MyHomeOnClick(Context ctx) {
					this.ctx=ctx;
			}
			
			public void onClick(View v) {
				ctx.startActivity(new Intent(ctx, DUBwiseUAVTalk.class));
			}
			
		}
		
		home.setOnClickListener(new MyHomeOnClick(context));
		this.addView(home);
		
		if (this.isInEditMode())
			return;

		
		ConnectionStatusDoBarGadget csv=new ConnectionStatusDoBarGadget(context);
		csv.setLayoutParams(lp);
		this.addView(csv);

		IconicSystemAlarmsActionBarThingy alv=new IconicSystemAlarmsActionBarThingy(context);
		alv.setLayoutParams(lp);
		this.addView(alv);
		
		
		/*if (!profile.equals("hori")) {
			ArtificialHorizon instr=new ArtificialHorizon(context);
			instr.setLayoutParams(lp);
			this.addView(instr);
		}*/
		
	}

}
