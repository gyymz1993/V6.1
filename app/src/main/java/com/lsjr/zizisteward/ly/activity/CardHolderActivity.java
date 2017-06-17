package com.lsjr.zizisteward.ly.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.libs.zxing.CaptureActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.MyCardBean;
import com.lsjr.zizisteward.bean.MyCardBean.Card;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.slide.SlideBaseAdapter;
import com.lsjr.zizisteward.slide.SlideListView;
import com.lsjr.zizisteward.utils.CustomDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardHolderActivity extends Activity implements OnClickListener {

    private SlideListView mlv;
    private CHAdapter adapter;
    private LinearLayout ll_add;
    private LinearLayout ll_back;
    private LinearLayout ll_new_add;
    private LinearLayout ll_contacts;
    private PopupWindow popupWindow;
    private List<Card> card = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.card_holder_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.mlv = (SlideListView) super.findViewById(R.id.mlv);
        this.ll_add = (LinearLayout) super.findViewById(R.id.ll_add);
        this.ll_back = (LinearLayout) super.findViewById(R.id.ll_back);
        this.ll_new_add = (LinearLayout) super.findViewById(R.id.ll_new_add);
        this.ll_contacts = (LinearLayout) super.findViewById(R.id.ll_contacts);

        this.ll_add.setOnClickListener(this);
        this.ll_back.setOnClickListener(this);
        this.ll_new_add.setOnClickListener(this);
        this.ll_contacts.setOnClickListener(this);

        this.getCard();

        this.mlv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivityForResult(new Intent(CardHolderActivity.this,
                        CardHolderDetails.class).putExtra("id",
                        card.get(position).getId())
                        .putExtra("activity", "cha")
                        .putExtra("name", card.get(position).getUsername()), 1);
            }
        });
    }

    private void getCard() {
        CustomDialogUtils.startCustomProgressDialog(CardHolderActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "290");
        map.put("user_id",
                Fragment_ChatList.addSign(
                        Long.valueOf(App.getUserInfo().getId()), "u"));

        new HttpClientGet(CardHolderActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        CustomDialogUtils.stopCustomProgressDialog(CardHolderActivity.this);
                        System.out.println(result);

                        MyCardBean mBean = new Gson().fromJson(result,
                                MyCardBean.class);
                        adapter = null;
                        card = null;
                        card = mBean.getCard();
                        adapter = new CHAdapter(CardHolderActivity.this, card);
                        mlv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (card.size() >= 3) {
                            ll_new_add.setVisibility(View.GONE);
                        } else {
                            ll_new_add.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(MyError myError) {
                        CustomDialogUtils.stopCustomProgressDialog(CardHolderActivity.this);
                        super.onFailure(myError);
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_add:

                showPopupWindow(ll_add);

                break;

            case R.id.ll_back:
                finish();
                break;

            case R.id.ll_new_add:
                startActivityForResult(new Intent(CardHolderActivity.this,
                        NewCardActivity.class).putExtra("activity", "new"), 1);
                break;

            case R.id.ll_contacts:

                startActivity(new Intent(CardHolderActivity.this,
                        MyContactsActivity.class));
                break;
        }
    }

    private class CHAdapter extends SlideBaseAdapter {

        private Context context;
        private ViewHolder view;
        private List<Card> card;

        public CHAdapter(Context context, List<Card> card) {
            super(context);
            this.context = context;
            this.card = card;
        }

        @Override
        public int getCount() {
            return null == card ? 0 : card.size();
        }

        @Override
        public Object getItem(int position) {
            return card.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {

                convertView = createConvertView(position);

                view = new ViewHolder(convertView);

                convertView.setTag(view);

            } else {

                view = (ViewHolder) convertView.getTag();

            }

            view.tv_name.setText(card.get(position).getUsername());
            view.tv_company_name.setText(card.get(position).getCompany_name());
            view.tv_position_name.setText(card.get(position).getPosition());

            view.ll_delete.setTag(position);
            view.ll_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int pos = (int) v.getTag();
                    Map<String, String> map = new HashMap<>();
                    map.put("OPT", "288");
                    map.put("cardid", card.get(pos).getId());
                    new HttpClientGet(context, null, map, false,
                            new HttpClientGet.CallBacks<String>() {

                                @Override
                                public void onSuccess(String result) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(
                                                result);

                                        String error = jsonObject
                                                .getString("error");

                                        if (error.equals("1")) {
                                            card.remove(pos);
                                            adapter.notifyDataSetChanged();
                                            if (card.size() >= 3) {
                                                ll_new_add.setVisibility(View.GONE);
                                            } else {
                                                ll_new_add.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            });

            view.ll_qr_code.setTag(position);
            view.ll_qr_code.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    startActivity(new Intent(context, CardQrCodeActivirty.class)
                            .putExtra("id", card.get(pos).getId())
                            .putExtra("name", card.get(pos).getUsername())
                            .putExtra(
                                    "c_name",
                                    null == card.get(pos).getPosition() ? ""
                                            : card.get(pos).getPosition()));

                }
            });

            return convertView;
        }

        private class ViewHolder {
            /**
             * 人名
             */
            private TextView tv_name;
            /**
             * 公司名字
             */
            private TextView tv_company_name;
            /**
             * 职位名
             */
            private TextView tv_position_name;
            /**
             * 名片二维码
             */
            private LinearLayout ll_qr_code;
            /**
             * 发名片
             */
            private LinearLayout ll_send_card;
            private LinearLayout ll_delete;

            public ViewHolder(View v) {
                this.tv_name = (TextView) v.findViewById(R.id.tv_name);
                this.ll_delete = (LinearLayout) v.findViewById(R.id.ll_delete);
                this.tv_company_name = (TextView) v
                        .findViewById(R.id.tv_company_name);
                this.tv_position_name = (TextView) v
                        .findViewById(R.id.tv_position_name);
                this.ll_qr_code = (LinearLayout) v
                        .findViewById(R.id.ll_qr_code);
                this.ll_send_card = (LinearLayout) v
                        .findViewById(R.id.ll_send_card);
            }
        }

        @Override
        public int getFrontViewId(int position) {
            return R.layout.card_holder_activity_item;
        }

        @Override
        public int getLeftBackViewId(int position) {
            return 0;
        }

        @Override
        public int getRightBackViewId(int position) {
            return R.layout.card_holder_activity_right_delete;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 4:
                if (resultCode == RESULT_OK) {
                    // 获取解析的数据
                    String text = data.getStringExtra("text");
                    handleResult(text);
                }

                break;

            default:

                getCard();

                break;
        }
    }

    private void handleResult(String text) {
        // 普通字符串
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (text.startsWith("http://") || text.startsWith("https://")) {
            // 跳到浏览器加载网页
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("tel:")) {
            // 跳到打电话页面
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("smsto:")) {
            // 跳到发短信页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("mailto:")) {
            // 跳到发邮件页面
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("market://")) {
            // 跳到应用市场页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(text));
            startActivity(intent);
        } else if (text.startsWith("ziziUser:")) {
            String[] str = text.split("ziziUser:");
            getQrCode("1", str[1]);
        } else if (text.startsWith("ziziGroup:")) {
            String[] str = text.split("ziziGroup:");
            getQrCode("2", str[1]);
        } else if (text.startsWith("zizicard:")) {
            String[] str = text.split("zizicard:");
            // 获取解析的数据
            startActivity(new Intent(CardHolderActivity.this,
                    CardHolderDetails.class).putExtra("id",
                    str[1]).putExtra("activity", "save"));
        } else {
            // 用弹窗展示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(CardHolderActivity.this);
            builder.setTitle("扫描结果:");// 标题
            builder.setMessage(text);// 内容
            builder.setPositiveButton("确定", null);
            builder.show();
        }
    }

    private void getQrCode(final String _type, final String content) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "266");
        map.put("content", content);
        map.put("type", _type);
        new HttpClientGet(CardHolderActivity.this, null, map, false,
                new HttpClientGet.CallBacks<String>() {

                    @Override
                    public void onSuccess(String result) {
                        try {
                            System.out.println(result);
                            JSONObject jObject = new JSONObject(result);
                            String error = jObject.getString("error");

                            if (error.equals("1")) {
                                switch (_type) {
                                    case "1":
                                        startActivity(new Intent(CardHolderActivity.this,
                                                QrCodePersonalActivity.class)
                                                .putExtra("id", content));
                                        break;

                                    case "2":
                                        startActivity(new Intent(CardHolderActivity.this,
                                                QrCodeAddGroupActivity.class)
                                                .putExtra("id", content));
                                        break;
                                }
                            } else {
                                // 用弹窗展示信息
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        CardHolderActivity.this);
                                builder.setTitle("扫描结果:");// 标题
                                builder.setMessage(content);// 内容
                                builder.setPositiveButton("确定", null);
                                builder.show();
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

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(CardHolderActivity.this)
                .inflate(R.layout.card_bubble_activity, null);
        // 设置按钮的点击事件
        LinearLayout ll_rich_scan = (LinearLayout) contentView
                .findViewById(R.id.ll_rich_scan);

        LinearLayout ll_certification = (LinearLayout) contentView
                .findViewById(R.id.ll_certification);

        ll_rich_scan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
				startActivityForResult(new Intent(CardHolderActivity.this,CaptureActivity.class), 4);
            }
        });

        ll_certification.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivity(new Intent(CardHolderActivity.this,
                        ProfessionalCertificationActivity.class));
            }
        });

        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                220, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.icon_back_space));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }
}
