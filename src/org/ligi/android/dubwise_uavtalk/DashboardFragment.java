package org.ligi.android.dubwise_uavtalk;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v=null;
		
		switch (num) {
		case 0:
			v = inflater.inflate(R.layout.dashboard, container, false);
			break;
		case 1:
			v = inflater.inflate(R.layout.dashboard_service, container, false);
			break;
		}
		
		Activity a=this.getActivity();
	    new IntentStartOnClick(new Intent(a,OutputTestActivity.class),a)
        	.bind2view(R.id.dashboard_btn_output,v);
        new IntentStartOnClick(new Intent(a,CurveEditActivity.class),a)
        	.bind2view(R.id.dashboard_btn_curve,v);
        new IntentStartOnClick(new Intent(a,ChannelViewActivity.class),a)
    		.bind2view(R.id.dashboard_btn_channels,v);
    
        new IntentStartOnClick(new Intent(a,InstrumentDisplayActivity.class),a)
    		.bind2view(R.id.dashboard_btn_pfd,v);
        new IntentStartOnClick(new Intent(a,PITuneActivity.class),a)
    		.bind2view(R.id.dashboard_btn_tune,v);
        new IntentStartOnClick(new Intent(a,ConnectionMenu.class),a)
    		.bind2view(R.id.dashboard_btn_connection,v);
        new IntentStartOnClick(new Intent(a,StatusVoicePreferencesActivity.class),a)
        	.bind2view(R.id.dashboard_btn_sound,v);

        new IntentStartOnClick(new Intent(a,DUBwiseMapActivity.class),a)
    	.bind2view(R.id.dashboard_btn_map,v);

		return v;
	}
}
