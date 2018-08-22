package com.lzx.allenLee.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lzx.allenLee.AppApplication;
import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.mvp.IView;
import com.lzx.allenLee.util.ActivityUtil;

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivity, IView {
    protected Context mContext = null;
    protected AppApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myApplication = (AppApplication) getApplication();
        //添加Activity
        AppActivityManager.addActivity(this);
        //设置方向
        if (Global.getGlobal().supportLandscape)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置无标题
        ActivityUtil.setNoTitle(this);
        initUncaughtExceptionHandler();
        baseInit();
    }

    /**
     * 基础初始化 绑定黄油刀,  使用binding的复写 不绑
     */
    protected void baseInit() {
//        setContentView(contentViewId);
//        try {
//            ButterKnife.bind(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void initUncaughtExceptionHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        //注册crashHandler   
        crashHandler.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppActivityManager.removeActivity(this);
        super.onDestroy();
    }


    /**
     * 初始化页面布局
     */
    protected abstract void initView();

    /**
     * 初始话数据
     */
    protected abstract void initData();


    /**
     * 停止服务并结束所有的Activity退出应用
     */
    @Override
    public void isExitApp() {
        new AlertDialog.Builder(mContext)
                .setTitle("退出")
                .setMessage("确定要退出吗？")
                .setCancelable(false)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//						stopService();
                        myApplication.exitApp();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * 判断是否有网络连接,没有返回false
     */
    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否有网络连接，若没有，则弹出网络设置对话框，返回false
     */
    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    /**
     * 判断GPS定位服务是否开启
     */
    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断基站定位是否开启
     */
    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查内存卡可读
     */
    @Override
    public void checkMemoryCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            new AlertDialog.Builder(mContext)
                    .setTitle("检测内存卡")
                    .setMessage("请检查内存卡")
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_SETTINGS);
                                    mContext.startActivity(intent);
                                }
                            })
                    .setNegativeButton("退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();

                                }
                            }).create().show();
        }
    }

    /**
     * 打开网络设置对话框
     */
    public void openWirelessSet() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder
                .setTitle("网络设置")
                .setMessage("检查网络")
                .setPositiveButton("网络设置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                mContext.startActivity(intent);
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        dialogBuilder.show();
    }

    /**
     * 关闭键盘
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 返回上下文对象
     */
    @Override
    public Context getContext() {
        return mContext;
    }

    /**
     * 返回登录用户的SharedPreferences对象
     */
//	@Override
//	public SharedPreferences getLoginUserSharedPre() {
//		return preferences;
//	}

    /**
     * 获取用户在线状态
     */
//	@Override
//	public boolean getUserOnlineState() {
//		return false;
//	}

    /**
     * 设置用户在线状态
     */
//	@Override
//	public void setUserOnlineState(boolean isOnline) {
//
//	}


    /**
     * 从当前activity跳转到目标activity,<br>
     * 如果目标activity曾经打开过,就重新展现,<br>
     * 如果从来没打开过,就新建一个打开
     *
     * @param cls
     */
    public void gotoExistActivity(Class<?> cls) {
        Intent intent;
        intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    /**
     * 新建一个activity打开
     *
     * @param cls
     */
    public void gotoActivity(Class<?> cls) {
        Intent intent;
        intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * 通用消息提示
     *
     * @param resId
     */
    public void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 通用消息提示
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 从资源获取字符串
     *
     * @param resId
     * @return
     */
    public String getStr(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 从EditText 获取字符串
     *
     * @param editText
     * @return
     */
    public String getStr(EditText editText) {
        return editText.getText().toString();
    }

    @Override
    public void inited() {

    }
}
