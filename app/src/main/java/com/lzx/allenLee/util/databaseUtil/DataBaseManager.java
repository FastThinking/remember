package com.lzx.allenLee.util.databaseUtil;


import java.io.File;
import java.io.FileNotFoundException;

import com.lzx.allenLee.global.Global;
import com.lzx.allenLee.service.UserPasswordManager;
import com.lzx.allenLee.util.encryptionUtil.EncryptUtil;
/**
 * 1.管理BasicDBManager对象。初始化时创建数据库管理对象BasicDBManager，打开数据库
 * 2.DataBaseManager采用单例模式，getInstance()获取数据库管理对象DataBaseManager。保证只有一个数据库管理对象。
 * @author allenlee
 *
 */
public class DataBaseManager
{
  private static DataBaseManager dbManager = null;
  private BasicDBManager sysdbManager;
  /**数据库名称*/
  private final String db_user_info = BasicDBManager.db_user_info;
  /**数据库sd卡相对路径*/
  private final String relativePath = "data/db" + "/";

  /**
   * 初始化时创建数据库管理对象BasicDBManager，打开数据库
   */
  public DataBaseManager()
  {
    init();
  }

  private void checkDataBase()
  {
    if (!new File(Global.getFileRootPath() + relativePath + db_user_info).exists()){
      clearOldDataBase();
      if (this.sysdbManager.openDB(db_user_info, true, "")){
    	  //打开数据库成功，创建表
        UserPasswordManager.getInstance(this.sysdbManager).createTable();
        String userName;
		try {
			userName = EncryptUtil.encrypt("admin");
			UserPasswordManager.getInstance(this.sysdbManager).insertUser("admin", userName, userName,"true");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
      }
    
    }else{//如果存在
    	this.sysdbManager.openDB(db_user_info, true, "");
    }
  }

  private void clearOldDataBase()
  {
    File localFile1 = new File(Global.getFileRootPath() + relativePath + db_user_info);
    if (localFile1.exists())
      localFile1.delete();
  }

  public static DataBaseManager getInstance()
  {
    if (dbManager == null)
      dbManager = new DataBaseManager();
    return dbManager;
  }
/**
 * 创建数据库管理对象，打开数据库
 */
  private void init()
  {
    this.sysdbManager = new BasicDBManager();
    checkDataBase();
  }

  public void closeDb()
  {
    this.sysdbManager.closeDB();
    this.sysdbManager = null;
  }

  public BasicDBManager getSysDb()
  {
    return this.sysdbManager;
  }
}
