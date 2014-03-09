package com.jackymok.quotations.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackymok.quotations.app.R;

/**
 * Created by Jacky on 09/03/14.
 */
public class QuotationFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    private int mPageNumber;

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
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_quotation, container, false);
//        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                getString(R.string.title_template_step, mPageNumber +1));
        return rootView;
    }
    public int getPageNumber() {
        return mPageNumber;
    }
}
