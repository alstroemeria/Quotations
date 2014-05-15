package com.jackymok.quotations.app.ui;


import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.services.QuotationRequest;

public class HomeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mCityNames;
    private int[] mCityImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        mTitle = mDrawerTitle = getTitle();
        mCityNames = getResources().getStringArray(R.array.drawer_items);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.city_images);
        mCityImages = new int [typedArray.length()];
        for (int i = 0; i < typedArray.length(); ++i) {
            mCityImages[i] = typedArray.getResourceId(i, 0);
        }
        typedArray.recycle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mCityNames));
        mDrawerList.setOnItemClickListener(this);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        QuotationRequest quoteRequest = new QuotationRequest(getApplicationContext());
        quoteRequest.Fetch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


//    //================================================================================
//    // GRIDFRAGMENT
//    //================================================================================
//    @Override
//    public void onGridViewItemClicked(long id, String category) {
//        Intent intent = new Intent(this, QuotationActivity.class);
//        intent.putExtra(QuotationActivity.CATEGORY_ID, id);
//        intent.putExtra(QuotationActivity.CATEGORY_KEY, category);
//        startActivity(intent);
//    }
//
//    //================================================================================
//    // CATEGORYFRAGMENT
//    //================================================================================
//    @Override
//    public void onListViewItemClicked(long id, String category) {
//        Intent intent = new Intent(this, QuotationActivity.class);
//        intent.putExtra(QuotationActivity.CATEGORY_ID, id);
//        intent.putExtra(QuotationActivity.CATEGORY_KEY, category);
//        startActivity(intent);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        FragmentManager transaction = getSupportFragmentManager();

        if (position == 0){
                transaction.beginTransaction()
                        .replace(R.id.content_frame,new FeatureFragment(),"FEATURE_FRAGMENT")
                        .commit();
        }
        else {

            Fragment fragment = new SampleFragment();
            Bundle args = new Bundle();
            args.putInt(SampleFragment.ARG_IMAGE_RES, mCityImages[position]);
            args.putInt(SampleFragment.ARG_ACTION_BG_RES, R.drawable.ab_background);
            fragment.setArguments(args);


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(mCityNames[position]);

        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
//
//    private void selectItem(int position) {
//
//        FragmentManager transaction = getSupportFragmentManager();
//        switch (position){
//            default:
//            case 0:
////                transaction.beginTransaction()
////                        .replace(R.id.content_frame,new StaggeredGridFragment(),"STAGGERED_GRID_FRAGMENT")
////                        .commit();
//                transaction.beginTransaction()
//                        .replace(R.id.content_frame,new FeatureFragment(),"FEATURE_FRAGMENT")
//                        .commit();
//                break;
//            case 1:
//                transaction.beginTransaction()
//                        .replace(R.id.content_frame,new CategoryFragment(),"CATEGORY_FRAGMENT")
//                        .commit();
//                break;
//        }
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//       // setTitle(mMenuTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
}
