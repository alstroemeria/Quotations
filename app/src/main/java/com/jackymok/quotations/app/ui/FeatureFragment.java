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
import android.widget.ListView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.adapters.QuotationCursorAdapter;
import com.jackymok.quotations.app.provider.AuthorContract;
import com.jackymok.quotations.app.provider.QuotationContract;
import com.jackymok.quotations.app.provider.QuotationProvider;

/**
 * Created by Jacky on 2014-05-13.
 */
public class FeatureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private QuotationCursorAdapter mCursorAdapter;
    private onGridViewItemClickedListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feature, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        mCursorAdapter = new QuotationCursorAdapter(getActivity().getApplicationContext(), null, 0);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        String[] projection = {QuotationContract.TABLE_QUOTATION+ "."+ QuotationContract.COLUMN_ID,QuotationContract.COLUMN_TEXT,
                AuthorContract.COLUMN_NAME, QuotationContract.TABLE_QUOTATION+ "."+QuotationContract.COLUMN_FAVOURITE,
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