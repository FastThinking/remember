package com.lzx.allenLee.util.apnUtil;

import java.io.Serializable;

public class ApnBean implements Serializable {

	private static final long serialVersionUID = 2L;
	private Short apnId = -1; 
    private String name=""; 
    private String apn=""; 
    private String mcc="460"; 
    private String mnc="03"; 
    private String numeric="46003";
    private String user=""; 
    private String password=""; 
    private String type="default,supl";
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getNumeric() {
		return numeric;
	}
	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Short getApnId() {
		return apnId;
	}
	public void setApnId(Short apnId) {
		this.apnId = apnId;
	}


}
