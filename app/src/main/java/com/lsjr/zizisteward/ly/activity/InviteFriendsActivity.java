package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupFriendBean;
import com.lsjr.zizisteward.bean.GroupMemberBean;
import com.lsjr.zizisteward.bean.GroupFriendBean.GroupFriend;
import com.lsjr.zizisteward.bean.GroupMemberBean.GroupMember;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class InviteFriendsActivity extends Activity implements OnClickListener {
	private LinearLayout ll_back;
	private LinearLayout ll_sure;
	private LinearLayout ll_search;
	private EditText et_search;
	private ImageView iv_clear;
	private ListView lv;
	private String Groupid;
	private String Groupmin;
	private String Groupmax;
	private InputMethodManager imm;
	private List<GroupFriend> groupFriend = new ArrayList<>();
	private InviteFriendsAdapter adapter;
	private int residue = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.invite_friends_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.lv = (ListView) super.findViewById(R.id.lv);
		this.et_search = (EditText) findViewById(R.id.et_search);
		this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.ll_sure = (LinearLayout) super.findViewById(R.id.ll_sure);
		this.ll_search = (LinearLayout) super.findViewById(R.id.ll_search);
		
		this.ll_back.setOnClickListener(this);
		this.ll_sure.setOnClickListener(this);
		this.iv_clear.setOnClickListener(this);
		
		this.Groupid = getIntent().getStringExtra("Groupid");
		this.Groupmin = getIntent().getStringExtra("Groupmin");
		this.Groupmax = getIntent().getStringExtra("Groupmax");
		
		residue = Integer.valueOf(Groupmax) - Integer.valueOf(Groupmin);  
		
		imm = (InputMethodManager) InviteFriendsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		
		getData();
		
		this.et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (  null != groupFriend ) {
					List<GroupFriend> g_space = new ArrayList<>();
					
					for (int i = 0; i < groupFriend.size(); i++) {
						if (groupFriend.get(i).getUser_name().contains(s)) {
							g_space.add(groupFriend.get(i));
						}
					}
					
					adapter = new InviteFriendsAdapter(InviteFriendsActivity.this, g_space);
					
					lv.setAdapter(adapter);
					
					if (s.length() > 0) {
						iv_clear.setVisibility(View.VISIBLE);
					}else {
						iv_clear.setVisibility(View.GONE);
					}
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
	protected void onResume() {
		super.onResume();
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
	}
	
	@Override
	protected void onDestroy() {
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		super.onDestroy();
	}
	
	private void getData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "226");
		map.put("groupId", Groupid);
		map.put("name", PreferencesUtils.getString(InviteFriendsActivity.this, "user_account"));
		new HttpClientGet(InviteFriendsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println(result);
				GroupFriendBean gfBean = new Gson().fromJson(result, GroupFriendBean.class);
				groupFriend = gfBean.getGroupFriend();
				adapter = new InviteFriendsAdapter(InviteFriendsActivity.this, groupFriend);
				
				/**for (int j = 0; j < GanapatiDataActivity.groupMember.size(); j++) {
					for (int i = 0; i < groupFriend.size(); i++) {
						
						if (GanapatiDataActivity.groupMember.get(j).getMember_name().equals(groupFriend.get(i).getName())) {
							groupFriend.remove(i);
						}else {
							groupFriend.get(i).setSpace(false);
						}
					}
				}*/
				
				lv.setAdapter(adapter);
				
				adapter.notifyDataSetChanged();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if (residue > 0) {
					
					if (groupFriend.get(position).isSpace()) {
						residue -= 1;
					}else {
						residue += 1;
					}
					groupFriend.get(position).setSpace(!groupFriend.get(position).isSpace());
					
					
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private class InviteFriendsAdapter extends BaseAdapter {
		private Context context;
		private ViewHolder view;
		private List<GroupFriend> groupFriend;
		
		public InviteFriendsAdapter(Context context,List<GroupFriend> groupFriend) {
			this.context = context;
			this.groupFriend = groupFriend;
		}

		@Override
		public int getCount() {
			return null == groupFriend ? 0 : groupFriend.size();
		}

		@Override
		public Object getItem(int position) {
			return groupFriend.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if ( null == convertView) {
				convertView = LayoutInflater.from(context).inflate(R.layout.all_group_members_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
			}else {
				view = (ViewHolder) convertView.getTag();
			}
			
			if (residue > 0 ) {
				view.iv_select.setVisibility(View.VISIBLE);
			}else {
				view.iv_select.setVisibility(View.GONE);
			}
			
			if (groupFriend.get(position).isSpace()) {
				view.iv_select.setImageResource(R.drawable.icon_all_true);
			}else {
				view.iv_select.setImageResource(R.drawable.icon_all_false);
			}
			
			Picasso.with(context).load(HttpConfig.IMAGEHOST + groupFriend.get(position).getPhoto()).into(view.iv);
			
			view.tv.setText(groupFriend.get(position).getUser_name());
			
			return convertView;
		}
		
		private class ViewHolder {
			
			private TextView tv;
			private ImageView iv;
			private ImageView iv_select;
			
			public ViewHolder(View v) {
				this.tv = (TextView) v.findViewById(R.id.tv);
				this.iv = (ImageView) v.findViewById(R.id.iv);
				this.iv_select = (ImageView) v.findViewById(R.id.iv_select);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		
		case R.id.ll_sure:
			
			String str = "";
			
			if ( null != groupFriend) {
				
				for (int i = 0; i < groupFriend.size(); i++) {
					if (groupFriend.get(i).isSpace()) {
						if (str.equals("")) {
							str = groupFriend.get(i).getName();
						}else {
							str += "," + groupFriend.get(i).getName();
						}
					}
				}
			}
			
			if (str.length() > 0) {
				Map<String, String> map = new HashMap<>();
				map.put("OPT", "225");
				map.put("groupId", Groupid);
				map.put("addMember", str);
				new HttpClientGet(InviteFriendsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						//getData();
						finish();
					}
				});
			}
			
			break;
			
		case R.id.iv_clear:
			this.et_search.setText("");
			adapter = new InviteFriendsAdapter(InviteFriendsActivity.this, groupFriend);
			adapter.notifyDataSetChanged();
			this.imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
			break;
		}
	}
}
