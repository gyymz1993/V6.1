package com.lsjr.zizisteward.ly.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

import com.lsjr.zizisteward.R;

import java.util.Random;

/**
 * Created by Ly on 2017/6/2.
 */

public class ImageCode {

    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static ImageCode bmpCode;

    public static ImageCode getInstance() {
        if (null == bmpCode) {
            bmpCode = new ImageCode();
        }

        return bmpCode;
    }

    private static final int DEFAULT_HEIGHT = 40;
    private static final int DEFAULT_WIDTH = 60;
    private static final int BASE_PADDING_LEFT = 5;
    private static final int BASE_PADDING_TOP = 15;
    private static final int DEFAULT_FONT_SIZE = 20;
    private static final int RANGE_PADDING_TOP = 20;
    private static final int DEFAULT_CODE_LENGTH = 4;
    private static final int DEFAULT_LINE_NUMBER = 2;
    private static final int RANGE_PADDING_LEFT = 12;

    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;

    private int base_padding_left = BASE_PADDING_LEFT;
    private int range_padding_left = RANGE_PADDING_LEFT;
    private int base_padding_top = BASE_PADDING_TOP;
    private int range_padding_top = RANGE_PADDING_TOP;

    private int font_size = DEFAULT_FONT_SIZE;
    private int codeLength = DEFAULT_CODE_LENGTH;
    private int line_number = DEFAULT_LINE_NUMBER;

    private String code;
    private int padding_left;
    private int padding_top;
    private Random random = new Random();

    //验证码图片
    public Bitmap createBitmap() {
        padding_left = 0;

        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();
        c.drawColor(Color.parseColor("#00000000"));
        Paint paint = new Paint();
        paint.setTextSize(font_size);

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }

        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }

        c.save( Canvas.ALL_SAVE_FLAG );//保存
        c.restore();//
        return bp;
    }

    public String getCode() {
        return code;
    }

    //验证码
    private String createCode() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());  //true为粗体，false为非粗体
        float skewX = random.nextInt(11) / 8;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//      paint.setUnderlineText(true); //true为下划线，false为非下划线
//      paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }
}
