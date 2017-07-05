package com.k3k.unplat.controller.slg;

import com.k3k.unplat.entity.slg.SlgOrder;
import com.k3k.unplat.entity.slg.SlgPaymentStatis;
import com.k3k.unplat.service.slg.SlgPaymentService;
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
@RequestMapping(value = "slgpay")
public class SlgPaymentController {
    private static final Log LOGGER = LogFactory.getLog(SlgPaymentController.class);
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SlgPaymentService slgPaymentService;

    @RequestMapping(value = "slgpay", method = RequestMethod.GET)
    public ModelAndView slgPay() {
        /**
         * 查看SLG充值统计，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG/paymentstatis");
        return mav;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView slgPayList() {
        /**
         * 查看SLG充值排行，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG/paylist");
        return mav;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void query(Page<SlgPaymentStatis> page, SlgPaymentStatis filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        String DateRange = filter.getDateRange();
        LOGGER.info("query dataRange : " + filter.getDateRange());
        int checkType = 1;
        try {
            if (null != DateRange && !DateRange.equals("")) {
                String[] dateS = DateRange.split("&");
                String edateStr = dateS[1].trim();
                String sdateStr = dateS[0].trim();
                filter.setSdate(df.parse(sdateStr).getTime());
                filter.setEdate(df.parse(edateStr).getTime() + 86400000L);
            }
            Calendar instance = Calendar.getInstance();
            if (filter.getEdate() == 0L) {
                instance.add(Calendar.DATE, 1);
                String edate = df.format(instance.getTime());
                long e = df.parse(edate).getTime();
                filter.setEdate(e);
                LOGGER.info("Edate: " + edate + "; " + e);
            }
            page.setPage((startIndex + pageSize) / pageSize);
            page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
            page = slgPaymentService.findPage(page, filter, checkType);
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", page.getResults());
            ret.put("iTotalRecords", page.getTotalRows());
            ret.put("iTotalDisplayRecords", page.getTotalRows());
            ServletUtils.responseJson(response, ret);
        } catch (Exception e) {
            LOGGER.error("Query :"+e.getMessage());
        }

    }

    @RequestMapping(value = "queryorder", method = RequestMethod.POST)
    public void queryOrder(Page<SlgOrder> page, SlgOrder filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        Map<String, Object> ret = new HashMap<String, Object>();
        String DateRange = filter.getDateRange();
        String userid = filter.getUserid();
        if(StringUtils.isNumeric(userid)||StringUtils.isBlank(userid)){
            LOGGER.info("queryOrder dataRange : " + filter.getDateRange());
            try {
                if (null != DateRange && !DateRange.equals("")) {
                    String[] dateS = DateRange.split("&");
                    String sdateStr = dateS[0].trim();
                    String edateStr = dateS[1].trim();
                    filter.setSdate(df.parse(sdateStr).getTime());
                    filter.setEdate(df.parse(edateStr).getTime() + 86400000L);
                }
                Calendar instance = Calendar.getInstance();
                if (filter.getEdate() == 0L) {
                    instance.add(Calendar.DATE, 1);
                    String edate = df.format(instance.getTime());
                    long e = df.parse(edate).getTime();
                    filter.setEdate(e);
                    LOGGER.info("Edate: " + edate + "; " + e);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            page.setPage((startIndex + pageSize) / pageSize);
            page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
            page = slgPaymentService.findOrderPage(page, filter);
            ret.put("sEcho", request.getParameter("sEcho"));
            ret.put("aaData", page.getResults());
            ret.put("iTotalRecords", page.getTotalRows());
            ret.put("iTotalDisplayRecords", page.getTotalRows());
        }
        ServletUtils.responseJson(response, ret);
    }


    @RequestMapping(value = "querylist", method = RequestMethod.POST)
    public void querylist(Page<SlgPaymentStatis> page, SlgPaymentStatis filter, HttpServletRequest request, HttpServletResponse response) {
        int startIndex = Integer.parseInt(request.getParameter("iDisplayStart"));
        int pageSize = Integer.parseInt(request.getParameter("iDisplayLength"));
        int checkType = 2;
        String DateRange = filter.getDateRange();
        LOGGER.info("dataRange : " + filter.getDateRange());
        try {
            if (null != DateRange && !DateRange.equals("")) {
                String[] dateS = DateRange.split("&");
                String sdateStr = dateS[0].trim();
                String edateStr = dateS[1].trim();
                filter.setSdate(df.parse(sdateStr).getTime());
                filter.setEdate(df.parse(edateStr).getTime() + 86400000L);
            }
            Calendar instance = Calendar.getInstance();
            if (filter.getEdate() == 0L) {
                instance.add(Calendar.DATE, 1);
                String edate = df.format(instance.getTime());
                long e = df.parse(edate).getTime();
                filter.setEdate(e);
                LOGGER.info("Edate: " + edate +"; " + e);
            }
            if (filter.getSdate() == 0L) {
                instance.add(Calendar.DATE, -30);
                String sdate = df.format(instance.getTime());
                long s = df.parse(sdate).getTime();
                filter.setSdate(s);
                LOGGER.info("Sdate: " + sdate+"; "+ s);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        page.setPage((startIndex + pageSize) / pageSize);
        page.setPageSize(Integer.parseInt(request.getParameter("iDisplayLength")));
        page = slgPaymentService.findPage(page, filter, checkType);
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("sEcho", request.getParameter("sEcho"));
        ret.put("aaData", page.getResults());
        ret.put("iTotalRecords", page.getTotalRows());
        ret.put("iTotalDisplayRecords", page.getTotalRows());
        ServletUtils.responseJson(response, ret);

    }
}
