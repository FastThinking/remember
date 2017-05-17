package com.lzx.allenLee.serviceDao;

import java.util.List;

import com.lzx.allenLee.bean.PasswordInfo;

/**
 * 业务逻辑接口
 * @author allenlee
 *
 */
public interface AppServiceDao {
	/**
	 * 从数据库表查询该账号信息是否正确
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean userCheck(String userId,String password);
	/**
	 * 查询所有的账号密码
	 * @return
	 */
	public List<PasswordInfo> queryAllPassword(String likeTitle);
	
	public boolean insertPassword(PasswordInfo info);
	
	public boolean deletePassword(String userId);
	
	public boolean updatePassword(PasswordInfo info);
	
	public boolean createKeyFile(String key);
	
}
