package org.ligi.android.dubwise_uavtalk;

import org.ligi.android.dubwise_uavtalk.instruments.InstrumentDisplayActivity;

import android.content.Context;
import android.content.Intent;

public class DUBwiseUAVTalkDevSettings {

	/**
	 * the intent that should be fired when the connection done 
	 *
	 * @param ctx
	 * @return
	 */
	public final static Intent getStartIntent(Context ctx) {
		return new Intent(ctx,InstrumentDisplayActivity.class); // release setting
		//return new Intent(ctx,PITuneActivity.class);
	}
}
