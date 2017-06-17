package com.hyphenate.easeui.model;

import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.lsjr.zizisteward.R;

public class EaseDefaultEmojiconDatas {

    private static String[] emojis = new String[]{
            EaseSmileUtils.ee_1,
            EaseSmileUtils.ee_2,
            EaseSmileUtils.ee_3,
            EaseSmileUtils.ee_4,
            EaseSmileUtils.ee_5,
            EaseSmileUtils.ee_6,
            EaseSmileUtils.ee_7,
            EaseSmileUtils.ee_8,
            EaseSmileUtils.ee_9,
            EaseSmileUtils.ee_10,
            EaseSmileUtils.ee_11,
            EaseSmileUtils.ee_12,
            EaseSmileUtils.ee_13,
            EaseSmileUtils.ee_14,
            EaseSmileUtils.ee_15,
            EaseSmileUtils.ee_16,
            EaseSmileUtils.ee_17,
            EaseSmileUtils.ee_18,
            EaseSmileUtils.ee_19,
            EaseSmileUtils.ee_20,
            EaseSmileUtils.ee_21,
            EaseSmileUtils.ee_22,
            EaseSmileUtils.ee_23,
            EaseSmileUtils.ee_24,
            EaseSmileUtils.ee_25,
            EaseSmileUtils.ee_26,
            EaseSmileUtils.ee_27,
            EaseSmileUtils.ee_28,
            EaseSmileUtils.ee_29,
            EaseSmileUtils.ee_30,
            EaseSmileUtils.ee_31,
            EaseSmileUtils.ee_32,
            EaseSmileUtils.ee_33,
            EaseSmileUtils.ee_34,
            EaseSmileUtils.ee_35,

    };

    private static int[] icons = new int[]{
            R.drawable.ee_one,
            R.drawable.ee_two,
            R.drawable.ee_three,
            R.drawable.ee_four,
            R.drawable.ee_five,
            R.drawable.ee_six,
            R.drawable.ee_seven,
            R.drawable.ee_eight,
            R.drawable.ee_nine,
            R.drawable.ee_ten,
            R.drawable.ee_eleven,
            R.drawable.ee_twelve,
            R.drawable.ee_thirteen,
            R.drawable.ee_fourteen,
            R.drawable.ee_fiveteen,
            R.drawable.ee_sixteen,
            R.drawable.ee_seventeen,
            R.drawable.ee_eighteen,
            R.drawable.ee_nineteen,
            R.drawable.ee_twenty,
            R.drawable.ee_twenty_one,
            R.drawable.ee_twenty_two,
            R.drawable.ee_twenty_three,
            R.drawable.ee_twenty_four,
            R.drawable.ee_twenty_five,
            R.drawable.ee_twenty_six,
            R.drawable.ee_twenty_seven,
            R.drawable.ee_twenty_eight,
            R.drawable.ee_twenty_nine,
            R.drawable.ee_thirty,
            R.drawable.ee_thirty_one,
            R.drawable.ee_thirty_two,
            R.drawable.ee_thirty_three,
            R.drawable.ee_thirty_four,
            R.drawable.ee_thirty_five,
    };


    private static final EaseEmojicon[] DATA = createData();

    private static EaseEmojicon[] createData() {
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for (int i = 0; i < icons.length; i++) {
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }

    public static EaseEmojicon[] getData() {
        return DATA;
    }
}
