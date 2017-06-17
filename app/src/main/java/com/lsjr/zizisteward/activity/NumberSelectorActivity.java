package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.NumberPicker;

public class NumberSelectorActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number);
		NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
		np.setMaxValue(20);
		np.setMinValue(0);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);
	}
}
