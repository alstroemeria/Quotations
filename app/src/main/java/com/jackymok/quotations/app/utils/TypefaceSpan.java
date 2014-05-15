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
import android.util.TypedValue;

import com.jackymok.quotations.app.AppController;


public class TypefaceSpan extends MetricAffectingSpan {
    /** An <code>LruCache</code> for previously loaded typefaces. */
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<String, Typeface>(12);

    private Typeface mTypeface;
    private Context mContext;


    public TypefaceSpan(Context context, String typefaceName) {
        mTypeface = sTypefaceCache.get(typefaceName);
        mContext = context;

        if (mTypeface == null) {
            mTypeface = AppController.getInstance().getFont(typefaceName);

            // Cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
    }
    private void modifyPaint(TextPaint p) {
        int MY_DIP_VALUE = 16;
        int pixel= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, mContext.getResources().getDisplayMetrics());

        p.setTypeface(mTypeface);
        // Note: This flag is required for proper typeface rendering
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        p.setTextSize(pixel);//or what ever size you want

       // p.setColor( mContext.getResources().getColor(R.color.flatui_belize_hole));

    }

    @Override
    public void updateMeasureState(TextPaint p) {

        modifyPaint(p);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        modifyPaint(tp);
    }


}