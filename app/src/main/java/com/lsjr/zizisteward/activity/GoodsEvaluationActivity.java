package com.lsjr.zizisteward.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.ShareTime;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.RoundImageView;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.zizisteward.view.refresh.SuperListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */

public class GoodsEvaluationActivity extends BaseActivity {
    String shopid;
    HashMap<String, String> map;
    SuperListView listview_supermans;
    TextView commit, name;
    View item_labels;
    GridView gridView_labels;
    List<Cate_label> list_labels = new ArrayList<Cate_label>();
    LabelAdapter adapter_label;
    List<Evaluation> list_supermans = new ArrayList<Evaluation>();
    SimpleDateFormat formatter;
    private boolean isRefresh = true;
    private int pageNum = 1;
    SupermansAdapter adapter_super;
    View view_left, view_right;
    int name_width;

    @Override
    public int getContainerView() {
        return R.layout.activity_goods_evaluation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopid = getIntent().getStringExtra("shopid");
        setmTitle("商品评论");
        listview_supermans = (SuperListView) findViewById(R.id.listview_supermans);
        commit = (TextView) findViewById(R.id.commit);
        gridView_labels = (GridView) findViewById(R.id.gridView_labels);
        view_left = findViewById(R.id.view_left);
        view_right = findViewById(R.id.view_right);
        name = (TextView) findViewById(R.id.name);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widthPixels = dm.widthPixels;
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) view_left.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) view_right.getLayoutParams();
        ViewTreeObserver vto = name.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                name.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                name_width = name.getWidth();
            }
        });
        params1.width = widthPixels / 2 - 100;
        params2.width = widthPixels / 2 - 100;
        System.out.print("距离" + ((widthPixels - name_width) / 2 - 10));
        view_left.setLayoutParams(params1);
        view_right.setLayoutParams(params2);

        formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        getLabelData();
        initLayout();
    }

    private void initLayout() {
        adapter_super = new SupermansAdapter(GoodsEvaluationActivity.this, list_supermans);
        listview_supermans.setAdapter(adapter_super);
        listview_supermans.setOnRefreshListener(new SuperListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });
        listview_supermans.setOnLoadMoreListener(new SuperListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData();
            }
        });
        listview_supermans.refresh();
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(GoodsEvaluationActivity.this, SendEvalActivity.class);
                inten.putExtra("id", shopid);
                inten.putExtra("type", "1");
                startActivity(inten);
            }
        });
    }

    /*标签*/
    private void getLabelData() {
        map = new HashMap<String, String>();
        map.put("OPT", "333");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("shop_id", shopid);
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.print("评论标签" + result);
                CommentLabels bean = GsonUtil.getInstance().fromJson(result, CommentLabels.class);
                list_labels = bean.getCate_label();
                adapter_label = new LabelAdapter(GoodsEvaluationActivity.this, list_labels);
                gridView_labels.setAdapter(adapter_label);
                if (bean.getIs_expert().equals("0")) {//不是达人
                    commit.setVisibility(View.GONE);
                } else {
                    commit.setVisibility(View.VISIBLE);
                }
            /*    for (int i = 0; i < list_labels.size(); i++) {
                    if (list_labels.get(i).getDoesup().equals("0")) {
                        list_labels.get(i).setZan(false);
                    } else {
                        list_labels.get(i).setZan(true);
                    }
                }*/
                gridView_labels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("OPT", "335");
                        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
                        map.put("shop_id", shopid);
                        map.put("label_id", list_labels.get(position).getLabelid());
                        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                    getLabelData();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });
    }


    /*达人评论列表*/
    public void getData() {
        if (isRefresh) {
            pageNum = 1;
            adapter_super.removeAll();
        }
        map = new HashMap<String, String>();
        map.put("OPT", "334");
        map.put("shop_id", shopid);
        map.put("currPage", String.valueOf(pageNum++));
        map.put("pageSize", "");
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.print("商品列表" + result);
                GoodsList bean = GsonUtil.getInstance().fromJson(result, GoodsList.class);
                System.out.print("多少条" + bean.getEvaluation().size());
                if (1 != pageNum) {
                    list_supermans.addAll(bean.getEvaluation());
                    adapter_super.setList(list_supermans);
                } else {
                    list_supermans = bean.getEvaluation();
                    adapter_super = new SupermansAdapter(GoodsEvaluationActivity.this, list_supermans);
                    listview_supermans.setAdapter(adapter_super);
                    adapter_super.notifyDataSetChanged();
                }
                if (list_supermans.size() < bean.getEvaluation().size()) {
                    listview_supermans.setIsLoadFull(false);
                }
                listview_supermans.finishRefresh();
                listview_supermans.finishLoadMore();
            }

        });
    }

    private class SupermansAdapter extends BaseAdapter {
        private Context context;
        ViewHolder holder;
        List<Evaluation> list_supermans;

        public SupermansAdapter(Context context, List<Evaluation> list_supermans) {
            this.list_supermans = list_supermans;
            this.context = context;
        }

        public void setList(List<Evaluation> list_supermans) {
            this.list_supermans = list_supermans;
            notifyDataSetChanged();
        }

        public void add(Evaluation page) {
            this.list_supermans.add(page);
            notifyDataSetChanged();
        }

        public void addFirst(Evaluation page) {
            this.list_supermans.add(0, page);
            notifyDataSetChanged();
        }

        public void addAll(List<Evaluation> list) {
            this.list_supermans.addAll(list);
            notifyDataSetChanged();
        }

        public void remove(int position) {
            this.list_supermans.remove(position);
            notifyDataSetChanged();
        }

        public void removeAll() {
            this.list_supermans.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list_supermans == null ? 0 : list_supermans.size();
        }

        @Override
        public Object getItem(int position) {
            return list_supermans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_daren_list, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_ming.setText(list_supermans.get(position).getUser_name());
            holder.tv_content.setText(list_supermans.get(position).getCate_comment());
            Glide.with(context).load(HttpConfig.IMAGEHOST + list_supermans.get(position).getPhoto()).into(holder.youliao_yuantu);
            String share_time = formatter.format(Long.valueOf(list_supermans.get(position).getComment_time().getTime()));
            holder.tv_time.setText(share_time);
            return convertView;
        }
    }

    private class LabelAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder holder;
        private List<Cate_label> list_labels;

        public LabelAdapter(Context context, List<Cate_label> list_labels) {
            this.context = context;
            this.list_labels = list_labels;
        }

        @Override
        public int getCount() {
            return list_labels == null ? 0 : list_labels.size();
        }

        @Override
        public Object getItem(int position) {
            return list_labels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_care_type, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(list_labels.get(position).getLabelName() + " " + list_labels.get(position).getNumber());
            if ("1".equals(list_labels.get(position).getDoesup())) {
                holder.tv_name.setTextColor(0xffFF9900);
                holder.tv_name.setBackgroundResource(R.drawable.care_selected);
            } else {
                holder.tv_name.setTextColor(0xff000000);
                holder.tv_name.setBackgroundResource(R.drawable.type_back);
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView tv_name, tv_time, tv_ming, tv_content;
        RoundImageView youliao_yuantu;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_ming = (TextView) view.findViewById(R.id.tv_ming);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            youliao_yuantu = (RoundImageView) view.findViewById(R.id.youliao_yuantu);
        }
    }

    /*达人推荐列表*/
    public class GoodsList {
        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<Evaluation> getEvaluation() {
            return evaluation;
        }

        public void setEvaluation(List<Evaluation> evaluation) {
            this.evaluation = evaluation;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        private String error;
        private List<Evaluation> evaluation;
        private String msg;
    }

    private class Evaluation {
        public String getCate_comment() {
            return cate_comment;
        }

        public void setCate_comment(String cate_comment) {
            this.cate_comment = cate_comment;
        }

        public ShareTime getComment_time() {
            return comment_time;
        }

        public void setComment_time(ShareTime comment_time) {
            this.comment_time = comment_time;
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel_id() {
            return label_id;
        }

        public void setLabel_id(String label_id) {
            this.label_id = label_id;
        }

        public String getPersistent() {
            return persistent;
        }

        public void setPersistent(String persistent) {
            this.persistent = persistent;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        private String cate_comment;
        private ShareTime comment_time;
        private String entityId;
        private String id;
        private String label_id;
        private String persistent;
        private String photo;
        private String shop_id;
        private String user_id;
        private String user_name;
    }

    /*评论标签*/
    public class CommentLabels {
        public List<Cate_label> getCate_label() {
            return cate_label;
        }

        public void setCate_label(List<Cate_label> cate_label) {
            this.cate_label = cate_label;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getIs_expert() {
            return is_expert;
        }

        public void setIs_expert(String is_expert) {
            this.is_expert = is_expert;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        private List<Cate_label> cate_label;
        private String error;
        private String is_expert;
        private String msg;
    }

    private class Cate_label {
        private String Doesup;
        private String labelName;
        private String labelid;
        private String number;
        private boolean isZan;

        public boolean isZan() {
            return isZan;
        }

        public void setZan(boolean zan) {
            isZan = zan;
        }


        public String getDoesup() {
            return Doesup;
        }

        public void setDoesup(String doesup) {
            Doesup = doesup;
        }


        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }

        public String getLabelid() {
            return labelid;
        }

        public void setLabelid(String labelid) {
            this.labelid = labelid;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
