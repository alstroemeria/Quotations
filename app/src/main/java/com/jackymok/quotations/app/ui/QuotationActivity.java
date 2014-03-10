package com.jackymok.quotations.app.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.provider.QuotationContract;
import com.jackymok.quotations.app.provider.QuotationProvider;

;

public class QuotationActivity extends BaseActivty implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final String CATEGORY_KEY = "CATEGORY_KEY";
    public static final String CATEGORY_ID = "CATEGORY_ID" ;

    private ViewPager mPager;
    private CursorPagerAdapter mPagerAdapter;
    private String filter;
    private String mCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotations);


        Intent intent = getIntent();
        String mCategory = intent.getStringExtra(CATEGORY_KEY);
        filter = QuotationContract.COLUMN_CATEGORY + " = " + intent.getLongExtra(CATEGORY_ID,1);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new CursorPagerAdapter<QuotationFragment>(getSupportFragmentManager()
                ,QuotationFragment.class,null,null);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, HomeActivity.class));
                return true;

//            case R.id.action_previous:
//                // Go to the previous step in the wizard. If there is no previous step,
//                // setCurrentItem will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//                return true;
//
//            case R.id.action_next:
//                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
//                // will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {QuotationContract.COLUMN_ID,QuotationContract.COLUMN_TEXT,
                QuotationContract.COLUMN_AUTHOR, QuotationContract.COLUMN_FAVOURITE,
                 QuotationContract.COLUMN_READ, QuotationContract.COLUMN_CATEGORY};
        CursorLoader cursorLoader = new CursorLoader(this, QuotationProvider.CONTENT_URI_QUOTAIONS,
                projection, filter , null, null);
        mPagerAdapter.setProjection(projection);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPagerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPagerAdapter.swapCursor(null);

    }
}
