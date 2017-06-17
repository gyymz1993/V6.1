package com.lsjr.zizisteward.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.newview.MyGridView;
import com.lsjr.zizisteward.utils.Bimp;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/31.
 */

public class PhotosUpLoadActivity extends Activity {
    ImageView iv_add;
    MyGridView gridview;
    private static final int REQUEST_CODE = 732;
    private ArrayList<String> mResults = new ArrayList<>();
    private String[] images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_up_load);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        gridview = (MyGridView) findViewById(R.id.gridview);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PhotosUpLoadActivity.this, ImagesSelectorActivity.class);
//                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
//                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                System.out.print("回调" + mResults);
                StringBuilder sb = new StringBuilder();
                for (String result : mResults) {
                    sb.append(result).append(",");
                }
                System.out.print("过来的" + sb.toString());
                images = sb.toString().split(",");
                MyAdapter adapter = new MyAdapter(PhotosUpLoadActivity.this, images);
                gridview.setAdapter(adapter);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class MyAdapter extends BaseAdapter {
        Context context;
        MyViewHolder holder;
        String[] images;

        public MyAdapter(Context context, String[] images) {
            this.context = context;
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
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
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_upload_photos, parent, false);
                holder = new MyViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }
            try {
                holder.image.setImageBitmap(Bimp.revitionImageSize(images[position]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    public class MyViewHolder {
        ImageView image;

        public MyViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
