package com.jackymok.quotations.app.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.provider.CategoryContract;
import com.jackymok.quotations.app.provider.QuotationProvider;

/**
 * Created by Jacky on 09/03/14.
 */
public class CategoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter mAdapter;
    public  onListViewItemClickedListener mListener;


    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = new String[] { CategoryContract.COLUMN_NAME};
        int[] to = new int[] { R.id.listing_title};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.row_listview, null, from,to, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getActivity().getSupportLoaderManager().initLoader(0, null, this);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onListViewItemClickedListener) {
            mListener = (onListViewItemClickedListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Opens the second activity if an entry is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onListViewItemClicked(id, "placeholder");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection =  { CategoryContract.COLUMN_NAME, CategoryContract.COLUMN_ID };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                QuotationProvider.CONTENT_URI_CATEGORIES, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter!=null)
            mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }


    public interface onListViewItemClickedListener {
        public void onListViewItemClicked(long id, String category);
    }
}
