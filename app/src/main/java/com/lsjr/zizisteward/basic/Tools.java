package com.lsjr.zizisteward.basic;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.adapter.AreasAdapter;
import com.lsjr.zizisteward.utils.DensityUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Tools {
    public static PopupWindow popupWindow;


    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("") || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }
        return false;
    }


    public static boolean isNull(String s) {

        return null == s || s.equals("") || s.equalsIgnoreCase("null");
    }

    /**
     */
    public static String getText(TextView v) {
        if (v != null) {
            return v.getText().toString().trim();
        }
        return "";
    }

    /**
     */
    public static String getText(EditText v) {
        if (v != null) {
            return v.getText().toString().trim();
        }
        return "";
    }

    /**
     */
    public static boolean isNull(TextView v) {
        return null == v || Tools.isNull(Tools.getText(v));
    }

    /**
     */
    public static boolean isNull(EditText v) {
        return null == v || Tools.isNull(Tools.getText(v));
    }

    /**
     */
    public static boolean validatePhone(String phone) {
        if (isNull(phone)) {
            return false;
        }
        String pattern = "^1[3,4,5,6,7,8]+\\d{9}$";
        return phone.matches(pattern);
    }

    /**
     */
    public static boolean validateTel(String tel) {
        if (isNull(tel)) {
            return false;
        }
        String pattern = "^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$";
        return tel.matches(pattern);
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    public static void showPopupMenu(Context context, View view, final List<String> mList,
                                     AdapterView.OnItemClickListener itemClickListener) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.pop_show_menu_layout, null);
        ListView mListMenu = (ListView) view1.findViewById(R.id.listView);
        final AreasAdapter orderAdapter = new AreasAdapter(context, mList);
        mListMenu.setAdapter(orderAdapter);
        mListMenu.setOnItemClickListener(itemClickListener);
        popupWindow = new PopupWindow(view, measureContentWidth(context, orderAdapter),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mListMenu.setOnItemClickListener(itemClickListener);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view1);
        // popupWindow.showAsDropDown(view);
        popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, DensityUtil.dip2px(context, 10),
                DensityUtil.dip2px(context, 68));

    }

    /**
     * @param context
     * @param listAdapter
     * @return
     */
    private static int measureContentWidth(Context context, ListAdapter listAdapter) {
        ViewGroup mMeasureParent = null;
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;

        final ListAdapter adapter = listAdapter;
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(context);
            }

            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth + DensityUtil.dip2px(context, 16);
    }
}
