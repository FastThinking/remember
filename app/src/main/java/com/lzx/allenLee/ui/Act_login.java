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
import android.widget.ImageView;
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
import com.lzx.allenLee.util.fingerprint.AppUtils;
import com.lzx.allenLee.util.fingerprint.DeviceUtils;
import com.lzx.allenLee.util.fingerprint.FingerPrintException;
import com.lzx.allenLee.util.fingerprint.FingerprintManagerUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Act_login extends BaseActivity {
    @BindView(R.id.tvFingerprint)
    TextView tvFingerprint;
    @BindView(R.id.ivState)
    ImageView ivState;
    private Button bt_login;
    private Button bt_exit;
    private CheckBox cb_rememberPwd;
    private EditText et_userName;
    private EditText et_password;
    private LinearLayout ll_login;
    private Handler mHandler;
    private TextView tv_num;

    public final static String TYPE = "type";
    public final static String LOGIN = "login";//登陆验证场景
    public final static String SETTING = "setting";
    public final static String LOGIN_SETTING = "login_setting";//登陆后的引导设置
    public final static String CLEAR = "clear";

    private String mType;
    public static boolean isShow;
    /**
     * 是否支持指纹验证
     */
    private boolean mIsSupportFingerprint;
    private boolean isInAuth = false;

    private FingerprintManagerUtil mFingerprintManagerUtil;
    private FingerPrintTypeController mFingerPrintTypeController;
    private ArrayList<String> methodOrderArrayList;
    private String mBeginAuthenticateMethodName;
    private Map<String, String> exceptionTipsMappingMap;
    private Map<String, String> mi5TipsMappingMap;
    boolean isFingerprintLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
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
        initialize();
    }

    @Override
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

    @OnClick(R.id.tvFingerprint)
    public void onViewClicked() {
        if (isFingerprintLogin) {
            stopFingerprintListen();
            stopAnim();
        } else {
            if (mIsSupportFingerprint) {
                beginAuthenticate();
            } else {
                mFingerPrintTypeController.onAuthenticationError(exceptionTipsMappingMap.get(LOGIN));
            }
        }
    }


    protected void initialize() {
        mType = getIntent().getStringExtra(TYPE);
        if (TextUtils.isEmpty(mType)) {
            mType = LOGIN;
        }
        mFingerprintManagerUtil = new FingerprintManagerUtil(this, () -> beginAuthAnim(), new MyAuthCallbackListener());
        mIsSupportFingerprint = mFingerprintManagerUtil.isSupportFingerprint();
        mFingerPrintTypeController = new FingerPrintTypeController();

        methodOrderArrayList = new ArrayList<>();

        //普通异常情况提示
        exceptionTipsMappingMap = new HashMap<>();
        exceptionTipsMappingMap.put(SETTING, getString(R.string.fingerprint_no_support_fingerprint_gesture));
        exceptionTipsMappingMap.put(LOGIN_SETTING, getString(R.string.fingerprint_no_support_fingerprint_gesture));
        exceptionTipsMappingMap.put(CLEAR, null);
        exceptionTipsMappingMap.put(LOGIN, getString(R.string.fingerprint_no_support_fingerprint_account));

        //小米5乱回调生命周期的异常情况提示
        mi5TipsMappingMap = new HashMap<>();
        mi5TipsMappingMap.put(SETTING, getString(R.string.tips_mi5_setting_open_close_error));
        mi5TipsMappingMap.put(LOGIN_SETTING, getString(R.string.tips_mi5_login_setting_error));
        mi5TipsMappingMap.put(CLEAR, getString(R.string.tips_mi5_setting_open_close_error));
        mi5TipsMappingMap.put(LOGIN, getString(R.string.tips_mi5_login_auth_error));

        initByType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShow = true;
        mIsSupportFingerprint = mFingerprintManagerUtil.isSupportFingerprint();
        //回来的时候自动调起验证
        if (isInAuth) {
            initByType();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFingerprintListen();
        isInAuth = mFingerprintManagerUtil != null && mFingerprintManagerUtil.getIsInAuth();
        methodOrderArrayList.add(AppUtils.getMethodName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isShow = false;
        methodOrderArrayList.clear();
    }

    private void stopFingerprintListen() {
        if (mFingerprintManagerUtil != null) {
            mFingerprintManagerUtil.stopsFingerprintListen();
        }
    }

    private void initByType() {
        switch (mType) {
            case SETTING:
            case LOGIN_SETTING:
                initSetting();
                break;
            case CLEAR:
                initVerify(getString(R.string.fingerprint_is_empty_clear));
                break;
            case LOGIN:
                initVerify(getString(R.string.fingerprint_is_empty_login));
                break;
        }
    }


    private void initSetting() {
        if (!mIsSupportFingerprint) {
            jumpToGesture(mType);
            return;
        }
        beginAuthenticate();
    }


    private void initVerify(String errorContent) {
        if (!mIsSupportFingerprint) {
            logoutAndClearFingerPrint();
            return;
        }

        beginAuthenticate();
    }

    private void beginAuthenticate() {
        startAnim();
        mBeginAuthenticateMethodName = AppUtils.getMethodName();
        methodOrderArrayList.add(mBeginAuthenticateMethodName);
        try {
            mFingerprintManagerUtil.beginAuthenticate();
        } catch (FingerPrintException e) {
            onAuthExceptionOrBeIntercept();
        }
    }

    private void beginAuthAnim() {
    }


    public class MyAuthCallbackListener implements FingerprintManagerUtil.AuthenticationCallbackListener {

        @Override
        public void onAuthenticationSucceeded(boolean isAuthSuccess) {
            methodOrderArrayList.add(AppUtils.getMethodName());
            stopAnim();
            if (isAuthSuccess) {
                mFingerPrintTypeController.onAuthenticationSucceeded();
            } else {
                onAuthExceptionOrBeIntercept();
            }
        }

        @Override
        public void onAuthenticationError(int errMsgId, String errString) {
            stopAnim();
            switch (errMsgId) {
                case FingerprintManagerUtil.MyAuthCallback.ERROR_BEYOND:
                    mFingerPrintTypeController.onAuthenticationError(null);
                    break;
                case FingerprintManagerUtil.MyAuthCallback.ERROR_CANCEL:
                    compatibilityDispose();
                    methodOrderArrayList.clear();
                    break;
                default:
                    break;
            }
        }

        /**
         * 针对小米5的兼容，小米5在验证过程中切到后台再回来时，开启验证会直接回调onAuthenticationError，无法继续验证
         * 所以存储函数调用顺序，判断是否一开启验证马上就回调onAuthenticationError
         */
        private void compatibilityDispose() {
            int size = methodOrderArrayList.size();
            if (size <= 0) {
                return;
            }
            if ("MI 5".equals(DeviceUtils.getPhoneModel()) && mBeginAuthenticateMethodName.equals(methodOrderArrayList.get(size - 1))) {
                mFingerPrintTypeController.onAuthenticationError(mi5TipsMappingMap.get(mType));
            }
        }

        @Override
        public void onAuthenticationFailed() {
            methodOrderArrayList.add(AppUtils.getMethodName());
            onAuthFail(getString(R.string.fingerprint_auth_fail));
        }

        @Override
        public void onAuthenticationHelp(String helpString) {
            methodOrderArrayList.add(AppUtils.getMethodName());
            onAuthFail(helpString);
        }
    }

    /**
     * 验证过程异常 或 验证结果被恶意劫持
     * 该失败场景都会清掉指纹再次登陆引导设置，所以如果是关闭场景按成功来处理
     */
    private void onAuthExceptionOrBeIntercept() {
        if (CLEAR.equals(mType)) {
            mFingerPrintTypeController.onAuthenticationSucceeded();
        } else {
            mFingerPrintTypeController.onAuthenticationError(exceptionTipsMappingMap.get(mType));
            clearFingerPrintSign();
        }
    }

    private void onAuthFail(String text) {
        Toast.makeText(Act_login.this, text, Toast.LENGTH_LONG).show();

    }

    private void onAuthSuccess(String text) {
        Toast.makeText(Act_login.this, text, Toast.LENGTH_LONG).show();
        ActivityUtil.DirectToActivity(Act_login.this, PasswordListActivity.class);
        Act_login.this.finish();
    }


    private void jumpToGesture(String type) {
    }


    private void logoutAndClearFingerPrint() {
    }

    private void clearFingerPrintSign() {
    }

    private interface FingerPrintType {
        void onAuthenticationSucceeded();

        void onAuthenticationError(String content);
    }

    private class LoginAuthType implements FingerPrintType {
        @Override
        public void onAuthenticationSucceeded() {
            onAuthSuccess(getString(R.string.fingerprint_auth_success));
        }

        @Override
        public void onAuthenticationError(String content) {
            onAuthFail(content);
        }
    }

    private class ClearType implements FingerPrintType {
        @Override
        public void onAuthenticationSucceeded() {
            onAuthSuccess(getString(R.string.fingerprint_close_success));
        }

        @Override
        public void onAuthenticationError(String content) {
        }
    }

    private class LoginSettingType implements FingerPrintType {
        @Override
        public void onAuthenticationSucceeded() {
            onAuthSuccess(getString(R.string.fingerprint_set_success));
        }

        @Override
        public void onAuthenticationError(String content) {
        }
    }

    private class SettingType implements FingerPrintType {
        @Override
        public void onAuthenticationSucceeded() {
            onAuthSuccess(getString(R.string.fingerprint_set_success));
            finish();
        }

        @Override
        public void onAuthenticationError(String content) {
        }
    }

    private class FingerPrintTypeController implements FingerPrintType {
        private Map<String, FingerPrintType> typeMappingMap = new HashMap<>();

        public FingerPrintTypeController() {
            typeMappingMap.put(SETTING, new SettingType());
            typeMappingMap.put(LOGIN_SETTING, new LoginSettingType());
            typeMappingMap.put(CLEAR, new ClearType());
            typeMappingMap.put(LOGIN, new LoginAuthType());
        }

        @Override
        public void onAuthenticationSucceeded() {
            FingerPrintType fingerPrintType = typeMappingMap.get(mType);
            if (null != fingerPrintType) {
                fingerPrintType.onAuthenticationSucceeded();
            }
        }

        @Override
        public void onAuthenticationError(String content) {
            FingerPrintType fingerPrintType = typeMappingMap.get(mType);
            if (null != fingerPrintType) {
                fingerPrintType.onAuthenticationError(content);
            }
        }
    }

    private void startAnim() {
        isFingerprintLogin = true;
        tvFingerprint.setText("取消指纹登录");
        ivState.setVisibility(View.VISIBLE);
    }

    private void stopAnim() {
        isFingerprintLogin = false;
        tvFingerprint.setText("指纹登录");
        ivState.setVisibility(View.GONE);
    }
}