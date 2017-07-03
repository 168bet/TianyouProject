package com.k3k.unplat.controller.slg;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.slg.SlgImg;
import com.k3k.unplat.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by jef on 2017/3/3.
 */
@Controller
@RequestMapping(value = "slgimg")
public class SlgImgController {

    private static final Log LOGGER = LogFactory.getLog(SlgLoseController.class);


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView slgImg() {
        /**
         * 查看SLG头像审核，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        mav.setViewName("admin/SLG_GM/slg_headimg");
        return mav;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public void query(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String code = "requestType=listPicVer";
        LOGGER.info("GET PIC: " + " Code:" + code + "&" + Constants.SLG_KEY);
        String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
        LOGGER.info("GET PIC: " + " Token:" + token);
        String sendInfo = "requestType=listPicVer&token=" + token;
        LOGGER.info("GET PIC: " + " Send Info: " + sendInfo);
        List<SlgImg> result = new ArrayList<SlgImg>();
        try {
            String sendGet = HttpUtils.sendGet(Constants.SLG_SERVER, sendInfo, "");
            LOGGER.info("GET PIC  Info: " + sendGet);
            if (StringUtils.isNotBlank(sendGet)) {
                JSONObject obj = JSONObject.fromObject(sendGet);
                if (obj.has("baseUrl")) {
                    String baseUrl = obj.get("baseUrl").toString();
                    LOGGER.info("BaseUrl:" + baseUrl);
                    if (obj.has("photoes")) {
                        String photoes = obj.get("photoes").toString();
                        String photoStr = photoes.substring(1, photoes.length() - 1);
                        LOGGER.info("Photo:" + photoStr);
                        List<String> resultStr = Arrays.asList(photoStr.split(","));
                        for (int i = 0; i < resultStr.size(); i++) {
                            LOGGER.info("HeadIMG "+ i + ":" + resultStr.get(i));
                            List<String> imgStr = Arrays.asList(resultStr.get(i).split(":"));
                            SlgImg img = new SlgImg();
                            img.setUid(imgStr.get(0).replace("\"", ""));
                            String imgAdr = baseUrl + imgStr.get(1).replace("\"", "");
                            LOGGER.info("Img Address :" + imgAdr);
                            img.setHeadimg(imgAdr);
                            result.add(img);
                        }
                        LOGGER.info("Result Size:"+result.size());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        ret.put("sEcho", request.getParameter("sEcho"));
        ret.put("aaData", result);
        ret.put("iTotalRecords", result.size());
        ret.put("iTotalDisplayRecords", result.size());
        ServletUtils.responseJson(response, ret);
    }

    @RequestMapping(value = "done", method = RequestMethod.POST)
    public void done(HttpServletRequest request, HttpServletResponse response) {
        //头像审核
        Map<String, Object> ret = new HashMap<String, Object>();
        String logID = "[IMG DONE]: ";
        String userid = StringUtils.isNotBlank(request.getParameter("userid")) ? request.getParameter("userid") : "";
        String type = StringUtils.isNotBlank(request.getParameter("type")) ? request.getParameter("type") : "";
        LOGGER.info("头像审核: Type:" + type + "; UserID:" + userid);

        String code = "requestType="+type+"&" + "toUid=" + userid;
        LOGGER.info(logID+" Code:" + code + "&" + Constants.SLG_KEY);
        String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
        LOGGER.info(logID+" Token:" + token);
        String sendInfo = "toUid=" + userid + "&requestType="+type+ "&token=" + token;
        LOGGER.info(logID+" Send Info: " + sendInfo);
        try {
            int result = SlgUtils.sendResult(sendInfo);
            LOGGER.info(logID+" 调用接口结果:" + result);
            ret.put("result", result);
        } catch (Exception e) {
            LOGGER.error("封禁玩家:" + e.getMessage());
            ret.put("result", 2);
        }
        ServletUtils.responseJson(response, ret);
    }

}
