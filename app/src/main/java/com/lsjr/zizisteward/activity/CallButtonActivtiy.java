package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.RealNameConfirmActivity.StateBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallButtonActivtiy extends Activity implements OnClickListener {
    private RelativeLayout call;
    private ImageView iv_chat_window;
    private String friend_id;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private StateBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_button);
        ImageView iv_dismiss = (ImageView) findViewById(R.id.iv_dismiss);
        iv_dismiss.setOnClickListener(this);
        this.iv_chat_window = (ImageView) super.findViewById(R.id.iv_chat_window);
        call = (RelativeLayout) findViewById(R.id.RelativeLayout_call);
        call.setOnClickListener(this);

        iv_chat_window.setOnClickListener(this);
        conversationList.clear();
        conversationList.addAll(loadConversationList());
    }

    @Override
    protected void onResume() {
        getState();
        super.onResume();
    }

    private void getState() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("状态" + result);
                bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dismiss:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.iv_chat_window:
                if (PreferencesUtils.getBoolean(CallButtonActivtiy.this, "isLogin")) {
                    if (App.getUserInfo().getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                        Toast.makeText(CallButtonActivtiy.this, R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    } else {
                        App.CallSteward(CallButtonActivtiy.this);
                    }
                } else {
                    final Dialog dialog = new Dialog(CallButtonActivtiy.this, R.style.dialog);
                    dialog.setContentView(R.layout.popup_delete_address);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_msg.setText("请先登录,好吗?");
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(CallButtonActivtiy.this, NoteLoginActivity.class);
                            intent.putExtra("personal", "chat");
                            startActivity(intent);
                            finish();
                        }
                    });
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
                    dialog.show();

                }

                break;
            case R.id.RelativeLayout_call:
                if ("1".equals(bean.getIsIdNumberVerified())) {
                    Intent phoneIntent2 = new Intent(CallButtonActivtiy.this, NewCallStewardActivity.class);
                    startActivityForResult(phoneIntent2, 123);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    final Dialog dialog = new Dialog(CallButtonActivtiy.this, R.style.dialog);
                    dialog.setContentView(R.layout.dialog_real_name_confirm);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(CallButtonActivtiy.this, RealNameConfirmActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    });
                    dialog.show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        return super.onKeyDown(keyCode, event);

    }

    private List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting so use
         * synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(
                            new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            if (sortItem.second.getUserName().toUpperCase().equals(App.getUserInfo().getGname())) {
                list.add(0, sortItem.second);
            } else {
                list.add(sortItem.second);
            }
        }

        boolean Cancer = false;

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getUserName().toUpperCase().equals(App.getUserInfo().getGname())) {

                Cancer = true;
            }
        }

        if (!Cancer) {
            EMConversation single = EMClient.getInstance().chatManager().getConversation(App.getUserInfo().getGname(),
                    EaseCommonUtils.getConversationType(1), true);
            list.add(0, single);
        }

        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == 222) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        if (requestCode == 1 && resultCode == 419) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(CallButtonActivtiy.this, NewCallStewardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 200);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
