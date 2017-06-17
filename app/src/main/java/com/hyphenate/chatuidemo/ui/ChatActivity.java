package com.hyphenate.chatuidemo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;

import java.text.SimpleDateFormat;


public class ChatActivity extends BaseActivity {
    public static ChatActivity activityInstance;
    public static EaseChatFragment chatFragment;
    public static String toChatUsername;
    public static Context context;
    public static SimpleDateFormat format;
    public static boolean Can_you_guess_what_is_it = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        context = ChatActivity.this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        Can_you_guess_what_is_it = getIntent().getExtras().getBoolean("guess");

        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static void dismiss() {
        ((ChatActivity) context).finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.steward = false;
        activityInstance = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
