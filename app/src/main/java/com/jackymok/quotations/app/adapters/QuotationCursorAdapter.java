package com.jackymok.quotations.app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.provider.QuotationContract;

/**
* Created by Jacky on 11/03/14.
*/
public class QuotationCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public QuotationCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c,flags);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.list_item_quote, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(QuotationContract.COLUMN_TEXT));
        String author = c.getString(c.getColumnIndexOrThrow(QuotationContract.COLUMN_AUTHOR));

        DynamicHeightTextView title_text = (DynamicHeightTextView)v.findViewById(R.id.staggered_text);
        TextView author_text = (TextView)v.findViewById(R.id.listing_author);

        title_text.setText(title);
        author_text.setText(author);
    }
}