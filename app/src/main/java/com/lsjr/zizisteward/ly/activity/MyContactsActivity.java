package com.lsjr.zizisteward.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CardContactsBean;
import com.lsjr.zizisteward.bean.CardContactsBean.Contacts;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**我的人脉*/
public class MyContactsActivity extends Activity implements OnClickListener {

	private MyListView mlv;
	private LinearLayout ll_back;
	private LinearLayout ll_editor;
	private TextView tv_delete;
	private MCAdapter adapter;
	private TextView tv;
	private boolean is_space = false;
	private List<Contacts> contacts = new ArrayList<>();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_contacts_activity);
		this.findViewById();
	}

	private void findViewById() {
		
		this.tv = (TextView) super.findViewById(R.id.tv);
		this.mlv = (MyListView) super.findViewById(R.id.mlv);
		this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
		this.tv_delete = (TextView) super.findViewById(R.id.tv_delete);
		this.ll_editor = (LinearLayout) super.findViewById(R.id.ll_editor);
		
		this.getData();
		
		this.ll_back.setOnClickListener(this);
		this.tv_delete.setOnClickListener(this);
		this.ll_editor.setOnClickListener(this);

	}
	
	private void getData() {
		CustomDialogUtils.startCustomProgressDialog(MyContactsActivity.this, "请稍候");
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "294");
		map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
		new HttpClientGet(MyContactsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(MyContactsActivity.this);
				CardContactsBean ccBean = new Gson().fromJson(result, CardContactsBean.class);
				contacts =  ccBean.getContacts();
				adapter = new MCAdapter(MyContactsActivity.this, contacts);
				mlv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(MyContactsActivity.this);
				super.onFailure(myError);
			}
		});
	}
	
	private class MCAdapter extends BaseAdapter {
		
		private Context context;
		private ViewHolder view;
		private List<Contacts> contacts;
		
		public MCAdapter(Context context, List<Contacts> contacts) {
			this.context = context;
			this.contacts = contacts;
		}

		@Override
		public int getCount() {
			return null == contacts ? 0 : contacts.size();
		}

		@Override
		public Object getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == convertView) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.my_contacts_activity_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			} else {
				
				view = (ViewHolder) convertView.getTag();
			}
			
			view.tv_name.setText(contacts.get(position).getUsername());
			view.tv_company_name.setText(null == contacts.get(position).getCompany_name() ? "" : contacts.get(position).getCompany_name());
			view.tv_position_name.setText(null == contacts.get(position).getPosition() ? "" : contacts.get(position).getPosition());
			
			if (is_space) {
				view.ll_check.setVisibility(View.VISIBLE);
			} else {
				view.ll_check.setVisibility(View.GONE);
			}
			
			if (contacts.get(position).isSpace()) {
				view.iv_check.setImageResource(R.drawable.icon_card_check_true);
			} else {
				view.iv_check.setImageResource(R.drawable.icon_card_check_false);
			}
			
			view.ll_check.setTag(position);
			view.ll_check.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					contacts.get(pos).setSpace(!contacts.get(pos).isSpace());
					adapter.notifyDataSetChanged();
				}
			});
			
			view.ll.setTag(position);
			view.ll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int pos = (int) v.getTag();
					
					if (!is_space) {
						startActivityForResult(new Intent(MyContactsActivity.this,
								CardHolderDetails.class).putExtra("id",
								contacts.get(pos).getTcid())
								.putExtra("activity", "mca"), 1);
					}
				}
			});
			
			return convertView;
		}
		
		private class ViewHolder {
			private LinearLayout ll;
			private TextView tv_name;
			private ImageView iv_check;
			private LinearLayout ll_check;
			private TextView tv_company_name;
			private TextView tv_position_name;
			
			public ViewHolder(View v) {
				this.ll = (LinearLayout) v.findViewById(R.id.ll);
				this.tv_name = (TextView) v.findViewById(R.id.tv_name);
				this.iv_check = (ImageView) v.findViewById(R.id.iv_check);
				this.ll_check = (LinearLayout) v.findViewById(R.id.ll_check);
				this.tv_company_name = (TextView) v.findViewById(R.id.tv_company_name);
				this.tv_position_name = (TextView) v.findViewById(R.id.tv_position_name);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.ll_back:
			finish();
			break;
			
		case R.id.ll_editor:
			
			String ly = tv.getText().toString();
			
			if (ly.equals("取消")) {
				is_space = false;
				
				for (int i = 0; i < contacts.size(); i++) {
					contacts.get(i).setSpace(false);
				}
				tv.setText("编辑");
				tv_delete.setVisibility(View.GONE);
			} else {
				tv.setText("取消");
				is_space = true;
				tv_delete.setVisibility(View.VISIBLE);
			}
			
			adapter.notifyDataSetChanged();
			
			break;
			
		case R.id.tv_delete:
			
			String id = "";
			
			for (int i = 0; i < contacts.size(); i++) {
				if (contacts.get(i).isSpace()) {
					
					if (id.equals("")) {
						id = contacts.get(i).getId();
					} else {
						id += "," + contacts.get(i).getId();
					}
				}
			}
			
			if (id.equals("")) {
				Toast.makeText(MyContactsActivity.this, "没有选择任何名片...", Toast.LENGTH_SHORT).show();
			} else {
				DeleteSaveCard(id);
			}
			
			break;
		}
	}
	
	private void DeleteSaveCard(String contactsid) {
		CustomDialogUtils.startCustomProgressDialog(MyContactsActivity.this, "请稍候");
		Map<String, String> map = new HashMap<>();
		map.put("OPT", "293");
		map.put("contactsid", contactsid);
		new HttpClientGet(MyContactsActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

			@Override
			public void onSuccess(String result) {
				CustomDialogUtils.stopCustomProgressDialog(MyContactsActivity.this);
				is_space = false;
				tv_delete.setVisibility(View.GONE);
				tv.setText("编辑");
				getData();
			}
			
			@Override
			public void onFailure(MyError myError) {
				CustomDialogUtils.stopCustomProgressDialog(MyContactsActivity.this);
				super.onFailure(myError);
			}
		});
	}
}

