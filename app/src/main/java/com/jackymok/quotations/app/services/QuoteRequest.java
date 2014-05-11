package com.jackymok.quotations.app.services;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.jackymok.quotations.app.provider.QuotationProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jacky on 07/05/14.
 */
public class QuoteRequest {
    Context context;
    VolleyRequest volleyRequest;

    public QuoteRequest(Context context){
        this.context = context;
        volleyRequest = new VolleyRequest();
    }

    public void Fetch(){
        volleyRequest.Fetch("http://quotey.herokuapp.com/api/v1/quotes",new QuoteParser() );
    }


    class QuoteParser implements VolleyCallback{
        @Override
        public void jsonResponse(JSONObject response) {
            Log.d("Volley", response.toString());
            try {
                JSONArray quotes = response.getJSONArray("quotes");
                ContentValues[] values = new ContentValues[quotes.length()];
                JSONObject quote;
                for (int i = 0; i < quotes.length(); i++) {
                    values[i] = new ContentValues();
                    quote = quotes.getJSONObject(i);
                    values[i].put("_id", quote.getInt("id"));
                    values[i].put("text", quote.getString("text"));
                    values[i].put("category", quote.getInt("category_id"));
                    values[i].put("author", quote.getInt("author_id"));
                }
                context.getContentResolver().bulkInsert(QuotationProvider.CONTENT_URI_QUOTAIONS,values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
