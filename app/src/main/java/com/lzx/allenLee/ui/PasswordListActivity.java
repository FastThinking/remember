package com.lzx.allenLee.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzx.allenLee.R;
import com.lzx.allenLee.base.BaseActivity;
import com.lzx.allenLee.bean.MessageEvent;
import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.os.AppConstant;
import com.lzx.allenLee.service.UserPasswordManager;
import com.lzx.allenLee.ui.adapter.AdapterPasswordList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PasswordListActivity extends BaseActivity implements OnItemClickListener {
    private EditText et_name;
    private ListView lv_password;
    private AdapterPasswordList adapter;
    private Button menu;
    // 为每个菜单定义一个标识
    final int MENU1ADD = 0x111;
    final int MENU2DELETE = 0x112;
    final int MENU3UPDATE = 0x113;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.password_list);
        this.initView();
        EventBus.getDefault().register(this);
        ItemOnLongClick1();
        showDataList("");
    }

    private void ItemOnLongClick1() {
        // 注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
        lv_password.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.add(0, MENU1ADD, 0, "添加");
                menu.add(0, MENU2DELETE, 0, "删除");
                menu.add(0, MENU3UPDATE, 0, "修改");

            }
        });

    }

    protected void onResume() {
        super.onResume();
    }

    private void showDataList(String likeTitle) {
        List<PasswordInfo> list = UserPasswordManager.getInstance().queryAllPassword(likeTitle);
        adapter = new AdapterPasswordList(PasswordListActivity.this, list);
        lv_password.setAdapter(adapter);
        Log.i("list.size()", list.size() + "");
    }

    private void refreshDataList(String likeTitle) {
        List<PasswordInfo> list = UserPasswordManager.getInstance().queryAllPassword(likeTitle);
        adapter.setList(list);
        Log.i("list.size()", list.size() + "");
    }

    // private boolean deletePassword(){
    // return
    // AppServiceManager.getInstance(PasswordListActivity.this).deletePassword(userId);
    // }

    // 每次创建上下文菜单时都会触发该方法
    @Override
    public void onCreateContextMenu(ContextMenu menu, View source, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU1ADD, 0, "添加");
        menu.add(0, MENU2DELETE, 0, "删除");
        menu.add(0, MENU3UPDATE, 0, "修改");
        // 将三个菜单项设为单选菜单项
        menu.setGroupCheckable(0, true, true);
        // 设置上下文菜单的标题、图标
        menu.setHeaderIcon(R.drawable.tools);
        menu.setHeaderTitle("菜单");
    }

    // 菜单项被单击时触发该方法。
    @Override
    public boolean onContextItemSelected(MenuItem mi) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) mi.getMenuInfo();
        int itemPosition = menuInfo.position;
        View view = menuInfo.targetView;
        switch (mi.getItemId()) {
            case MENU1ADD:
                mi.setChecked(true);
                gotoActivity(PasswordAddActivity.class);
                break;
            case MENU2DELETE:
                mi.setChecked(true);
                if (deletePassword(view)) {
                    refreshDataList("");
                }
                break;
            case MENU3UPDATE:
                mi.setChecked(true);
                goUpdatePassword(view, itemPosition);

                break;
        }
        return true;
    }

    /**
     * @param v
     * @return
     */
    private boolean deletePassword(View v) {
        TextView tv_userId = (TextView) (v.findViewById(R.id.tv_userId));
        String userId = tv_userId.getText().toString();
        return UserPasswordManager.getInstance().deletePassword(userId);

    }

    private void goUpdatePassword(View v, int position) {
        TextView tv_userId = (TextView) v.findViewById(R.id.tv_userId);
        TextView tv_title = (TextView) v.findViewById(R.id.tv_title);
        TextView tv_userName = (TextView) v.findViewById(R.id.tv_userName);
        TextView tv_password = (TextView) v.findViewById(R.id.tv_password);
        TextView tv_isLogin = (TextView) v.findViewById(R.id.tv_isLogin);
        TextView tv_des = (TextView) v.findViewById(R.id.tv_des);
        PasswordInfo info = new PasswordInfo();
        info.setUserId(Integer.parseInt(tv_userId.getText().toString()));
        info.setTitle(tv_title.getText().toString());
        info.setUserName(tv_userName.getText().toString());
        info.setPassword(tv_password.getText().toString());
        info.setIsLogin(tv_isLogin.getText().toString());
        info.setDes(tv_des.getText().toString());

        Intent intent = new Intent();
        intent.putExtra("com.lzx.allenLee.ui.PasswordListActivity.PasswordInfo", info);
        intent.putExtra("CMD", "UPDATE");
        intent.setClass(this, PasswordAddActivity.class);
        this.startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        goUpdatePassword(view, position);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordListActivity.this);
            builder.setTitle("退出");
            builder.setMessage("确定要退出吗？");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitApp();
                    // 结束当前Activity
                    ((Activity) PasswordListActivity.this).finish();
                }
            });
            builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(AppConstant.packageName);
        System.exit(0);
    }

    @Override
    protected void initView() {
        et_name = (EditText) this.findViewById(R.id.et_name);
        lv_password = (ListView) this.findViewById(R.id.lv_password);
        menu = (Button) this.findViewById(R.id.menu);
        lv_password.setOnItemClickListener(this);
        menu.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoActivity(PasswordAddActivity.class);
            }

        });
        et_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshDataList(s.toString());
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(getContext(), "刷新列表！", Toast.LENGTH_LONG).show();
        refreshDataList("");
    }

    ;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
