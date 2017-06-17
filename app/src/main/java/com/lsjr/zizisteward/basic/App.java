package com.lsjr.zizisteward.basic;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.DemoDBManager;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.lsjr.zizisteward.activity.SimpleVoiceActivity;
import com.lsjr.zizisteward.bean.AddressBookBean;
import com.lsjr.zizisteward.bean.KeyWordBean;
import com.lsjr.zizisteward.bean.LoginOutBean;
import com.lsjr.zizisteward.bean.NoteLoginBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 *
 */
public class App extends Application {
    public static Context context;
    public static App app;
    public String userCellPhone;// 注册账号
    public String passWord;// 注册密码
    public String realName;// 姓名
    public String idNo;// 身份证号
    public String isShiming;
    public static NoteLoginBean.RepMap userInfo;
   // public static RegisterInfo registerInfo;
    public static LoginOutBean loginOutBean;
    public static KeyWordBean keyWordBean;
    public static boolean steward = false;
    public List<Activity> activities = new ArrayList<Activity>();
    public static String currentUserNick = "";
    private static final String TAG = "JPush";
    public final String PREF_USERNAME = "username";
    private static App instance;
    protected static List<EMConversation> conversationList = new ArrayList<EMConversation>();

    public static IWXAPI msgApi;
    public String wxIndentNo;
    public String wxMoney;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        context = this;
        app = this;
        instance = this;


        JPushInterface.init(this); // 初始化 JPush
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志

        DemoHelper.getInstance().init(context);

        setUserInfo(PreferencesUtils.getObject(this, "userinfo", NoteLoginBean.RepMap.class));

        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
        msgApi.registerApp(Constants.APP_ID);
    }



    public String getWxIndentNo() {
        return wxIndentNo;
    }

    public void setWxIndentNo(String wxIndentNo) {
        this.wxIndentNo = wxIndentNo;
    }

    public String getWxMoney() {
        return wxMoney;
    }

    public void setWxMoney(String wxMoney) {
        this.wxMoney = wxMoney;
    }

    public String getIsShiming() {
        return isShiming;
    }

    public void setIsShiming(String isShiming) {
        this.isShiming = isShiming;
    }

//    public static RegisterInfo getRegisterInfo() {
//        return registerInfo;
//    }
//
//    public static void setRegisterInfo(RegisterInfo registerInfo) {
//        App.registerInfo = registerInfo;
//    }

    public static KeyWordBean getKeyWordBean() {
        return keyWordBean;
    }

    public static void setKeyWordBean(KeyWordBean keyWordBean) {
        App.keyWordBean = keyWordBean;
    }

    public static LoginOutBean getLoginOutBean() {
        return loginOutBean;
    }

    public static void setLoginOutBean(LoginOutBean loginOutBean) {
        App.loginOutBean = loginOutBean;
    }

    public static NoteLoginBean.RepMap getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(NoteLoginBean.RepMap userInfo) {
        App.userInfo = userInfo;
    }

    public String getUserCellPhone() {
        return userCellPhone;
    }

    public void setUserCellPhone(String userCellPhone) {
        this.userCellPhone = userCellPhone;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    /**
     * 完美退出程序
     */
    public static void prefectExit() {
        for (Activity activity : getInstance().activities) {
            activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static Activity getActivity(String className) {
        for (Activity activity : getInstance().activities) {
            if (activity.getClass().getName().equals(className)) {
                return activity;
            }
        }
        return null;
    }

    public ProgressDialog getProgressDialog(Activity act) {
        return new ProgressDialog(act);
    }

    private Map<String, Activity> activityList = new HashMap<String, Activity>();

    // 添加Activity 到容器中
    public void addActivity(Activity activity) {
        activityList.put(activity.getLocalClassName(), activity);
    }

    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    public static Context getContext() {
        return context;
    }

    public static App getIns() {
        return instance;
    }

    public static void CallSteward(final Context context) {
        CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(context);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String isIdNumberVerified = jsonObject.getString("isIdNumberVerified");

                    if (isIdNumberVerified.equals("0")) {
                        ToastUtils.show(context, "请先实名认证");
                    } else if (isIdNumberVerified.equals("1")) {
                        conversationList.clear();
                        conversationList.addAll(loadConversationList());

                        EMConversation conversation = conversationList.get(0);

                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("friend_id", App.getUserInfo().getGmid());
                        intent.putExtra("user_id", App.getUserInfo().getId());
                        intent.putExtra(Constant.EXTRA_USER_ID, App.getUserInfo().getGname());
                        intent.putExtra("userId", App.getUserInfo().getGname());
                        intent.putExtra("nike", "(孜孜管家) " + App.getUserInfo().getGuser_name());
                        App.steward = true;

                        String date = PreferencesUtils.getString(context, "welcomes_date");

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date curDate = new Date(System.currentTimeMillis());
                        String str = formatter.format(curDate) + "-" + App.getUserInfo().getId();

                        if (null == date || date.length() < 1 || !date.equals(str)) {

                            EMMessage e = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            EMTextMessageBody body = new EMTextMessageBody("欢迎来到孜孜，小孜将竭诚为您服务!");
                            e.setReceipt(App.getUserInfo().getGname());
                            e.addBody(body);
                            e.setUnread(true);
                            conversation.insertMessage(e);

                            PreferencesUtils.putString(context, "welcomes_date", str);
                        }
                        context.startActivity(intent);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(context);
                Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                super.onFailure(myError);
            }
        });
    }

    @SuppressWarnings("unused")
    private static Collection<? extends EMConversation> loadConversationList() {
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
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            if (sortItem.second.getUserName().equals(App.getUserInfo().getGname())) {
                list.add(0, sortItem.second);
            } else {
                list.add(sortItem.second);
            }
        }

        boolean Cancer = false;

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getUserName().equals(App.getUserInfo().getGname())) {

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

    private static void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
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

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static void requestAddressBook(final Context context, final int type) {
        CustomDialogUtils.startCustomProgressDialog(context, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "206");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(context);
                AddressBookBean abBean = new Gson().fromJson(result, AddressBookBean.class);

                if (null != abBean.getFriends()) {
                    SimpleVoiceActivity.friends = abBean.getFriends();
                }

                Intent mIntent = new Intent(context, com.hyphenate.chatuidemo.ui.MainActivity.class);

//                if (type == 1) {
                mIntent.putExtra("activity", String.valueOf(type));
//                }

                context.startActivity(mIntent);
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(context);
                super.onFailure(myError);
            }
        });
    }

    /**
     * 添加分享记录
     */
    public static void AddShareRecord(final Context context, int share_type) {
        // 0 商品分享 1 视界 2 朋友圈
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "514");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("share_type", String.valueOf(share_type));
        map.put("location", "");
        map.put("url", "");
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("App", result);
                AddShareIntegral(context);
            }

            @Override
            public void onFailure(MyError myError) {
                //super.onFailure(myError);
            }
        });
    }

    /**
     * 分享成功增加积分
     */
    public static void AddShareIntegral(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "300");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("App", result);
            }

            @Override
            public void onFailure(MyError myError) {
                //super.onFailure(myError);
            }
        });
    }

    public static void LoginInstantMessage(final Context context,final String phone,final String passWord) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                DemoDBManager.getInstance().closeDB();
                DemoHelper.getInstance().setCurrentUserName(phone);
                EMClient.getInstance().login(phone, passWord, new EMCallBack() {
                    @SuppressWarnings({"unused", "static-access"})
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                    }

                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
            }
        }).start();
    }
}
