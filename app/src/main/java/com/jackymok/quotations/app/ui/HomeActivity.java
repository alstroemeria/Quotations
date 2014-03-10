package com.jackymok.quotations.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.jackymok.quotations.app.R;

public class HomeActivity extends BaseActivty implements CategoryFragment.onListViewItemClickedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager transaction = getSupportFragmentManager();
        transaction.beginTransaction()
                .replace(R.id.content_frame,new CategoryFragment(),"CATEGORY_FRAGMENT")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListViewItemClicked(long id, String category) {
        Intent intent = new Intent(this, QuotationActivity.class);
        intent.putExtra(QuotationActivity.CATEGORY_ID, id);
        intent.putExtra(QuotationActivity.CATEGORY_KEY,category);
        startActivity(intent);
    }
}
