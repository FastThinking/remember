package com.lzx.allenLee.mvp;

/**
 * Created by allen on 2018/8/22 15:54.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.ref.WeakReference;

import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

public abstract class BaseFragment extends Fragment implements IView {
    @Nullable
    private Activity mActivity;
    @Nullable
    private WeakReference mRootView;
    private Unbinder mUnbinder;
    private boolean inited;
    private static final int FRAGMENT_REQUEST_REFRESH_KEY = 54541;

    @Nullable
    protected final Activity getMActivity() {
        return this.mActivity;
    }

    protected final void setMActivity(@Nullable Activity var1) {
        this.mActivity = var1;
    }

    @Nullable
    protected final WeakReference getMRootView() {
        return this.mRootView;
    }

    protected final void setMRootView(@Nullable WeakReference var1) {
        this.mRootView = var1;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FRAGMENT_REQUEST_REFRESH_KEY && resultCode == Activity.RESULT_OK) {
            this.onRefresh();
        }

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = (Activity) this.getActivity();
        this.init();
    }


    /**
     * mFragmentView创建完成后,初始化具体的view 只会调用一次
     */
    protected abstract void initView(View rootView, Bundle savedInstanceState);

    /**
     * 初始话事件监听
     */
    protected abstract void initListener();

    /**
     * view 创建之前 获取intent数据等
     */
    protected abstract void init();

    /**
     * 请求回来刷新时调用
     */
    void onRefresh() {
    }
}
