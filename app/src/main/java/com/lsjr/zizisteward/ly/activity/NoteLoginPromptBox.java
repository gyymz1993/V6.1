package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/6/2.
 */

public class NoteLoginPromptBox extends Activity implements View.OnClickListener{

    private RelativeLayout rl_parent;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_cancel;

    private String action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.note_login_prompt_box);
        this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
        this.tv_title = (TextView) super.findViewById(R.id.tv_title);
        this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
        this.tv_content = (TextView) super.findViewById(R.id.tv_content);

        this.action = getIntent().getStringExtra("action");

        if (action.equals("deal")) {
            this.tv_title.setText("无法登录");
            this.tv_content.setText("请您仔细阅读《用户协议》并勾选");
            this.tv_cancel.setText("勾选，并提交");
        } else {
            this.tv_title.setText("错误");
            this.tv_content.setText("数字或图形验证码错误，请重新输入。");
            this.tv_cancel.setText("取消");
        }

        this.rl_parent.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_parent:
                rl_parent.setVisibility(View.GONE);
                finish();
                break;

            case R.id.tv_cancel:

                if (action.equals("deal")) {
                    setResult(77);
                } else {
                    setResult(7);
                }
                finish();
                break;
        }
    }
}
