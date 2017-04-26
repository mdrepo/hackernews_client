package com.mrd.hackernews.ui_components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mrd.hackernews.R;

/**
 * Created by mayurdube on 26/04/17.
 */

public class HNTextView extends TextView {

    public HNTextView(Context context) {
        super(context);
    }

    public HNTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HNTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HNTextView);
            String fontName = a.getString(R.styleable.HNTextView_fontName);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
