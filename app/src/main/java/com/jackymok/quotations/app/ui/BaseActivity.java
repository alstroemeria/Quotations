package com.jackymok.quotations.app.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.utils.TypefaceSpan;

/**
 * Created by Jacky on 10/03/14.
 */
public class BaseActivity extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setActionBar("Q U O T A T I O N");

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


    private void setActionBar(String title) {
        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "Roboto-Light.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        // Update the action bar title with the TypefaceSpan instance
        if (android.os.Build.VERSION.SDK_INT >= 11){
            ActionBar actionBar = getActionBar();
            actionBar.setTitle(s);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }
}