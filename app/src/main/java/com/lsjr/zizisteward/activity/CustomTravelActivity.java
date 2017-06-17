package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.utils.DateUtils;

public class CustomTravelActivity extends BaseActivity implements OnClickListener {

    private EditText start, end;
    private DateUtils mDateTimePickDialogUtil;
    private TextView mNext, tianzi, chenren, ertong;
    private RelativeLayout mRe_start, re_mu;
    private Intent mIntent;
    private EditText mEd_origin, et_finish;
    private EditText mEd_days, tv_document, ed_childern;
    private CheckBox mCheckbox;
    private TextView mHong_mu, hong_chu, hong_time, hong_tianshu, hong_renshu;

    @Override
    public int getContainerView() {
        return R.layout.activity_custom_travel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("定制出行");

        mRe_start = (RelativeLayout) findViewById(R.id.re_start);
        re_mu = (RelativeLayout) findViewById(R.id.re_mu);
        mEd_origin = (EditText) findViewById(R.id.ed_origin);
        et_finish = (EditText) findViewById(R.id.et_finish);

        start = (EditText) findViewById(R.id.start);
        end = (EditText) findViewById(R.id.end);

        mNext = (TextView) findViewById(R.id.next);// 下一步

        mEd_days = (EditText) findViewById(R.id.ed_days);
        tianzi = (TextView) findViewById(R.id.tianzi);

        tv_document = (EditText) findViewById(R.id.tv_document);
        chenren = (TextView) findViewById(R.id.chenren);// 成人

        ed_childern = (EditText) findViewById(R.id.ed_childern);
        ertong = (TextView) findViewById(R.id.ertong);// 儿童

        mCheckbox = (CheckBox) findViewById(R.id.checkbox);

        mHong_mu = (TextView) findViewById(R.id.hong_mu);
        hong_chu = (TextView) findViewById(R.id.hong_chu);
        hong_time = (TextView) findViewById(R.id.hong_time);
        hong_tianshu = (TextView) findViewById(R.id.hong_tianshu);
        hong_renshu = (TextView) findViewById(R.id.hong_renshu);

        if (!(TextUtils.isEmpty(start.getText().toString()))) {
            hong_time.setVisibility(View.GONE);
        }

        if (!(TextUtils.isEmpty(mEd_days.getText().toString()))) {
            hong_tianshu.setVisibility(View.GONE);
        }

        init();

    }

    private void init() {

        start.setOnClickListener(this);
        mEd_origin.setOnClickListener(this);
        et_finish.setOnClickListener(this);
        end.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mRe_start.setOnClickListener(this);
        re_mu.setOnClickListener(this);
        start.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    hong_time.setVisibility(View.GONE);
                }
            }
        });
        end.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    hong_time.setVisibility(View.GONE);
                }
            }
        });
        mEd_days.addTextChangedListener(new TextWatcher() {// 出行天数

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tianzi.setVisibility(View.VISIBLE);
                    hong_tianshu.setVisibility(View.GONE);
                }
                if (s.length() == 0) {
                    tianzi.setVisibility(View.GONE);
                }

            }
        });

        tv_document.addTextChangedListener(new TextWatcher() {// 成人

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    chenren.setVisibility(View.GONE);
                }
                if (s.length() > 0) {
                    chenren.setVisibility(View.VISIBLE);
                    hong_renshu.setVisibility(View.GONE);
                }

            }
        });
        ed_childern.addTextChangedListener(new TextWatcher() {// 儿童

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ertong.setVisibility(View.GONE);
                }
                if (s.length() > 0) {
                    ertong.setVisibility(View.VISIBLE);
                    hong_renshu.setVisibility(View.GONE);
                }

            }
        });
        mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCheckbox.setChecked(true);
                    System.out.println("状态" + isChecked);
                } else {
                    System.out.println("状态" + isChecked);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                mDateTimePickDialogUtil = new DateUtils(CustomTravelActivity.this, "");
                mDateTimePickDialogUtil.dateTimePicKDialog(start);

                break;
            case R.id.end:

                mDateTimePickDialogUtil = new DateUtils(CustomTravelActivity.this, "");
                mDateTimePickDialogUtil.dateTimePicKDialog(end);

                break;
            case R.id.next:

                if (!(TextUtils.isEmpty(et_finish.getText().toString().trim()))
                        && !(TextUtils.isEmpty(mEd_origin.getText().toString().trim()))
                        && !(TextUtils.isEmpty(start.getText().toString().trim()))
                        && !(TextUtils.isEmpty(mEd_days.getText().toString().trim()))
                        && (!(TextUtils.isEmpty(tv_document.getText().toString().trim()))
                        || !(TextUtils.isEmpty(ed_childern.getText().toString().trim())))) {
                    mIntent = new Intent(CustomTravelActivity.this, TravelPlanActivity.class);
                    mIntent.putExtra("destination", et_finish.getText().toString().trim());
                    mIntent.putExtra("origin", mEd_origin.getText().toString().trim());
                    mIntent.putExtra("start", start.getText().toString().trim());
                    mIntent.putExtra("end", end.getText().toString().trim());
                    mIntent.putExtra("days", mEd_days.getText().toString().trim());
                    mIntent.putExtra("document", tv_document.getText().toString().trim());
                    mIntent.putExtra("children", ed_childern.getText().toString().trim());
                    if (mCheckbox.isChecked() == true) {
                        mIntent.putExtra("state", "1");
                    } else {
                        mIntent.putExtra("state", "0");
                    }

                    startActivityForResult(mIntent, 333);

                } else {
                    if (TextUtils.isEmpty(et_finish.getText().toString().trim())) {
                        mHong_mu.setVisibility(View.VISIBLE);
                    }

                    if (TextUtils.isEmpty(mEd_origin.getText().toString().trim())) {
                        hong_chu.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.isEmpty(start.getText().toString())) {
                        hong_time.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.isEmpty(mEd_days.getText().toString())) {
                        hong_tianshu.setVisibility(View.VISIBLE);
                    }
                    if (TextUtils.isEmpty(tv_document.getText().toString())
                            && TextUtils.isEmpty(ed_childern.getText().toString().trim())) {
                        hong_renshu.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.re_start:
                mIntent = new Intent(CustomTravelActivity.this, OriginActivity.class);
                startActivityForResult(mIntent, 1);
                break;
            case R.id.ed_origin:
                mIntent = new Intent(CustomTravelActivity.this, OriginActivity.class);
                startActivityForResult(mIntent, 1);
                break;
            case R.id.re_mu:
                mIntent = new Intent(CustomTravelActivity.this, DestinationActivity.class);
                startActivityForResult(mIntent, 3);
                break;
            case R.id.et_finish:
                mIntent = new Intent(CustomTravelActivity.this, DestinationActivity.class);
                startActivityForResult(mIntent, 3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            String name = data.getStringExtra("name");
            mEd_origin.setText(name);
            hong_chu.setVisibility(View.GONE);

        }
        if (requestCode == 3 && resultCode == 22) {
            String name = data.getStringExtra("name");
            et_finish.setText(name);
            mHong_mu.setVisibility(View.GONE);

        }
        if (requestCode == 333 && resultCode == 12) {
            finish();
        }
    }
}
