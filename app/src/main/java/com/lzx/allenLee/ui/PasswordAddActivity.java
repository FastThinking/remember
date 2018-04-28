package com.lzx.allenLee.ui;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzx.allenLee.R;
import com.lzx.allenLee.base.BaseActivity;
import com.lzx.allenLee.bean.MessageEvent;
import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.service.UserPasswordManager;
import com.lzx.allenLee.util.encryptionUtil.EncryptUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;

public class PasswordAddActivity extends BaseActivity {
    private Button btn_add;
    private Button btn_return;
    private EditText et_title, et_userName, et_password, et_des;
    private TextView tv_userId, tv_appTitle;
    private CheckBox cb_isLogin;
    private PasswordInfo passInfo;
    private String CMD = "INSERT";
    private String initUserName = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.password_add);

        this.initView();

        btn_add.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                savePasswordInfo();
                EventBus.getDefault().post(new MessageEvent());
            }
        });

        btn_return.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                PasswordAddActivity.this.finish();
            }
        });

        CMD = this.getIntent().getStringExtra("CMD");
        if (CMD == null || CMD.length() == 0) {

        } else if (CMD.equals("UPDATE")) {
            passInfo = (PasswordInfo) this.getIntent().getSerializableExtra("com.lzx.allenLee.ui.PasswordListActivity.PasswordInfo");
            if (passInfo == null) {
                Toast.makeText(this, "没有获取到参数。", Toast.LENGTH_LONG).show();
            } else {
                initUserName = passInfo.getUserName();
                showDetail(passInfo);
            }

        }
    }

    private void showDetail(PasswordInfo info) {
        tv_userId.setText(info.getUserId() == -1 ? "" : info.getUserId() + "");
        et_title.setText(info.getTitle() == null ? "" : info.getTitle());
        et_userName.setText(info.getUserName() == null ? "" : info.getUserName());
        et_password.setText(info.getPassword() == null ? "" : info.getPassword());
        et_des.setText(info.getDes() == null ? "" : info.getDes());
        cb_isLogin.setChecked(info.getIsLogin().equals("true") ? true : false);
        if (info.getUserName().equals("admin")) {
            cb_isLogin.setChecked(true);
            cb_isLogin.setEnabled(false);
        }
        tv_appTitle.setText(R.string.password_update);
        btn_add.setText(R.string.option_update);

    }

    private void savePasswordInfo() {

        if (checkInputCondition()) {
            if (CMD != null && CMD.equals("UPDATE")) {
                String userName = et_userName.getText().toString();
                if (initUserName.equals("admin") && !userName.equals("admin")) {
                    Toast.makeText(this, "不能修改admin账号名称！", Toast.LENGTH_LONG).show();
                } else {
                    if (updatePasswordInfor()) {
                        Toast.makeText(this, "修改成功！", Toast.LENGTH_LONG).show();
                        forwardToNextActivity();
                    } else {
                        Toast.makeText(this, "修改失败！", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                if (insertPasswordInfor()) {
                    Toast.makeText(this, "添加成功！", Toast.LENGTH_LONG).show();
                    forwardToNextActivity();
                } else {
                    Toast.makeText(this, "添加失败！", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(this, "输入不能为空！", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkInputCondition() {
        String title = et_title.getText().toString();
        String userName = et_userName.getText().toString();
        String password = et_password.getText().toString();
        String des = et_des.getText().toString();
        boolean isChecked = cb_isLogin.isChecked();
        if (title == null || title.length() == 0) {
            return false;
        } else if (userName == null || userName.length() == 0) {
            return false;
        } else if (password == null || password.length() == 0) {
            return false;
        } else {
            if (passInfo == null) {
                passInfo = new PasswordInfo();
            }
            passInfo.setTitle(title);
            passInfo.setDes(des);
            try {
                passInfo.setUserName(EncryptUtil.encrypt(userName));
                passInfo.setPassword(EncryptUtil.encrypt(password));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (isChecked) {
                passInfo.setIsLogin("true");
            } else {
                passInfo.setIsLogin("false");
            }
            return true;
        }
    }

    private boolean insertPasswordInfor() {
//		return AppServiceManager.getInstance(this).insertPassword(passInfo);
        return UserPasswordManager.getInstance().insertPassword(passInfo);

    }

    private boolean updatePasswordInfor() {
//		return AppServiceManager.getInstance(this).updatePassword(passInfo);

        return UserPasswordManager.getInstance().updatePassword(passInfo);

    }

    private void forwardToNextActivity() {
        this.finish();
    }

    @Override
    protected void initView() {
        btn_add = (Button) this.findViewById(R.id.btn_add);
        btn_return = (Button) this.findViewById(R.id.btn_return);
        et_title = (EditText) this.findViewById(R.id.et_title);
        et_userName = (EditText) this.findViewById(R.id.et_userName);
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_des = (EditText) this.findViewById(R.id.et_des);
        tv_userId = (TextView) this.findViewById(R.id.tv_userId);
        tv_appTitle = (TextView) this.findViewById(R.id.tv_appTitle);
        cb_isLogin = (CheckBox) this.findViewById(R.id.cb_isLogin);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub

    }


}
