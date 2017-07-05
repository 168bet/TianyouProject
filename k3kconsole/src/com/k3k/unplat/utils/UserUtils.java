package com.k3k.unplat.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.LoginUserEntity;

public class UserUtils {

	public static final void setCurrentUser(LoginUserEntity user) {

		Subject currentUser = SecurityUtils.getSubject();

		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(Constants.CURRENT_USER, user);
			}
		}
	}

	public static final LoginUserEntity getCurrentUser() {

		Subject currentUser = SecurityUtils.getSubject();

		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				LoginUserEntity user = (LoginUserEntity) session.getAttribute(Constants.CURRENT_USER);
				if (null != user) {
					return user;
				}
			}
		}
		return null;
	}
}
