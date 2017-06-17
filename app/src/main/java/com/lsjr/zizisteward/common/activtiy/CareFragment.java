package com.lsjr.zizisteward.common.activtiy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.PersonalShiJieStatisticsActivity;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;
import com.lsjr.zizisteward.utils.ToastUtils;
import com.zizisteward.view.refresh.SuperListView;
import com.zizisteward.view.refresh.SuperListView.OnLoadMoreListener;
import com.zizisteward.view.refresh.SuperListView.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CareFragment extends Fragment {
    private View rootView;
    private HorizontalScrollView hsl;
    private LinearLayout ll_parent;
    private String[] titles = {"全部", "资深摄影师", "美食专家", "豪车品鉴师", "游艇名家", "独立设计师", "飞行达人", "时装模特", "家居潮人", "旅途王者"};
    private TextView toolsTextviews[];
    private View views[];
    private LayoutInflater layoutInflater;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private SuperListView listview_care;
    private int pageNum = 1;
    private boolean isRefresh = true;
    private List<CareUsers> list = new ArrayList<CareUsers>();
    private CareAdapter adapter;
    private String tolerant_shefen = "";
    private TextView tv_no_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_care, null);
            layoutInflater = LayoutInflater.from(getContext());
            hsl = (HorizontalScrollView) rootView.findViewById(R.id.hsl);
            ll_parent = (LinearLayout) rootView.findViewById(R.id.ll_parent);
            listview_care = (SuperListView) rootView.findViewById(R.id.listview_care);
            tv_no_content = (TextView) rootView.findViewById(R.id.tv_no_content);
            initType();// 头部分类
            initView();// 初始化数据
        }
        return rootView;
    }

    private void initView() {
        adapter = new CareAdapter(getContext(), list);
        listview_care.setAdapter(adapter);
        listview_care.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                isRefresh = true;
                getData(tolerant_shefen);
            }
        });
        listview_care.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData(tolerant_shefen);
            }
        });
        listview_care.refresh();
    }

    private void getData(String shefen) {
        if (isRefresh) {
            pageNum = 1;
            adapter.removeAll();
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "32");
        map.put("user_id", EncryptUtils.addSign(Long.parseLong(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum++));
        map.put("identity_type", shefen);
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("关注列表" + result);
                CareListBean bean = GsonUtil.getInstance().fromJson(result, CareListBean.class);

                if (1 == pageNum) {
                    list = bean.getTusers();
                    adapter = new CareAdapter(getContext(), list);
                    listview_care.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    list.addAll(bean.getTusers());
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

                if (list.size() < bean.getTusers().size()) {
                    listview_care.setIsLoadFull(false);
                }

                listview_care.finishRefresh();
                listview_care.finishLoadMore();

            }
        });
    }

    public class CareAdapter extends BaseAdapter {
        private Context context;
        private List<CareUsers> list;
        private ViewHolder mHolder;

        public CareAdapter(Context context, List<CareUsers> list) {
            this.list = list;
            this.context = context;
        }

        public void setList(List<CareUsers> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void add(CareUsers page) {
            this.list.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(CareUsers page) {
            this.list.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<CareUsers> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list.clear();
            notifyDataSetChanged();
        }

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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_care_type_list, null);
                mHolder = new ViewHolder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Glide.with(context).load(HttpConfig.IMAGEHOST + list.get(position).getPhoto()).into(mHolder.user_photo);
            mHolder.tv_name.setText(list.get(position).getUser_name());
            mHolder.tv_type.setText(list.get(position).getIdentity_type());
            if ("0".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_zero);
            }
            if ("1".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_one);
            }
            if ("2".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_two);
            }
            if ("3".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_three);
            }
            if ("4".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_three);
            }
            if ("5".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_five);
            }
            if ("6".equals(list.get(position).getCredit_level_id())) {
                mHolder.iv_level.setImageResource(R.drawable.level_six);
            }
            mHolder.ll_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("OPT", "31");
                    map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                    map.put("to_user_id", list.get(position).getUserid());
                    new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                        @Override
                        public void onSuccess(String result) {
                            BasicParameterBean bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                            ToastUtils.show(getContext(), bean.getMsg());
                            isRefresh = true;
                            getData(tolerant_shefen);
                            adapter.notifyDataSetChanged();
                            PreferencesUtils.putBoolean(getContext(), "isChange", true);
                        }
                    });

                }
            });
            mHolder.user_photo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PersonalShiJieStatisticsActivity.class);
                    intent.putExtra("user_id", list.get(position).getUserid());
                    intent.putExtra("shijie_statistics", "mingrenbang");
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @SuppressWarnings("unused")
        private class ViewHolder {
            private RoundImageView user_photo;
            private ImageView iv_level;
            private TextView tv_name, tv_type;
            private RelativeLayout ll_cancel;

            public ViewHolder(View view) {
                user_photo = (RoundImageView) view.findViewById(R.id.user_photo);
                iv_level = (ImageView) view.findViewById(R.id.iv_level);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_type = (TextView) view.findViewById(R.id.tv_type);
                ll_cancel = (RelativeLayout) view.findViewById(R.id.ll_cancel);
            }
        }

    }

    private void initType() {
        toolsTextviews = new TextView[titles.length];
        views = new View[titles.length];
        for (int i = 0; i < titles.length; i++) {
            View view = layoutInflater.inflate(R.layout.item_care_type, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.tv_name);
            textView.setText(titles[i]);
            ll_parent.addView(view);
            toolsTextviews[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);

    }

    private OnClickListener toolsItemListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == 0 && TextUtils.isEmpty(tolerant_shefen)) {

                tolerant_shefen = "";

            } else if (tolerant_shefen.equals(titles[v.getId()])) {

                tolerant_shefen = titles[v.getId()];

            } else {

                System.out.println("什么鬼" + v.getId());
                isRefresh = true;
                changeTextColor(v.getId());
                changeTextLocation(v.getId());
                if (0 == v.getId()) {
                    getData("");
                    tolerant_shefen = "";
                } else {
                    getData(titles[v.getId()]);
                    tolerant_shefen = titles[v.getId()];
                }

            }
        }
    };

    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextviews.length; i++) {
            if (i != id) {
                toolsTextviews[i].setBackgroundResource(R.drawable.type_back);
                toolsTextviews[i].setTextColor(0xff000000);
            }
        }
        toolsTextviews[id].setTextColor(0xffFF9900);
        toolsTextviews[id].setBackgroundResource(R.drawable.care_selected);
    }

    /**
     * 改变栏目位置
     */
    @SuppressWarnings("unused")
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getLeft() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        hsl.smoothScrollTo(x, 0);
    }

    /**
     * 返回scrollview的中间位置
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = hsl.getRight() - hsl.getLeft();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     */
    private int getViewheight(View view) {
        return view.getRight() - view.getLeft();
    }

    private class CareListBean {
        private List<CareUsers> tusers;

        public List<CareUsers> getTusers() {
            return tusers;
        }

        public void setTusers(List<CareUsers> tusers) {
            this.tusers = tusers;
        }

    }

    private class CareUsers {
        private String credit_level_id;
        private String identity_type;
        private String photo;
        private String user_name;
        private String userid;

        public String getCredit_level_id() {
            return credit_level_id;
        }

        public void setCredit_level_id(String credit_level_id) {
            this.credit_level_id = credit_level_id;
        }

        public String getIdentity_type() {
            return identity_type;
        }

        public void setIdentity_type(String identity_type) {
            this.identity_type = identity_type;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}
