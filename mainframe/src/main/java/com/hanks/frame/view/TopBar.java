package com.hanks.frame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanks.frame.R;
import com.hanks.frame.base.HjjActivity;

/**
 * com.hanks.frame.view.TopBar
 */
public class TopBar extends RelativeLayout {

    ImageView backButton;

    TextView topTitle;

    ImageView ivTopRight;

    TextView tvTopRight;

    TextView tvTopLeft;


    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_toolbar, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.search_view);
        backButton = (ImageView) view.findViewById(R.id.btn_back);
        topTitle = (TextView) view.findViewById(R.id.tv_topTitle);
        ivTopRight = (ImageView) view.findViewById(R.id.btn_top_right);
        tvTopRight = (TextView) view.findViewById(R.id.tv_top_right);
        tvTopLeft = (TextView) view.findViewById(R.id.tv_top_left);
        backButton.setVisibility(array.getBoolean(R.styleable.search_view_is_has_back, true) ? VISIBLE : GONE);
        topTitle.setText(array.getString(R.styleable.search_view_title));
        ivTopRight.setImageDrawable(array.getDrawable(R.styleable.search_view_right_iv));
        tvTopRight.setText(array.getString(R.styleable.search_view_bar_right_tv));
        tvTopLeft.setText(array.getString(R.styleable.search_view_bar_left_tv));
        setOnTranslucent(array.getBoolean(R.styleable.search_view_is_chengjin, true));
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof HjjActivity)
                    ((HjjActivity) getContext()).onBackPressed();
            }
        });
    }


    public void setTitle(@StringRes int resId) {
        if (topTitle != null)
            topTitle.setText(resId);
    }

    public void setTitle(CharSequence title) {
        if (topTitle != null) {
            topTitle.setText(title.toString());
        }
    }

    public String getTitle() {
        return topTitle.getText().toString().trim();
    }

    /**
     * 设置右边图片
     *
     * @param resId
     */
    public void setIvRight(@DrawableRes int resId) {
        ivTopRight.setImageResource(resId);
    }

    /**
     * 设置左边图片
     *
     * @param resId
     */
    public void setIvBackButton(@DrawableRes int resId, OnClickListener clickListener) {
        backButton.setImageResource(resId);
        backButton.setOnClickListener(clickListener);
    }

    /**
     * 右边图片按钮点击事件
     *
     * @param clickListener
     */
    public void setIvRightClick(OnClickListener clickListener) {
        ivTopRight.setVisibility(VISIBLE);
        ivTopRight.setOnClickListener(clickListener);
    }
    /**
     * 右边图片按钮点击事件
     *
     * @param clickListener
     */
    public void setIvRightClick(@DrawableRes int resId,OnClickListener clickListener) {
        ivTopRight.setVisibility(VISIBLE);
        ivTopRight.setImageResource(resId);
        ivTopRight.setOnClickListener(clickListener);
    }


    /**
     * 右边文字按钮点击事件
     *
     * @param str
     * @param clickListener
     */
    public void setTvRight(String str, OnClickListener clickListener) {
        tvTopRight.setText(str);
        tvTopRight.setVisibility(VISIBLE);
        tvTopRight.setOnClickListener(clickListener);
    }

    /**
     * 右边文字按钮点击事件
     *
     * @param str
     * @param clickListener
     */
    public void setTvLeft(String str, OnClickListener clickListener) {
        tvTopLeft.setText(str);
        tvTopLeft.setVisibility(VISIBLE);
        tvTopLeft.setOnClickListener(clickListener);
    }

    /**
     * 右边文字按钮点击事件
     *
     * @param clickListener
     */
    public void setTvRightClick(OnClickListener clickListener) {
        tvTopRight.setVisibility(VISIBLE);
        tvTopRight.setOnClickListener(clickListener);
    }


    /**
     * 返回按钮监听
     *
     * @param onClickListener
     */
    public void setBackClick(OnClickListener onClickListener) {
        backButton.setOnClickListener(onClickListener);
    }


    /**
     * 设置是否需要渐变
     */
    public void setNeedTranslucent(int transAlpha) {
        setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        getBackground().setAlpha(transAlpha);
    }

    /**
     * 启用 透明状态栏(重写此方法可以取消透明)
     */
    protected void setOnTranslucent(boolean translucent) {
        if (translucent && getContext() instanceof HjjActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            setPadding(0, getContext().getResources().getDimensionPixelSize(resourceId), 0, 0);
        }
    }
}

