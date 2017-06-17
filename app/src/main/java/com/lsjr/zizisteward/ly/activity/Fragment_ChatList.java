package com.lsjr.zizisteward.ly.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseConversationList.EaseConversationListHelper;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.QuickContactsBean.LinkMan;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.slide.SlideBaseAdapter;
import com.lsjr.zizisteward.slide.SlideListView;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.shove.security.Encrypt;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_ChatList extends Fragment implements OnClickListener {
    private final static int MSG_REFRESH = 2;
    private View rootView;
    private LinearLayout ll_add;
    private LinearLayout ll_back;
    private static SlideListView slv_chat_list;
    private static FragmentChatListAdapter adapter;
    // protected static List<EMConversation> conversationList = new
    // ArrayList<EMConversation>();
    public static List<ListChatBean.ListChat> conversationList = new ArrayList<ListChatBean.ListChat>();
//    public static EaseConversationListHelper cvsListHelper;
    private EditText et_search;
    private ImageView iv_clear;
    private LinearLayout ll_clear;
    private LinearLayout ll_background;
    private LinkMan lMan;
    private List<LinkMan> linkMans = new ArrayList<LinkMan>();
    private String user_id = "";
    protected InputMethodManager imm;
    private String randomCodes = "";
    private String friend_id;

    private static Context context;
    private String Groupid;
    private String Groupname;
    private String Groupmax;
    private String Groupmin;
    private String Groupisopen;
    private String Groupisowner;
    private String Groupdescription;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_REFRESH: {
                    refreshData();
                    break;
                }
            }
        }
    };

    public static void refreshData() {

        conversationList.clear();
        conversationList.addAll(loadConversationList());

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_chat_list, null);

            findViewById();

            user_id = App.getUserInfo().getId();
        }

        return rootView;
    }

    public static String addSign(long id, String action) {
        String des = Encrypt.encrypt3DES(id + "," + action + ","
                + dateToString(new Date()), "TNNYF17DbevNyxVv");
        String md5 = Encrypt.MD5(des + "TNNYF17DbevNyxVv");
        String sign = des + md5.substring(0, 8);
        return sign;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private void findViewById() {

        context = getContext();

        this.imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        this.ll_clear = (LinearLayout) rootView.findViewById(R.id.ll_clear);

        this.ll_background = (LinearLayout) rootView
                .findViewById(R.id.ll_background);
        this.iv_clear = (ImageView) rootView.findViewById(R.id.iv_clear);
        this.et_search = (EditText) rootView.findViewById(R.id.et_search);
        this.ll_add = (LinearLayout) rootView.findViewById(R.id.ll_add);
        this.ll_back = (LinearLayout) rootView.findViewById(R.id.ll_back);
        this.slv_chat_list = (SlideListView) rootView
                .findViewById(R.id.slv_chat_list);

        conversationList = new ArrayList<>();

        conversationList.addAll(loadConversationList());

        adapter = new FragmentChatListAdapter(getActivity(), conversationList);

        this.slv_chat_list.setAdapter(adapter);

        this.ll_back.setOnClickListener(this);
        this.ll_add.setOnClickListener(this);

        this.slv_chat_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                if (position == 0) {

                    getIsRealName();

                } else {

                    String username = conversationList.get(position)
                            .getUserName();

                    Intent intent = new Intent(getActivity(),
                            ChatActivity.class);

                    if (App.getUserInfo().getGname() == username) {
                        intent.putExtra("guess", true);
                    } else {
                        intent.putExtra("guess", false);
                    }

                    if (null != conversationList.get(position).getNike()
                            && conversationList.get(position).getNike()
                            .length() > 0) {
                        intent.putExtra("nike", conversationList.get(position)
                                .getNike());
                    } else {
                        intent.putExtra("nike", conversationList.get(position)
                                .getUserName());
                    }

                    conversationList.get(position).setUnread(0);

                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        this.ll_clear.setOnClickListener(this);
        this.et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                List<ListChatBean.ListChat> _space = new ArrayList<>();

                for (int i = 0; i < conversationList.size(); i++) {

                    if (conversationList.get(i).getUserName().contains(s)
                            || conversationList.get(i).getNike().contains(s)
                            || conversationList.get(i).getNike().toLowerCase()
                            .contains(s)
                            || conversationList.get(i).getNike().toUpperCase()
                            .contains(s)) {

                        _space.add(conversationList.get(i));

                    }
                }

                adapter = new FragmentChatListAdapter(context, _space);

                slv_chat_list.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                if (s.length() > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                    ll_background.setVisibility(View.GONE);
                } else {
                    iv_clear.setVisibility(View.GONE);
                    ll_background.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onResume() {
        handler.sendEmptyMessage(MSG_REFRESH);
        super.onResume();
    }

    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    private void getIsRealName() {
        CustomDialogUtils.startCustomProgressDialog(getContext(), "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "88");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(context, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils.stopCustomProgressDialog(context);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String isIdNumberVerified = jsonObject
                                    .getString("isIdNumberVerified");

                            if (isIdNumberVerified.equals("0")) {

                                Toast.makeText(context, "请先实名认证", Toast.LENGTH_SHORT).show();

                            } else if (isIdNumberVerified.equals("1")) {

                                String username = conversationList.get(0)
                                        .getUserName();

                                Intent intent = new Intent(getActivity(),
                                        ChatActivity.class);

                                if (App.getUserInfo().getGname() == username) {
                                    intent.putExtra("guess", true);
                                } else {
                                    intent.putExtra("guess", false);
                                }

                                if (null != conversationList.get(0).getNike()
                                        && conversationList.get(0).getNike()
                                        .length() > 0) {
                                    intent.putExtra("nike", conversationList
                                            .get(0).getNike());
                                } else {
                                    intent.putExtra("nike", conversationList
                                            .get(0).getUserName());
                                }

                                conversationList.get(0).setUnread(0);


                                intent.putExtra(Constant.EXTRA_USER_ID,
                                        username);
                                startActivity(intent);
                                adapter.notifyDataSetChanged();
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

    private class FragmentChatListAdapter extends SlideBaseAdapter {

        private ViewHolder view;
        private Context context;
        // private List<EMConversation> data;
        private List<ListChatBean.ListChat> data;

        public FragmentChatListAdapter(Context context, List<ListChatBean.ListChat> data) {
            super(context);
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return null == data ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = createConvertView(position);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            if (data.get(position).getUserName()
                    .equals(App.getUserInfo().getGname())
                    || null == data.get(position).getUserName()
                    || data.get(position).getUserName().length() < 1) {

                view.iv_steward_head.setVisibility(View.VISIBLE);
                view.iv_head.setVisibility(View.GONE);

                if (null != App.getUserInfo().getGphoto()
                        && App.getUserInfo().getGphoto().length() > 0) {
                    Picasso.with(context)
                            .load(HttpConfig.IMAGEHOST
                                    + App.getUserInfo().getGphoto())
                            .into(view.iv_steward_head);
                } else {
                    view.iv_steward_head.setImageResource(R.drawable.icon_head);
                }

                view.iv_steward_topic.setVisibility(View.VISIBLE);
                view.tv_name.setText(data.get(position).getNike());

                if (null != data.get(position).getM_content()
                        && data.get(position).getM_content().length() > 0) {

                    view.tv_content_steward.setVisibility(View.VISIBLE);
                    view.tv_content.setVisibility(View.GONE);

                    if (data.get(position).getM_content()
                            .equals("欢迎来到孜孜，小孜将竭诚为您服务!")) {

                        view.tv_content_steward.setText(data.get(position)
                                .getM_content());

                    } else {

                        view.tv_content_steward.setText(ReturnContent(
                                data.get(position).getM_content(),
                                data.get(position).getM_type()));

                    }

                    view.tv_time.setText(data.get(position).getM_time());

                } else {

                    view.tv_content_steward.setText("欢迎来到孜孜，小孜将竭诚为您服务!");

                    view.tv_time.setText("");
                }

            } else {

                view.iv_steward_topic.setVisibility(View.GONE);
                view.iv_steward_head.setVisibility(View.GONE);
                view.iv_head.setVisibility(View.VISIBLE);
                view.tv_content_steward.setVisibility(View.GONE);
                view.tv_content.setVisibility(View.VISIBLE);

                if (null != data.get(position).getPhoto()
                        && data.get(position).getPhoto().length() > 1) {
                    Picasso.with(context).load(data.get(position).getPhoto())
                            .into(view.iv_head);
                } else {
                    view.iv_head
                            .setImageResource(R.drawable.ease_default_avatar);
                }

                view.tv_name.setText(data.get(position).getNike());

                if (null != data.get(position).getM_content()
                        && data.get(position).getM_content().length() > 0) {

                    view.tv_content.setText(ReturnContent(data.get(position)
                            .getM_content(), data.get(position).getM_type()));

                    view.tv_time.setText(data.get(position).getM_time());

                } else {
                    view.tv_content.setText("");

                    view.tv_time.setText("");
                }
            }

            if (data.get(position).getUnread() > 0) {
                // 显示未读消息数
                view.iv_red.setVisibility(View.VISIBLE);
                view.tv_number.setVisibility(View.VISIBLE);
                view.tv_number.setText("[" + data.get(position).getUnread()
                        + "条] ");
            } else {
                view.iv_red.setVisibility(View.GONE);
                view.tv_number.setVisibility(View.GONE);
            }

            view.ll_delete.setTag(position);
            view.ll_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    if (!data.get(pos).getUserName()
                            .equals(App.getUserInfo().getGname())) {
                        EMClient.getInstance()
                                .chatManager()
                                .deleteConversation(
                                        data.get(pos).getUserName(), true);
                        InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
                                context);
                        inviteMessgeDao.deleteMessage(data.get(pos)
                                .getUserName());
                        refresh();
                        ((MainActivity) getActivity()).updateUnreadLabel();
                    } else {
                        refresh();
                    }
                }
            });

            return convertView;
        }

        @Override
        public int getFrontViewId(int position) {
            return R.layout.fragment_chat_list_item;
        }

        @Override
        public int getLeftBackViewId(int position) {
            return R.layout.left_delete;
        }

        @Override
        public int getRightBackViewId(int position) {
            return 0;
        }

        private class ViewHolder {

            private RoundImageView iv_head;
            private TextView tv_name;
            private TextView tv_content;

            /**
             * 消息附件布局
             */
            private LinearLayout ll_adjunct;
            /**
             * 消息时间
             */
            private TextView tv_time;
            /**
             * 新消息红点
             */
            private ImageView iv_red;

            private LinearLayout ll_delete;
            private ImageView iv_steward_head;
            private ImageView iv_steward_topic;
            private TextView tv_number;
            private TextView tv_content_steward;

            public ViewHolder(View v) {
                this.tv_content_steward = (TextView) v
                        .findViewById(R.id.tv_content_steward);
                this.tv_number = (TextView) v.findViewById(R.id.tv_number);
                this.iv_red = (ImageView) v.findViewById(R.id.iv_red);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_time = (TextView) v.findViewById(R.id.tv_time);
                this.iv_head = (RoundImageView) v.findViewById(R.id.iv_head);
                this.tv_content = (TextView) v.findViewById(R.id.tv_content);
                this.ll_delete = (LinearLayout) v.findViewById(R.id.ll_delete);
                this.ll_adjunct = (LinearLayout) v
                        .findViewById(R.id.ll_adjunct);
                this.iv_steward_head = (ImageView) v
                        .findViewById(R.id.iv_steward_head);
                this.iv_steward_topic = (ImageView) v
                        .findViewById(R.id.iv_steward_topic);
            }
        }
    }

    private String ReturnContent(String content, String type) {

        if (type.equals("TXT")) {
            String[] one = content.split("msg\\{from\\:");

            String[] two = one[1].split("\\, to\\:");

            String[] three = one[1].split(" body\\:txt\\:");

            return three[1].substring(1, three[1].length() - 1);

        } else if (type.equals("IMAGE")) {
            return "一张图片消息";
        } else if (type.equals("FILE")) {
            return "一个文件消息";
        } else if (type.equals("VOICE")) {
            return "一 条语音消息";
        } else if (type.equals("VIDEO")) {
            return "一条视频消息";
        } else {
            return "";
        }
    }

    private static List<ListChatBean.ListChat> loadConversationList() {
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

        boolean Cancer = false;

        List<EMConversation> HXlist = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            if (sortItem.second.getType().toString().equals("Chat")) {
                if (sortItem.second.getUserName().equals(
                        App.getUserInfo().getGname())) {
                    HXlist.add(0, sortItem.second);
                    Cancer = true;
                } else {
                    HXlist.add(sortItem.second);
                }
            }
        }

        String date;

        if (null != PreferencesUtils.getString(context, "welcomes_date")) {

            date = PreferencesUtils.getString(context, "welcomes_date");
        } else {
            date = null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate) + "-" + App.getUserInfo().getId();

        if (!Cancer) {
            EMConversation single = EMClient
                    .getInstance()
                    .chatManager()
                    .getConversation(App.getUserInfo().getGname(),
                            EaseCommonUtils.getConversationType(1), true);

            if (null == date || date.length() < 1 || !date.equals(str)) {
                EMMessage e = EMMessage
                        .createReceiveMessage(EMMessage.Type.TXT);
                e.setReceipt(App.getUserInfo().getGname());
                EMTextMessageBody body = new EMTextMessageBody(
                        "欢迎来到孜孜，小孜将竭诚为您服务!");
                e.setUnread(true);
                e.addBody(body);
                single.appendMessage(e);
                PreferencesUtils.putString(context, "welcomes_date", str);
            }

            HXlist.add(0, single);
        } else {

            if (null == date || date.length() < 1 || !date.equals(str)) {
                EMMessage e = EMMessage
                        .createReceiveMessage(EMMessage.Type.TXT);
                e.setReceipt(App.getUserInfo().getGname());
                EMTextMessageBody body = new EMTextMessageBody(
                        "欢迎来到孜孜，小孜将竭诚为您服务!");
                e.setUnread(true);
                e.addBody(body);
                HXlist.get(0).appendMessage(e);
                PreferencesUtils.putString(context, "welcomes_date", str);
            }
        }

        List<ListChatBean.ListChat> list = new ArrayList<>();

        for (int i = 0; i < HXlist.size(); i++) {

            ListChatBean.ListChat lcBean = new ListChatBean.ListChat();

            if (HXlist.get(i).getType().toString().equals("Chat")) {

                if (HXlist.get(i).getUserName().equals(App.getUserInfo().getGname())) {
                    // 如果是管家

                    // 聊天类型
                    lcBean.setChatType(HXlist.get(i).getType().toString());

                    // 用户帐号
                    lcBean.setUserName(HXlist.get(i).getUserName());

                    // 用户聊天内容
                    lcBean.setM_content(null == HXlist.get(i).getLastMessage() ? "欢迎来到孜孜，小孜将竭诚为您服务!"
                            : HXlist.get(i).getLastMessage().toString());

                    // 用户聊天内容类型
                    lcBean.setM_type(null == HXlist.get(i).getLastMessage() ? "TXT"
                            : HXlist.get(i).getLastMessage().getType()
                            .toString());

                    // 用户聊天的时间
                    lcBean.setM_time(null == HXlist.get(i).getLastMessage() ? ""
                            : EaseUserUtils.returnTime(HXlist.get(i)
                            .getLastMessage().getMsgTime()));

                    // 消息未读数
                    lcBean.setUnread(null == HXlist.get(i).getLastMessage() ? 0
                            : HXlist.get(i).getUnreadMsgCount());

                    // 所有消息数
                    lcBean.setAllMsg(null == HXlist.get(i).getLastMessage() ? 0
                            : HXlist.get(i).getAllMsgCount());

                    // 昵称
                    lcBean.setNike("(孜孜管家) " + App.getUserInfo().getGuser_name());

                    // 头像
                    lcBean.setPhoto(HttpConfig.IMAGEHOST
                            + App.getUserInfo().getGphoto());

                    // 管家需要置顶
                    list.add(0, lcBean);

                } else {
                    // 如果是用户

                    // 聊天类型
                    lcBean.setChatType(HXlist.get(i).getType().toString());

                    // 用户帐号
                    lcBean.setUserName(HXlist.get(i).getUserName());

                    // 用户聊天内容
                    lcBean.setM_content(null == HXlist.get(i).getLastMessage() ? ""
                            : HXlist.get(i).getLastMessage().toString());

                    // 用户聊天内容类型
                    lcBean.setM_type(null == HXlist.get(i).getLastMessage() ? ""
                            : HXlist.get(i).getLastMessage().getType()
                            .toString());

                    // 用户聊天的时间
                    lcBean.setM_time(null == HXlist.get(i).getLastMessage() ? ""
                            : EaseUserUtils.returnTime(HXlist.get(i)
                            .getLastMessage().getMsgTime()));

                    // 消息未读数
                    lcBean.setUnread(null == HXlist.get(i).getLastMessage() ? 0
                            : HXlist.get(i).getUnreadMsgCount());

                    // 所有消息数
                    lcBean.setAllMsg(null == HXlist.get(i).getLastMessage() ? 0
                            : HXlist.get(i).getAllMsgCount());

                    // 昵称
                    lcBean.setNike(EaseUserUtils.ReturnNike(HXlist.get(i)
                            .getUserName()));

                    // 头像
                    lcBean.setPhoto(EaseUserUtils.ReturnAvatar(HXlist.get(i)
                            .getUserName()));

                    // 用户还是按环信的顺序排
                    list.add(lcBean);
                }
            }
        }

        int space = 0;

        for (int i = 0; i < list.size(); i++) {
            space += list.get(i).getUnread();
        }

        if (space > 0) {
            MainActivity.unreadLabel.setText(String.valueOf(space));
            MainActivity.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            MainActivity.unreadLabel.setVisibility(View.INVISIBLE);
        }

        return list;
    }

    @Override
    public void onStop() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_back:

                MainActivity.dismiss();

                break;

            case R.id.ll_add:

                startActivity(new Intent(getActivity(), NewFriendWayActivity.class));

                break;

            case R.id.ll_clear:

                this.et_search.setText("");

                conversationList.clear();
                conversationList.addAll(loadConversationList());
                adapter = new FragmentChatListAdapter(getActivity(),
                        conversationList);
                this.slv_chat_list.setAdapter(adapter);

                this.imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);

                break;
        }
    }
}
