package com.hyphenate.easeui.utils;

import android.content.Context;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.SimpleVoiceActivity;
import com.lsjr.zizisteward.activity.SixthNewActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.InstantUserBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        System.out.println(username);
        if (username.equals(App.getUserInfo().getGname())) {
            Glide.with(context).load(HttpConfig.IMAGEHOST + App.getUserInfo().getGphoto()).into(imageView);
        } else {
            getUserAvatar(context, username, imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(Context context, String username, TextView textView) {
        if (textView != null) {
            getUserNike(context, username, textView);
        }
    }

    public static String returnUserNick(Context context, final String username) {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "429");
        map.put("name", username);

        return username;
    }

    public static String ReturnAvatar(String username) {

        String u_n = "";

        if (null != SimpleVoiceActivity.friends && SimpleVoiceActivity.friends.size() > 0) {

            for (int i = 0; i < SimpleVoiceActivity.friends.size(); i++) {

                if (username.equals(SimpleVoiceActivity.friends.get(i).getAccount())) {

                    u_n = HttpConfig.IMAGEHOST + SimpleVoiceActivity.friends.get(i).getPhoto();

                }

            }

        }

        return null == u_n && u_n.length() < 1 ? null : u_n;

    }

    public static String ReturnNike(String username) {

        String u_n = username;

        if (null != SimpleVoiceActivity.friends && SimpleVoiceActivity.friends.size() > 0) {

            for (int i = 0; i < SimpleVoiceActivity.friends.size(); i++) {

                if (username.equals(SimpleVoiceActivity.friends.get(i).getAccount())) {

                    u_n = SimpleVoiceActivity.friends.get(i).getUser_name();

                }

            }

        }

        return u_n;

    }

    public static void getUserAvatar(final Context context, final String username, ImageView imageView) {

        if (null == username || username.length() < 1) {
            Glide.with(context).load(HttpConfig.IMAGEHOST + App.getUserInfo().getGphoto()).into(imageView);
        } else if (username.equals(App.getUserInfo().getGname())) {
            Glide.with(context).load(HttpConfig.IMAGEHOST + App.getUserInfo().getGphoto()).into(imageView);
        } else {
            if (null != SimpleVoiceActivity.friends && SimpleVoiceActivity.friends.size() > 0) {
                boolean space = false;
                for (int i = 0; i < SimpleVoiceActivity.friends.size(); i++) {
                    if (username.equals(SimpleVoiceActivity.friends.get(i).getAccount())) {

                        Glide.with(context).load(HttpConfig.IMAGEHOST + SimpleVoiceActivity.friends.get(i).getPhoto()).into(imageView);
                        space = true;
                        break;
                    }
                }

                if (!space) {
                    final ImageView iv = imageView;
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "429");
                    map.put("name", username);
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {

                            InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                            if (null != iuBean) {
                                Glide.with(context).load(HttpConfig.IMAGEHOST + iuBean.getPhoto()).into(iv);
                            } else {
                                if (null == username || username.length() < 1) {
                                    Glide.with(context).load(R.drawable.icon_head).into(iv);
                                } else {
                                    Glide.with(context).load(R.drawable.ease_default_avatar).into(iv);
                                }
                            }
                        }
                    });
                }
            } else {
                final ImageView iv = imageView;
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "429");
                map.put("name", username);
                new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                        if (null != iuBean) {
                            Glide.with(context).load(HttpConfig.IMAGEHOST + iuBean.getPhoto()).into(iv);
                        } else {
                            if (null == username || username.length() < 1) {
                                Glide.with(context).load(R.drawable.icon_head).into(iv);
                            } else {
                                Glide.with(context).load(R.drawable.ease_default_avatar).into(iv);
                            }
                        }
                    }
                });
            }
        }
    }

    public static void getUserData(final Context context, final String username, TextView tv, ImageView iv, int pos) {

        final TextView _tv = tv;
        final ImageView _iv = iv;
        final int p = pos;

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "429");
        map.put("name", username);
        new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                if (_tv != null && _iv != null) {

                    if (null != iuBean) {
                        _tv.setText(iuBean.getUser_name() + "(临时会话)");
                        Glide.with(context).load(HttpConfig.IMAGEHOST + iuBean.getPhoto()).into(_iv);

                        if (null != Fragment_ChatList.conversationList && Fragment_ChatList.conversationList.size() >= (p + 1)) {
                            Fragment_ChatList.conversationList.get(p).setNike(iuBean.getUser_name() + "(临时会话)");
                        }
                    } else {
                        _tv.setText(username + "(临时会话)");
                        if (null != Fragment_ChatList.conversationList && Fragment_ChatList.conversationList.size() >= (p + 1)) {
                            Fragment_ChatList.conversationList.get(p).setNike(username + "(临时会话)");
                        }
                    }
                }
            }
        });
    }

    public static void getUserNike(final Context context, final String username, TextView textView) {

        if (null == username || username.length() < 1) {
            textView.setText("孜孜管家");
        } else {
            if (null != SimpleVoiceActivity.friends && SimpleVoiceActivity.friends.size() > 0) {
                boolean space = false;
                for (int i = 0; i < SimpleVoiceActivity.friends.size(); i++) {
                    if (username.equals(SimpleVoiceActivity.friends.get(i).getAccount())) {
                        textView.setText(SimpleVoiceActivity.friends.get(i).getUser_name());
                        space = true;
                        break;
                    }
                }

                if (!space) {

                    final TextView tv = textView;

                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "429");
                    map.put("name", username);
                    new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {

                            InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                            if (tv != null) {

                                if (null != iuBean) {
                                    tv.setText(iuBean.getUser_name());
                                } else {
                                    tv.setText(username);
                                }
                            }
                        }
                    });
                }
            } else {

                final TextView tv = textView;

                Map<String, String> map = new HashMap<>();
                map.put("OPT", "429");
                map.put("name", username);
                new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {

                        InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                        if (tv != null) {

                            if (null != iuBean) {
                                tv.setText(iuBean.getUser_name());
                            } else {
                                tv.setText(username);
                            }
                        }
                    }
                });
            }
        }
    }

    public static void getNewUserNike(final Context context, final String username, TextView textView, String content) {

        if (null != SimpleVoiceActivity.friends && SimpleVoiceActivity.friends.size() > 0) {
            boolean space = false;
            for (int i = 0; i < SimpleVoiceActivity.friends.size(); i++) {
                if (username.equals(SimpleVoiceActivity.friends.get(i).getAccount())) {
                    textView.setText(SimpleVoiceActivity.friends.get(i).getUser_name());
                    space = true;
                    break;
                }
            }

            if (!space) {

                final TextView tv = textView;

                Map<String, String> map = new HashMap<>();
                map.put("OPT", "429");
                map.put("name", username);
                new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);

                        InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                        if (tv != null) {

                            if (null != iuBean) {
                                tv.setText(iuBean.getUser_name());
                            } else {
                                tv.setText(username);
                            }
                        }
                    }
                });
            }
        } else {

            final TextView tv = textView;

            Map<String, String> map = new HashMap<>();
            map.put("OPT", "429");
            map.put("name", username);
            new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
                    System.out.println(result);

                    InstantUserBean iuBean = new Gson().fromJson(result, InstantUserBean.class);

                    if (tv != null) {

                        if (null != iuBean) {
                            tv.setText(iuBean.getUser_name());
                        } else {
                            tv.setText(username);
                        }
                    }
                }
            });
        }
    }

    public static String returnTime(long space) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        SimpleDateFormat format_year = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_month = new SimpleDateFormat("MM");
        SimpleDateFormat format_day = new SimpleDateFormat("dd");
        SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");

        //系统时间
        Date current_date = new Date(System.currentTimeMillis());
        int current_year = Integer.valueOf(format_year.format(current_date));
        int current_month = Integer.valueOf(format_month.format(current_date));
        int current_day = Integer.valueOf(format_day.format(current_date));
        String current_time = format_time.format(current_date);

        //消息时间
        Date actual_date = new Date(space);
        int actual_year = Integer.valueOf(format_year.format(actual_date));
        int actual_month = Integer.valueOf(format_month.format(actual_date));
        int actual_day = Integer.valueOf(format_day.format(actual_date));
        String actual_time = format_time.format(actual_date);

        if (current_year > actual_year) {
            return "三天前";
        } else if (current_year == actual_year) {
            if (current_month > actual_month) {
                return "三天前";
            } else if (current_month == actual_month) {
                int _space = current_day - actual_day;
                if (_space == 1) {
                    return "昨天";
                } else if (_space == 2) {
                    return "前天";
                } else if (_space == 0) {
                    return actual_time;
                } else {
                    return "三天前";
                }
            } else {
                return "三天前";
            }
        } else {
            return "三天前";
        }
    }

    public static void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList,
                new Comparator<Pair<Long, EMConversation>>() {
                    @Override
                    public int compare(final Pair<Long, EMConversation> con1,
                                       final Pair<Long, EMConversation> con2) {

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

    public static List<EMConversation> loadConversationList() {
        Map<String, EMConversation> conversations = EMClient.getInstance()
                .chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation
                            .getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            EaseUserUtils.sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        return list;
    }
}
