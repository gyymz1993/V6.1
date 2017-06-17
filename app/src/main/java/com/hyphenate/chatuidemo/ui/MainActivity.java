/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatuidemo.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.db.UserDao;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.ly.activity.Fragment_AddressBook;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;
import com.lsjr.zizisteward.ly.activity.Fragment_Found;
import com.lsjr.zizisteward.ly.activity.New_Topic_Old_Style;
import com.lsjr.zizisteward.ly.activity.NoteLoginActivity;

import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {

    protected static final String TAG = "MainActivity";
    // textview for unread message count
    public static TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;
    public static TextView unread_topic_number;

    private Button[] mTabs;
    // private ContactListFragment contactListFragment;
    private Fragment[] fragments;
    private int index = 0;
    private static int currentTabIndex = 0;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;
    // private ConversationListFragment conversationListFragment;
    private Fragment_ChatList f_cl;
    private Fragment_AddressBook f_ab;
    //private Fragment_Circle_Friends f_cf;
    private Fragment_Found f_f;
    private New_Topic_Old_Style n_tos;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;
    public static Context context;

    private String activity;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    public static void dismiss() {
        ((MainActivity) context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        /** 如果在另外的设备登录 */
        // make sure activity will not in background if user is logged into
        // another device or removed
        // if (savedInstanceState != null &&
        // savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
        // DemoHelper.getInstance().logout(false, null);
        // // startActivity(new Intent(this, NoteLoginActivity.class));
        // finish();
        // return;
        // } else if (savedInstanceState != null &&
        // savedInstanceState.getBoolean("isConflict", false)) {
        // finish();
        // // startActivity(new Intent(this, NoteLoginActivity.class));
        // return;
        // }

        setContentView(R.layout.em_activity_main);
        // runtime permission for android 6.0, just require all permissions here
        // for simple
        requestPermissions();

        // initView();

        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unread_topic_number = (TextView) findViewById(R.id.unread_topic_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);

        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_ring);
        mTabs[2] = (Button) findViewById(R.id.btn_circle);
        mTabs[3] = (Button) findViewById(R.id.btn_address_list);
        // select first tab
        // mTabs[0].setSelected(true);

        // umeng api
        // MobclickAgent.updateOnlineConfig(this);
        // UmengUpdateAgent.setUpdateOnlyWifi(false);
        // UmengUpdateAgent.update(this);

        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            // showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            // showAccountRemovedDialog();
        }

        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        f_cl = new Fragment_ChatList();
        n_tos = new New_Topic_Old_Style();
        //f_cf = new Fragment_Circle_Friends();
        f_ab = new Fragment_AddressBook();
        f_f = new Fragment_Found();

        fragments = new Fragment[]{f_cl, n_tos, f_f, f_ab};
        context = this;

        activity = getIntent().getStringExtra("activity");

        switch (Integer.valueOf(activity)) {
            case 0:
                //默认消息界面
                mTabs[0].setSelected(true);

                currentTabIndex = 0;

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, f_cl)
                        .add(R.id.fragment_container, f_f)
                        .add(R.id.fragment_container, n_tos)
                        .add(R.id.fragment_container, f_ab)
                        .hide(f_ab).hide(n_tos).hide(f_f)
                        .show(f_cl).commit();
                break;

            case 1:
                //默认话题
                mTabs[1].setSelected(true);

                currentTabIndex = 1;

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, f_cl)
                        .add(R.id.fragment_container, f_f)
                        .add(R.id.fragment_container, n_tos)
                        .add(R.id.fragment_container, f_ab)
                        .hide(f_cl).hide(f_f).hide(f_ab)
                        .show(n_tos).commit();
                break;

            case 2:
                //默认朋友圈
                mTabs[2].setSelected(true);

                currentTabIndex = 2;

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, f_cl)
                        .add(R.id.fragment_container, f_f)
                        .add(R.id.fragment_container, n_tos)
                        .add(R.id.fragment_container, f_ab)
                        .hide(f_cl).hide(f_f).hide(f_ab)
                        .show(f_f).commit();

                break;

            case 3:
                //默认通讯录
                mTabs[2].setSelected(true);

                currentTabIndex = 3;

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, f_cl)
                        .add(R.id.fragment_container, f_f)
                        .add(R.id.fragment_container, n_tos)
                        .add(R.id.fragment_container, f_ab)
                        .hide(f_cl).hide(f_f).hide(f_ab)
                        .show(f_ab).commit();

                break;
        }

        // register broadcast receiver to receive the change of group from
        // DemoHelper
        // registerBroadcastReceiver();

        // EMClient.getInstance().contactManager().setContactListener(new
        // MyContactListener());
        // debug purpose only
        // registerInternalDebugReceiver();
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                // Toast.makeText(MainActivity.this, "All permissions have been
                // granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                // Toast.makeText(MainActivity.this, "Permission " + permission
                // + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * init views
     */
    private void initView() {

        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unread_topic_number = (TextView) findViewById(R.id.unread_topic_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);

        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
        mTabs[1] = (Button) findViewById(R.id.btn_ring);
        mTabs[2] = (Button) findViewById(R.id.btn_circle);
        mTabs[3] = (Button) findViewById(R.id.btn_address_list);
        // select first tab
        mTabs[0].setSelected(true);
    }

    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;

            case R.id.btn_ring:
                index = 1;
                break;

            case R.id.btn_circle:
                index = 2;
                break;

            case R.id.btn_address_list:
                index = 3;
                break;
        }

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();

        if (currentTabIndex != index) {
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }

        for (int i = 0; i < 4; i++) {
            mTabs[i].setSelected(false);
        }

        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            // red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();// 获取自定义action
            }
            // end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    /**
                     * if (conversationListFragment != null) {
                     * conversationListFragment.refresh(); }
                     */

                    if (null != f_cl) {
                        f_cl.refresh();
                        n_tos.refreshData();
                    }
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    // if (conversationListFragment != null) {
                    // conversationListFragment.refresh();
                    // }
                    if (null != f_cl) {
                        f_cl.refresh();
                        n_tos.refreshData();
                    }
                } else if (currentTabIndex == 1) {
                    /**
                     * if(contactListFragment != null) {
                     * contactListFragment.refresh(); }
                     */
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                    if (null != f_cl) {
                        f_cl.refresh();
                        n_tos.refreshData();
                    }
                }
                // end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null
                            && username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10,
                                Toast.LENGTH_LONG).show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

        // unregisterBroadcastReceiver();
        //
        // try {
        // unregisterReceiver(internalDebugReceiver);
        // } catch (Exception e) {
        // }
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        // int count = getUnreadMsgCountTotal();

        int Personal_count = getPersonalMessageCount();
        int Topic_count = getTopiclMessageCount();

        if (Personal_count > 0) {
            // unreadLabel.setText(String.valueOf(Personal_count));
            // unreadLabel.setVisibility(View.VISIBLE);
            // com.lsjr.zizisteward.activity.SixthNewActivity.space = true;
            // com.lsjr.zizisteward.activity.SixthNewActivity.Toast();
            Fragment_ChatList.refreshData();
        } else {
            // unreadLabel.setVisibility(View.GONE);
        }

        if (Topic_count > 0) {
            // unread_topic_number.setText(String.valueOf(Topic_count));
            // unread_topic_number.setVisibility(View.VISIBLE);
            // com.lsjr.zizisteward.activity.SixthNewActivity.space = true;
            // com.lsjr.zizisteward.activity.SixthNewActivity.Toast();
            n_tos.refreshData();
        } else {
            // unreadLabel.setVisibility(View.GONE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    // unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public static int getUnreadMsgCountTotal() {

        // 未读总数
        int unreadMsgCountTotal = 0;
        // ChatRoom未读总条数
        int chatroomUnreadMsgCount = 0;
        // GroupChat未读总条数
        int groupChatUnreadMsgCount = 0;
        // Chat未读总条数
        int ChatUnreadMsgCount = 0;

        // 获得未读信息数
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {

            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();

            if (conversation.getType() == EMConversationType.GroupChat) {
                groupChatUnreadMsgCount = groupChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }

            if (conversation.getType() == EMConversationType.Chat) {
                ChatUnreadMsgCount = ChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }

        // System.out.println("话题未读消息数: " + groupChatUnreadMsgCount);
        // System.out.println("个人聊天未读消息数: " + ChatUnreadMsgCount);

        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    public int getPersonalMessageCount() {

        // 未读总数
        int unreadMsgCountTotal = 0;
        // ChatRoom未读总条数
        int chatroomUnreadMsgCount = 0;
        // GroupChat未读总条数
        int groupChatUnreadMsgCount = 0;
        // Chat未读总条数
        int ChatUnreadMsgCount = 0;

        // 获得未读信息数
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {

            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();

            if (conversation.getType() == EMConversationType.GroupChat) {
                groupChatUnreadMsgCount = groupChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }

            if (conversation.getType() == EMConversationType.Chat) {
                ChatUnreadMsgCount = ChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }

        // System.out.println("话题未读消息数: " + groupChatUnreadMsgCount);
        // System.out.println("个人聊天未读消息数: " + ChatUnreadMsgCount);

        return unreadMsgCountTotal - chatroomUnreadMsgCount - groupChatUnreadMsgCount;
    }

    public int getTopiclMessageCount() {

        // 未读总数
        int unreadMsgCountTotal = 0;
        // ChatRoom未读总条数
        int chatroomUnreadMsgCount = 0;
        // GroupChat未读总条数
        int groupChatUnreadMsgCount = 0;
        // Chat未读总条数
        int ChatUnreadMsgCount = 0;

        // 获得未读信息数
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {

            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();

            if (conversation.getType() == EMConversationType.GroupChat) {
                groupChatUnreadMsgCount = groupChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }

            if (conversation.getType() == EMConversationType.Chat) {
                ChatUnreadMsgCount = ChatUnreadMsgCount + conversation.getUnreadMsgCount();
            }
        }

        // System.out.println("话题未读消息数: " + groupChatUnreadMsgCount);
        // System.out.println("个人聊天未读消息数: " + ChatUnreadMsgCount);

        return unreadMsgCountTotal - chatroomUnreadMsgCount - ChatUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background

        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        // super.onSaveInstanceState(outState);
    }

    /**
     * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
     *           (keyCode == KeyEvent.KEYCODE_BACK) { moveTaskToBack(false);
     *           return true; } return super.onKeyDown(keyCode, event); }
     */

    /**
     * show the dialog when user logged into another device
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                // conflictBuilder.setPositiveButton(R.string.ok, new
                // DialogInterface.OnClickListener() {
                //
                // @Override
                // public void onClick(DialogInterface dialog, int which) {
                // dialog.dismiss();
                // conflictBuilder = null;
                // // 强制退出登录
                // Map<String, String> map = new HashMap<String, String>();
                // map.put("OPT", "198");
                // map.put("id", App.getUserInfo().getId());
                // map.put("user_id",
                // EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()),
                // "u"));
                // new HttpClientGet(MainActivity.this, null, map, false, new
                // HttpClientGet.CallBacks<String>() {
                // private LoginOutBean mBean;
                //
                // @Override
                // public void onSuccess(String result) {
                // mBean = GsonUtil.getInstance().fromJson(result,
                // LoginOutBean.class);
                // System.out.println("退出的消息" + result);
                // DemoHelper.getInstance().logout(false, new EMCallBack() {
                //
                // @Override
                // public void onSuccess() {
                // App.setLoginOutBean(mBean);
                // PreferencesUtils.putBoolean(MainActivity.this, "isLogin",
                // false);
                // Intent intent = new Intent(MainActivity.this,
//                 NoteLoginActivity.class);
                // intent.putExtra("personal", "login_out");
                // startActivity(intent);
                // finish();
                // }
                //
                // @Override
                // public void onProgress(int arg0, String arg1) {
                // }
                //
                // @Override
                // public void onError(int arg0, String arg1) {
                // }
                // });
                // }
                //
                // @Override
                // public void onFailure(MyError myError) {
                // super.onFailure(myError);
                // }
                // });
                //
                // }
                // });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    /**
     * show the dialog if user account is removed
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        // startActivity(new Intent(MainActivity.this,
//                         NoteLoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            // showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            // showAccountRemovedDialog();
        }
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                // startActivity(new Intent(MainActivity.this,
//                                 NoteLoginActivity.class));

                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

}
