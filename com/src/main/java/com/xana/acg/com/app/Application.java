package com.xana.acg.com.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.xana.acg.com.R;
import com.xana.acg.com.widget.BlurTransformation;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Application extends android.app.Application {
    private static Application instance;

    private final List<Activity> activityList = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private Date date;

    public static void setImage(View view, String uri){
        Glide.with(view.getContext())
                .load(uri)
                .bitmapTransform(new BlurTransformation(view.getContext()))
                .into(new ViewTarget<View, GlideDrawable>(view) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        view.setBackground(resource.getCurrent());
                    }
                });

    }

    public static synchronized MediaPlayer getMediaPlayer(){
        if(instance.mediaPlayer==null)
            instance.mediaPlayer = new MediaPlayer();
        return instance.mediaPlayer;
    }

    public static synchronized Date getDate(){
        if(instance.date==null)
            instance.date = new Date();
        return instance.date;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                activityList.add(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                activityList.remove(activity);
            }
        });
    }

    public void finishAll(){
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
        showLogin(this);

    }
    protected void showLogin(Context context){

    }

    /**
     * ??????????????????
     * @return Application
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * ???????????????????????????
     * @return ??????APP????????????????????????
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * ???????????????????????????????????????
     *
     * @return ????????????
     */
    public static File getPortraitTmpFile() {
        // ?????????????????????????????????
        File dir = new File(getCacheDirFile(), "portrait");
        // ?????????????????????????????????
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();

        // ?????????????????????????????????
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // ????????????????????????????????????????????????
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }

    /**
     * ?????????????????????????????????
     * @param isTmp ???????????????????????? True??????????????????????????????????????????
     * @return ?????????????????????
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // aar
        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * ????????????Toast
     * @param msg ?????????
     */
    public static void showToast(final String msg) {
        // ????????????????????????????????????show??????
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ????????????Toast
     * @param msgId ??????????????????????????????
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }

}
