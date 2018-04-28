package com.lzx.allenLee.service;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.lzx.allenLee.base.AppActivityManager;
import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.serviceDao.AppServiceDao;
import com.lzx.allenLee.util.databaseUtil.BasicDBManager;
import com.lzx.allenLee.util.databaseUtil.DataBaseManager;
import com.lzx.allenLee.util.fileManager.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户密码信息管理
 *
 * @author allenlee
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

        List<PasswordInfo> list = new ArrayList<PasswordInfo>();
        Cursor cursor = null;
        try {
            cursor = this.dbManager.executeQueryCursor(sql.toString(), null);
            while (cursor != null && cursor.moveToNext()) {
                PasswordInfo pass = new PasswordInfo();
                pass.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
                pass.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                pass.setDes(cursor.getString(cursor.getColumnIndex("des")));
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
        String sql = "insert into t_user(userId,title,des,userName,password,isLogin) values(null,'" + info.getTitle()
                + "','" + info.getDes() + "','" + info.getUserName() + "','" + info.getPassword() + "','" + info.getIsLogin() + "');";
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
        String sql = "update t_user set title='" + info.getTitle() + "',des='" + info.getDes()
                + "',userName='" + info.getUserName()
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