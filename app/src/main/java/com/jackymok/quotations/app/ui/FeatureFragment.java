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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.adapters.QuotationCursorAdapter;
import com.jackymok.quotations.app.provider.AuthorContract;
import com.jackymok.quotations.app.provider.QuotationContract;
import com.jackymok.quotations.app.provider.QuotationProvider;
import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;

/**
 * Created by Jacky on 2014-05-13.
 */
public class FeatureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ArrayAdapter<String> adapter;

    private FadingActionBarHelper mFadingHelper;
    private QuotationCursorAdapter mCursorAdapter;
    private onGridViewItemClickedListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = mFadingHelper.createView(inflater);
        ImageView img = (ImageView) view.findViewById(R.id.image_header);
        img.setImageResource(R.drawable.tokyo);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        mCursorAdapter = new QuotationCursorAdapter(getActivity().getApplicationContext(), null, 0);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                long quotationId = cursor.getLong(cursor.getColumnIndex(QuotationContract.COLUMN_ID));
                //mListener.onGridViewItemClicked(quotationId, "placeholder");
            }
        });
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof onGridViewItemClickedListener) {
//            mListener = (onGridViewItemClickedListener) activity;
//        }

        int actionBarBg = R.drawable.ab_background;

        mFadingHelper = new FadingActionBarHelper()
                .actionBarBackground(actionBarBg)
                .headerLayout(R.layout.header_light)
                .contentLayout(R.layout.activity_listview)
                .lightActionBar(actionBarBg == R.drawable.ab_background_light);
        mFadingHelper.initActionBar(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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