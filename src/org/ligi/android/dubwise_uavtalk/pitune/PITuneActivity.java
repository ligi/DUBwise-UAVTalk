package org.ligi.android.dubwise_uavtalk.pitune;


import java.util.HashMap;

import org.ligi.android.dubwise_uavtalk.statusvoice.UAVObjectLink;
import org.ligi.android.uavtalk.dubwise.R;
import org.ligi.android.uavtalk.dubwise.R.id;
import org.openpilot.uavtalk.UAVObjectFieldDescription;
import org.openpilot.uavtalk.UAVObjects;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PITuneActivity extends Activity {
	private HashMap<String,UAVObjectFieldDescription> fieldDescByName;
	
		
		
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.pitune);
	        
	        fieldDescByName=new HashMap<String,UAVObjectFieldDescription>();
	        for (UAVObjectFieldDescription d:UAVObjects.getStabilizationSettings().getFieldDescriptions())
	    		fieldDescByName.put(d.getName(), d);
	        		
	        Float mod_base=0.0001f;
	        connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"),0),id.kp_nick,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"),1),id.ki_nick,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("PitchRatePID"),3),id.ilimit_nick,mod_base);
	        
	        connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"),0),id.kp_roll,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"),1),id.ki_roll,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("RollRatePID"),3),id.ilimit_roll,mod_base);
	        
	        connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"),0),id.kp_yaw,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"),1),id.ki_yaw,mod_base);
	        connect(new UAVObjectLink(fieldDescByName.get("YawRatePID"),3),id.ilimit_yaw,mod_base);
	        
	   }

	   private void connect(UAVObjectLink lnk,int resid,float mod_base) {
		   View v=this.findViewById(resid);
	       TextView tv= (TextView)v.findViewById(id.pival_val);
	       Button up= (Button)v.findViewById(id.pival_up);
	       Button down= (Button)v.findViewById(id.pival_down);
	       class ModOnClick implements View.OnClickListener {

	    	   private TextView myTextView;
	    	   private UAVObjectLink myObjLnk;
	    	   private float mod_val;
	    	   
	    	   public ModOnClick(UAVObjectLink lnk,TextView tv,float mod_val) {
	    		   myTextView=tv;
	    		   myObjLnk=lnk;
	    		   this.mod_val=mod_val;
	    		   setText();
	    	   }
	    	   
	    	   private void setText() {
	    		   myTextView.setText(String.format("%.6f", myObjLnk.getAsFloat()));		
	    	   }
			
	    	   public void onClick(View v) {
	    		   myObjLnk.setField(myObjLnk.getAsFloat()+mod_val);		
	    		   setText();
	    	   }
	    	   
	       }
	       up.setOnClickListener(new ModOnClick(lnk,tv,mod_base));
	       down.setOnClickListener(new ModOnClick(lnk,tv,-mod_base));
		  // tv.setText(lnk.getValueAsString());
	   }
}
