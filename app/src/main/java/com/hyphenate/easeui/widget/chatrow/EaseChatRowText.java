package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.ly.activity.Fragment_ChatList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {

        contentView = (TextView) findViewById(R.id.tv_chatcontent);
//		
//		contentView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				TextView tv = (TextView) v;
//				System.out.println(tv.getText().toString());
//			}
//		});
    }

    public static String exChange(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(Character.toLowerCase(c));
                } else if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                }
            }
        }

        return sb.toString();
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());

        if (null != message && !message.isAcked()) {
            Date d1 = new Date(message.getMsgTime());
            Map<String, String> map = new HashMap<>();

            if (message.getFrom().toString().equals(message.getFrom())) {
                map.put("OPT","217");
                map.put("steward_id", App.getUserInfo().getGmid());
            } else {
                map.put("OPT", "208");
            }

            map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
            map.put("from_user_id", App.getUserInfo().getGmid());
            map.put("to_user_id", App.getUserInfo().getId());
            map.put("post_messages", txtBody.getMessage().toString());
            map.put("sign", "receive");
            map.put("messageTime", ChatActivity.format.format(d1));

            new HttpClientGet(context, null, map, false, new HttpClientGet.CallBacks<String>() {

                @Override
                public void onSuccess(String result) {
//                    Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(MyError myError) {

                    //super.onFailure(myError);
                }
            });
        }

        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

    }
}
