package com.k3k.unplat.controller.slg;

import com.k3k.unplat.entity.slg.SlgLevel;
import com.k3k.unplat.service.slg.SlgRankService;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.ServletUtils;
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
 * Created by jef on 2017/2/24.
 */
@Controller
@RequestMapping(value = "slgrank")
public class SlgRankController {

    private static final Log LOGGER = LogFactory.getLog(SlgRankController.class);

    @Autowired
    private SlgRankService slgRankService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView slgPay() {
        /**
         * 查看SLG领主排行，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG/rank");
        return mav;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void query(Page<SlgLevel> page, SlgLevel filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        LOGGER.info("Level Rank Type:"+filter.getType());
        try {
            page.setPage((startIndex + pageSize) / pageSize);
            page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
            if(filter.getType()==1){
                page = slgRankService.findPageLevel(page, filter);
            }else if(filter.getType()==2){
                page = slgRankService.findPageBuilding(page, filter);
            }else {
                page = slgRankService.findPagePower(page, filter);
            }
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", page.getResults());
            ret.put("iTotalRecords", page.getTotalRows());
            ret.put("iTotalDisplayRecords", page.getTotalRows());
            ServletUtils.responseJson(response, ret);
        } catch (Exception e) {
            LOGGER.error(e);
        }

    }

}
