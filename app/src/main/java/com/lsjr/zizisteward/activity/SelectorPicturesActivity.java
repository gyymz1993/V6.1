package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.lcq.activity.IntoIndentityConfirmActivity;
import com.lsjr.zizisteward.ly.activity.IdentityAuthenticationActivity;
import com.lsjr.zizisteward.ly.activity.ProfessionalCertificationActivity;
import com.lsjr.zizisteward.ly.activity.SCFBean;
import com.lsjr.zizisteward.ly.activity.SendCircleFriendsActivity;
import com.lsjr.zizisteward.ly.activity.SendShareActivity;
import com.lsjr.zizisteward.utils.PermissionUtils;

import java.io.File;

public class SelectorPicturesActivity extends Activity implements OnClickListener {

    private TextView tv_album;
    private TextView tv_cancel;
    private TextView tv_camera;
    private RelativeLayout rl_parent;
    private final static int CAMERA = 1;
    private final static int ALBUM = 2;
    private String localTempImgDir = "ly";
    private String localTempImgFileName;
    private int pos;
    private boolean space;
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.popwindowcatfriend_isone);

        this.rl_parent = (RelativeLayout) super.findViewById(R.id.rl_parent);
        this.tv_cancel = (TextView) super.findViewById(R.id.popcatfriendisone_cancel);
        this.tv_camera = (TextView) super.findViewById(R.id.popcatfriendisone_pictures);
        this.tv_album = (TextView) super.findViewById(R.id.popcatfriendisone_pictureschoose);

        this.tv_album.setOnClickListener(this);
        this.rl_parent.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
        this.tv_camera.setOnClickListener(this);

        this.pos = getIntent().getIntExtra("pos", 0);
        this.activity = getIntent().getStringExtra("activity");
        this.space = getIntent().getBooleanExtra("space", false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.popcatfriendisone_cancel:

                this.tv_album.setVisibility(View.GONE);
                this.tv_camera.setVisibility(View.GONE);
                this.tv_cancel.setVisibility(View.GONE);
                this.finish();

                break;

            case R.id.rl_parent:

                this.tv_album.setVisibility(View.GONE);
                this.tv_camera.setVisibility(View.GONE);
                this.tv_cancel.setVisibility(View.GONE);
                this.finish();

                break;

            case R.id.popcatfriendisone_pictures:
                if (PermissionUtils.isCameraPermission(SelectorPicturesActivity.this, 68)) {
                    // 相机
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {

                        File dir = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        localTempImgFileName = SendCircleFriendsActivity.p_name + pos + ".jpg";
                        File f = new File(dir, localTempImgFileName);
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                        startActivityForResult(intent, CAMERA);
                    }
                }
                break;
            case R.id.popcatfriendisone_pictureschoose:
                if (PermissionUtils.isCameraPermission(SelectorPicturesActivity.this, 68)) {
                    // 相册
                    try {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, ALBUM);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(SelectorPicturesActivity.this, "未能找到照片", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case CAMERA:
                    // 相机

                    if (activity.equals("scf")) {
                        if (SendZiShangActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            SendZiShangActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            SendZiShangActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendZiShangActivity.p_name + pos + ".jpg";
                            SendZiShangActivity.img_bean.get(pos).setPath(path);
                            SendZiShangActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                    + "/" + localTempImgDir + "/" + SendZiShangActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendZiShangActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendZiShangActivity.p_name + pos);
                            imgBean.setPath(path);
                            SendZiShangActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + SendZiShangActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();
                    } else if (activity.equals("pca")) {

                        if (ProfessionalCertificationActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            ProfessionalCertificationActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            ProfessionalCertificationActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + ProfessionalCertificationActivity.p_name + pos + ".jpg";
                            ProfessionalCertificationActivity.img_bean.get(pos).setPath(path);
                            ProfessionalCertificationActivity.img_bean.get(pos)
                                    .setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                            + ProfessionalCertificationActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + ProfessionalCertificationActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + ProfessionalCertificationActivity.p_name + pos);
                            imgBean.setPath(path);
                            ProfessionalCertificationActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + ProfessionalCertificationActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();

                    } else if (activity.equals("ssa")) {

                        if (SendShareActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            SendShareActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            SendShareActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShareActivity.p_name + pos + ".jpg";
                            SendShareActivity.img_bean.get(pos).setPath(path);
                            SendShareActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory() + "/"
                                    + localTempImgDir + "/" + SendShareActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShareActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShareActivity.p_name + pos);
                            imgBean.setPath(path);
                            SendShareActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + SendShareActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();

                    } else if (activity.equals("fankui")) {
                        if (SendFeedBackActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            SendFeedBackActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            SendFeedBackActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendFeedBackActivity.p_name + pos + ".jpg";
                            SendFeedBackActivity.img_bean.get(pos).setPath(path);
                            SendFeedBackActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                    + "/" + localTempImgDir + "/" + SendFeedBackActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendFeedBackActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendFeedBackActivity.p_name + pos);
                            imgBean.setPath(path);
                            SendFeedBackActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + SendFeedBackActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();

                    } else if (activity.equals("shijie")) {
                        if (SendShiJieActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            SendShiJieActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            SendShiJieActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShiJieActivity.p_name + pos + ".jpg";
                            SendShiJieActivity.img_bean.get(pos).setPath(path);
                            SendShiJieActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory() + "/"
                                    + localTempImgDir + "/" + SendShiJieActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShiJieActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + SendShiJieActivity.p_name + pos);
                            imgBean.setPath(path);
                            SendShiJieActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + SendShiJieActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();
                    } else if (activity.equals("shaidan")) {
                        if (BaskSingleActivity.img_bean.get(pos).getType() == 3) {
                            BaskSingleActivity.img_bean.remove(pos);
                        }
                        if (space) {
                            BaskSingleActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + BaskSingleActivity.p_name + pos + ".jpg";
                            BaskSingleActivity.img_bean.get(pos).setPath(path);
                            BaskSingleActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory() + "/"
                                    + localTempImgDir + "/" + BaskSingleActivity.p_name + pos);
                            setResult(2);
                        } else {
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + BaskSingleActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + BaskSingleActivity.p_name + pos);
                            imgBean.setPath(path);
                            BaskSingleActivity.img_bean.add(imgBean);
                            setResult(1);
                        }
                        finish();
                    } else if (activity.equals("shenfen_confirm")) {
                        if (IntoIndentityConfirmActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            IntoIndentityConfirmActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            IntoIndentityConfirmActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IntoIndentityConfirmActivity.p_name + pos + ".jpg";
                            IntoIndentityConfirmActivity.img_bean.get(pos).setPath(path);
                            IntoIndentityConfirmActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory() + "/"
                                    + localTempImgDir + "/" + IntoIndentityConfirmActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IntoIndentityConfirmActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IntoIndentityConfirmActivity.p_name + pos);
                            imgBean.setPath(path);
                            IntoIndentityConfirmActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + IntoIndentityConfirmActivity.img_bean.size());
                            setResult(1);
                        }
                        finish();
                    } else {
                        if (IdentityAuthenticationActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");
                            IdentityAuthenticationActivity.img_bean.remove(pos);
                        }

                        if (space) {
                            System.out.println("如果是替换");
                            IdentityAuthenticationActivity.img_bean.get(pos).setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IdentityAuthenticationActivity.p_name + pos + ".jpg";
                            IdentityAuthenticationActivity.img_bean.get(pos).setPath(path);
                            IdentityAuthenticationActivity.img_bean.get(pos)
                                    .setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                            + IdentityAuthenticationActivity.p_name + pos);
                            setResult(2);
                        } else {
                            System.out.println("如果是新增");
                            SCFBean.ImgBean imgBean = new SCFBean.ImgBean();
                            imgBean.setType(1);
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IdentityAuthenticationActivity.p_name + pos + ".jpg";
                            imgBean.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                    + IdentityAuthenticationActivity.p_name + pos);
                            imgBean.setPath(path);
                            IdentityAuthenticationActivity.img_bean.add(imgBean);
                            System.out.println("拍照新增后：  " + IdentityAuthenticationActivity.img_bean.size());
                            setResult(1);
                        }

                        finish();
                    }

                    break;

                case ALBUM:

                    // 相册

                    ContentResolver resolver = this.getContentResolver();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    Uri uri = data.getData();
                    CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor.getColumnIndex(pojo[0]));
                    if (path != null && path.length() > 0) {
                        System.out.println("相册 " + path);
                    }

                    if (activity.equals("scf")) {
                        if (SendZiShangActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");

                            SendZiShangActivity.img_bean.remove(pos);
                        }

                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {

                            if (space) {
                                System.out.println("如果是替换");

                                SendZiShangActivity.img_bean.get(pos).setType(1);
                                SendZiShangActivity.img_bean.get(pos).setPath(path);
                                SendZiShangActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + SendZiShangActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                System.out.println("如果是新增");

                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + SendZiShangActivity.p_name + pos);
                                ib.setUri(uri);
                                SendZiShangActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    } else if (activity.equals("shijie")) {
                        if (SendShiJieActivity.img_bean.get(pos).getType() == 3) {
                            System.out.println("如果是个加号 就移除他");

                            SendShiJieActivity.img_bean.remove(pos);
                        }

                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {

                            if (space) {
                                System.out.println("如果是替换");

                                SendShiJieActivity.img_bean.get(pos).setType(1);
                                SendShiJieActivity.img_bean.get(pos).setPath(path);
                                SendShiJieActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + SendShiJieActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                System.out.println("如果是新增");

                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + SendShiJieActivity.p_name + pos);
                                ib.setUri(uri);
                                SendShiJieActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    } else if (activity.equals("shaidan")) {
                        if (BaskSingleActivity.img_bean.get(pos).getType() == 3) {
                            BaskSingleActivity.img_bean.remove(pos);
                        }
                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {
                            if (space) {
                                BaskSingleActivity.img_bean.get(pos).setType(1);
                                BaskSingleActivity.img_bean.get(pos).setPath(path);
                                BaskSingleActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + BaskSingleActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + BaskSingleActivity.p_name + pos);
                                ib.setUri(uri);
                                BaskSingleActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    } else if (activity.equals("ssa")) {
                        if (SendShareActivity.img_bean.get(pos).getType() == 3) {
                            SendShareActivity.img_bean.remove(pos);
                        }
                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {
                            if (space) {
                                SendShareActivity.img_bean.get(pos).setType(1);
                                SendShareActivity.img_bean.get(pos).setPath(path);
                                SendShareActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + SendShareActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + SendShareActivity.p_name + pos);
                                ib.setUri(uri);
                                SendShareActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    } else if (activity.equals("fankui")) {
                        if (SendFeedBackActivity.img_bean.get(pos).getType() == 3) {
                            SendFeedBackActivity.img_bean.remove(pos);
                        }
                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {
                            if (space) {
                                SendFeedBackActivity.img_bean.get(pos).setType(1);
                                SendFeedBackActivity.img_bean.get(pos).setPath(path);
                                SendFeedBackActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + SendFeedBackActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + SendFeedBackActivity.p_name + pos);
                                ib.setUri(uri);
                                SendFeedBackActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    } else if (activity.equals("shenfen_confirm")) {
                        if (IntoIndentityConfirmActivity.img_bean.get(pos).getType() == 3) {
                            IntoIndentityConfirmActivity.img_bean.remove(pos);
                        }
                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {
                            if (space) {
                                IntoIndentityConfirmActivity.img_bean.get(pos).setType(1);
                                IntoIndentityConfirmActivity.img_bean.get(pos).setPath(path);
                                IntoIndentityConfirmActivity.img_bean.get(pos).setNew_path(Environment.getExternalStorageDirectory()
                                        + "/" + localTempImgDir + "/" + IntoIndentityConfirmActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + IntoIndentityConfirmActivity.p_name + pos);
                                ib.setUri(uri);
                                IntoIndentityConfirmActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }

                    } else {
                        if (IdentityAuthenticationActivity.img_bean.get(pos).getType() == 3) {
                            IdentityAuthenticationActivity.img_bean.remove(pos);
                        }
                        if (null == path) {
                            Toast.makeText(SelectorPicturesActivity.this, "选择图片失败,请重新选择...", Toast.LENGTH_SHORT).show();
                        } else {
                            if (space) {
                                IdentityAuthenticationActivity.img_bean.get(pos).setType(1);
                                IdentityAuthenticationActivity.img_bean.get(pos).setPath(path);
                                IdentityAuthenticationActivity.img_bean.get(pos)
                                        .setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                                + IdentityAuthenticationActivity.p_name + pos);
                                setResult(2);
                                finish();
                            } else {
                                SCFBean.ImgBean ib = new SCFBean.ImgBean();
                                ib.setType(2);
                                ib.setPath(path);
                                ib.setNew_path(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                                        + IdentityAuthenticationActivity.p_name + pos);
                                ib.setUri(uri);
                                IdentityAuthenticationActivity.img_bean.add(ib);
                                setResult(1);
                                finish();
                            }
                        }
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
