package com.hanks.frame.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hanks.frame.R;
import com.hanks.frame.utils.Ulog;


/**
 * @author 侯建军
 * @data on 2017/12/21 15:06
 * @org www.hopshine.com
 * @function 请填写
 * @email 474664736@qq.com
 */
public class ProgressBarWebView extends WebView {
    /**
     * 加载进度
     */
    private ProgressBar progressBar;

    public ProgressBarWebView(Context context) {
        super(context);
        initWebViewSet();
    }

    public ProgressBarWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSet();
    }

    public ProgressBarWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebViewSet();
    }

    /**
     * 初始化WebView设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSet() {

        //首选创建一个进度条，我们这里创建的是一个横向的进度条
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        //设置该进度条的位置参数
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dip2px(3), 0, 0));
        //我们想要设置该进度条的背景样式
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.style_progress);
        //设置背景样式
        progressBar.setProgressDrawable(drawable);
        //调用本身的addView（其实是调用ViewManager里的方法，看源码）方法讲进度条添加到当前布局视图中
        progressBar.setProgressDrawable(drawable);
        addView(progressBar);

        progressBar.setMax(100);//设置加载进度最大值
        WebSettings webSettings = getSettings();
//        if (Build.VERSION.SDK_INT >= 19) {
//            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
//        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        }
        addJavascriptInterface(getContext(),"android");//*******************************这行代码非常重要否则无法html无法调用Android 的方法
        setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/


        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);//
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        setSaveEnabled(true);
        setKeepScreenOn(true);


        // 设置setWebChromeClient对象
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                mRxTextAutoZoom.setText(title);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //设置此方法可在WebView中打开链接，反之用浏览器打开
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!getSettings().getLoadsImagesAutomatically()) {
                    getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false; // doc上的注释为: True if the host application wants to handle the key event itself, otherwise return false(如果程序需要处理,那就返回true,如果不处理,那就返回false)
                // 我们这个地方返回false, 并不处理它,把它交给webView自己处理.
            }
        });
        setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                getContext().startActivity(intent);
            }
        });


        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && ProgressBarWebView.this.canGoBack()) {
                        ProgressBarWebView.this.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 设置的dp值
     * @return
     */
    public int dip2px(float dpValue) {
        float scale_px = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale_px + 0.5f);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        Ulog.i("WebView_url",url);
    }
}
