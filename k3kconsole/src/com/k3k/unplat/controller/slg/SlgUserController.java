package com.k3k.unplat.controller.slg;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.slg.SlgUser;
import com.k3k.unplat.service.slg.SlgUserService;
import com.k3k.unplat.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jef on 2017/4/6.
 */
@Controller
@RequestMapping(value = "slguser")
public class SlgUserController {
    private static final Log LOGGER = LogFactory.getLog(SlgUserPreserveController.class);
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SlgUserService slgUserService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView sjlUser() {
        /**
         * 查看SLG用户，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG_GM/slg_userinfo");
        return mav;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void query(SlgUser filter, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<String, Object>();
        try {
            List<SlgUser> result = slgUserService.getUserList(filter);
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", result);
            ret.put("iTotalRecords",result.size());
            ret.put("iTotalDisplayRecords",result.size());
        } catch (Exception e) {
            LOGGER.error(e);
        }
        ServletUtils.responseJson(response, ret);
    }

    @RequestMapping(value = "done", method = RequestMethod.POST)
    public void done( HttpServletRequest request, HttpServletResponse response) {
        //封号-解封
        String userid = StringUtils.isNotBlank(request.getParameter("userid")) ? request.getParameter("userid") : "";
        String type = StringUtils.isNotBlank(request.getParameter("type")) ? request.getParameter("type") : "";
        String logID = "[User Done]:"+type;

        LOGGER.info(logID+" UserID: "+userid + " Type: "+type);
        Map<String, Object> ret = new HashMap<String, Object>();
        String code = "requestType="+type+"&" + "toUid=" + userid;
        LOGGER.info(logID+" Code:" + code + "&" + Constants.SLG_KEY);
        String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
        LOGGER.info(logID+" Token:" + token);
        String sendInfo = "toUid=" + userid + "&requestType="+type+ "&token=" + token;
        LOGGER.info(logID+" Send Info: " + sendInfo);
        try {
            int result = SlgUtils.sendResult(sendInfo);
            //String sendGet = HttpUtils.sendGet(Constants.SLG_SERVER, sendInfo, "");
            //sendGet = "{'code':0}";
            LOGGER.info(logID+" 调用接口结果:" + result);
            ret.put("message", "封禁玩家成功!");
            ret.put("result", result);
        } catch (Exception e) {
            LOGGER.error("封禁玩家:" + e.getMessage());
            ret.put("message", "封禁玩家,接口错误!");
            ret.put("result", 2);
        }
        ServletUtils.responseJson(response, ret);
    }
}
