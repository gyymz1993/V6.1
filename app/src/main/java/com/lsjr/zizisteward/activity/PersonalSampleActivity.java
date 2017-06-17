package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.SampleBean;
import com.lsjr.zizisteward.bean.SampleBean.SampleDetail;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.mybetterandroid.wheel.other.CustomListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class PersonalSampleActivity extends BaseActivity {
    private CustomListView mCus_listview;
    List<SampleDetail> list = new ArrayList<SampleDetail>();

    @Override
    public int getContainerView() {
        return R.layout.activity_personal_sample;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("服务方案");
        mCus_listview = (CustomListView) findViewById(R.id.cus_listview);
        mCus_listview.initSlideMode(CustomListView.MOD_LEFT);

        getData();
    }

    private int pageNum = 1;

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "179");
        map.put("currPage", String.valueOf(pageNum++));
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(PersonalSampleActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                SampleBean bean = GsonUtil.getInstance().fromJson(result, SampleBean.class);
                list = bean.getPage().getPage();
                SampleAdapter adapter = new SampleAdapter();
                mCus_listview.setAdapter(adapter);
                mCus_listview.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(PersonalSampleActivity.this, ServicesJARActivity.class);
                        intent.putExtra("push_link", list.get(position).getPush_link());

                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });

    }

    public class SampleAdapter extends BaseAdapter {

        private ViewHolder mHolder;

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(PersonalSampleActivity.this).inflate(R.layout.item_sample3, parent,
                        false);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mTitle.setText(list.get(position).getTitle());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(list.get(position).getPush_time().getTime());
            mHolder.mTime.setText(time);
            int lose_time = list.get(position).getLose_time();
            if (lose_time > 1440) {
                int days = lose_time / (24 * 60);
                int hours = (lose_time - (24 * 60 * days)) / 60;
                int minutes = lose_time - ((24 * 60 * days) + (hours * 60));
                mHolder.mResidue_time.setText("剩余" + days + "天" + hours + "小时" + minutes + "分");
            } else if (60 < lose_time && lose_time < 1440) {
                int hours = lose_time / 60;
                int minutes = lose_time - (hours * 60);
                mHolder.mResidue_time.setText("剩余" + hours + "小时" + minutes + "分");

            } else if (lose_time < 60 && lose_time > 0) {
                int minutes = lose_time;
                mHolder.mResidue_time.setText("剩余" + minutes + "分");
            }

            mHolder.mDelete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("OPT", "180");
                    map.put("aid", list.get(position).getAid());
                    new HttpClientGet(PersonalSampleActivity.this, null, map, false,
                            new HttpClientGet.CallBacks<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    BasicParameterBean bean = GsonUtil.getInstance().fromJson(result,
                                            BasicParameterBean.class);
                                    Toast.makeText(PersonalSampleActivity.this, bean.getMsg(), Toast.LENGTH_SHORT)
                                            .show();
                                    getData();
                                }

                                @Override
                                public void onFailure(MyError myError) {
                                    super.onFailure(myError);
                                }
                            });
                }
            });

            return convertView;
        }

    }

    public class ViewHolder {
        private TextView mTime;
        private TextView mTitle;
        private RelativeLayout mDelete;
        private TextView mResidue_time;

        public ViewHolder(View view) {
            mTime = (TextView) view.findViewById(R.id.time);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDelete = (RelativeLayout) view.findViewById(R.id.delete);
            mResidue_time = (TextView) view.findViewById(R.id.residue_time);
        }

    }

}
