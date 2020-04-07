package com.hanks.frame.utils.glide;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hanks.frame.R;
import com.hanks.frame.utils.UtilDisplay;


/**
 * Glide工具类
 * Created by houjianjun on 17/4/21.
 */
public class UtilGlide {
    /**
     * 等待的图片
     */
    private static final int placeholderPic = R.mipmap.rc_image_error;
    /**
     * 出错的图片
     */
    private static final int errorPic = R.mipmap.rc_image_error;

    /**
     * ******************************************************************
     * //******************************************************************
     * //***请不要用 application 的 context 去替代 activity的 context*******
     * //******************************************************************
     * //******************************************************************
     * <p>
     * 使用Glide加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, final ImageView imageView) {
        try {
            Glide.with(context)
                    .load(getStringEND(url))
                    .asBitmap()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                    .placeholder(placeholderPic).error(errorPic)
                    .into(new MyBitmapImageViewTarget(imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Glide加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImgNomal(Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(getStringEND(url))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .placeholder(placeholderPic).error(errorPic)
                .crossFade()
                .into(imageView);
    }

    /**
     * 使用Glide加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImgCeterCrop(Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(getStringEND(url))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .placeholder(placeholderPic).error(errorPic)
                .crossFade()
                .into(imageView);
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, final ImageView imageView) {
        Glide.with(context)
                .load(getStringEND(imageUrl))
                .fitCenter()
                .placeholder(placeholderPic).error(errorPic)
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImage(final Context context, String url, final ImageView imageView) {
        loadRoundImage(context, url, imageView, 10);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param url
     * @param imageView
     * @param radius    圆角弧度
     */
    public static void loadRoundImage(final Context context, String url, final ImageView imageView, final int radius) {
        Glide.with(context)
                .load(getStringEND(url))
                .asBitmap()
                .centerCrop()
                .placeholder(placeholderPic)
                .error(errorPic)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(UtilDisplay.dp2px(context, radius)); //设置圆角弧度
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 显示圆形图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImgForIvHead(final Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(getStringEND(url))
                .asBitmap() //这句不能少，否则下面的方法会报错
                .centerCrop()
                .placeholder(placeholderPic).error(errorPic)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public static void loadImgForIvHeadWithBorder(Context context, String url, final ImageView imageView) {

        Glide.with(context)
                .load(url)
                .placeholder(placeholderPic).error(errorPic)
                .crossFade()
                .transform(new GlideCircleTransform(context, 2, context.getResources()
                        .getColor(android.R.color.white)))
                .into(imageView);
    }


    /**
     * 加载图片,需要设置默认图片
     *
     * @param context
     * @param url
     * @param resId
     * @param imageView
     */
    public static void loadImg(Context context, String url, int resId, ImageView imageView) {
        Glide.with(context).load(getStringEND(url)).centerCrop().placeholder(resId).crossFade().into(imageView);
    }

    /**
     * 加载图片,需要设置默认图片,错误图片
     *
     * @param context
     * @param url
     * @param drawable
     * @param imageView
     */
    public static void loadImg(Context context, String url, Drawable drawable, ImageView imageView) {
        Glide.with(context).load(getStringEND(url)).placeholder(drawable).error(drawable).into(imageView);
    }

    /**
     * (1)
     * 显示图片Imageview
     *
     * @param context  上下文
     * @param url      图片链接
     * @param imgeview 组件
     */
    public static void showImageView(Context context, String url,
                                     ImageView imgeview) {
        Glide.with(context).load(url)// 加载图片
                // 设置错误图片
                .crossFade()// 设置淡入淡出效果，默认300ms，可以传参
                .placeholder(placeholderPic).error(errorPic)// 设置占位图
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .into(imgeview);
    }

    /**
     * (1)
     * 显示图片Imageview
     *
     * @param context  上下文
     * @param errorimg 错误的资源图片
     * @param url      图片链接
     * @param imgeview 组件
     */
    public static void showImageView(Context context, int errorimg, String url,
                                     ImageView imgeview) {
        Glide.with(context).load(getStringEND(url))// 加载图片
                .error(errorimg)// 设置错误图片
                .crossFade()// 设置淡入淡出效果，默认300ms，可以传参
                .placeholder(errorimg)// 设置占位图
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .into(imgeview);

    }

    /**
     * （2）
     * 获取到Bitmap---不设置错误图片，错误图片不显示
     *
     * @param context
     * @param imageView
     * @param url
     */

    public static void showImageViewGone(Context context,
                                         final ImageView imageView, String url) {
        Glide.with(context).load(getStringEND(url)).asBitmap()

                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {

                        imageView.setVisibility(View.VISIBLE);
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);
                        imageView.setImageDrawable(bd);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setVisibility(View.GONE);
                    }

                });

    }

    /**
     * （3）
     * 设置RelativeLayout
     * <p>
     * 获取到Bitmap
     *
     * @param context
     * @param errorimg
     * @param url
     * @param bgLayout
     */

    public static void showImageView(Context context, int errorimg, String url,
                                     final RelativeLayout bgLayout) {
        Glide.with(context).load(getStringEND(url)).asBitmap().error(errorimg)// 设置错误图片

                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .placeholder(errorimg)// 设置占位图
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        bgLayout.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        bgLayout.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    /**
     * （4）
     * 设置LinearLayout
     * <p>
     * 获取到Bitmap
     *
     * @param context
     * @param errorimg
     * @param url
     * @param bgLayout
     */

    public static void showImageView(Context context, int errorimg, String url,
                                     final LinearLayout bgLayout) {
        Glide.with(context).load(getStringEND(url)).asBitmap().error(errorimg)// 设置错误图片

                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .placeholder(errorimg)// 设置占位图
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        bgLayout.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        bgLayout.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    /**
     * （5）
     * 设置FrameLayout
     * <p>
     * 获取到Bitmap
     *
     * @param context
     * @param errorimg
     * @param url
     * @param frameBg
     */

    public static void showImageView(Context context, int errorimg, String url,
                                     final FrameLayout frameBg) {
        Glide.with(context).load(getStringEND(url)).asBitmap().error(errorimg)// 设置错误图片

                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .placeholder(errorimg)// 设置占位图
                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        frameBg.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        frameBg.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    /**
     * （6）
     * 获取到Bitmap 高斯模糊         RelativeLayout
     *
     * @param context
     * @param errorimg
     * @param url
     * @param bgLayout
     */

    public static void showImageViewBlur(Context context, int errorimg,
                                         String url, final RelativeLayout bgLayout) {
        Glide.with(context).load(url).asBitmap().error(errorimg)
                // 设置错误图片

                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                // 缓存修改过的图片
                .placeholder(errorimg)
                .centerCrop()
                .transform(new GlideBlurTransformation(context))// 高斯模糊处理
                // 设置占位图

                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);

                        bgLayout.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        bgLayout.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    /**
     * （7）
     * 获取到Bitmap 高斯模糊 LinearLayout
     *
     * @param context
     * @param errorimg
     * @param url
     * @param bgLayout
     */

    public static void showImageViewBlur(Context context, int errorimg,
                                         String url, final LinearLayout bgLayout) {
        Glide.with(context).load(getStringEND(url)).asBitmap().error(errorimg)
                // 设置错误图片

                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                // 缓存修改过的图片
                .placeholder(errorimg)
                .transform(new GlideBlurTransformation(context))// 高斯模糊处理
                // 设置占位图

                .into(new SimpleTarget<Bitmap>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onResourceReady(Bitmap loadedImage,
                                                GlideAnimation<? super Bitmap> arg1) {
                        BitmapDrawable bd = new BitmapDrawable(loadedImage);
                        bgLayout.setBackground(bd);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        // TODO Auto-generated method stub
                        super.onLoadFailed(e, errorDrawable);

                        bgLayout.setBackgroundDrawable(errorDrawable);
                    }

                });

    }

    /**
     * （8）
     * 显示图片 圆角显示  ImageView
     *
     * @param context  上下文
     * @param errorimg 错误的资源图片
     * @param url      图片链接
     * @param imgeview 组件
     */
    public static void showImageViewToCircle(Application context, int errorimg,
                                             String url, ImageView imgeview) {
        Glide.with(context).load(getStringEND(url))
                // 加载图片
                .error(errorimg)
                // 设置错误图片
                .crossFade()
                // 设置淡入淡出效果，默认300ms，可以传参
                .placeholder(errorimg)
                // 设置占位图
                .transform(new GlideCircleTransform(context))//圆角
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存修改过的图片
                .into(imgeview);

    }

    public static void loadImageNoDisk(Context context, String url, final ImageView imageView) {
        Glide.with(context)
                .load(url)

                .asBitmap() //加载一张静态图片，不需要Glide自动帮我判断它到底是静图还是GIF图。.asGif()表示加载gif图片
                .placeholder(placeholderPic).error(errorPic)   //占位图片  当当前加载图片未显示时临时显示的图片
                .error(placeholderPic)     //当出现网路错误或加载出错时显示的图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)  //禁用掉Glide的硬盘缓存功能，DiskCacheStrategy.NONE 表示当前图片不缓存
//                                                           DiskCacheStrategy.NONE： 表示不缓存任何内容。
//                                                           DiskCacheStrategy.SOURCE： 表示只缓存原始图片。
//                                                           DiskCacheStrategy.RESULT： 表示只缓存转换过后的图片（默认选项）。
//                                                           DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
//                .override(100, 100)   给图片指定一个固定的大小，//一般Glide会自动判断ImageView的大小
//                .skipMemoryCache(true) //表示禁用掉Glide的内存缓存功能

                .into(imageView);

        /**
         * 1.  .crossFade(500)  Glide默认是包含淡入淡出动画的时间为300ms(毫秒),我们可以修改这个动画的时间
         * 2.  .dontAnimate()    不希望有淡入淡出动画时
         *3.  加载本地视频缩略图

         网络上很多文章上都是从一篇译文里面拷贝过来的,里面说Glide可以加载本地视频,但是那篇译文漏翻译一句了,
         Glide只会加载本地视频的第一帧,也就是缩略图,而且其实加载缩略图的时候也无需转化为Uri,直接把File丢进去就行了
         mVideoFile = new File(Environment.getExternalStorageDirectory(), "xiayu.mp4");
         Glide.with(this).load(mVideoFile).placeholder(R.mipmap.place).error(R.mipmap.icon_photo_error).into(mIv);
         *
         * 4.内存缓存最大空间
         Glide的内存缓存其实涉及到比较多的计算,这里就介绍最重要的一个参数,就是内存缓存最大空间
         内存缓存最大空间(maxSize)=每个进程可用的最大内存 * 0.4
         (低配手机的话是: 每个进程可用的最大内存 * 0.33)
         *
         * 磁盘缓存大小 250M
         *
         * 磁盘缓存目录: 项目/cache/image_manager_disk_cache
         *
         *清除所有缓存
         清除所有内存缓存(需要在Ui线程操作)
         Glide.get(this).clearMemory();
         清除所有磁盘缓存(需要在子线程操作)
         Glide.get(MainActivity.this).clearDiskCache();
         注:在使用中的资源不会被清除
         *
         *
         */


    }


    /**
     * 画圆
     */
    public static class CircleTransform extends BitmapTransformation {
        public CircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * 补全图片地址
     *
     * @param url
     * @return
     */
    @NonNull
    public static String getStringEND(String url) {
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        return url;
    }


}

