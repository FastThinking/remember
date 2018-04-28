package com.lzx.allenLee.ui;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ebt.m.commons.buscomponent.permission.RxPermissions;
import com.lzx.allenLee.R;
import com.lzx.allenLee.base.AppActivityManager;
import com.lzx.allenLee.base.BaseActivity;
import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.os.AppConstant;
import com.lzx.allenLee.util.UIHelper;

import java.io.InputStream;
import java.util.Random;

public class Act_start extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        initSysInfo();
                    } else {
                        UIHelper.makeToast(getApplicationContext(), "请允许相关权");
                        finish();
                    }
                });
        setContentView(R.layout.start);
        initView();
        init();
    }

    /**
     * 初始化系统数据库信息
     */
    private void initSysInfo() {

        //初始化应用程序常量
        AppConstant.init(this);
        //获取显示信息
        Global.getGlobal().getDisplayInfo(this);
        //初始化应用信息
        AppActivityManager.initAppData(this);
//		//初始化数据库system.db3
//		DataBaseManager.getInstance();

    }

    @Override
    protected void initView() {
        ImageView iv_img = (ImageView) this.findViewById(R.id.iv_img);
        iv_img.setImageDrawable(getLoadingDrawable());
    }

    private void init() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoActivity(Act_login.class);
                Act_start.this.finish();
            }
        }, 2 * 1000);
    }

    /**
     * 加载图片
     *
     * @return
     */
    private Drawable getLoadingDrawable() {
        Random rand = new Random();
        int num = rand.nextInt(21);
        String startPgn = "sys/start" + num + ".png";
        Drawable localDrawable;
        try {
            InputStream localInputStream = getAssets().open(startPgn);
            if (localInputStream == null) {
                startPgn = "sys/start0.png";
                localInputStream = getAssets().open(startPgn);
                localDrawable = Drawable.createFromStream(localInputStream, startPgn);
                localInputStream.close();
            } else {
                localDrawable = Drawable.createFromStream(localInputStream, startPgn);
                localInputStream.close();
            }
        } catch (OutOfMemoryError localOutOfMemoryError) {
            System.gc();
            localDrawable = null;
        } catch (Exception localException) {
            localDrawable = null;
        }
        return localDrawable;
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub

    }
}
