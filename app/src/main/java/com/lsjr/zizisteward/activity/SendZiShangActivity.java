package com.lsjr.zizisteward.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.RealNameConfirmActivity.StateBean;
import com.lsjr.zizisteward.basic.App;
import com.lsjr.zizisteward.basic.BaseActivity;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.BasicUserInfo;
import com.lsjr.zizisteward.bean.ImageItem;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.SCFBean;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.Bimp;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.EncryptUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.MultipartRequest;
import com.lsjr.zizisteward.utils.ToastUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendZiShangActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mLl_base;
    private RelativeLayout mSend_zishang;
    private EditText et_content;
    private TextView tv_number;
    private ImageView mIv_level;
    private EditText mDouzi_number;
    private MyGridView gv_pic, gv_classic;
    private List<String> mList;
    private TextView mTv_level;
    private BasicUserInfo mBean;

    private GVAdapter gAdapter;
    public static List<SCFBean.ImgBean> img_bean = new ArrayList<SCFBean.ImgBean>();
    private int max_num = 9;
    public static String p_name = "ly";
    public static String path;// 图片地址
    private CharSequence temp;
    private PopWindowCatFriend mPopw;
    private String credit;// 用户等级
    private int width;
    private String shiming_type;// 实名认证状态
    private String[] titles = {"大牌", "轻奢", "潮牌", "限量", "个性", "定制", "匠品", "户外", "美味", "家居", "出行", "健检"};
    private List<Classic> list_classic = new ArrayList<Classic>();
    private ClassicAdapter adapter_classic;
    private String hobies = "";

    @Override
    public int getContainerView() {
        return R.layout.activity_send_zishang;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitle("发送孜赏");
        setmRight("发送");
        mLl_base = (LinearLayout) findViewById(R.id.ll_base);// 父布局
        mSend_zishang = (RelativeLayout) findViewById(R.id.re_right);// 发送按钮
        et_content = (EditText) findViewById(R.id.et_content);// 发送编辑内容
        tv_number = (TextView) findViewById(R.id.tv_number);// 编辑文字数量
        mTv_level = (TextView) findViewById(R.id.tv_level);// 用户等级
        mIv_level = (ImageView) findViewById(R.id.iv_level);// 等级图标
        mDouzi_number = (EditText) findViewById(R.id.et_number);// 逗孜数
        gv_pic = (MyGridView) findViewById(R.id.gv_pic);// 点击图片编辑
        gv_classic = (MyGridView) findViewById(R.id.gv_classic);/*选择分类*/

        init();
        initListener();
        DisplayMetrics dm = new DisplayMetrics();
        SendZiShangActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        LayoutParams lParams = (LayoutParams) gv_pic.getLayoutParams();
        lParams.width = width / 3 * 2;
        gv_pic.setLayoutParams(lParams);
        initClassic();

    }

    private void initClassic() {
        for (int i = 0; i < titles.length; i++) {
            Classic name = new Classic();
            name.setName(titles[i]);
            list_classic.add(name);
        }
        adapter_classic = new ClassicAdapter(SendZiShangActivity.this, list_classic);
        gv_classic.setAdapter(adapter_classic);
        gv_classic.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list_classic.get(position).setChecked(!list_classic.get(position).isChecked);
                adapter_classic.notifyDataSetChanged();
            }
        });

    }

    private class ClassicAdapter extends BaseAdapter {
        private Context context;
        private LCQHolder holder;
        private List<Classic> list_classic;

        public ClassicAdapter(Context context, List<Classic> list_classic) {
            this.context = context;
            this.list_classic = list_classic;
        }

        @Override
        public int getCount() {
            return list_classic == null ? 0 : list_classic.size();
        }

        @Override
        public Object getItem(int position) {
            return list_classic.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_classic_fenlei_first_new, parent, false);
                holder = new LCQHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (LCQHolder) convertView.getTag();
            }
            if (list_classic.get(position).isChecked) {
                holder.name.setTextColor(0xffff9900);
            } else {
                holder.name.setTextColor(0xff000000);
            }
            holder.name.setText(list_classic.get(position).getName());
            return convertView;
        }

        private class LCQHolder {
            private TextView name;

            public LCQHolder(View view) {
                name = (TextView) view.findViewById(R.id.name);
            }
        }


    }

    private void init() {
        img_bean = new ArrayList<>();
        mPopw = new PopWindowCatFriend(SendZiShangActivity.this, "发送界面");
        SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
        imgBean.setType(3);
        img_bean.add(imgBean);

        this.gAdapter = new GVAdapter(SendZiShangActivity.this, img_bean);
        this.gv_pic.setAdapter(gAdapter);

        this.et_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_number.setText(temp.length() + "/240字");
            }
        });

        gv_pic.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
                if (isOpen == true) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
                }

                boolean space = false;

                if (img_bean.get(pos).getType() != 3) {
                    space = true;
                }

                startActivityForResult(new Intent(SendZiShangActivity.this, SelectorPicturesActivity.class)
                        .putExtra("pos", pos).putExtra("space", space).putExtra("activity", "scf"), 1);

            }
        });
    }

    @Override
    protected void onResume() {
        getData();
        getStateData();
        super.onResume();
    }

    /* 用户实名认证状态 */
    private void getStateData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "88");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                StateBean bean = GsonUtil.getInstance().fromJson(result, StateBean.class);
                shiming_type = bean.getIsIdNumberVerified();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    public class GVAdapter extends BaseAdapter {
        private Context context;
        private List<SCFBean.ImgBean> iBeans;
        private ViewHolder view;

        public GVAdapter(Context context, List<SCFBean.ImgBean> iBeans) {
            this.context = context;
            this.iBeans = iBeans;
        }

        @Override
        public int getCount() {
            return null == iBeans ? 0 : iBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return iBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_published_grida, parent, false);
                view = new ViewHolder(convertView);
                LayoutParams lParams = (LayoutParams) view.img.getLayoutParams();
                lParams.width = ((width / 3) * 2) / 3 - 4;
                lParams.height = ((width / 3) * 2) / 3 - 4;
                view.img.setLayoutParams(lParams);
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }

            if (img_bean.get(position).getType() == 3) {
                view.img.setImageResource(R.drawable.add_photo);
            } else if (img_bean.get(position).getType() == 2) {
                try {
                    view.img.setImageBitmap(revitionImageSize(img_bean.get(position).getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (img_bean.get(position).getType() == 1) {

                try {

                    view.img.setImageBitmap(revitionImageSize(img_bean.get(position).getPath()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return convertView;
        }

        private class ViewHolder {
            private ImageView img;

            public ViewHolder(View v) {
                this.img = (ImageView) v.findViewById(R.id.item_grida_image);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                System.out.println("回调:  " + img_bean.size());
                if (img_bean.size() != 9 && img_bean.get(img_bean.size() - 1).getType() != 3) {
                    SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                    imgBean.setType(3);
                    img_bean.add(imgBean);
                }
                gAdapter.notifyDataSetChanged();
                break;
            case 2:
                gAdapter.notifyDataSetChanged();
                break;

        }
    }

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /* 用户消息 */
    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("OPT", "199");
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        new HttpClientGet(getApplicationContext(), null, map, false, new HttpClientGet.CallBacks<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("用户消息" + result);
                mBean = GsonUtil.getInstance().fromJson(result, BasicUserInfo.class);
                credit = mBean.getCredit();
                if ("0".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_zero);
                    mTv_level.setText("v0级会员");
                }
                if ("1".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_one);
                    mTv_level.setText("v1级会员");
                }
                if ("2".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_two);
                    mTv_level.setText("v2级会员");
                }
                if ("3".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_three);
                    mTv_level.setText("v3级会员");
                }
                if ("4".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_three);
                    mTv_level.setText("v3级会员");
                }
                if ("5".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_five);
                    mTv_level.setText("v5级会员");
                }
                if ("6".equals(mBean.getCredit())) {
                    mIv_level.setImageResource(R.drawable.level_six);
                    mTv_level.setText("v6级会员");
                }
            }
        });

    }

    private void initListener() {
        mSend_zishang.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_right:
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    ToastUtils.show(getApplicationContext(), "孜赏内容不能为空哦...");
                    return;
                }

                if ((img_bean.size() - 1) == 0) {
                    Toast.makeText(SendZiShangActivity.this, "孜赏图片不能为空哦...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(mDouzi_number.getText().toString())
                        || "0".equals(mDouzi_number.getText().toString())) {
                    Toast.makeText(SendZiShangActivity.this, "您还没有打赏豆孜...", Toast.LENGTH_SHORT).show();
                    return;
                }


                for (int i = 0; i < list_classic.size(); i++) {

                    if (list_classic.get(i).isChecked()) {

                        if (hobies.equals("")) {
                            hobies = list_classic.get(i).getName();
                        } else {
                            hobies += "," + list_classic.get(i).getName();
                        }
                    }
                }
                System.out.print("李晨奇" + hobies);
                String[] ids = hobies.split(",");
                System.out.print("大小" + ids.length);
                if (ids.length == 0) {
                    ToastUtils.show(getApplicationContext(), "请至少选择1个分类");
                    return;
                }

                if (ids.length > 2) {
                    ToastUtils.show(getApplicationContext(), "最多选择3个分类");
                    return;
                }

                if (credit.equals("0")) {
                    ToastUtils.show(getApplicationContext(), "您的等级还不够,暂时不能发送孜赏");
                    return;
                }

                if ("0".equals(shiming_type)) {
                    final Dialog dialog = new Dialog(SendZiShangActivity.this, R.style.dialog);
                    dialog.setContentView(R.layout.dialog_real_name_confirm);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER | Gravity.CENTER);
                    TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
                    TextView tv_confirm = (TextView) dialog.findViewById(R.id.tv_confirm);
                    tv_cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    tv_confirm.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(SendZiShangActivity.this, RealNameConfirmActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                    return;
                }
                UpDataPicture();
                break;
        }
    }

    private void UpDataPicture() {
        CustomDialogUtils.startCustomProgressDialog(SendZiShangActivity.this, "正在发送孜赏...");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", EncryptUtils.addSign(Long.valueOf(App.getUserInfo().getId()), "u"));
        map.put("content", et_content.getText().toString());
        map.put("customTag", hobies);
        if (img_bean.size() == 9) {
            map.put("imgNumber", String.valueOf(img_bean.size()));
        } else {
            map.put("imgNumber", String.valueOf(img_bean.size() - 1));
        }
        map.put("zizipeas", String.valueOf(Integer.parseInt(mDouzi_number.getText().toString()) * 500));
        map.put("enjoy_id", "");
        map.put("type", "1");
        new Thread(new Runnable() {
            public void run() {
                Newcompress(img_bean, map);
            }
        }).start();

    }

    protected void Newcompress(List<SCFBean.ImgBean> img_bean, Map<String, String> map) {
        for (int i = 0; i < img_bean.size(); i++) {

            if (img_bean.get(i).getType() != 3) {

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                float hh = dm.heightPixels;
                float ww = dm.widthPixels;
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(img_bean.get(i).getPath(), opts);
                opts.inJustDecodeBounds = false;
                int w = opts.outWidth;
                int h = opts.outHeight;
                int size = 0;
                if (w <= ww && h <= hh) {
                    size = 1;
                } else {
                    double scale = w >= h ? w / ww : h / hh;
                    double log = Math.log(scale) / Math.log(2);
                    double logCeil = Math.ceil(log);
                    size = (int) Math.pow(2, logCeil);
                }
                opts.inSampleSize = size;
                bitmap = BitmapFactory.decodeFile(img_bean.get(i).getPath(), opts);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int quality = 100;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                System.out.println(baos.toByteArray().length);
                while (baos.toByteArray().length > 45 * 1024) {
                    baos.reset();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    if (quality > 30) {
                        quality -= 20;
                    } else {
                        break;
                    }
                }

                try {
                    baos.writeTo(new FileOutputStream(img_bean.get(i).getNew_path() + "new" + ".jpg"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        baos.flush();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<SCFBean.ImgBean> _img = new ArrayList<SCFBean.ImgBean>();

        for (int j = 0; j < img_bean.size(); j++) {

            if (img_bean.get(j).getType() != 3) {
                _img.add(img_bean.get(j));
            }
        }

        String[] path = new String[_img.size()];
        for (int i = 0; i < _img.size(); i++) {
            path[i] = _img.get(i).getNew_path() + "new" + ".jpg";
            System.out.println(path[i]);
        }

        doUploadTest(map, path);
    }

    private void doUploadTest(Map<String, String> params, String[] path) {
        RequestQueue mSingleQueue = Volley.newRequestQueue(getApplicationContext());
        List<File> f = new ArrayList<File>();
        for (int i = 0; i < path.length; i++) {
            File files = new File(path[i]);
            f.add(files);
        }
        MultipartRequest request = new MultipartRequest(HttpConfig.IMAGEHOST + "/app/uploadEnjoy",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("孜赏结果" + response);
                        BasicParameterBean bean = GsonUtil.getInstance().fromJson(response, BasicParameterBean.class);
                        CustomDialogUtils.stopCustomProgressDialog(getApplicationContext());
                        if ("抱歉，您的等级无法发送视界".equals(bean.getMsg())) {
                            final Dialog dialog = new Dialog(SendZiShangActivity.this, R.style.dialog);
                            dialog.setContentView(R.layout.dialog_upgrade);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER | Gravity.CENTER);
                            TextView tv_upgrade = (TextView) dialog.findViewById(R.id.tv_upgrade);
                            tv_upgrade.setOnClickListener(new OnClickListener() {

                                @SuppressWarnings("static-access")
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), VipUpgradeActivity.class);
                                    intent.putExtra("url", "/Membershipprivileges/premiummember?userid="
                                            + App.getInstance().getUserInfo().getId());
                                    startActivity(intent);
                                }
                            });
                            dialog.show();
                        } else {
                            Toast.makeText(SendZiShangActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            SendZiShangActivity.this.setResult(1, intent);
                            SendZiShangActivity.this.finish();
                        }

                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogUtils.stopCustomProgressDialog(SendZiShangActivity.this);
                Toast.makeText(getApplicationContext(), "发表失败,请稍后重试!", Toast.LENGTH_SHORT).show();
            }
        }, "enjoyImg", f, params);

        mSingleQueue.add(request);
    }

    public class Classic {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private String name;
        private boolean isChecked;
    }
}
