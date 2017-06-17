package com.lsjr.zizisteward.ly.activity;

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

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检
 */
public class MedicalActivity extends Activity implements OnClickListener {

    private com.lsjr.zizisteward.newview.MyListView mlv;
    private LinearLayout ll_back;
    private MedicalAdapter adapter;
    private PullToRefreshLayout ptrl;
    private LinearLayout ll_look_over;
    private List<MedicalBean.Medicalcombination> medicalcombination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.medical_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.mlv = (com.lsjr.zizisteward.newview.MyListView) super.findViewById(R.id.mlv);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);
        this.ll_look_over = (LinearLayout) super.findViewById(R.id.ll_look_over);

        this.ll_back.setOnClickListener(this);
        this.ll_look_over.setOnClickListener(this);

        this.ptrl.setOnRefreshListener(new MyListener());

        this.mlv.setDividerHeight(0);

        this.getData();
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "302");
        new HttpClientGet(this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                MedicalBean mBean = new Gson().fromJson(result, MedicalBean.class);
                medicalcombination = mBean.getMedicalcombination();

                adapter = new MedicalAdapter(MedicalActivity.this, medicalcombination);

                mlv.setAdapter(adapter);
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(MedicalActivity.this, PhysicaDetailsActivity.class).putExtra("id", medicalcombination.get(position).getId()));
            }
        });
    }

    private class MedicalAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<MedicalBean.Medicalcombination> medicalcombination;

        public MedicalAdapter(Context context, List<MedicalBean.Medicalcombination> medicalcombination) {
            this.context = context;
            this.medicalcombination = medicalcombination;
        }

        @Override
        public int getCount() {
            return null == medicalcombination ? 0 : medicalcombination.size();
        }

        @Override
        public Object getItem(int position) {
            return medicalcombination.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.medical_activity_item, null);

                this.view = new ViewHolder(convertView);

                convertView.setTag(this.view);

            } else {

                this.view = (ViewHolder) convertView.getTag();

            }

            this.view.tv_price.setText("¥ " + medicalcombination.get(position).getSprice());
            this.view.tv_company_name.setText(medicalcombination.get(position).getBname());
            this.view.tv_service_name.setText(medicalcombination.get(position).getSname());
            this.view.tv_title.setText(medicalcombination.get(position).getTheme_name());
//			this.view.tv_original_price.setText("¥ " + Medicalcombination.get(position).getCost_price());
//			
//			this.view.tv_original_price.getPaint().setAntiAlias(true);//抗锯齿
//			this.view.tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰

            Picasso.with(context).load(HttpConfig.IMAGEHOST + medicalcombination.get(position).getSpicfirst()).into(view.iv);

            this.view.ll_recommended.setTag(position);
            this.view.ll_recommended.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    startActivity(new Intent(MedicalActivity.this,
                            PhysicalExaminationMoreActivity.class)
                            .putExtra("title", medicalcombination.get(pos).getTheme_name()));
                }
            });

            return convertView;
        }

        private class ViewHolder {
            /**推荐*/
            /**
             * 推荐分类
             */
            private TextView tv_title;
            private LinearLayout ll_recommended;
            /**
             * 图片
             */
            private ImageView iv;
            /**
             * 服务名称
             */
            private TextView tv_service_name;
            /**
             * 公司名字
             */
            private TextView tv_company_name;
            /**
             * 原价
             */
//			private TextView tv_original_price;
            private LinearLayout ll_service;
            private TextView tv_price;

            public ViewHolder(View v) {
                this.iv = (ImageView) v.findViewById(R.id.iv);
                this.tv_title = (TextView) v.findViewById(R.id.tv_title);
                this.tv_price = (TextView) v.findViewById(R.id.tv_price);
                this.ll_service = (LinearLayout) v.findViewById(R.id.ll_service);
                this.tv_service_name = (TextView) v.findViewById(R.id.tv_service_name);
                this.tv_company_name = (TextView) v.findViewById(R.id.tv_company_name);
                this.ll_recommended = (LinearLayout) v.findViewById(R.id.ll_recommended);
//				this.tv_original_price = (TextView) v.findViewById(R.id.tv_original_price);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_look_over:
                if (PreferencesUtils.getBoolean(MedicalActivity.this, "isLogin") == true) {
                    startActivity(new Intent(MedicalActivity.this, MedicalRecordActivity.class));
                } else {
                    Toast.makeText(MedicalActivity.this, "请先登录...", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
