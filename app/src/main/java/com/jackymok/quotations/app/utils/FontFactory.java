package com.jackymok.quotations.app.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Jacky on 15/03/14.
 */
public class FontFactory {
    private  static FontFactory instance;
    private HashMap<String, Typeface> fontMap = new HashMap<String, Typeface>();

    private FontFactory() {
    }

    public static FontFactory getInstance() {
        if (instance == null){
            instance = new FontFactory();
        }
        return instance;
    }

    public Typeface getFont(Context context,String typefaceName) {
        Typeface typeface = fontMap.get(typefaceName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),String.format("fonts/%s", typefaceName));
            fontMap.put(typefaceName, typeface);
        }
        return typeface;
    }
}