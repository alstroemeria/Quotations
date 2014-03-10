package com.jackymok.quotations.app.utils;

/**
 * Created by Jacky on 09/03/14.
 */

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;


public class TypefaceSpan extends MetricAffectingSpan {
    /** An <code>LruCache</code> for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

    private Typeface mTypeface;

    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(context.getApplicationContext()
                    .getAssets(), String.format("fonts/%s", typefaceName));

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }

    @Override
    public void updateMeasureState(TextPaint p) {

        int MY_DIP_VALUE = 5; //5dp

        p.setTypeface(mTypeface);
        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        p.setTextSize(100);//or what ever size you want
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
        // Note: This flag is required for proper typeface rendering
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        tp.setTextSize(100);//or what ever size you want
    }


}