package com.lsjr.zizisteward.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.common.activtiy.ReserveCateActivity;
import com.lsjr.zizisteward.common.activtiy.ReserveChooseActivity;
import com.lsjr.zizisteward.common.activtiy.ReserveHealthActivity;
import com.lsjr.zizisteward.common.activtiy.ReserveTravelActivity;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;

import java.util.HashMap;

public class MyReserveFragment extends Fragment {
    private View rootView;
    private ListView mSlv_reserve;
    private String[] names = {"出行", "美食", "精选活动", "健康"};
    private int[] images = {R.drawable.black_gray, R.drawable.food_gray, R.drawable.home_gray,
            R.drawable.jiangkang_gray};
    private MyCount count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_reserve, null);
            mSlv_reserve = (ListView) rootView.findViewById(R.id.slv_reserve);
            initView();
        }
        return rootView;
    }

    private void initView() {
        getData();
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "414");
        map.put("user_id", EncryptUtils.addSign(Integer.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("我的预定" + result);
                count = GsonUtil.getInstance().fromJson(result, MyCount.class);
                MyReserveAdapter adapter = new MyReserveAdapter(getContext(), names, count);
                mSlv_reserve.setAdapter(adapter);
                mSlv_reserve.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:// 出行
                                startActivity(new Intent(getContext(), ReserveTravelActivity.class));
                                break;
                            case 1:// 美食
                                startActivity(new Intent(getContext(), ReserveCateActivity.class));
                                break;
                            case 2:// 精选活动
                                startActivity(new Intent(getContext(), ReserveChooseActivity.class));
                                break;
                            case 3:// 健康
                                startActivity(new Intent(getContext(), ReserveHealthActivity.class));
                                break;
                        }
                    }
                });
            }
        });

    }

    public class MyReserveAdapter extends BaseAdapter {

        private ViewHolder mHolder;
        private Context context;
        private String[] names;
        private MyCount count;

        public MyReserveAdapter(Context context, String[] names, MyCount count) {
            this.context = context;
            this.count = count;
            this.names = names;
        }

        @Override
        public int getCount() {
            return names == null ? 0 : names.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_reserve_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            mHolder.mIv.setImageResource(images[position]);
            mHolder.mName.setText(names[position]);
            if (position == 0) {
                mHolder.mCount.setText(count.getUserBook().getTripCount() + " / " + count.getUserBook().getTripSum());
            }
            if (position == 1) {
                mHolder.mCount.setText(count.getUserBook().getCateCount() + " / " + count.getUserBook().getCateSum());
            }
            if (position == 2) {
                mHolder.mCount
                        .setText(count.getUserBook().getAtivityCount() + " / " + count.getUserBook().getAtivitySum());
            }
            if (position == 3) {
                mHolder.mCount
                        .setText(count.getUserBook().getHealthCount() + " / " + count.getUserBook().getHealthSum());
            }
            return convertView;
        }

    }

    public class ViewHolder {
        private ImageView mIv;
        private TextView mName;
        private TextView mCount;

        public ViewHolder(View view) {
            mIv = (ImageView) view.findViewById(R.id.iv);
            mName = (TextView) view.findViewById(R.id.name);
            mCount = (TextView) view.findViewById(R.id.count);
        }
    }

    public class MyCount {
        private UserBook UserBook;

        public UserBook getUserBook() {
            return UserBook;
        }

        public void setUserBook(UserBook userBook) {
            UserBook = userBook;
        }
    }

    public class UserBook {
        private String AtivityCount;
        private String AtivitySum;
        private String CateCount;
        private String CateSum;
        private String HealthCount;
        private String HealthSum;
        private String TripCount;
        private String TripSum;

        public String getAtivityCount() {
            return AtivityCount;
        }

        public void setAtivityCount(String ativityCount) {
            AtivityCount = ativityCount;
        }

        public String getAtivitySum() {
            return AtivitySum;
        }

        public void setAtivitySum(String ativitySum) {
            AtivitySum = ativitySum;
        }

        public String getCateCount() {
            return CateCount;
        }

        public void setCateCount(String cateCount) {
            CateCount = cateCount;
        }

        public String getCateSum() {
            return CateSum;
        }

        public void setCateSum(String cateSum) {
            CateSum = cateSum;
        }

        public String getHealthCount() {
            return HealthCount;
        }

        public void setHealthCount(String healthCount) {
            HealthCount = healthCount;
        }

        public String getHealthSum() {
            return HealthSum;
        }

        public void setHealthSum(String healthSum) {
            HealthSum = healthSum;
        }

        public String getTripCount() {
            return TripCount;
        }

        public void setTripCount(String tripCount) {
            TripCount = tripCount;
        }

        public String getTripSum() {
            return TripSum;
        }

        public void setTripSum(String tripSum) {
            TripSum = tripSum;
        }
    }

}
