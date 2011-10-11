package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.R;
import android.app.Activity;
import android.view.Window;

public class DUBwiseUAVTalkActivityCommons {

	public static void before_content(Activity activity) {

		String skin=DUBwiseUAVTalkPreferences.getString(activity, R.string.pref_key_skin,"default");
				
		if (skin.equals("default"))
			activity.setTheme(R.style.base_theme);
		else
			activity.setTheme(R.style.transparent_theme);
		
		activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
	}
}
