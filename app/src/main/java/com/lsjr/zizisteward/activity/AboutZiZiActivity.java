package com.lsjr.zizisteward.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.BaseActivity;

public class AboutZiZiActivity extends BaseActivity implements OnClickListener {

    private LinearLayout lin_feed_back;
    private LinearLayout common_question;
    private LinearLayout service_clause;


    /*ymz518我来了*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("关于孜孜");
        String version = getIntent().getStringExtra("version");
        lin_feed_back = (LinearLayout) findViewById(R.id.lin_feed_back);
        common_question = (LinearLayout) findViewById(R.id.common_question);
        service_clause = (LinearLayout) findViewById(R.id.service_clause);
        lin_feed_back.setOnClickListener(this);
        common_question.setOnClickListener(this);
        service_clause.setOnClickListener(this);
        TextView tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("V" + version);
    }

    @Override
    public int getContainerView() {
        return R.layout.activity_about_zizi;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_feed_back:
                startActivity(new Intent(AboutZiZiActivity.this, FeekBackActivity.class));// 意见反馈
                break;
            case R.id.common_question:
                startActivity(new Intent(AboutZiZiActivity.this, CommonQuestionActivity.class));
                break;
            case R.id.service_clause:
                startActivity(new Intent(AboutZiZiActivity.this, ServiceClauseActivity.class));
                break;
            default:
                break;
        }
    }
}
