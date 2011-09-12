package org.ligi.android.dubwise_uavtalk.dashboard;

import org.ligi.android.R;
import org.ligi.android.common.intents.IntentHelper.IntentStartOnClick;
import org.ligi.android.dubwise_uavtalk.channelview.ChannelViewActivity;
import org.ligi.android.dubwise_uavtalk.channelview.CurveEditActivity;
import org.ligi.android.dubwise_uavtalk.connection.ConnectionMenu;
import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;
import org.ligi.android.dubwise_uavtalk.map.DUBwiseMapActivity;
import org.ligi.android.dubwise_uavtalk.outputtest.OutputTestActivity;
import org.ligi.android.dubwise_uavtalk.pitune.PITuneActivity;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferencesActivity;

import com.google.android.apps.iosched.ui.widget.DashboardLayout;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DashboardFragment extends Fragment {
	private int num=0;
	private final static String ARGUMENT_KEY="dashboard_num_key";
	
	public static DashboardFragment newInstance(int num) {
		DashboardFragment res=new DashboardFragment();
		Bundle argument = new Bundle();
		argument.putInt(ARGUMENT_KEY, num);
		res.setArguments(argument);
		return res;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		num = getArguments() != null ? getArguments().getInt(ARGUMENT_KEY) : 0;
	}
	
	public Button createDashboardButton(int string_resID,int image_resID,Intent i,String tag) {
		Button b=new Button(this.getActivity());
		b.setText(string_resID);
		
		String base_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/DUBwise/images/dashboard/";
        Drawable img = Drawable.createFromPath(base_path+tag+".png"); 
        if (img==null)
        	img=this.getResources().getDrawable(image_resID);
        
		int img_size=this.getResources().getDimensionPixelSize(R.dimen.dashboard_image_size);
		
		img.setBounds(new Rect(0,0,img_size*img.getIntrinsicWidth()/img.getIntrinsicHeight(),img_size));
		
		b.setCompoundDrawables(null,img, null,null);
		b.setBackgroundDrawable(null);
		b.setTextColor(Color.GRAY);
		new IntentStartOnClick(i,this.getActivity())
      		.bind2view(b);
		return b;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		DashboardLayout v=new DashboardLayout(this.getActivity());
		
		switch (num) {
		case 0:
			v.addView(createDashboardButton(R.string.pfd,R.drawable.pfd_square,new Intent(this.getActivity(),InstrumentDisplayActivity.class),"pfd"));
			v.addView(createDashboardButton(R.string.map,R.drawable.map_square,new Intent(this.getActivity(),DUBwiseMapActivity.class),"map"));
			break;
		case 1:
			v.addView(createDashboardButton(R.string.connection,R.drawable.antenna_square,new Intent(this.getActivity(),ConnectionMenu.class),"con"));
			v.addView(createDashboardButton(R.string.output_test,R.drawable.engine_square,new Intent(this.getActivity(),OutputTestActivity.class),"out"));
			v.addView(createDashboardButton(R.string.throttle_curve,R.drawable.curve_square,new Intent(this.getActivity(),CurveEditActivity.class),"curve"));
			v.addView(createDashboardButton(R.string.channels,R.drawable.rc_square,new Intent(this.getActivity(),ChannelViewActivity.class),"chan"));
			v.addView(createDashboardButton(R.string.pitune,R.drawable.tune_square,new Intent(this.getActivity(),PITuneActivity.class),"tune"));
			v.addView(createDashboardButton(R.string.status_voice,R.drawable.sound_square,new Intent(this.getActivity(),StatusVoicePreferencesActivity.class),"voice"));
			break;
		}
		return v;
	}
	
	
}
