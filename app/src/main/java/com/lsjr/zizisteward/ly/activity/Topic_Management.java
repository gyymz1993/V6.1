package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.GroupListBean;
import com.lsjr.zizisteward.bean.GroupListBean.GroupList;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**创建的群管理*/
public class Topic_Management extends Activity implements OnClickListener {
	
	/**返回*/
	private LinearLayout ll_back;
	/**标题*/
	private TextView tv_title;
	/**控制*/
	private TextView tv_control;
	private LinearLayout ll_control;
	/**解散话题*/
	private LinearLayout ll_delete;
	/**禁止互加好友*/
	private TextView tv_ban;
	/**展示*/
	private MyGridView mgv;
	private RelativeLayout rl_delete;
	private TextView tv_delete;
	private TextView tv_cancel;
	private Topic_Management_Adapter adapter;
	private List<GroupList> list_create = new ArrayList<>();

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int space = 0;
			for (int i = 0; i < list_create.size(); i++) {
				if (list_create.get(i).getGroup().isSpace())
					space += 1;
			}

			if (space > 0) {
				tv_title.setVisibility(View.VISIBLE);
				tv_title.setText("已选择" + String.valueOf(space) + "话题");
			} else {
				tv_title.setVisibility(View.GONE);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.topic_management_activity);

		this.mgv = (MyGridView) super.findViewById(R.id.mgv);
		this.tv_ban = (TextView) super.findViewById(R.id.tv_ban);
		this.tv_title = (TextView) super.findViewById(R.id.tv_title);
		this.tv_delete = (TextView) super.findViewById(R.id.tv_delete);
		this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.tv_control = (TextView) super.findViewById(R.id.tv_control);
		this.ll_delete = (LinearLayout) super.findViewById(R.id.ll_delete);
		this.rl_delete = (RelativeLayout) super.findViewById(R.id.rl_delete);
		this.ll_control = (LinearLayout) super.findViewById(R.id.ll_control);

		this.mgv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (list_create.get(position).getGroup().getIs_add_friend().equals("1")) {
					SetGroupAFState(list_create.get(position).getGroup().getId(),"0",position);
				} else {
					list_create.get(position).getGroup().setSpace(!list_create.get(position).getGroup().isSpace());
					tv_ban.setVisibility(View.VISIBLE);
					ll_control.setVisibility(View.VISIBLE);

					adapter.notifyDataSetChanged();

					handler.sendEmptyMessage(0);
				}
			}
		});

		this.tv_ban.setOnClickListener(this);
		this.ll_back.setOnClickListener(this);
		this.tv_delete.setOnClickListener(this);
		this.tv_cancel.setOnClickListener(this);
		this.ll_delete.setOnClickListener(this);
		this.ll_control.setOnClickListener(this);

		this.CreateData();
	}

	private void CreateData() {
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "409");
		map.put("name", PreferencesUtils.getString(Topic_Management.this, "user_account"));
		new HttpClientGet(Topic_Management.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				System.out.println("我创建的群：" + result);
				GroupListBean gBean = new Gson().fromJson(result,GroupListBean.class);
				list_create = new ArrayList<>();
				list_create = gBean.getGrouplist();

				adapter = new Topic_Management_Adapter(Topic_Management.this, list_create);

				mgv.setAdapter(adapter);

				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.ll_back:
			
			finish();
			
			break;

		case R.id.ll_control:
			
			if (null != list_create) {
				
				for (int i = 0; i < list_create.size(); i++) {
					list_create.get(i).getGroup().setSpace(false);
				}
				
				adapter.notifyDataSetChanged();
			}

			tv_title.setVisibility(View.GONE);
			rl_delete.setVisibility(View.GONE);
			ll_control.setVisibility(View.GONE);
			tv_ban.setVisibility(View.INVISIBLE);

			break;
			
		case R.id.tv_ban:

			String _id = "";

			for (int i = 0; i < list_create.size(); i++) {
				if (list_create.get(i).getGroup().isSpace()) {

					if (_id.equals("")) {
						_id = list_create.get(i).getGroup().getId();
					} else {
						_id += "," + list_create.get(i).getGroup().getId();
					}
				}
				list_create.get(i).getGroup().setSpace(false);
			}
			
			adapter.notifyDataSetChanged();
			
			tv_ban.setVisibility(View.INVISIBLE);
			tv_title.setVisibility(View.GONE);
			ll_control.setVisibility(View.GONE);

			if (_id.length() > 0) {
				SetGroupAddFriendState(_id,"1");
			}
			
			break;

		case R.id.ll_delete:

			boolean space = false;
			for (int i = 0; i < list_create.size(); i++) {
				if (list_create.get(i).getGroup().isSpace()) {
					space = true;
				}
			}

			if (space) {
				rl_delete.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(Topic_Management.this,"您还未选择任何话题",Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.tv_cancel:

			rl_delete.setVisibility(View.GONE);
			for (int i = 0; i < list_create.size(); i++) {
				list_create.get(i).getGroup().setSpace(false);
			}

			adapter.notifyDataSetChanged();

			break;

		case R.id.tv_delete:

			String id = "";

			for (int i = 0; i < list_create.size(); i++) {
				if (list_create.get(i).getGroup().isSpace()) {

					if (id.equals("")) {
						id = list_create.get(i).getGroup().getGroupId();
					} else {
						id += "," + list_create.get(i).getGroup().getGroupId();
					}
				}
				list_create.get(i).getGroup().setSpace(false);
			}

			adapter.notifyDataSetChanged();

			tv_ban.setVisibility(View.INVISIBLE);
			tv_title.setVisibility(View.GONE);
			ll_control.setVisibility(View.GONE);

			if (id.length() > 0) {
				rl_delete.setVisibility(View.GONE);
				DeleteGroup(id);
			}

			break;

		}
	}

	/***/
	private void DeleteGroup(String id ) {
		CustomDialogUtils.startCustomProgressDialog(Topic_Management.this,"请稍候");
		Map<String,String> map = new HashMap<>();
		map.put("OPT","229");
		map.put("groupId",id);
		new HttpClientGet(Topic_Management.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);
				CreateData();
				New_Topic_Old_Style.CreateData();
				System.out.print(result);
			}

			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);
				super.onFailure(myError);
			}
		});
	}

	private void SetGroupAddFriendState(String _id ,final String state) {
		CustomDialogUtils.startCustomProgressDialog(Topic_Management.this,"请稍候");
		Map<String,String> map = new HashMap<>();
		map.put("OPT","434");
		map.put("groupIds",_id);
		map.put("state", state);
		new HttpClientGet(Topic_Management.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);

				CreateData();
				System.out.print(result);
			}

			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);
				super.onFailure(myError);
			}
		});
	}

	private void SetGroupAFState(String _id ,final String state,final int pos) {
		CustomDialogUtils.startCustomProgressDialog(Topic_Management.this,"请稍候");
		Map<String,String> map = new HashMap<>();
		map.put("OPT","434");
		map.put("groupIds",_id);
		map.put("state", state);
		new HttpClientGet(Topic_Management.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);

				list_create.get(pos).getGroup().setIs_add_friend("0");
				adapter = new Topic_Management_Adapter(Topic_Management.this,list_create);
				mgv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
//				CreateData();
				System.out.print(result);
			}

			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(Topic_Management.this);
				super.onFailure(myError);
			}
		});
	}
}