package com.k3k.unplat.controller.slg;

import com.k3k.unplat.entity.slg.SlgLevelStatis;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jef on 2016/12/9.
 */
@Controller
@RequestMapping(value = "slglevel")
public class SlgLevelStatisController {
    private static final Log LOGGER = LogFactory.getLog(SlgLevelStatisController.class);
    private static final SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SlgLevelService slgLevelService;

    @RequestMapping(value = "slguserlv", method = RequestMethod.GET)
    public ModelAndView slgUserLv() {
        /**
         * 查看SLG玩家等级战斗力统计，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG/playerstatis");
        return mav;
    }

    @RequestMapping(value = "queryUser", method = RequestMethod.POST)
    public void queryUser(Page<SlgLevelStatis> page, SlgLevelStatis filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        LOGGER.info("Date: "+filter.getDate());
        Calendar instance = Calendar.getInstance();
        try {
            if (StringUtils.isBlank(filter.getDate())) {
                String sdate = df.format(instance.getTime());
                long e = df.parse(sdate).getTime();
                filter.setDate(String.valueOf(e));
            }else {
                long e = df.parse(filter.getDate()).getTime();
                filter.setDate(String.valueOf(e));
            }
            page.setPage((startIndex + pageSize) / pageSize);
            page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
            page = slgLevelService.findPage(page, filter);
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", page.getResults());
            ret.put("iTotalRecords", page.getTotalRows());
            ret.put("iTotalDisplayRecords", page.getTotalRows());
            ServletUtils.responseJson(response, ret);
        }catch (Exception e){
            LOGGER.error(e);
        }

    }

    @RequestMapping(value = "slgbdlv", method = RequestMethod.GET)
    public ModelAndView slgBuildingLv() {
        /**
         * 查看SLG玩家等级战斗力统计，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG/buildingstatis");
        return mav;
    }

    @RequestMapping(value = "queryBD", method = RequestMethod.POST)
    public void queryBD(Page<SlgLevelStatis> page, SlgLevelStatis filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        LOGGER.info("Date: "+filter.getDate());
        Calendar instance = Calendar.getInstance();
        try {
            if (StringUtils.isBlank(filter.getType())) {
                filter.setType("3");
            }
            if (StringUtils.isBlank(filter.getDate())) {
                String sdate = df.format(instance.getTime());
                long e = df.parse(sdate).getTime();
                filter.setDate(String.valueOf(e));
            }else {
                long e = df.parse(filter.getDate()).getTime();
                filter.setDate(String.valueOf(e));
            }
            page.setPage((startIndex + pageSize) / pageSize);
            page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
            page = slgLevelService.findPage(page, filter);
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", page.getResults());
            ret.put("iTotalRecords", page.getTotalRows());
            ret.put("iTotalDisplayRecords", page.getTotalRows());
            ServletUtils.responseJson(response, ret);
        }catch (Exception e){
            LOGGER.error(e);
        }

    }


}
