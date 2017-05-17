package com.lzx.allenLee.util.databaseUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lzx.allenLee.global.Global;

/**
 * 数据库操作基本类 1.打开，关闭数据库 2.开启关闭事务 3.执行查询，更新sql语句 4.查询表的列数属性等相关信息
 * 
 * @author allenlee
 * 
 */
public class BasicDBManager {
	private String TAG = BasicDBManager.class.getSimpleName();
	private SQLiteOpenHelper dbOpenHelper; // 依赖SQLiteOpenHelper创建db对象
	private SQLiteDatabase db;
	private boolean isOpenSuccess = true; // 判断数据库是否开启状态
	private String dbPath;
	private boolean isSDcardDB = true;

	/** 数据库名称 */
	public static final String db_user_info = "user_info.db3";

	public BasicDBManager() {

	}

	public BasicDBManager(SQLiteOpenHelper dbOpenHelper) {
		isSDcardDB = false;
		this.dbOpenHelper = dbOpenHelper;
		this.openDB();
	}

	/**
	 * 打开数据库
	 * 
	 * @param dbAbsName
	 *            db绝对路径
	 * @param dbPath
	 *            db相对路径
	 * @return
	 */
	public boolean openDB() {
		// this.db = SQLiteDatabase.openDatabase(this.dbPath, null,
		// SQLiteDatabase.CREATE_IF_NECESSARY);
		this.db = this.dbOpenHelper.getWritableDatabase();
		if (db != null) {
			isOpenSuccess = true;
		}
		return isOpenSuccess;

	}

	/**
	 * 打开sd卡上的数据库
	 * 
	 * @param dbAbsName
	 *            db绝对路径
	 * @param isNewCreate
	 * @param dbPath
	 *            db相对路径
	 * @return
	 */
	public boolean openDB(String dbAbsName, boolean isNewCreate, String dbPath) {

		try {
			if (dbPath.trim().equals("")) // dbPath 为空时创建
			{
				this.dbPath = (Global.getFileRootPath() + "data/db" + "/");

				File dbFile = new File(this.dbPath);

				if ((!dbFile.exists()) && (isNewCreate) && (!dbFile.mkdirs())) {
					// 创建数据库失败
					this.isOpenSuccess = false;
					return isOpenSuccess;
				}
			} else {
				if (!dbPath.endsWith("/")) {
					this.dbPath = dbPath;
				} else {
					dbPath = dbPath + "/";
				}
			}
			this.dbPath += dbAbsName;

			File localFile2 = new File(this.dbPath);

			// 在指定位置创建数据库或打开此处的数据库
			this.db = SQLiteDatabase.openDatabase(this.dbPath, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
			if (this.db != null) {
				this.isOpenSuccess = true;
			} else {
				isOpenSuccess = false;
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());

		}

		return isOpenSuccess;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	/**
	 * 新增表字段
	 * 
	 * @return
	 */
	protected boolean addTableColumn(String tableName, List<Column> list) {
		StringBuffer sql = new StringBuffer("ALTER TABLE " + tableName
				+ " ADD ");
		Iterator<Column> it = list.iterator();
		while (it.hasNext()) {
			Column cn = it.next();
			sql.append(cn.getColumnName() + " ").append(
					cn.getColumnType() + ",");
		}
		String sqlStr = sql.toString();
		sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
		Log.d(TAG, "addTableColumn sql:" + sqlStr);
		try {
			// this.openDB(BasicDBManager.db_user_info, false, "");
			this.getDb().execSQL(sqlStr);
		} catch (Exception e) {
			return false;
		} finally {
			// this.closeDB();
		}

		return true;

	}

	/**
	 * 执行查询语句，查询结果列都以String类型存储
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public java.util.ArrayList<String[]> executeQuery(String sql,
			String[] selectionArgs) {
		ArrayList<String[]> rows = new ArrayList<String[]>();
		if (this.isOpenSuccess) {
			try {
				// this.openDB(BasicDBManager.db_user_info, false, "");
				Log.i("----BasicDBManager---", sql);
				Cursor cusor = this.getDb().rawQuery(sql, null);
				String[] columns = new String[cusor.getColumnCount()];
				while (cusor.moveToNext()) {
					for (int i = 0; i < cusor.getColumnCount(); i++) {
						columns[i] = cusor.getString(i);
					}
					rows.add(columns);
				}
				if (cusor != null) {
					cusor.close();
					cusor = null;
				}
			} catch (Exception e) {

			} finally {
				// this.closeDB();
			}
		}

		return rows;
	}

	public Cursor executeQueryCursor(String sql, String[] selectionArgs) {
		Cursor cursor = null;
		if (this.isOpenSuccess) {
			try {
				// this.openDB(BasicDBManager.db_user_info, false, "");
				cursor = this.getDb().rawQuery(sql, null);
			} catch (Exception e) {

			} finally {
				// this.closeDB();
			}
		}

		return cursor;
	}

	/**
	 * 自定义执行插入，更新，删除语句，
	 * 
	 * @param updateSql
	 * @return
	 */
	public boolean executeSql(String updateSql) {
		boolean result = false;
		try {
			// this.openDB(BasicDBManager.db_user_info, false, "");
			if (this.isOpenSuccess) {
				this.getDb().execSQL(updateSql);
				result = true;
			}
		} catch (Exception localException) {

		} finally {
			// this.closeDB();
		}
		return result;
	}

	public SQLiteDatabase getDb() {
		if (db == null) {
			if (isSDcardDB) {
				openDB(db_user_info, false, "");
			} else {
				openDB();
			}

		}
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}
}

/**
 * 列属性
 * 
 * @author allenlee
 * 
 */
class Column {
	private String columnName;
	private String columnType;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

}