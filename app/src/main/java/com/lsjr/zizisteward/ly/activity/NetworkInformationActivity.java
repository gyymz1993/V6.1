package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupMemberBean;
import com.lsjr.zizisteward.bean.GroupMemberBean.GroupMember;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkInformationActivity extends Activity implements OnClickListener {
	
	/**返回键*/
	private LinearLayout ll_back;
	/**群成员实际数量*/
	private TextView tv_num;
	/**群成员最大数量*/
	private TextView tv_max;
	/**查看所有群成员*/
	private LinearLayout ll_parent;
	/**群资料界面只显示最多五个群成员*/
	private ImageView iv_one,iv_two,iv_three,iv_four,iv_five;
	/**查看群具体简介*/
	private LinearLayout ll_circle; 
	/**立即加入*/
	private TextView tv_join;
	/**退出该群*/
	private TextView tv_exit;
	/**群分享*/
	private LinearLayout ll_group_share;
	
	private TextView tv_title;
	private TextView tv_content;
	
	private LinearLayout ll_clear;
	private LinearLayout ll_all;
	
	public static String Groupname;
	public static String Groupmin;
	public static String Groupmax;
	public static String Groupid;
	public static String Groupisopen;
	public static String Groupisowner;
	public static String Groupdescription;
	public static List<GroupMember> groupMember = new ArrayList<>();
	private String activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.network_information_activity);
		this.findViewById();
	}

	private void findViewById() {
		
		this.tv_num = (TextView) super.findViewById(R.id.tv_num);
		this.tv_max = (TextView) super.findViewById(R.id.tv_max);
		this.iv_one = (ImageView) super.findViewById(R.id.iv_one);
		this.iv_two = (ImageView) super.findViewById(R.id.iv_two);
		this.tv_join = (TextView) super.findViewById(R.id.tv_join);
		this.tv_exit = (TextView) super.findViewById(R.id.tv_exit);
		this.iv_four = (ImageView) super.findViewById(R.id.iv_four);
		this.iv_five = (ImageView) super.findViewById(R.id.iv_five);
		this.tv_title = (TextView) super.findViewById(R.id.tv_title);
		this.ll_all = (LinearLayout) super.findViewById(R.id.ll_all);
		this.iv_three = (ImageView) super.findViewById(R.id.iv_three);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.tv_content = (TextView) super.findViewById(R.id.tv_content);
		this.ll_clear = (LinearLayout) super.findViewById(R.id.ll_clear);
		this.ll_parent = (LinearLayout) super.findViewById(R.id.ll_parent);
		this.ll_circle = (LinearLayout) super.findViewById(R.id.ll_circle);
		this.ll_group_share = (LinearLayout) super.findViewById(R.id.ll_group_share);
		
		this.ll_all.setOnClickListener(this);
		this.ll_back.setOnClickListener(this);
		this.tv_join.setOnClickListener(this);
		this.tv_exit.setOnClickListener(this);
		this.ll_clear.setOnClickListener(this);
		this.ll_parent.setOnClickListener(this);
		this.ll_circle.setOnClickListener(this);
		this.ll_group_share.setOnClickListener(this);
		
		Groupid = getIntent().getStringExtra("Groupid");
    	Groupmin = getIntent().getStringExtra("Groupmin");
    	activity = getIntent().getStringExtra("activity");
    	Groupmax = getIntent().getStringExtra("Groupmax");
    	Groupname = getIntent().getStringExtra("Groupname");
    	Groupisopen = getIntent().getStringExtra("Groupisopen");
    	Groupisowner = getIntent().getStringExtra("Groupisowner");
    	Groupdescription = getIntent().getStringExtra("Groupdescription");
	
    	this.tv_num.setText(Groupmin);
    	this.tv_max.setText(Groupmax);
    	this.tv_title.setText(Groupname);
    	this.tv_content.setText(Groupdescription);
    	
    	activity = getIntent().getStringExtra("activity");
    	
    	if (activity.equals("CHAT")) {
			this.tv_exit.setVisibility(View.VISIBLE);
		}else if (activity.equals("friend")) {
			this.ll_group_share.setVisibility(View.GONE);
			this.ll_clear.setVisibility(View.GONE);
			this.tv_join.setVisibility(View.VISIBLE);
		} else {
			this.tv_exit.setVisibility(View.VISIBLE);
		}
    	
    	getData();
	}
	
	private void getData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "223");
		map.put("name", PreferencesUtils.getString(NetworkInformationActivity.this, "user_account"));
		map.put("groupId", Groupid);
		
		new HttpClientGet(NetworkInformationActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("群成员:" + result);
				
				GroupMemberBean gBean = new Gson().fromJson(result, GroupMemberBean.class); 
				groupMember = gBean.getGroupMember();
				
				tv_num.setText( null == groupMember ? "0" : String.valueOf(groupMember.size()));
				
				for (int i = 0; i < groupMember.size(); i++) {
					switch (i) {
					case 0:
						Picasso.with(NetworkInformationActivity.this)
						.load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
						.into(iv_one);
						break;
						
					case 1:
						Picasso.with(NetworkInformationActivity.this)
						.load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
						.into(iv_two);
						break;
						
					case 2:
						Picasso.with(NetworkInformationActivity.this)
						.load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
						.into(iv_three);
						break;
						
					case 3:
						Picasso.with(NetworkInformationActivity.this)
						.load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
						.into(iv_four);
						break;
						
					case 4:
						Picasso.with(NetworkInformationActivity.this)
						.load(HttpConfig.IMAGEHOST + groupMember.get(i).getPhoto())
						.into(iv_five);
						break;
						
					}
				}
			}
			
			@Override
			public void onFailure(MyError myError) {
				super.onFailure(myError);
			}
		});
	}


	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.ll_back:
			
			finish();
			
			break;
			
		case R.id.ll_all:
			startActivityForResult(new Intent(NetworkInformationActivity.this
					,AllGroupMembersActivity.class)
					.putExtra("activity", "NET"),1);
			
			break;
			
		case R.id.ll_parent:
			
			break;
			
		case R.id.ll_circle:
			startActivityForResult(new Intent(NetworkInformationActivity.this,GroupToIntroduceActivity.class)
			.putExtra("activity", "NET")
			.putExtra("Groupid", Groupid)
			.putExtra("content", null == tv_content.getText().toString() ? "" : tv_content.getText().toString()), 3);
			break;		
			
		case R.id.tv_join:
			System.out.println("加入");
			Map<String, String> map = new HashMap<>();
			map.put("OPT", "230");
			map.put("name", PreferencesUtils.getString(NetworkInformationActivity.this, "user_account"));
			map.put("groupId", Groupid);
			new HttpClientGet(NetworkInformationActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					Toast.makeText(NetworkInformationActivity.this, "加入群成功", Toast.LENGTH_SHORT).show();
					
//					EMClient.getInstance()
//					.chatManager()
//					.deleteConversation(Groupid, true);
//			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(
//					NetworkInformationActivity.this);
//			inviteMessgeDao.deleteMessage(Groupid);

					New_Topic_Old_Style.CreateData();
					New_Topic_Old_Style.JoinData();
					New_Topic_Old_Style.FriendsData();
					finish();
				}
			});
			
			break;	
			
		case R.id.tv_exit:
			
			Map<String, String> map1 = new HashMap<>();
			map1.put("OPT", "231");
			map1.put("name", PreferencesUtils.getString(NetworkInformationActivity.this, "user_account"));
			map1.put("groupId", Groupid);
			new HttpClientGet(NetworkInformationActivity.this, null, map1, false, new HttpClientGet.CallBacks<String>() {

				@Override
				public void onSuccess(String result) {
					System.out.println("退出群: " + result);
					setResult(4);
					finish();
					ChatActivity.dismiss();
					Fragment_ChatList.refreshData();
					New_Topic_Old_Style.CreateData();
					New_Topic_Old_Style.JoinData();
					New_Topic_Old_Style.FriendsData();
				}
			});
			
			break;	
			
		case R.id.ll_clear:
			System.out.println(Groupid);
			EMConversation conversation = EMClient.getInstance().chatManager()
								.getConversation(EMClient.getInstance().groupManager()
					.getGroup(Groupid).getGroupId(), EMConversationType.GroupChat);
			if (conversation != null) {
				conversation.clearAllMessages();
			}
			
			Toast.makeText(NetworkInformationActivity.this, "清空消息成功...", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.ll_group_share:
			startActivity(new Intent(NetworkInformationActivity.this,GroupShareActivity.class)
			.putExtra("activity", "net").putExtra("Groupid", Groupid));
			break;
		}
	}
}
