package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.uavtalk.dubwise.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

public class DUBwiseUAVTalkPreferencesActivity extends PreferenceActivity {

	  @Override
      protected void onCreate(Bundle savedInstanceState) {
		  DUBwiseUAVTalkActivityCommons.before_content(this);    
		  
		  super.onCreate(savedInstanceState);
		  addPreferencesFromResource(R.xml.preferences);
		  this.setContentView(R.layout.list);
		  getListView().setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	  }
}
