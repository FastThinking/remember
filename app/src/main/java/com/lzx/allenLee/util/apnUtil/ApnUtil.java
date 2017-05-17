package com.lzx.allenLee.util.apnUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;



public class ApnUtil {
	Context context;
	private final Uri APN_URI = Uri.parse("content://telephony/carriers");
	private final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	public ApnUtil(Context context){
		this.context = context;
	}
	/**
	 * @description ���APN�ؼ���ģ���ѯAPN���Ƿ����ĳAPN
	 * @author allenlee
	 * @param 3gvpdn �̶������
	 */
	public int queryApnId_LikeApnKeyWord(String apnParam) {
		int apnId = -1; // Ĭ��-1

		Uri uri = APN_URI;
		ContentResolver resolver = context.getContentResolver();
		Cursor c = resolver.query(uri, new String[] { "_id", "name", "apn" },
				"apn like '%"+apnParam+"%'", null, null);

		// ����APN����
		if (c != null && c.moveToNext()) {
			apnId = c.getShort(c.getColumnIndex("_id"));
			String apnName = c.getString(c.getColumnIndex("name"));
			String apn = c.getString(c.getColumnIndex("apn"));

			Log.v("APN", apnId + apnName + apn);
		}

		return apnId;
	}

	/**
	 * ��ȡ��ǰ��Apn����id
	 */
	public int getCurrentApnId() {
		int vpnId = -1;
		ContentResolver resolver = context.getContentResolver();
		Cursor c = null;
		c = resolver.query(CURRENT_APN_URI, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				int idIndex = c.getColumnIndex("_id");
				vpnId = c.getShort(idIndex);
			}
		}
		return vpnId;
	}
	/**
	 * ��ȡ��ǰ��Apn����
	 * @return
	 */
	public ApnBean getCurrentApn() {
		ApnBean apnBean = null;
		ContentResolver resolver = context.getContentResolver();
		Cursor c = null;
		c = resolver.query(CURRENT_APN_URI, null, null, null, null);
		if (c != null) {
			apnBean = new ApnBean();
			if (c.moveToFirst()) {
				apnBean.setApnId(c.getShort(c.getColumnIndex("_id")));
				apnBean.setName(c.getString(c.getColumnIndex("name")));
				apnBean.setApn(c.getString(c.getColumnIndex("apn")));
			}
		}
		return apnBean;
	}

	// ����һ��3GWap�����

	public int addAPN(ApnBean apnBean) {
		int id = -1;
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", apnBean.getName());
		values.put("apn", apnBean.getApn());
		values.put("mcc", apnBean.getMcc());
		values.put("mnc", apnBean.getMnc());
		values.put("numeric", apnBean.getNumeric());
		values.put("user", apnBean.getUser());
		values.put("password", apnBean.getPassword());
		values.put("type", apnBean.getType());

		Cursor c = null;
		Uri newRow = resolver.insert(APN_URI, values);
		if (newRow != null) {
			c = resolver.query(newRow, null, null, null, null);
			int idIndex = c.getColumnIndex("_id");
			c.moveToFirst();
			id = c.getShort(idIndex);
		}
		if (c != null) {
			c.close();
			c = null;
		}
		return id;

	}

	/**
	 * �л������
	 */ 
	public void setApnWithApnId(int ApnId) {

		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("apn_id", ApnId);
		resolver.update(CURRENT_APN_URI, values, null, null);

	}
}
