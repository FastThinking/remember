package com.lzx.allenLee.serviceDao;

import java.util.ArrayList;
import java.util.List;

import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.util.databaseUtil.AppDBOpenHelper;
import com.lzx.allenLee.util.databaseUtil.BasicDBManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * AppDbManager使用单例模式 1.获取全局唯一的数据库管理器 2.
 * 
 * @author allenlee
 *
 */
public class AppServiceManager implements AppServiceDao {

	private static AppServiceManager dbManager;
	private BasicDBManager basicDBManager; // 依赖BasicDBManager管理数据库对象

	private AppServiceManager(Context context) {
		AppDBOpenHelper mOpenHelper = new AppDBOpenHelper(context); // 初始化时赋值
		// this.db = this.mOpenHelper.getWritableDatabase();
		basicDBManager = new BasicDBManager(mOpenHelper);
	}

	public static AppServiceManager getInstance(Context context) {
		if (dbManager != null) {
			return dbManager;
		} else {
			return new AppServiceManager(context);
		}
	}

	@Override
	public boolean userCheck(String userName, String password) {
		boolean result = false;
		// String sql = "select * from t_user where userId ='"+userId+"' and
		// password = '"+password+"'";
		// Cursor cursor =
		// this.mOpenHelper.getWritableDatabase().query(sql,null,null,null,null,null,null);

		String tableName = "t_user";
		String[] columns = null;
		String selection = "userName ='" + userName + "' and password = '" + password + "'";
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		Cursor cursor = null;
		try {
			cursor = basicDBManager.getDb().query(tableName, columns, selection, selectionArgs, groupBy, having,
					orderBy);
			if (cursor.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			basicDBManager.closeDB();
		}

		return result;
	}

	@Override
	public List<PasswordInfo> queryAllPassword(String likeTitle) {
		// String sql = "select * from t_user";
		// Cursor cursor =
		// this.mOpenHelper.getWritableDatabase().query(sql,null,null,null,null,null,null);
		List<PasswordInfo> list = new ArrayList<PasswordInfo>();
		String tableName = "t_user";
		String[] columns = null;
		String selection = null;
		if (!TextUtils.isEmpty(likeTitle)) {
			selection = "title like %" + likeTitle + "%";
		}
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		Cursor cursor = null;
		try {
			cursor = basicDBManager.getDb().query(tableName, columns, selection, selectionArgs, groupBy, having,
					orderBy);
			while (cursor != null && cursor.moveToNext()) {
				PasswordInfo pass = new PasswordInfo();
				pass.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
				pass.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				pass.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
				pass.setPassword(cursor.getString(cursor.getColumnIndex("password")));
				list.add(pass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			basicDBManager.closeDB();
		}

		return list;
	}

	@Override
	public boolean insertPassword(PasswordInfo info) {
		boolean rs = true;
		String sql = "insert into t_user(userId,title,userName,password,isLogin) values(null,'" + info.getTitle()
				+ "','" + info.getUserName() + "','" + info.getPassword() + "','" + info.getIsLogin() + "');";
		try {
			basicDBManager.getDb().execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			basicDBManager.closeDB();
		}
		return rs;
	}

	@Override
	public boolean deletePassword(String userId) {
		// TODO Auto-generated method stub
		boolean rs = true;
		// String sql = "delete from t_user where userId='"+userId+"';";
		try {
			String[] whereArgs = { userId };
			// basicDBManager.getDb().execSQL(sql);
			basicDBManager.getDb().delete("t_user", "userId=?", whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			basicDBManager.closeDB();
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
			basicDBManager.getDb().execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
			rs = false;
		} finally {
			basicDBManager.closeDB();
		}
		return rs;
	}

	@Override
	public boolean createKeyFile(String key) {
		// TODO Auto-generated method stub
		return false;
	}

}
