package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupMemberBean;
import com.lsjr.zizisteward.bean.GroupMemberBean.GroupMember;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AllGroupMembersActivity extends Activity implements OnClickListener {

	private LinearLayout ll_back;
	private LinearLayout ll_delete;
	private TextView tv_edit;
	private ListView lv;
	private AllGroupMembersAdapter adapter;
	private LinearLayout ll_search;
	private EditText et_search;
	private ImageView iv_clear;
	private String activity;
	private List<GroupMember> groupMember = new ArrayList<>();
	private String Groupid;
	private InputMethodManager imm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.all_group_members_activity);
		this.findViewById();
	}

	private void findViewById() {
		this.lv = (ListView) super.findViewById(R.id.lv);
		this.et_search = (EditText) findViewById(R.id.et_search);
		this.tv_edit = (TextView) super.findViewById(R.id.tv_edit);
		this.iv_clear = (ImageView) super.findViewById(R.id.iv_clear);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.ll_delete = (LinearLayout) super.findViewById(R.id.ll_delete);
		this.ll_search = (LinearLayout) super.findViewById(R.id.ll_search);
		
		this.tv_edit.setOnClickListener(this);
		this.ll_back.setOnClickListener(this);
		this.ll_delete.setOnClickListener(this);
		
		this.activity = getIntent().getStringExtra("activity");
		
		if (activity.equals("GAN")) {
			groupMember = GanapatiDataActivity.groupMember;
			Groupid = GanapatiDataActivity.Groupid;
			this.tv_edit.setVisibility(View.VISIBLE);
		}else {
			groupMember = NetworkInformationActivity.groupMember;
			Groupid = NetworkInformationActivity.Groupid;
		}
		
		for (int i = 0; i < groupMember.size(); i++) {
			groupMember.get(i).setSpace(false);
		}
		
		imm = (InputMethodManager) AllGroupMembersActivity.this.getSystemService(
				Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		
		adapter = new AllGroupMembersAdapter(AllGroupMembersActivity.this, groupMember);
		this.lv.setAdapter(adapter);
		
		this.iv_clear.setOnClickListener(this);
		
		this.lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(groupMember.get(position).getIs_owner());
				if (!groupMember.get(position).getIs_owner().equals("1")) {
					groupMember.get(position).setSpace(!groupMember.get(position).isSpace());
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		this.et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (  null != groupMember ) {
					List<GroupMember> g_space = new ArrayList<>();
					
					for (int i = 0; i < groupMember.size(); i++) {
						if (groupMember.get(i).getUser_name().contains(s)) {
							g_space.add(groupMember.get(i));
						}
					}
					adapter = new AllGroupMembersAdapter(AllGroupMembersActivity.this, g_space);
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
	protected void onDestroy() {
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		setResult(1);
		super.onDestroy();
	}
	
	private class AllGroupMembersAdapter extends BaseAdapter {
		
		private Context context;
		private List<GroupMember> groupMember;
		private ViewHolder view;
		
		public AllGroupMembersAdapter(Context context, List<GroupMember> groupMember) {
			this.context = context;
			this.groupMember = groupMember;
		}

		@Override
		public int getCount() {
			return null == groupMember ? 0 : groupMember.size();
		}

		@Override
		public Object getItem(int position) {
			return groupMember.get(position);
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
			
			if (groupMember.get(position).isSpace()) {
				view.iv_select.setImageResource(R.drawable.icon_all_true);
			}else {
				view.iv_select.setImageResource(R.drawable.icon_all_false);
			}
			
			if (activity.equals("GAN") && ll_delete.getVisibility() == View.VISIBLE ) {
				System.out.println(groupMember.get(position).getIs_owner());
				if (groupMember.get(position).getIs_owner().equals("1")) {
					view.iv_select.setVisibility(View.VISIBLE);
					view.iv_select.setImageResource(R.drawable.icon_king);
				}else {
					view.iv_select.setVisibility(View.VISIBLE);
				}
			}else {
				view.iv_select.setVisibility(View.GONE);
			}
			
			Picasso.with(context).load(HttpConfig.IMAGEHOST + groupMember.get(position).getPhoto()).into(view.iv);
			
			view.tv.setText(groupMember.get(position).getUser_name());
			
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

	private void getData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "223");
		map.put("name", PreferencesUtils.getString(AllGroupMembersActivity.this, "user_account"));
		map.put("groupId", Groupid);
		new HttpClientGet(AllGroupMembersActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				
				System.out.println("群成员： "  + result);
				
				GroupMemberBean gBean = new Gson().fromJson(result, GroupMemberBean.class); 
				groupMember = new ArrayList<>();
				groupMember = gBean.getGroupMember();
				
				for (int i = 0; i < groupMember.size(); i++) {
					groupMember.get(i).setSpace(false);
				}
				
				adapter = new AllGroupMembersAdapter(AllGroupMembersActivity.this, groupMember);
				
				lv.setAdapter(adapter);
				
				adapter.notifyDataSetChanged();
				
				CustomDialogUtils.stopCustomProgressDialog(AllGroupMembersActivity.this);
			}
			
			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(AllGroupMembersActivity.this);
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
			
		case R.id.ll_delete:
			
			String str = "";
			
			if ( null != groupMember) {
				
				for (int i = 0; i < groupMember.size(); i++) {
					if (groupMember.get(i).isSpace()) {
						if (str.equals("")) {
							str = groupMember.get(i).getMember_name();
						}else {
							str += "," + groupMember.get(i).getMember_name();
						}
					}
				}
			}
			
			if (str.length() > 0) {
				CustomDialogUtils.startCustomProgressDialog(AllGroupMembersActivity.this, "请稍后...");
				Map<String, String> map = new HashMap<>();
				map.put("OPT", "224");
				map.put("groupId",Groupid);
				map.put("deleteMember", str);
				new HttpClientGet(AllGroupMembersActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

					@Override
					public void onSuccess(String result) {
						System.out.println("删除: "  + result);
						getData();
					}
					
					@Override
					public void onFailure(MyError myError) {
						CustomDialogUtils.stopCustomProgressDialog(AllGroupMembersActivity.this);
						super.onFailure(myError);
					}
				});
			}
			
			break;
			
		case R.id.tv_edit:
			String str1 = tv_edit.getText().toString();
			
			if (str1.equals("编辑")) {
				tv_edit.setText("取消");
				ll_delete.setVisibility(View.VISIBLE);
				ll_search.setVisibility(View.VISIBLE);
			}else {
				tv_edit.setText("编辑");
				ll_delete.setVisibility(View.GONE);
				ll_search.setVisibility(View.GONE);
			}
			
			adapter.notifyDataSetChanged();
			
			break;
			
		case R.id.ll_clear:
			
			this.et_search.setText("");
			adapter = new AllGroupMembersAdapter(AllGroupMembersActivity.this, groupMember);
			adapter.notifyDataSetChanged();
			this.imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
			break;
		}
	}
}
