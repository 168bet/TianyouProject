package com.k3k.unplat.controller.slgGM;

import com.k3k.unplat.entity.slg.SlgUser;
import com.k3k.unplat.service.slg.SlgLevelService;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.ServletUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jef on 2017/1/16.
 */
@Controller
@RequestMapping(value = "slguserinfo")
public class UserInfoController {
    private static final Log LOGGER = LogFactory.getLog(UserInfoController.class);

    @Autowired
    private SlgLevelService slgLevelService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView slgAccount() {
        /**
         * 查看SLG角色信息，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        LOGGER.info("SJL User Start");
        mav.setViewName("admin/SLG_GM/slg_userinfo");
        return mav;
    }

    @RequestMapping(value = "query")
    public void query(Page<SlgUser> page, SlgUser filter, HttpServletRequest request, HttpServletResponse response){
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        page.setPage((startIndex+pageSize)/pageSize);
        page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
        LOGGER.info("Page: "+page.getStartIndex()+";"+page.getPageSize());
        page = slgLevelService.getUser(page,filter);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("sEcho", request.getParameter("sEcho"));
        ret.put("aaData", page.getResults());
        ret.put("iTotalRecords", page.getTotalRows());
        ret.put("iTotalDisplayRecords", page.getTotalRows());
        ServletUtils.responseJson(response, ret);
    }

}
