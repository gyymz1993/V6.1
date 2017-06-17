package com.lsjr.zizisteward.ly.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CateLabelBean.Cate_Label;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.unionpay.mobile.android.widgets.ad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class NotExpertReviewActivity extends Activity implements OnClickListener {

	private View view;
	private GridView gv;
	private TextView tv_sure;
	private NERAdapter adapter;
	private String id;
	private List<Cate_Label> cate_label;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.not_expert_review_activity);
		this.findViewById();
	}

	private void findViewById() {
		
		this.view = super.findViewById(R.id.view);
		this.gv = (GridView) super.findViewById(R.id.gv);
		this.tv_sure = (TextView) super.findViewById(R.id.tv_sure);
		
		this.view.setOnClickListener(this);
		this.tv_sure.setOnClickListener(this);
		
		cate_label = (List<Cate_Label>) getIntent().getSerializableExtra("label");
		
		this.id = getIntent().getStringExtra("id");
		
		//String[] name = {"味道赞","性价比高","环境优雅","服务员靓","美女多","帅哥多","富二代多","记者多","优惠多","打折多","回头客","阿西吧"};
		adapter = new NERAdapter(NotExpertReviewActivity.this, cate_label);
		gv.setAdapter(adapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cate_label.get(position).setSpace(!cate_label.get(position).isSpace());
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private class NERAdapter extends BaseAdapter {

		private Context context;
		private List<Cate_Label> cate_label;
		private ViewHolder view;
		
		public NERAdapter(Context context,List<Cate_Label> cate_label) {
			this.context = context;
			this.cate_label = cate_label; 
		}
		
		@Override
		public int getCount() {
			return null == cate_label ? 0 : cate_label.size();
		}

		@Override
		public Object getItem(int position) {
			return cate_label.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (null == convertView) {
				
				convertView = LayoutInflater.from(context).inflate(R.layout.not_expert_review_activity_item, null);
				
				view = new ViewHolder(convertView);
				
				convertView.setTag(view);
				
			} else {
				
				view = (ViewHolder) convertView.getTag();
				
			}
			
			view.tv.setText(cate_label.get(position).getLabelName());
			
			if (cate_label.get(position).isSpace()) {
				view.tv.setTextColor(Color.parseColor("#AE9171"));
				view.tv.setBackgroundResource(R.drawable.icon_oval_frame_true);
			} else {
				view.tv.setTextColor(Color.parseColor("#888888"));
				view.tv.setBackgroundResource(R.drawable.icon_oval_frame_false);
			}
			
			return convertView;
		}
		
		private class ViewHolder {
			private TextView tv;
			
			public ViewHolder(View v) {
				this.tv = (TextView) v.findViewById(R.id.tv);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view:
			finish();
			break;
			
		case R.id.tv_sure:
			String space = "";
			
				for (int i = 0; i < cate_label.size(); i++) {
					
					if (cate_label.get(i).isSpace()) {
						
						if (space.equals("")) {
							space = cate_label.get(i).getId();
						} else {
							space += "," + cate_label.get(i).getId();
						}
					}
				}
				
				if (space.length() < 1) {
					Toast.makeText(NotExpertReviewActivity.this, "请填写用餐内容...", Toast.LENGTH_SHORT).show();
				} else {
					Map<String, String> map = new HashMap<>();
					map.put("OPT", "297");
					map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
					map.put("shop_id", id);
					map.put("label_id", space);
					map.put("cate_comment", "");
					new HttpClientGet(NotExpertReviewActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

						@Override
						public void onSuccess(String result) {
							System.out.println(result);
							try {
								JSONObject jObject = new JSONObject(result);
								String error = jObject.getString("error");
								
								if (error.equals("1")) {
									Toast.makeText(NotExpertReviewActivity.this, "发布成功...", Toast.LENGTH_SHORT).show();
									finish();
								} else if(error.equals("2")) {
									Toast.makeText(NotExpertReviewActivity.this, "您已经评论过...", Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(NotExpertReviewActivity.this, "发布失败,请重试...", Toast.LENGTH_SHORT).show();
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void onFailure(MyError myError) {
							super.onFailure(myError);
						}
					});
				}
				
			break;
		}
	}
}
