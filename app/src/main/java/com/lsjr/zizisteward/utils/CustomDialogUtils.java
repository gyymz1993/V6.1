package com.lsjr.zizisteward.utils;

import com.lsjr.zizisteward.newview.CustomProgressDialog;

import android.content.Context;

/**
 * 
 */
public class CustomDialogUtils {

    public static CustomProgressDialog customProgressDialog = null;

    public static void startCustomProgressDialog(final Context context, String msg) {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog.createDialog(context);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.setMessage(msg);
        }
        customProgressDialog.show();
    }

    public static void stopCustomProgressDialog(Context context) {
        if (customProgressDialog != null) {
            customProgressDialog.dismiss();
            customProgressDialog = null;
        }
    }
}
