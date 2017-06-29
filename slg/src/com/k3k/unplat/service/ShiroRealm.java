package com.k3k.unplat.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.k3k.unplat.dao.LoginUserMapper;
import com.k3k.unplat.entity.LoginUserEntity;
import com.k3k.unplat.utils.UserUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class ShiroRealm extends AuthorizingRealm {
	
	private static final Log logger = LogFactory.getLog(ShiroRealm.class);
	
	@Autowired
	private LoginUserMapper userMapper;//注入dao
//	protected SystemAccountDao systemAccountDao;   

	/** 
	 * 授权信息 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userid = (String) principals.fromRealm(getName()).iterator().next();
		if (userid != null) {
//			Account user = accountService.findAccountByUserName(username);
//			SystemAccount user = systemAccountDao.findByUserName(username);
//			MultipleDataSource.setDataSourceKey("localDBDS");
//			User user = userMapper.selectByPrimaryKey(Integer.parseInt(userid));
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//			info.addRole("3");
			return info;
			/* if( user != null && user.getRoles() != null ){  
			     SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();  
			     for( SecurityRole each: user.getRoles() ){  
			             info.addRole(each.getName());  
			             info.addStringPermissions(each.getPermissionsAsString());  
			     }  
			     return info;  
			 } */
		}
		return null;
	}

	/** 
	* 认证信息 
	*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		logger.info(token.getUsername() + ", REMEMBER ME ? " + token.isRememberMe());
//		SystemAccount user = systemAccountDao.findByUserName(token.getUsername());	
//		MultipleDataSource.setDataSourceKey("gameDBDS");
		LoginUserEntity user = userMapper.selectByUserName(token.getUsername());
		logger.info(user == null);
		if (user != null) {
			logger.info(user.getUserName() + " " + user.getRealName() + " " + user.getRoleId());
			UserUtils.setCurrentUser(user);
			return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
		} else
			return null;
	}

}
