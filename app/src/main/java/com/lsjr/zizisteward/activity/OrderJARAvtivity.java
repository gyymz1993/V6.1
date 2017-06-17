package com.lsjr.zizisteward.activity;

import com.unionpay.UPPayAssistEx;

import android.app.Activity;

public class OrderJARAvtivity extends InPutPasswordActivity {

	@Override
	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		UPPayAssistEx.startPay(activity, null, null, tn, mode);
	}

}
