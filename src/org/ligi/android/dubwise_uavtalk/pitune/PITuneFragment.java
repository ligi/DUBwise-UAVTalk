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
			connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 0),
					v.findViewById(id.kp_nick), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 1),
					v.findViewById(id.ki_nick), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"), 3),
					v.findViewById(id.ilimit_nick), mod_base);

			connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 0),
					v.findViewById(id.kp_roll), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 1),
					v.findViewById(id.ki_roll), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"), 3),
					v.findViewById(id.ilimit_roll), mod_base);

			connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 0),
					v.findViewById(id.kp_yaw), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 1),
					v.findViewById(id.ki_yaw), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"), 3),
					v.findViewById(id.ilimit_yaw), mod_base);
			break;
		case 1:
			mod_base = 0.1f;
			middle_str="Attitude / Outer loop";
			
			connect(new UAVObjectLink(fieldDescByName.get("PitchPI"), 0),
					v.findViewById(id.kp_nick), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("PitchPI"), 1),
					v.findViewById(id.ki_nick), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("PitchPI"), 2),
					v.findViewById(id.ilimit_nick), mod_base);

			connect(new UAVObjectLink(fieldDescByName.get("RollPI"), 0),
					v.findViewById(id.kp_roll), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("RollPI"), 1),
					v.findViewById(id.ki_roll), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("RollPI"), 2),
					v.findViewById(id.ilimit_roll), mod_base);

			connect(new UAVObjectLink(fieldDescByName.get("YawPI"), 0),
					v.findViewById(id.kp_yaw), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("YawPI"), 1),
					v.findViewById(id.ki_yaw), mod_base);
			connect(new UAVObjectLink(fieldDescByName.get("YawPI"), 2),
					v.findViewById(id.ilimit_yaw), mod_base);
			break;
		}
		((TextView)v.findViewById(R.id.kp_spacerlabel)).setText("Kp " + middle_str +" Kp");
		((TextView)v.findViewById(R.id.ki_spacerlabel)).setText("Ki " + middle_str +" Ki");
		((TextView)v.findViewById(R.id.ilimit_spacerlabel)).setText("ILimit " + middle_str +" ILimit");
				
		

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void connect(UAVObjectLink lnk, View v, float mod_base) {

		TextView tv = (TextView) v.findViewById(id.pival_val);
		Button up = (Button) v.findViewById(id.pival_up);
		Button down = (Button) v.findViewById(id.pival_down);
		class ModOnClick implements View.OnClickListener {

			private TextView myTextView;
			private UAVObjectLink myObjLnk;
			private float mod_val;

			public ModOnClick(UAVObjectLink lnk, TextView tv, float mod_val) {
				myTextView = tv;
				myObjLnk = lnk;
				this.mod_val = mod_val;
				setText();
			}

			private void setText() {
				myTextView
						.setText(String.format("%.6f", myObjLnk.getAsFloat()));
			}

			public void onClick(View v) {
				myObjLnk.setField(myObjLnk.getAsFloat() + mod_val);
				setText();
			}

		}
		up.setOnClickListener(new ModOnClick(lnk, tv, mod_base));
		down.setOnClickListener(new ModOnClick(lnk, tv, -mod_base));
	}
}
