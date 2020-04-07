package com.easyway.fixinventory.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyway.fixinventory.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 2017/4/19.
 */

public class MyTextView extends LinearLayout {


    private final float mContentSize;
    private final float mTitleSize;
    @BindView(R.id.item_input_tv_title)
    TextView tvTitleName;
    @BindView(R.id.item_input_edt_content)
    TextView edtContent;

    private String mTitle;
    private String mContent;

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_input_my_text_view, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.input_test_view);
        ButterKnife.bind(this, view);

        mTitle = array.getString(R.styleable.input_test_view_input_title);
        mContent = array.getString(R.styleable.input_test_view_input_content);
        mTitleSize = array.getDimension(R.styleable.input_test_view_input_title_size,14);
        mContentSize = array.getDimension(R.styleable.input_test_view_input_title_size,14);


        tvTitleName.setTextSize(mTitleSize);
        edtContent.setTextSize(mContentSize);

        tvTitleName.setText(mTitle);
        edtContent.setText(mContent);

    }



    public void setText(String content) {
        edtContent.setText(content);
    }
    public void setEmpty() {
        edtContent.setText("");
    }

    public String getText() {
        return edtContent.getText().toString().trim();
    }

}
