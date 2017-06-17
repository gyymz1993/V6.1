package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.HomeBrandDetail;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.MyPageShopBean;
import com.lsjr.zizisteward.bean.MyPageShopBean.pageShop;
import com.lsjr.zizisteward.bean.MyPageShopBean.pageShop.page;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.PullToRefreshLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 */
public class MyCollection extends Activity implements OnClickListener {

    private TextView tv;
    private MyGridView mgv;
    private TextView tv_cancel;
    private LinearLayout ll_more;
    private LinearLayout ll_back;
    private LinearLayout ll_none;
    private LinearLayout ll_cancel;
    private LinearLayout ll_editor;
    private PullToRefreshLayout ptrl;

    private int width;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean ly = false;
    private MyCollectionAdapter adapter;
    private List<page> page = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.my_collection_activity);

        this.tv = (TextView) super.findViewById(R.id.tv);
        this.mgv = (MyGridView) super.findViewById(R.id.mgv);
        this.ll_more = (LinearLayout) super.findViewById(R.id.ll_more);
        this.tv_cancel = (TextView) super.findViewById(R.id.tv_cancel);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_none = (LinearLayout) super.findViewById(R.id.ll_none);
        this.ll_cancel = (LinearLayout) super.findViewById(R.id.ll_cancel);
        this.ll_editor = (LinearLayout) super.findViewById(R.id.ll_editor);
        this.ptrl = (PullToRefreshLayout) super.findViewById(R.id.refresh_view);

        this.ll_back.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
        this.ll_cancel.setOnClickListener(this);
        this.ll_editor.setOnClickListener(this);

        this.getMyCollection(0);

        this.ptrl.setOnRefreshListener((PullToRefreshLayout.OnRefreshListener) new MyListener());

        DisplayMetrics dm = new DisplayMetrics();
        MyCollection.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        this.ptrl.setVisibility(View.VISIBLE);

        this.mgv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (ly) {

                    if (!page.get(position).isSpace()) {
                        tv.setText("反选");
                    }

                    page.get(position).setSpace(!page.get(position).isSpace());

                    adapter.notifyDataSetChanged();

                } else {

                    Intent intent = new Intent(MyCollection.this, HomeBrandDetail.class);
                    intent.putExtra("sid", page.get(position).getId());
                    startActivity(intent);

                }
            }
        });
    }

    private class MyListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pageNum = 1;
            getMyCollection(1);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pageNum++;
            getMyCollection(2);
        }
    }

    /**
     * 获得我的收藏列表集合
     */
    private void getMyCollection(final int space) {

        Map<String, String> map = new HashMap<>();
        map.put("OPT", "329");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("currPage", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));

        new HttpClientGet(MyCollection.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {

                MyPageShopBean mpsBean = new Gson().fromJson(result, MyPageShopBean.class);
                pageShop psBean = mpsBean.getPageShop();

                switch (space) {
                    case 0:
                        //第一次加载

                        page = new ArrayList<>();
                        page = psBean.getPage();

                        if (null != page) {
                            ll_none.setVisibility(View.GONE);
                            ptrl.setVisibility(View.VISIBLE);
                        } else {
                            ll_none.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        }

                        adapter = new MyCollectionAdapter(MyCollection.this, page);

                        mgv.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        if (null != page && page.size() >= pageSize) {
                            ll_more.setVisibility(View.VISIBLE);
                        } else {
                            ll_more.setVisibility(View.GONE);
                        }

                        break;

                    case 1:
                        //刷新

                        page = new ArrayList<>();
                        page = psBean.getPage();

                        if (null != page) {
                            ll_none.setVisibility(View.GONE);
                            ptrl.setVisibility(View.VISIBLE);
                        } else {
                            ll_none.setVisibility(View.VISIBLE);
                            ptrl.setVisibility(View.GONE);
                        }

                        adapter = new MyCollectionAdapter(MyCollection.this, page);

                        mgv.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        if (null != page && page.size() >= pageSize) {
                            ll_more.setVisibility(View.VISIBLE);
                        } else {
                            ll_more.setVisibility(View.GONE);
                        }

                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                        break;

                    case 2:
                        //加载更多

                        List<page> _page = new ArrayList<>();

                        _page = psBean.getPage();

                        if (null != _page) {
                            page.addAll(_page);

                            if (null != page) {
                                ll_none.setVisibility(View.GONE);
                                ptrl.setVisibility(View.VISIBLE);
                            } else {
                                ll_none.setVisibility(View.VISIBLE);
                                ptrl.setVisibility(View.GONE);
                            }

                            adapter = new MyCollectionAdapter(MyCollection.this, page);

                            mgv.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            if (null != _page && _page.size() >= pageSize) {
                                ll_more.setVisibility(View.VISIBLE);
                            } else {
                                ll_more.setVisibility(View.GONE);
                            }
                        }

                        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                        break;
                }
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    private class MyCollectionAdapter extends BaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<page> page;

        public MyCollectionAdapter(Context context, List<page> page) {
            this.context = context;
            this.page = page;
        }

        @Override
        public int getCount() {
            return null == page ? 0 : page.size();
        }

        @Override
        public Object getItem(int position) {
            return page.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = LayoutInflater.from(context).inflate(R.layout.my_collection_activity_item, null);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.ll.getLayoutParams();
            lp.width = width / 2;
            lp.height = width / 2 + 50;
            view.ll.setLayoutParams(lp);

            if (ly) {
                view.iv_check.setVisibility(View.VISIBLE);
            } else {
                view.iv_check.setVisibility(View.GONE);
            }

            if (page.get(position).isSpace()) {
                view.iv_check.setImageResource(R.drawable.icon_collection_true);
            } else {
                view.iv_check.setImageResource(R.drawable.icon_collection_false);
            }

            Picasso.with(context).load(HttpConfig.IMAGEHOST + page.get(position).getSpic()).into(view.iv);
            view.tv_name.setText(page.get(position).getSname());
            view.tv_price.setText("¥" + page.get(position).getSprice());

            return convertView;
        }

        private class ViewHolder {

            //选中框
            private ImageView iv_check;
            //商品图片
            private ImageView iv;
            //商品名字
            private TextView tv_name;
            //商品价格
            private TextView tv_price;

            private LinearLayout ll;
            private RelativeLayout rl_parent;

            public ViewHolder(View v) {

                this.iv = (ImageView) v.findViewById(R.id.iv);
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.tv_price = (TextView) v.findViewById(R.id.tv_price);
                this.iv_check = (ImageView) v.findViewById(R.id.iv_check);
                this.ll = (LinearLayout) v.findViewById(R.id.ll);
                this.rl_parent = (RelativeLayout) v.findViewById(R.id.rl_parent);
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

                System.out.println("点击了");

                ll_back.setVisibility(View.GONE);
                ll_cancel.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);

                if (tv.getText().toString().equals("管理")) {
                    tv.setText("全选");
                    tv_cancel.setVisibility(View.VISIBLE);
                } else if (tv.getText().toString().equals("反选")) {
                    for (int i = 0; i < page.size(); i++) {
                        page.get(i).setSpace(!page.get(i).isSpace());
                    }
                    adapter.notifyDataSetChanged();
                    tv.setText("全选");
                } else if (tv.getText().toString().equals("全选")) {
                    for (int i = 0; i < page.size(); i++) {
                        page.get(i).setSpace(true);
                    }
                    adapter.notifyDataSetChanged();
                    tv.setText("反选");
                }

                ly = true;
                adapter.notifyDataSetChanged();

                break;

            case R.id.ll_cancel:

                ll_cancel.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                ll_back.setVisibility(View.VISIBLE);
                tv.setText("管理");

                for (int i = 0; i < page.size(); i++) {
                    page.get(i).setSpace(false);
                }

                ly = false;
                adapter.notifyDataSetChanged();

                break;

            case R.id.tv_cancel:
                String _id = "";

                for (int i = 0; i < page.size(); i++) {
                    if (page.get(i).isSpace()) {
                        if (_id.equals("")) {
                            _id = page.get(i).getId();
                        } else {
                            _id += "," + page.get(i).getId();
                        }
                    }
                }

                if (!_id.equals("")) {
                    Cancel(_id);
                } else {
                    Toast.makeText(MyCollection.this, "您没有选中任何收藏的商品 ", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void Cancel(String _id) {
        CustomDialogUtils.startCustomProgressDialog(MyCollection.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "331");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("collect_id", _id);
        map.put("collect_type", "2");
        new HttpClientGet(MyCollection.this, null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(MyCollection.this);
                ll_cancel.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                ll_back.setVisibility(View.VISIBLE);
                tv.setText("管理");

                ly = false;
                pageNum = 1;
                getMyCollection(0);
            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(MyCollection.this);
                super.onFailure(myError);
            }
        });
    }
}
