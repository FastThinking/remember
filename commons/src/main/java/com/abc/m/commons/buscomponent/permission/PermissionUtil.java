package com.abc.m.commons.buscomponent.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by wanglu on 2017/12/4.
 */

public class PermissionUtil {


    private static class SingletonHolder {
        private static final PermissionUtil INSTANCE = new PermissionUtil();
    }

    public static PermissionUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private PermissionUtil() {
    }

    public boolean checkNecessaryPermissions(Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        return rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void clearFragmentManagerInsideFragments(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        }
    }
}
