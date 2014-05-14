package com.jackymok.quotations.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jackymok.quotations.app.R;
import com.jackymok.quotations.app.models.SLTextView;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.quotation, container, false);
        SLTextView textView = ((SLTextView) rootView.findViewById(R.id.fragment_quotation_text));
        ScrollView scrollView = ((ScrollView) rootView.findViewById(R.id.fragment_quotation_content));
        scrollView.setBackgroundColor(getResources().getColor(R.color.flatui_belize_hole));
        scrollView.setFillViewport(true);
        textView.setTypeFace("Lato-Hairline.ttf");
        textView.setTextColor(getResources().getColor(R.color.flatui_clouds));
        textView.setLetterSpacing(-3);
        textView.setLineSpacing(2, (float) 0.8);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); 
        textView.setText(mText);


        return rootView;
    }

}
