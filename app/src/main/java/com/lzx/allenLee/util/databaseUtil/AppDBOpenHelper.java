package com.lzx.allenLee.util.databaseUtil;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.util.encryptionUtil.EncryptUtil;
/**
 * 负责应用数据库和表的创建和修改。
 * @author allenlee
 *
 */
public class AppDBOpenHelper extends SQLiteOpenHelper{
	
	private static final String TAG = AppDBOpenHelper.class.getSimpleName();
	private Context mContext = null;
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "ytt.db";

    public AppDBOpenHelper(Context context){
    	super(context, DB_NAME, null, DB_VERSION);
    }
	public AppDBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, factory, DB_VERSION);
		
	}
	/**
	 * get Database Path
	 * 
	 * @param paramString
	 * @return
	 */
	public File getDatabasePath(String databaseName) {
		return new File(Global.getFileRootPath_() + "data/db" + "/"
				+ databaseName);
	}


	public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
		paramSQLiteDatabase
				.execSQL("CREATE TABLE t_user(userId INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,userName TEXT,password TEXT,isLogin TEXT);");
		String userName;
		try {
			userName = EncryptUtil.encrypt("admin");
			paramSQLiteDatabase
			.execSQL("insert into t_user(userId,title,userName,password) values(null,'管理员','"+userName+"','"+userName+"','true');");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }

	public void onOpen(SQLiteDatabase paramSQLiteDatabase) {
	}

	public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1,
			int paramInt2) {
		paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS t_user");
		onCreate(paramSQLiteDatabase);
		
		
	}

	public boolean tableIsExist(String tableName) {
		boolean isExist = false;
		if (tableName != null&&tableName.length()!=0){
			try {
				Cursor localCursor = getReadableDatabase().rawQuery(
						"select count(*) as c from sqlite_master where type ='table' and name ='"
								+ tableName.trim() + "' ", null);
				if (localCursor.moveToNext()) {
					int k = localCursor.getInt(0);
					if (k > 0)
						isExist = true;
				}
			
			} catch (Exception localException) {
				
			}
		}
		return isExist;
	}
}
