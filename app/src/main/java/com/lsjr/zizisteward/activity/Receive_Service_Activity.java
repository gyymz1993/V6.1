package com.lsjr.zizisteward.activity;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

public class Receive_Service_Activity extends Activity {

    private LinearLayout tv_sure;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_service_activity);

        this.tv_sure = (LinearLayout) super.findViewById(R.id.tv_sure);
        textview = (TextView) findViewById(R.id.textview);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            textview.setText(content);
        }
        // this.tv_sure.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        //
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("OPT", "198");
        // map.put("mid", App.getUserBean().getId());
        // map.put("user_id",
        // EncryptUtils.addSign(Integer.parseInt(App.getUserBean().getId()), "u"));
        // new HttpClientGet(Receive_Service_Activity.this, null, map, false,
        // new HttpClientGet.CallBacks<String>() {
        //
        // @Override
        // public void onSuccess(String result) {
        // System.out.println("�ǳ�����Ϣ" + result);
        // LoginOutBean bean = GsonUtil.getInstance().fromJson(result,
        // LoginOutBean.class);
        // App.setLoginOutBean(bean);
        // PreferencesUtils.putBoolean(Receive_Service_Activity.this, "isLogin",
        // false);
        // // Toast.makeText(Receive_Service_Activity.this,
        // // bean.getMsg(), Toast.LENGTH_SHORT).show();
        // // �����˳�������
        // DemoHXSDKHelper.getInstance().logout(false, new EMCallBack() {
        //
        // @Override
        // public void onSuccess() {
        // runOnUiThread(new Runnable() {
        // public void run() {
        //
        // CustomDialogUtils
        // .stopCustomProgressDialog(Receive_Service_Activity.this);
        // PreferencesUtils.putBoolean(Receive_Service_Activity.this, "isLogin",
        // false);
        // // PreferencesUtils.remove(PersonalInFoActivity.this,"userinfo");
        // startActivity(
//         new Intent(Receive_Service_Activity.this, NoteLoginActivity.class));
        // App.prefectExit();
        //
        // }
        // });
        // }

        // @Override
        // public void onProgress(int progress, String status) {
        //
        // }
        //
        // @Override
        // public void onError(int code, String message) {
        // runOnUiThread(new Runnable() {
        //
        // @Override
        // public void run() {
        // // TODO Auto-generated method
        // // stub
        // CustomDialogUtils
        // .stopCustomProgressDialog(Receive_Service_Activity.this);
        // // toast("unbind devicetokens
        // // failed");
        // }
        // });
        // }
        // });
        // }
        // });
        // }
        // });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        return false;
    }
}
