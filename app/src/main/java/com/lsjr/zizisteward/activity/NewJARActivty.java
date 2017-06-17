package com.lsjr.zizisteward.activity;

import com.unionpay.UPPayAssistEx;

import android.app.Activity;

public class NewJARActivty extends NewPersonalActivity {

	@Override
	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		UPPayAssistEx.startPay(activity, null, null, tn, mode);
	}

}
