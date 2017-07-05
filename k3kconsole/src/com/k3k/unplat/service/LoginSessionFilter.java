package com.k3k.unplat.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.k3k.unplat.entity.LoginInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.dao.LoginUserMapper;
import com.k3k.unplat.entity.LoginUserEntity;

public class LoginSessionFilter extends FormAuthenticationFilter {

    private static final Log LOGGER = LogFactory.getLog(LoginSessionFilter.class);
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest req = WebUtils.toHttp(request);
        HttpServletResponse res = WebUtils.toHttp(response);
        if (req.getHeader("x-requested-with") != null
                && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            res.setHeader("sessionStatus", "sessionOut");
            res.getWriter().write("session 超时");
        } else {
            String loginUrl = getLoginUrl();
            WebUtils.issueRedirect(request, response, loginUrl);
        }
    }

 /*   @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);

        //如果 isAuthenticated 为 false 证明不是登录过的，同时 isRememberd 为true 证明是没登陆直接通过记住我功能进来的
        if (!subject.isAuthenticated() && subject.isRemembered()) {
            LOGGER.info("通过Remember Me进来的...");
            //获取session看看是不是空的
            Session session = subject.getSession(true);
            //随便拿session的一个属性来看session当前是否是空的，我用userId，你们的项目可以自行发挥
            if (session.getAttribute(Constants.CURRENT_USER) == null) {
                //LOGGER.debug("是否需要初始化User");
                //如果是空的才初始化，否则每次都要初始化，项目得慢死  
                //这边根据前面的前提假设，拿到的是username
                String username = subject.getPrincipal().toString();
                LoginUserEntity user = userMapper.selectByUserName(username);
                //重新自定义session
                subject.getSession().setAttribute(Constants.CURRENT_USER, user);
                LOGGER.debug(username);
                //将登录信息插入数据库
                String ip = request.getRemoteAddr();
                LoginInfo record = new LoginInfo();
                record.setIp(ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip);
                record.setHost(request.getServerName());
                record.setUsername(subject.getPrincipal().toString());
                Calendar instance = Calendar.getInstance();
                record.setTime(df.format(instance.getTime()));
                record.setResult(0);
                record.setInfo("登录成功:通过Remember Me进入!");
                loginInfoService.insertLoginInfo(record);
            }
        }

        //这个方法本来只返回 subject.isAuthenticated() 现在我们加上 subject.isRemembered() 让它同时也兼容remember这种情况  
        return subject.isAuthenticated() || subject.isRemembered();
    }*/

}
