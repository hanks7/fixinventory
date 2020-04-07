package com.hanks.frame.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanks.frame.R;


/**
 * com.xjx.core.view.LoadingDialog
 * 
 * @author YuanChao <br/>
 *         create at 2015年7月15日 上午11:59:56
 */
public class LoadingDialog extends Dialog {
	protected View contentView;
	protected TextView textView;

	/**
	 * @param context
	 * @param cancelable
	 * @param cancelListener
	 */
	public LoadingDialog(Context context, boolean cancelable,
						 OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	/**
	 * @param context
	 * @param theme
	 */
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	/**
	 * @param context
	 */
	public LoadingDialog(Context context) {
		super(context, R.style.LoadingDialogStyle);
		init(context);
	}

	public LoadingDialog(Context context, View contentView) {
		super(context, R.style.LoadingDialogStyle);
		this.contentView = contentView;
		init(context);
	}

	public LoadingDialog(Context context, int theme, View contentView) {
		super(context, theme);
		this.contentView = contentView;
		init(context);
	}

	public void init(Context context) {
		setCancelable(true);// 不可以用“返回键”取消
		if (contentView == null)
			contentView = LayoutInflater.from(context).inflate(
					R.layout.dialog_loading, null);
		setContentView(contentView, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		textView = (TextView) findViewById(R.id.tv_loading_text);
		setBackgroundColor(context.getResources().getColor(R.color.frame_trans60));
	}

	public void setBackgroundColor(int color) {
		GradientDrawable myGrad = (GradientDrawable) contentView
				.getBackground();
		if (myGrad != null)
			myGrad.setColor(color);
	}

	public LoadingDialog setText(String text) {
		textView.setText(text);
		return this;
	}

	public LoadingDialog setText(int strId) {
		textView.setText(this.getContext().getResources().getString(strId));
		return this;
	}

	public View getContentView() {
		return contentView;
	}

	public TextView getTextView() {
		return textView;
	}

	public void hide() {
		try {
			if (isShowing()) {
				dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
