package com.lsjr.zizisteward.photoflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.http.HttpConfig;
import com.lsjr.zizisteward.ly.activity.FriendCircleSave;
import com.lsjr.zizisteward.photoflow.PhotoViewAttacher.OnPhotoTapListener;
import com.squareup.picasso.Picasso;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    public static Bitmap bm = null;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        mAttacher.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                System.out.println("什么鬼");
                mImageView.setDrawingCacheEnabled(true);
                bm = mImageView.getDrawingCache();

                startActivity(new Intent(getContext(), FriendCircleSave.class));

                return false;
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//		System.out.println(mImageUrl);

//		Glide.with(getContext()).load(HttpConfig.IMAGEHOST + mImageUrl).into(mImageView);

        String[] space = mImageUrl.split("/storage");

        if (space.length > 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(mImageUrl);
            mImageView.setImageBitmap(bitmap);
        } else {
            String[] _space = mImageUrl.split("http");

            if (_space.length > 1) {
                Picasso.with(getActivity()).load(mImageUrl).into(mImageView);
            } else {
                Picasso.with(getActivity()).load(HttpConfig.IMAGEHOST + mImageUrl).into(mImageView);
            }
        }

//		ImageLoader.getInstance().displayImage(HttpConfig.IMAGEHOST + mImageUrl, mImageView, new SimpleImageLoadingListener() {
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				String message = null;
//				switch (failReason.getType()) {
//				case IO_ERROR:
//					message = "下载错误";
//					break;
//				case DECODING_ERROR:
//					message = "图片无法显示";
//					break;
//				case NETWORK_DENIED:
//					message = "网络有问题，无法下载";
//					break;
//				case OUT_OF_MEMORY:
//					message = "图片太大无法显示";
//					break;
//				case UNKNOWN:
//					message = "未知的错误";
//					break;
//				}
//				Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//				progressBar.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				progressBar.setVisibility(View.GONE);
//				mAttacher.update();
//			}
//		});
    }
}
