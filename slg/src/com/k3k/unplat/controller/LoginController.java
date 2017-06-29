package com.k3k.unplat.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.k3k.unplat.entity.LoginInfo;
import com.k3k.unplat.utils.UnionUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.k3k.unplat.entity.LoginUserEntity;
import com.k3k.unplat.utils.ServletUtils;


@Controller
public class LoginController {

    private static final Log logger = LogFactory.getLog(LoginController.class);
    private static final SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    @RequestMapping("/login")
    public String index() {
        return "login";
    }

    @RequestMapping("/doLogin")
    public void userLogin(@RequestBody LoginUserEntity user, HttpServletRequest request, HttpServletResponse response) {
        boolean rememberMe = StringUtils.isNotBlank(user.getRememberMe()) && "on".equals(user.getRememberMe());
        String returnUrl = doLogin(user.getUserName(), user.getPassword(), rememberMe, request);
        logger.info("returnUrl : " + returnUrl + ", message : " + request.getAttribute("message"));
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("returnUrl", returnUrl);
        retMap.put("message", request.getAttribute("message"));
        ServletUtils.responseJson(response, retMap);
    }

    /***
     * @param userName
     * @param userpwd
     * @param request
     * @return
     * @throws
     * @author
     * @Description: 公共方法用于登陆验证
     */
    private String doLogin(String userName, String userpwd, boolean rememberMe, HttpServletRequest request) {
        String returnUrl = "index";
        Subject currentUser = SecurityUtils.getSubject();
        String ip = UnionUtils.getRemoteHost(request);
        LoginInfo record = new LoginInfo();
        record.setIp(ip);
        record.setUsername(userName);
        record.setHost(request.getServerName());
        logger.info("Host Name: "+request.getServerName());
        Calendar instance = Calendar.getInstance();
        record.setTime(df.format(instance.getTime()));
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(userName,
                    DigestUtils.md5Hex(userpwd));
            try {
                if (rememberMe) {
                    token.setRememberMe(true);
                    record.setResult(0);
                    record.setInfo("登录成功!");
                } else{
                    record.setResult(0);
                    record.setInfo("登录成功!");
                    token.setRememberMe(false);
                }
                request.setAttribute("message", "success");
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                request.setAttribute("message", "账号不存在！");
                record.setResult(1);
                record.setInfo("失败:账号不存在!");
                returnUrl = "login";
            } catch (IncorrectCredentialsException ice) {
                request.setAttribute("message", "密码错误！");
                record.setResult(1);
                record.setInfo("失败:密码错误!");
                returnUrl = "login";
            } catch (LockedAccountException lae) {
                request.setAttribute("message", "用户账号：" + token.getPrincipal()
                        + "被锁定！");
                record.setResult(1);
                record.setInfo("失败:用户账号被锁定!");
                returnUrl = "login";
            } catch (AuthenticationException ae) {
                logger.error(ae.getMessage(), ae);
                request.setAttribute("message", "登录异常！");
                record.setResult(1);
                record.setInfo("失败:登录异常!");
                returnUrl = "login";
            }
        }
        return returnUrl;
    }

    @RequestMapping("/exit")
    public void exit(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        logger.info("Logout successful!");
        request.getSession().invalidate();
    }


    public static void main(String[] args) {
        logger.info(UUID.randomUUID().toString());
        logger.info(DigestUtils.md5Hex("123456"));
    }
}
