package org.ligi.android.dubwise_uavtalk.dashboard;

import org.ligi.android.dubwise_uavtalk.channelview.ChannelViewActivity;
import org.ligi.android.dubwise_uavtalk.channelview.CurveEditActivity;
import org.ligi.android.dubwise_uavtalk.connection.ConnectionMenu;
import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;
import org.ligi.android.dubwise_uavtalk.outputtest.OutputTestActivity;
import org.ligi.android.dubwise_uavtalk.pitune.PITuneActivity;
import org.ligi.android.dubwise_uavtalk.statusvoice.StatusVoicePreferencesActivity;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.androidhelper.AndroidHelper;

import com.google.android.apps.iosched.ui.widget.DashboardLayout;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	
	public Button createDashboardButton(int string_resID,int image_resID,int image_sel_resID,Intent i,String tag) {
		final Button button=new Button(this.getActivity());
		button.setText(string_resID);
		
		String base_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/DUBwise/images/dashboard2/";
        Drawable d = Drawable.createFromPath(base_path+tag+".png"); 
        if (d==null)
        	d=this.getResources().getDrawable(image_resID);
        final Drawable img=d;
        
        if (image_sel_resID!=0)
        	d=this.getResources().getDrawable(image_sel_resID);
        else
        	d=null;
        /*
        if (image_sel_resID==0)
        	d=null;
        else
        	
       */
        
        final Drawable sel_img=(d);
        
		int img_size=this.getResources().getDimensionPixelSize(R.dimen.dashboard_image_size);
		Rect img_bounds=new Rect(0,0,img_size*img.getIntrinsicWidth()/img.getIntrinsicHeight(),img_size);
		img.setBounds(img_bounds);
		if (sel_img!=null)
			sel_img.setBounds(img_bounds);
		button.setTextAppearance(this.getActivity(), R.style.dashboard_button_textappearance);
		button.setCompoundDrawables(null, img, null, null);
		button.setBackgroundDrawable(null);
		
		button.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (sel_img == null)
                            view.setBackgroundResource(R.drawable.dashboard_bg);
                        else
                            button.setCompoundDrawables(null, sel_img, null, null);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (sel_img == null)
                            view.setBackgroundDrawable(null);
                        else
                            button.setCompoundDrawables(null, img, null, null);
                        break;
                }

                return false;
            }

        });
		button.setTextColor(Color.WHITE);
		
		AndroidHelper.at(button).startIntentOnClick(i);
		return button;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		DashboardLayout v=new DashboardLayout(this.getActivity());
		
		switch (num) {
		case 0:
			v.addView(createDashboardButton(R.string.pfd,R.drawable.dashboard_pfd_norm,R.drawable.dashboard_pfd_sel,new Intent(this.getActivity(),InstrumentDisplayActivity.class),"pfd"));
			break;
		case 1:
			v.addView(createDashboardButton(R.string.connection,R.drawable.dashboard_con_norm,R.drawable.dashboard_con_sel,new Intent(this.getActivity(),ConnectionMenu.class),"con"));
			v.addView(createDashboardButton(R.string.output_test,R.drawable.dashboard_out_norm,R.drawable.dashboard_out_sel,new Intent(this.getActivity(),OutputTestActivity.class),"out"));
			v.addView(createDashboardButton(R.string.throttle_curve,R.drawable.dashboard_curves_norm,R.drawable.dashboard_curves_sel,new Intent(this.getActivity(),CurveEditActivity.class),"curve"));
			v.addView(createDashboardButton(R.string.channels,R.drawable.dashboard_chan_norm,R.drawable.dashboard_chan_sel,new Intent(this.getActivity(),ChannelViewActivity.class),"chan"));
			v.addView(createDashboardButton(R.string.pitune,R.drawable.dashboard_tune_norm,R.drawable.dashboard_tune_sel,new Intent(this.getActivity(),PITuneActivity.class),"tune"));
			v.addView(createDashboardButton(R.string.status_voice,R.drawable.dashboard_voice_norm,R.drawable.dashboard_voice_sel,new Intent(this.getActivity(),StatusVoicePreferencesActivity.class),"voice"));
			break;
		case 2:
			v.addView(createDashboardButton(R.string.edit_uavobjects,android.R.drawable.ic_menu_preferences,android.R.drawable.ic_menu_preferences,new Intent("EDIT_UAVOBJECT"),"uavobjbrowse"));
			break;
		}
		
		return v;
	}
	

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
}
