package com.lsjr.zizisteward.activity;

import com.unionpay.UPPayAssistEx;

import android.app.Activity;

public class VipJARActivity extends VipBuyActivity {

	@Override
	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		UPPayAssistEx.startPay(activity, null, null, tn, mode);
	}
   
}
