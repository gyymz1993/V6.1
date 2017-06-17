package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.GoodsBean;
import com.lsjr.zizisteward.bean.GoodsBean.ContentData.Delivery;
import com.lsjr.zizisteward.bean.PaidBean.PaidDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationActivity extends Activity implements OnClickListener {

    private LinearLayout ll_parent;
    private ImageView iv_switch;
    private ListView lv_evaluation;
    private TextView tv_num;
    private SeekBar sb_service;
    private String gnum;
    private PaidDetail pd;
    private List<Delivery> list = new ArrayList<>();
    private EvaluationAdapte adapter;
    private LinearLayout mLl_back;
    private EditText mEt_input;
    private CharSequence temp;
    private TextView mTv_count;
    private RelativeLayout mRe_finish;
    private String mService_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.evaluation_activity);
        this.findViewById();
    }

    private void findViewById() {

        gnum = getIntent().getStringExtra("gnum");

        this.ll_parent = (LinearLayout) super.findViewById(R.id.ll_parent);
        this.iv_switch = (ImageView) super.findViewById(R.id.iv_switch);
        this.lv_evaluation = (ListView) super.findViewById(R.id.lv_evaluation);
        this.tv_num = (TextView) super.findViewById(R.id.tv_num);
        this.sb_service = (SeekBar) super.findViewById(R.id.sb_service);
        this.mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        mEt_input = (EditText) findViewById(R.id.et_input);
        mTv_count = (TextView) findViewById(R.id.tv_count);
        mRe_finish = (RelativeLayout) findViewById(R.id.re_finish);

        this.mLl_back.setOnClickListener(this);
        this.ll_parent.setOnClickListener(this);
        this.mRe_finish.setOnClickListener(this);

        getGoodsList();
        mEt_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTv_count.setText(temp.length() + "/240字");
            }
        });

        this.sb_service.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_num.setText(seekBar.getProgress() + "分");
                mService_point = tv_num.getText().toString().trim();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    private GoodsBean mBean;

    private void getGoodsList() {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "40");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("gnum", gnum);

        new HttpClientGet(EvaluationActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("这是什么啊" + result);
                mBean = GsonUtil.getInstance().fromJson(result, GoodsBean.class);
                list = mBean.getContentData().getDelivery();

                adapter = new EvaluationAdapte(EvaluationActivity.this, list);
                lv_evaluation.setAdapter(adapter);
            }
        });
    }

    private String mKid;
    private String mGoods_pot;

    private class EvaluationAdapte extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Delivery> list;

        public EvaluationAdapte(Context context, List<Delivery> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return null == list ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.evaluation_activity_item, null);
                view = new ViewHolder(convertView);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            view.tv_name.setText(list.get(position).getDsname());
            view.sb.setTag(position);
            view.sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int pos = (int) seekBar.getTag();
                    view.tv_num2.setText(seekBar.getProgress() + "分");
                    mGoods_pot = view.tv_num2.getText().toString().trim();
                    // adapter.notifyDataSetChanged();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }
            });
            mKid = list.get(position).getKid();
            return convertView;
        }

    }

    private class ViewHolder {
        private TextView tv_name;
        private SeekBar sb;
        private TextView tv_num2;

        public ViewHolder(View v) {
            sb = (SeekBar) v.findViewById(R.id.sb);
            tv_num2 = (TextView) v.findViewById(R.id.tv_num);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_parent:

                if (lv_evaluation.getVisibility() == View.VISIBLE) {

                    lv_evaluation.setVisibility(View.GONE);
                    iv_switch.setImageResource(R.drawable.xiangxia);

                } else {
                    lv_evaluation.setVisibility(View.VISIBLE);
                    iv_switch.setImageResource(R.drawable.xiangshang);
                }

                break;

            case R.id.ll_back:
                finish();
                break;
            case R.id.re_finish:// 完成
                if (TextUtils.isEmpty(mService_point)) {
                    Toast.makeText(EvaluationActivity.this, "您还没有对服务评价", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mEt_input.getText().toString().trim())) {
                    Toast.makeText(EvaluationActivity.this, "请您说点什么", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mGoods_pot)) {
                    Toast.makeText(EvaluationActivity.this, "您还没对具体商品打分", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("OPT", "45");
                map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                map.put("gnum", gnum);
                map.put("commodity_evaluate_list", mKid + "," + mGoods_pot.substring(0, 1) + ";");
                map.put("service_evaluate", App.getUserInfo().getGmid() + "," + mService_point.substring(0, 1));
                map.put("steward_content", mEt_input.getText().toString().trim());
                map.put("dev_id", list.get(0).getKid());

                new HttpClientGet(EvaluationActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        System.out.println("评价结果" + result);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                        Toast.makeText(EvaluationActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        EvaluationActivity.this.setResult(1, intent);
                        EvaluationActivity.this.finish();
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        super.onFailure(myError);
                    }
                });

                break;
        }
    }

}
