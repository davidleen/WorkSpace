package com.giants3.android.kit;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * span 幫助类
 * 支持放大部分文字，修改颜色
 */
public class SpanHelper {


    public static SpannableString integerSpan(Context context, CharSequence source, float proportion) {
        return integerSpan(context, source, proportion, 0);
    }

    public static SpannableString integerSpan(Context context, CharSequence source, int textColor) {
        return integerSpan(context, source, 0, textColor);

    }


    public static SpannableString integerSpan(Context context, CharSequence source, float proportion, int color) {


        SpannableString ss = null;
        if (context != null && !TextUtils.isEmpty(source)) {
            ss = new SpannableString(source);
            String regex = "-?\\d+";// ����������ʽ
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(source);
            while (matcher.find()) {
                if (proportion > 0)
                    ss.setSpan(new RelativeSizeSpan(proportion), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (color != 0)
                    ss.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }


}
