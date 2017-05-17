package com.lzx.allenLee.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.lzx.allenLee.AppApplication;
import com.lzx.allenLee.base.AppActivityManager;
import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.serviceDao.AppServiceDao;
import com.lzx.allenLee.util.databaseUtil.BasicDBManager;
import com.lzx.allenLee.util.databaseUtil.DataBaseManager;
import com.lzx.allenLee.util.fileManager.FileUtil;

/**
 * 用户密码信息管理
 * 
 * @author allenlee
 *
 */
public class UserPasswordManager implements AppServiceDao {
	private static UserPasswordManager uPassInstance = null;
	private BasicDBManager dbManager; // 依赖BasicDBManager操作数据库
	private final String TAG = UserPasswordManager.class.getSimpleName();
	private final String assetSqlPath = "sys/create_t_user.sql"; // 配置数据库创建sql文档地址
	private final String dbName = "";

	public UserPasswordManager(BasicDBManager paramBasicDBManager) {
		if (paramBasicDBManager != null)
			this.dbManager = paramBasicDBManager;
	}

	public static UserPasswordManager getInstance() {
		if (uPassInstance == null)
			uPassInstance = new UserPasswordManager(DataBaseManager.getInstance().getSysDb());
		return uPassInstance;
	}

	public static UserPasswordManager getInstance(BasicDBManager paramBasicDBManager) {
		if (uPassInstance == null)
			uPassInstance = new UserPasswordManager(paramBasicDBManager);
		return uPassInstance;
	}

	/*
	 * boolean checkCacheDirectory() { File localFile = new
	 * File(EventObj.OPEN_CACHEDIR); if (!localFile.exists())
	 * localFile.mkdirs(); return true; }
	 */

	/*
	 * public void checkFiles() { checkCacheDirectory();
	 * deleteDataByTime(Global.getOaSetInfo().saveCacheDays_); if (new
	 * File(EventObj.OPEN_CACHEDIR).exists()) { ArrayList localArrayList1 = new
	 * ArrayList(); FileUtil.listFiles(EventObj.OPEN_CACHEDIR, localArrayList1);
	 * ArrayList localArrayList2 = getAllCacheDatas(); int i = 0; while (i <
	 * localArrayList1.size()) { File localFile = (File)localArrayList1.get(i);
	 * if (localFile.isDirectory()) { i++; continue; } int j = 0; for (int k =
	 * 0; ; k++) { if (k < localArrayList2.size()) { OpenFileDataInfo
	 * localOpenFileDataInfo = (OpenFileDataInfo)localArrayList2.get(k); if
	 * (!localFile.getPath().equals(localOpenFileDataInfo.path_)) continue; j =
	 * 1; } if ((j != 0) || (!localFile.exists())) break; localFile.delete();
	 * break; } } } }
	 */
	public void createTable() {
		// assets/sys/文件夹路径
		String sql = FileUtil.getFileContentFromInputStream(assetSqlPath, AppActivityManager.getTopActivity());
		// String sql = FileUtil.getFileInputString("createusertable.sql",
		// AppActivityManager.getTopActivity());
		if (sql != null && sql.length() != 0) {
			this.dbManager.executeSql(sql);
		} else {
			Log.e(TAG, "createTable--sql为空！");
		}

	}

	public boolean userCheck(String userName, String password) {
		boolean result = false;
		String sql = "select * from t_user where userName ='" + userName + "' and password = '" + password
				+ "' and isLogin='true'";
		// Cursor cursor =
		// this.mOpenHelper.getWritableDatabase().query(sql,null,null,null,null,null,null);

		// String tableName = "t_user";
		// String []columns = null;
		// String selection = "userName ='"+userName+"' and password =
		// '"+password+"'";
		// String[] selectionArgs = null;
		// String groupBy = null;
		// String having = null;
		// String orderBy = null;
		// Cursor cursor = null;
		try {
			ArrayList<String[]> rows = this.dbManager.executeQuery(sql, null);
			if (rows.size() > 0) {
				result = true;
			}
			Log.i("userCheck", "记录条数：" + rows.size() + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return result;
	}

	/*
	 * void deleteDataByCount(int paramInt) { ArrayList localArrayList =
	 * getAllCacheDatas(); if (localArrayList.size() > paramInt) while
	 * (localArrayList.size() > paramInt) { OpenFileDataInfo
	 * localOpenFileDataInfo = (OpenFileDataInfo)localArrayList.get(0); File
	 * localFile = new File(localOpenFileDataInfo.path_); if
	 * (localFile.exists()) localFile.delete(); String str =
	 * getSQLDeleteScript(localOpenFileDataInfo.url_);
	 * this.sysDb_.executeUpdate(str); localArrayList.remove(0); } }
	 * 
	 * public void deleteDataByTime(String paramString) { int i = 0; try { int m
	 * = Integer.parseInt(paramString); i = m; label11: if (i < 0); while (true)
	 * { return; if (i == 0) { deleteDataByCount(0); continue; } long l =
	 * System.currentTimeMillis() - 86400000 * i; String str1 = new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(l)); ArrayList
	 * localArrayList1 = new ArrayList(); StringBuffer localStringBuffer = new
	 * StringBuffer(); localStringBuffer.
	 * append("select * from OpenFileCaches where updatetime < ").append("'" +
	 * str1 + "'").append("; "); String str2 = localStringBuffer.toString();
	 * ArrayList localArrayList2 = this.sysDb_.executeQuery(str2); if
	 * ((localArrayList2 == null) || (localArrayList2.size() <= 1)) continue;
	 * for (int j = 1; j < localArrayList2.size(); j++) { String[] arrayOfString
	 * = (String[])localArrayList2.get(j); String str4 =
	 * getValueByIndex(arrayOfString, 2); if (str4 == null) continue;
	 * OpenFileDataInfo localOpenFileDataInfo2 = new OpenFileDataInfo();
	 * localOpenFileDataInfo2.path_ = str4; localOpenFileDataInfo2.url_ =
	 * getValueByIndex(arrayOfString, 1); localOpenFileDataInfo2.updatetime_ =
	 * getValueByIndex(arrayOfString, 3);
	 * localArrayList1.add(localOpenFileDataInfo2); } for (int k = -1 +
	 * localArrayList1.size(); k >= 0; k--) { OpenFileDataInfo
	 * localOpenFileDataInfo1 = (OpenFileDataInfo)localArrayList1.get(k); File
	 * localFile = new File(localOpenFileDataInfo1.path_); if
	 * (localFile.exists()) localFile.delete(); String str3 =
	 * getSQLDeleteScript(localOpenFileDataInfo1.url_);
	 * this.sysDb_.executeUpdate(str3); } } } catch (Exception localException) {
	 * break label11; } }
	 * 
	 * ArrayList<OpenFileDataInfo> getAllCacheDatas() { ArrayList
	 * localArrayList1 = new ArrayList(); ArrayList localArrayList2 =
	 * this.sysDb_.executeQuery("select * from OpenFileCaches;"); if
	 * ((localArrayList2 == null) || (localArrayList2.size() <= 1)); while
	 * (true) { return localArrayList1; for (int i = 1; i <
	 * localArrayList2.size(); i++) { String[] arrayOfString =
	 * (String[])localArrayList2.get(i); String str =
	 * getValueByIndex(arrayOfString, 2); if (str == null) continue;
	 * OpenFileDataInfo localOpenFileDataInfo = new OpenFileDataInfo();
	 * localOpenFileDataInfo.path_ = str; localOpenFileDataInfo.url_ =
	 * getValueByIndex(arrayOfString, 1); localOpenFileDataInfo.updatetime_ =
	 * getValueByIndex(arrayOfString, 3);
	 * localArrayList1.add(localOpenFileDataInfo); } } }
	 * 
	 * public OpenFileDataInfo getCacheFilePath(String paramString) {
	 * OpenFileDataInfo localOpenFileDataInfo = null; String str1 =
	 * getSQLQueryScript(paramString); ArrayList localArrayList =
	 * this.sysDb_.executeQuery(str1); if ((localArrayList == null) ||
	 * (localArrayList.size() <= 1)); while (true) { return
	 * localOpenFileDataInfo; String[] arrayOfString =
	 * (String[])localArrayList.get(1); String str2 =
	 * getValueByIndex(arrayOfString, 2); if (str2 == null) continue; if (new
	 * File(str2).exists()) { localOpenFileDataInfo = new OpenFileDataInfo();
	 * localOpenFileDataInfo.path_ = str2; localOpenFileDataInfo.url_ =
	 * getValueByIndex(arrayOfString, 1); localOpenFileDataInfo.updatetime_ =
	 * getValueByIndex(arrayOfString, 3); continue; } String str3 =
	 * getSQLDeleteScript(paramString); this.sysDb_.executeUpdate(str3); } }
	 * 
	 * String getSQLDeleteScript(String paramString) { StringBuffer
	 * localStringBuffer = new StringBuffer();
	 * localStringBuffer.append("delete from OpenFileCaches where ");
	 * localStringBuffer.append("url = '").append(paramString).append("' ");
	 * localStringBuffer.append(';'); return localStringBuffer.toString(); }
	 * 
	 * String getSQLQueryScript(String paramString) { StringBuffer
	 * localStringBuffer = new StringBuffer();
	 * localStringBuffer.append("select * from OpenFileCaches where ");
	 * localStringBuffer.append("url = '").append(paramString).append("' ");
	 * localStringBuffer.append(';'); return localStringBuffer.toString(); }
	 * 
	 * String getValueByIndex(String[] paramArrayOfString, int paramInt) {
	 * String str1 = null; if (paramArrayOfString == null); while (true) {
	 * return str1; String str2 = paramArrayOfString[paramInt]; if (str2 ==
	 * null) continue; str1 = str2.toString(); } }
	 */
	public void insertUser(String title, String userName, String password, String isLogin) {
		// long l = System.currentTimeMillis();
		// String updateTime = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss").format(new Date(l));
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer.append("insert into t_user(userId,title,userName,password,isLogin) values(");
		localStringBuffer.append("null");
		localStringBuffer.append(",").append('\'').append(title).append('\'');
		localStringBuffer.append(",").append('\'').append(userName).append('\'');
		localStringBuffer.append(",").append('\'').append(password).append('\'');
		localStringBuffer.append(",").append('\'').append(isLogin).append('\'');
		localStringBuffer.append(");");
		String sql = localStringBuffer.toString();
		this.dbManager.executeSql(sql);
	}

	public List<PasswordInfo> queryAllPassword(String likeTitle) {
		StringBuffer sql = new StringBuffer("select * from t_user");
		if (!TextUtils.isEmpty(likeTitle)) {
			sql.append(" where title like '%" + likeTitle + "%'");
		}

		// Cursor cursor =
		// this.mOpenHelper.getWritableDatabase().query(sql,null,null,null,null,null,null);
		List<PasswordInfo> list = new ArrayList<PasswordInfo>();
		// String tableName = "t_user";
		// String []columns = null;
		// String selection = null;
		// String[] selectionArgs = null;
		// String groupBy = null;
		// String having = null;
		// String orderBy = null;
		Cursor cursor = null;
		try {
			cursor = this.dbManager.executeQueryCursor(sql.toString(), null);
			while (cursor != null && cursor.moveToNext()) {
				PasswordInfo pass = new PasswordInfo();
				pass.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
				pass.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				pass.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
				pass.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				pass.setIsLogin(cursor.getString(cursor.getColumnIndex("isLogin")));
				list.add(pass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			this.dbManager.closeDB();
		}

		return list;
	}

	@Override
	public boolean insertPassword(PasswordInfo info) {
		boolean rs = true;
		String sql = "insert into t_user(userId,title,userName,password,isLogin) values(null,'" + info.getTitle()
				+ "','" + info.getUserName() + "','" + info.getPassword() + "','" + info.getIsLogin() + "');";
		try {
			this.dbManager.openDB(BasicDBManager.db_user_info, false, "");
			this.dbManager.executeSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			this.dbManager.closeDB();
		}
		return rs;
	}

	@Override
	public boolean deletePassword(String userId) {
		// TODO Auto-generated method stub
		boolean rs = true;
		String sql = "delete from t_user where userId='" + userId + "';";
		try {
			// String[] whereArgs = {userId};
			// this.sysDb.getDb().execSQL(sql);
			this.dbManager.openDB(BasicDBManager.db_user_info, false, "");
			this.dbManager.executeSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			this.dbManager.closeDB();
		}
		return rs;
	}

	@Override
	public boolean updatePassword(PasswordInfo info) {
		boolean rs = true;
		String sql = "update t_user set title='" + info.getTitle() + "',userName='" + info.getUserName()
				+ "',password='" + info.getPassword() + "',isLogin='" + info.getIsLogin() + "' where userId='"
				+ info.getUserId() + "';";
		try {
			this.dbManager.openDB(BasicDBManager.db_user_info, false, "");
			this.dbManager.executeSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			this.dbManager.closeDB();
		}
		return rs;
	}

	@Override
	public boolean createKeyFile(String key) {

		return false;
	}

}