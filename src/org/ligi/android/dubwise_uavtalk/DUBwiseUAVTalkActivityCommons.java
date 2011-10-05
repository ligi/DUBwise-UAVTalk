package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.R;

import android.app.Activity;
import android.view.Window;

public class DUBwiseUAVTalkActivityCommons {

	public static void before_content(Activity activity) {
		activity.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		//activity.setTheme(R.style.transparent_theme);
		activity.setTheme(R.style.base_theme);
	}
}
