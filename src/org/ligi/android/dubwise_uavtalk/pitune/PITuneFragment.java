package org.ligi.android.dubwise_uavtalk.pitune;

import java.util.HashMap;

import org.ligi.android.dubwise_uavtalk.statusvoice.UAVObjectLink;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.android.uavtalk.dubwise.R.id;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PITuneFragment extends Fragment {
	private int mNum;

	static PITuneFragment newInstance(int num) {
		PITuneFragment f = new PITuneFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.pitune, container, false);

		HashMap<String, UAVObjectFieldDescription> fieldDescByName = new HashMap<String, UAVObjectFieldDescription>();
		for (UAVObjectFieldDescription d : UAVObjects
				.getStabilizationSettings().getFieldDescriptions())
			fieldDescByName.put(d.getName(), d);

		Float mod_base = 0.0001f;
		String middle_str="";
		switch(mNum) {
		case 0:
			mod_base = 0.0001f;
			middle_str="Rate / Inner loop";
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 0),
					v.findViewById(id.kp_nick), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 1),
					v.findViewById(id.ki_nick), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 3),
					v.findViewById(id.ilimit_nick), mod_base);

			connectFloat(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 0),
					v.findViewById(id.kp_roll), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 1),
					v.findViewById(id.ki_roll), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 3),
					v.findViewById(id.ilimit_roll), mod_base);

			connectFloat(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 0),
					v.findViewById(id.kp_yaw), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 1),
					v.findViewById(id.ki_yaw), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 3),
					v.findViewById(id.ilimit_yaw), mod_base);
			break;
		case 1:
			mod_base = 0.1f;
			middle_str="Attitude / Outer loop";
			
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchPI"), 0),
					v.findViewById(id.kp_nick), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchPI"), 1),
					v.findViewById(id.ki_nick), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("PitchPI"), 2),
					v.findViewById(id.ilimit_nick), mod_base);

			connectFloat(new UAVObjectLink(fieldDescByName.get("RollPI"), 0),
					v.findViewById(id.kp_roll), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("RollPI"), 1),
					v.findViewById(id.ki_roll), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("RollPI"), 2),
					v.findViewById(id.ilimit_roll), mod_base);

			connectFloat(new UAVObjectLink(fieldDescByName.get("YawPI"), 0),
					v.findViewById(id.kp_yaw), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("YawPI"), 1),
					v.findViewById(id.ki_yaw), mod_base);
			connectFloat(new UAVObjectLink(fieldDescByName.get("YawPI"), 2),
					v.findViewById(id.ilimit_yaw), mod_base);
			break;
		case 2:
			int mod_base_i = 1;
			
			connectInt(new UAVObjectLink(fieldDescByName.get("PitchMax"), 0),
					v.findViewById(id.kp_nick), mod_base_i);
			connectFloat(new UAVObjectLink(fieldDescByName.get("ManualRate"), 1),
					v.findViewById(id.ki_nick), mod_base_i,"%.0f");
			connectFloat(new UAVObjectLink(fieldDescByName.get("MaximumRate"), 1),
					v.findViewById(id.ilimit_nick), mod_base_i,"%.0f");

			connectInt(new UAVObjectLink(fieldDescByName.get("RollMax"), 0),
					v.findViewById(id.kp_roll), mod_base_i);
			connectFloat(new UAVObjectLink(fieldDescByName.get("ManualRate"), 0),
					v.findViewById(id.ki_roll), mod_base_i,"%.0f");
			connectFloat(new UAVObjectLink(fieldDescByName.get("MaximumRate"), 0),
					v.findViewById(id.ilimit_roll), mod_base_i,"%.0f");

			connectInt(new UAVObjectLink(fieldDescByName.get("YawMax"), 0),
					v.findViewById(id.kp_yaw), mod_base_i);
			connectFloat(new UAVObjectLink(fieldDescByName.get("ManualRate"), 2),
					v.findViewById(id.ki_yaw), mod_base_i,"%.0f");
			connectFloat(new UAVObjectLink(fieldDescByName.get("MaximumRate"), 2),
					v.findViewById(id.ilimit_yaw), mod_base_i,"%.0f");

			
			break;
		}
		
		switch(mNum) {
				case 0:
				case 1:
				
					((TextView)v.findViewById(R.id.kp_spacerlabel)).setText("Kp");
					((TextView)v.findViewById(R.id.ki_spacerlabel)).setText("KiKi");
					((TextView)v.findViewById(R.id.ilimit_spacerlabel)).setText("ILimit");
					break;
					
				case 2:
					((TextView)v.findViewById(R.id.kp_spacerlabel)).setText(R.string.full_stick_angle);
					((TextView)v.findViewById(R.id.ki_spacerlabel)).setText(R.string.full_stick_rate);
					((TextView)v.findViewById(R.id.ilimit_spacerlabel)).setText(R.string.max_rate_in_att);
					break;
		}
		

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void connectFloat(UAVObjectLink lnk, View v, float mod_base) {
		connectFloat(lnk, v, mod_base,"%.6f");
	}
	
	private void connectFloat(UAVObjectLink lnk, View v, float mod_base,String formater) {

		TextView tv = (TextView) v.findViewById(id.pival_val);
		Button up = (Button) v.findViewById(id.pival_up);
		Button down = (Button) v.findViewById(id.pival_down);
		class ModOnClick implements View.OnClickListener {

			private TextView myTextView;
			private UAVObjectLink myObjLnk;
			private float mod_val;
			private String formater;

			public ModOnClick(UAVObjectLink lnk, TextView tv, float mod_val, String formater) {
				myTextView = tv;
				myObjLnk = lnk;
				this.mod_val = mod_val;
				this.formater=formater;
				setText();
			}

			private void setText() {
				myTextView
						.setText(String.format(formater, myObjLnk.getAsFloat()));
			}

			public void onClick(View v) {
				myObjLnk.setField(myObjLnk.getAsFloat() + mod_val);
				setText();
			}

		}
		up.setOnClickListener(new ModOnClick(lnk, tv, mod_base,formater));
		down.setOnClickListener(new ModOnClick(lnk, tv, -mod_base,formater));
	}

	private void connectInt(UAVObjectLink lnk, View v, int mod_base) {

		TextView tv = (TextView) v.findViewById(id.pival_val);
		Button up = (Button) v.findViewById(id.pival_up);
		Button down = (Button) v.findViewById(id.pival_down);
		class ModOnClick implements View.OnClickListener {

			private TextView myTextView;
			private UAVObjectLink myObjLnk;
			private int mod_val;

			public ModOnClick(UAVObjectLink lnk, TextView tv, int mod_val) {
				myTextView = tv;
				myObjLnk = lnk;
				this.mod_val = mod_val;
				setText();
			}

			private void setText() {
				myTextView
						.setText(""+myObjLnk.getAsInt());
			}

			public void onClick(View v) {
				myObjLnk.setField(myObjLnk.getAsInt() + mod_val);
				setText();
			}

		}
		up.setOnClickListener(new ModOnClick(lnk, tv, mod_base));
		down.setOnClickListener(new ModOnClick(lnk, tv, -mod_base));
	}

}
