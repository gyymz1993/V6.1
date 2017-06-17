package com.lsjr.zizisteward.ly.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.hyphenate.chatuidemo.ui.MainActivity;
import com.libs.zxing.CaptureActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.bean.CircleFoundBean;
import com.lsjr.zizisteward.bean.CircleFoundBean.Circle;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.http.MyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Found extends Fragment implements OnClickListener {

    private View rootView;
    private LinearLayout ll_message;
    private LinearLayout ll_back;
    private LinearLayout ll_scan;
    private RoundImageView iv_head;
    private LinearLayout ll_circle;
    private LinearLayout ll_red_packet;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == rootView) {

            rootView = inflater.inflate(R.layout.fragment_found, null);

            ll_back = (LinearLayout) rootView.findViewById(R.id.ll_back);
            ll_scan = (LinearLayout) rootView.findViewById(R.id.ll_scan);
            iv_head = (RoundImageView) rootView.findViewById(R.id.iv_head);
            ll_circle = (LinearLayout) rootView.findViewById(R.id.ll_circle);
            ll_message = (LinearLayout) rootView.findViewById(R.id.ll_message);
            ll_red_packet = (LinearLayout) rootView.findViewById(R.id.ll_red_packet);

            ll_back.setOnClickListener(this);
            ll_scan.setOnClickListener(this);
            ll_circle.setOnClickListener(this);
            ll_red_packet.setOnClickListener(this);

        }

        return rootView;
    }

    private void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("OPT", "426");
        map.put("user_id", Fragment_ChatList.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                CircleFoundBean cfBean = new Gson().fromJson(result, CircleFoundBean.class);
                Circle circle = cfBean.getCircle();

                if (null != circle) {
                    ll_message.setVisibility(View.VISIBLE);

                    Picasso.with(getContext()).load(HttpConfig.IMAGEHOST + circle.getPhoto()).into(iv_head);

                } else {
                    ll_message.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                MainActivity.dismiss();
                break;

            case R.id.ll_circle:

                startActivity(new Intent(getActivity(),
                        Fragment_Circle_Friends.class));

                break;

            case R.id.ll_scan:

                startActivityForResult(new Intent(getContext(),
                        CaptureActivity.class), 1);

                break;

            case R.id.ll_red_packet:
                startActivity(new Intent(getContext(), RedPacketWeb.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    // 获取解析的数据
                    String text = data.getStringExtra("text");
                    handleResult(text);
                }
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
            startActivity(new Intent(getContext(),
                    CardHolderDetails.class).putExtra("id",
                    str[1]).putExtra("activity", "save"));
        } else {
            // 用弹窗展示信息
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        new HttpClientGet(getContext(), null, map, false,
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
                                        startActivity(new Intent(getContext(),
                                                QrCodePersonalActivity.class)
                                                .putExtra("id", content));
                                        break;

                                    case "2":
                                        startActivity(new Intent(getContext(),
                                                QrCodeAddGroupActivity.class)
                                                .putExtra("id", content));
                                        break;
                                }
                            } else {
                                // 用弹窗展示信息
                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        getContext());
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
}
