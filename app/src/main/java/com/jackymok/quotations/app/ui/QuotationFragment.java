package com.jackymok.quotations.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.provider.QuotationContract;

/**
 * Created by Jacky on 09/03/14.
 */
public class QuotationFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    private String mText;
    private String mAuthor;

    public QuotationFragment() {
    }

    public static QuotationFragment create(int mPageNumber) {
        QuotationFragment fragment = new QuotationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, mPageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mText = getArguments().getString(QuotationContract.COLUMN_TEXT);
        mAuthor = getArguments().getString(QuotationContract.COLUMN_AUTHOR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quotation, container, false);
        ((TextView) rootView.findViewById(R.id.fragment_quotation_text)).setText("\""+mText+"\"");
        ((TextView) rootView.findViewById(R.id.fragment_quotation_author)).setText("-"+ mAuthor);

        return rootView;
    }

}
