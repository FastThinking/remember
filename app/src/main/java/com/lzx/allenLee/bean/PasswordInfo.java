package com.lzx.allenLee.bean;

import java.io.Serializable;

/**
 * user password information
 *
 * @author allenlee
 */
public class PasswordInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int userId = -1;
    private String title;
    private String userName;
    private String password;
    private String isLogin;
    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }
}
