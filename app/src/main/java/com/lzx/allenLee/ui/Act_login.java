package com.lzx.allenLee.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.m.commons.buscomponent.permission.RxPermissions;
import com.lzx.allenLee.R;
import com.lzx.allenLee.base.BaseActivity;
import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.service.UserPasswordManager;
import com.lzx.allenLee.util.ActivityUtil;
import com.lzx.allenLee.util.AlertDialogForInput;
import com.lzx.allenLee.util.AlertDialogForInput.InputValidityListener;
import com.lzx.allenLee.util.UIHelper;
import com.lzx.allenLee.util.databaseUtil.DataBaseManager;
import com.lzx.allenLee.util.encryptionUtil.Coder;
import com.lzx.allenLee.util.encryptionUtil.EncryptUtil;
import com.lzx.allenLee.util.fileManager.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Act_login extends BaseActivity {
    private Button bt_login;
    private Button bt_exit;
    private CheckBox cb_rememberPwd;
    private EditText et_userName;
    private EditText et_password;
    private LinearLayout ll_login;
    private Handler mHandler;
    private TextView tv_num;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        // 初始化数据库system.db3
                        DataBaseManager.getInstance();
                    } else {
                        UIHelper.makeToast(getApplicationContext(), "请允许相关权");
                    }
                });
    }

    protected void initView() {

        // 设置背景图片
        this.ll_login = ((LinearLayout) findViewById(R.id.ll_login));
        this.ll_login.setBackgroundResource(R.drawable.default_bg);

        bt_login = (Button) this.findViewById(R.id.bt_login);
        bt_exit = (Button) this.findViewById(R.id.bt_exit);
        cb_rememberPwd = (CheckBox) this.findViewById(R.id.cb_rememberPwd);
        et_userName = (EditText) this.findViewById(R.id.et_userName);
        et_password = (EditText) this.findViewById(R.id.et_password);
        tv_num = (TextView) this.findViewById(R.id.tv_num);
        tv_num.setText("版本号：" + ActivityUtil.getSoftwareVersion(this));
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLogin()) {
                    ActivityUtil.DirectToActivity(Act_login.this, PasswordListActivity.class);
                    Act_login.this.finish();
                } else {
                    Toast toast = Toast.makeText(Act_login.this, "验证失败！", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

        });
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExitApp();
            }

        });

    }

    /**
     * 检查登录是否成功，从本地数据库查询账号信息
     *
     * @return
     */
    private boolean checkLogin() {
        boolean result = false;
        String userName = et_userName.getText().toString();
        String password = et_password.getText().toString();
        // result =
        // AppServiceManager.getInstance(Act_login.this).userCheck(EncryptUtil.encrypt(userName),
        // EncryptUtil.encrypt(password));
        try {
            result = UserPasswordManager.getInstance().userCheck(EncryptUtil.encrypt(userName),
                    EncryptUtil.encrypt(password));
        } catch (FileNotFoundException e) {
            Toast.makeText(getBaseContext(), "没有找到key文件。", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            alertSettingkey();
        }

        return result;
    }

    private void alertSettingkey() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.input_key, null);
        final EditText userKey = (EditText) contentView.findViewById(R.id.et_userKey);
        AlertDialogForInput dialog = new AlertDialogForInput(mContext, new InputValidityListener() {

            @Override
            public boolean validity() {
                String key = userKey.getText().toString().trim();
                // TODO:空格特殊字符校验
                if (TextUtils.isEmpty(key)) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public void onValidityOk() {
                // write key to key.txt
                String key = userKey.getText().toString().trim();
                FileUtil.createFilePath(new File(Global.keyDir));
                try {
                    FileUtil.writeSdcardFile(Global.keyPath, Coder.encryptBASE64(key.getBytes()));
                    Toast.makeText(getBaseContext(), "已为您创建专属的key文件。", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        dialog.setTitle("设置密钥");
        dialog.setContentView(contentView);
        dialog.show();
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub

    }

}