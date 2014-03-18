package com.jackymok.quotations.app.models;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jackymok.quotations.app.utils.FontFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacky on 15/03/14.
 */
public class SLTextView extends TextView {
    private boolean mNegativeLineSpacing = false;
    private float letterSpacing = 0; // normal
    private CharSequence originalText = "";
    private Context mContext;
    TextLinkClickListener mListener;

    public SLTextView(Context context) {
        super(context);
        mContext = context;
    }

    public SLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public SLTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    /* clickable links */
    public interface TextLinkClickListener {
        public void onTextLinkClick(View textView, String clickedString);
    }

    public void setOnTextLinkClickListener(TextLinkClickListener newListener) {
        mListener = newListener;
    }

    public class InternalURLSpan extends ClickableSpan {
        private String clickedSpan;

        public InternalURLSpan (String clickedString) {
            clickedSpan = clickedString;
        }

        @Override
        public void onClick(View textView) {
            mListener.onTextLinkClick(textView, clickedSpan.replace("\u00A0", ""));
        }
    }


    /* line spacing */
    @Override
    public void setLineSpacing(float add, float mult) {
        mNegativeLineSpacing = add < 0 || mult < 1;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mNegativeLineSpacing) {
            Layout layout = getLayout();
            int truncatedHeight = layout.getLineDescent(layout.getLineCount()-1);
            if (truncatedHeight < 0) truncatedHeight = truncatedHeight * -1;
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + truncatedHeight);
        }
    }

    public void setTypeFace(String typeface){
        Typeface font = FontFactory.getInstance().getFont(mContext, typeface);
        this.setTypeface(font);
    }


    /* letter spacing */
    public float getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        applyLetterSpacing();
    }

    @Override
    public CharSequence getText() {
        return originalText;
    }

    private void applyLetterSpacing() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < originalText.length(); i++) {
            builder.append(originalText.charAt(i));
            if (i+1 < originalText.length()) {
                builder.append("\u00A0");
            }
        }

        SpannableString finalText = new SpannableString(builder.toString());
        float letterSpacingBuf = (letterSpacing == -1) ? 0.1f : letterSpacing+1;
        if (builder.toString().length() > 1) {
            for (int i = 1; i < builder.toString().length(); i+=2) {
                finalText.setSpan(new ScaleXSpan(letterSpacingBuf/10), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Pattern hyperLinksPattern = Pattern.compile("([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])", Pattern.CASE_INSENSITIVE);
            Matcher matcher = hyperLinksPattern.matcher(originalText);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                int adjustedStart = (start == 0) ? 0 : (start * 2 - 1);
                int adjustedEnd = (end == 0) ? 0 : (end * 2 - 1);

                CharSequence textSpan =  finalText.subSequence(adjustedStart, adjustedEnd);
                InternalURLSpan span = new InternalURLSpan(textSpan.toString());
                finalText.setSpan(span, adjustedStart, adjustedEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        super.setText(finalText, BufferType.SPANNABLE);
    }
}
