package com.jackymok.quotations.app.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;
import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.adapters.QuotationCursorAdapter;
import com.jackymok.quotations.app.provider.QuotationContract;
import com.jackymok.quotations.app.provider.QuotationProvider;

/**
 * Created by Jacky on 11/03/14.
 */

public class StaggeredGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private QuotationCursorAdapter mCursorAdapter;
    private onGridViewItemClickedListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sgv, container, false);
        StaggeredGridView  staggeredGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);

        String[] from = new String[] { QuotationContract.COLUMN_TEXT};
        int[] to = new int[] { R.id.staggered_text};

        mCursorAdapter = new QuotationCursorAdapter(getActivity().getApplicationContext(), null, 0);
        staggeredGridView.setAdapter(mCursorAdapter);
        staggeredGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                long quotationId = cursor.getLong(cursor.getColumnIndex(QuotationContract.COLUMN_ID));
                mListener.onGridViewItemClicked(quotationId, "placeholder");
            }
        });
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onGridViewItemClickedListener) {
            mListener = (onGridViewItemClickedListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface onGridViewItemClickedListener {
        public void onGridViewItemClicked(long id, String category);
    }

    //================================================================================
    // LOADER
    //================================================================================
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {QuotationContract.COLUMN_ID,QuotationContract.COLUMN_TEXT,
                QuotationContract.COLUMN_AUTHOR, QuotationContract.COLUMN_FAVOURITE,
                QuotationContract.COLUMN_SEEN, QuotationContract.COLUMN_CATEGORY};
        CursorLoader cursorLoader = new CursorLoader(getActivity().getApplicationContext(), QuotationProvider.CONTENT_URI_QUOTATIONS,
                projection, null , null, null);
        return cursorLoader;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
