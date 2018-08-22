package com.lzx.allenLee.mvp;

import android.content.Context;
import android.support.annotation.CallSuper;

/**
 * @author XQ Yang
 * @date 2017/12/7  9:32
 */
public abstract class BasePresenter<V extends IView, M extends IModel> {
    protected V mView;
    protected M mModel;

    @CallSuper
    public void onAttachView(V view) {
        mView = view;
        if (mModel == null) {
            mModel = createModel();
        }
    }

    protected abstract M createModel();

    @CallSuper
    public void detachView() {
        if (mModel != null) {
            mModel.clearPool();
        }
        mModel = null;
        mView = null;
    }

    public Context getContext() {
        return mView.getContext();
    }

    public M getModel() {
        return mModel;
    }
}
