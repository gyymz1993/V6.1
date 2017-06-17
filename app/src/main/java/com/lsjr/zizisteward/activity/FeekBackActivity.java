package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.Tools;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeekBackActivity extends Activity implements OnClickListener {

    private EditText et_content;
    private TextView tv_count;
    private ImageView iv_add;

    private CharSequence temp;
    private ExecutorService threadPool;
    private RelativeLayout mRe_send;
    private RelativeLayout mRe_finish;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().addActivity(this);
        setContentView(R.layout.activity_feek_back);
        mRe_send = (RelativeLayout) findViewById(R.id.re_send);
        mRe_finish = (RelativeLayout) findViewById(R.id.re_finish);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_count = (TextView) findViewById(R.id.tv_count);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        initLayout();
        et_content.addTextChangedListener(mTextWatcher);
        threadPool = Executors.newFixedThreadPool(5);
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg"));
    }

    private void initLayout() {
        mRe_finish.setOnClickListener(this);
        mRe_send.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_finish:
                finish();
                break;
            case R.id.re_send:
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(FeekBackActivity.this, "请输入您的问题", Toast.LENGTH_SHORT).show();
                    return;
                }
                getSubmit();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                break;
            default:
                break;
        }
    }

    private Uri uri;
    private Parcelable parcelableExtra;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if (data != null) {
                uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == 3) {

            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                iv_add.setImageBitmap(bitmap);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 3);
    }

    private Handler mHandler = new Handler();

    private void getSubmit() {
        CustomDialogUtils.startCustomProgressDialog(FeekBackActivity.this, "正在提交");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "18");
        map.put("user_id", EncryptUtils.addSign(Integer.parseInt(App.getUserInfo().getId()), "u"));
        map.put("content", Tools.getText(et_content));
        new HttpClientGet(FeekBackActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                CustomDialogUtils.stopCustomProgressDialog(FeekBackActivity.this);
                Toast.makeText(FeekBackActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                PreferencesUtils.putObject(FeekBackActivity.this, "userinfo", App.getUserInfo());
                finish();

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
                CustomDialogUtils.stopCustomProgressDialog(FeekBackActivity.this);
            }
        });

    }

    TextWatcher mTextWatcher = new TextWatcher() {

        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = et_content.getSelectionStart();
            editEnd = et_content.getSelectionEnd();
            tv_count.setText(temp.length() + "/240");
            if (temp.length() > 240) {
                Toast.makeText(FeekBackActivity.this, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                et_content.setText(s);
                et_content.setSelection(tempSelection);
            }

        }
    };

}
