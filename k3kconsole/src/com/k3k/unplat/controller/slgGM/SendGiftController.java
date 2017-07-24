package com.k3k.unplat.controller.slgGM;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.slg.SlgUserlist;
import com.k3k.unplat.service.slg.SlgLevelService;
import com.k3k.unplat.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jef on 2017/1/16.
 */
@Controller
@RequestMapping(value = "slgift")
public class SendGiftController {


    @Autowired
    private SlgLevelService slgLevelService;

    private static final Log LOGGER = LogFactory.getLog(SendGiftController.class);

    @RequestMapping(value = "list")
    public ModelAndView slgGift() {
        /**
         * 查看SLG发奖管理，可以根据相应条件筛选
         */

        ModelAndView mav = new ModelAndView();
        LOGGER.info("SJL Gift Start");
        mav.setViewName("admin/SLG_GM/slg_giftsend");
        return mav;
    }

    @RequestMapping(value = "send")
    public void sendGift(HttpServletRequest request, HttpServletResponse response) {
        String title = StringUtils.isNotBlank(request.getParameter("title")) ? request.getParameter("title") : "";
        String content = StringUtils.isNotBlank(request.getParameter("content")) ? request.getParameter("content") : "";
        String server = StringUtils.isNotBlank(request.getParameter("server")) ? request.getParameter("server") : "0";
        String unionid = StringUtils.isNotBlank(request.getParameter("union")) ? request.getParameter("union") : "0";
        String gift = StringUtils.isNotBlank(request.getParameter("gift")) ? request.getParameter("gift") : "";
        String type = request.getParameter("type");
        LOGGER.info("发送礼物信息: " + server + ";" + unionid + ";" + title + ";" + content + ";" + type);
        Map<String, Object> ret = new HashMap<String, Object>();
        LOGGER.info("礼物列表: " + gift);
        String list = "";
        int ifsend = 0;  //判断用户数量是否正常,允许发送。 0-允许
        if (StringUtils.isBlank(title) || StringUtils.isBlank(content) || ("[]").equals(gift)) {
            ret.put("message", "邮件标题、内容、礼物不能为空!");
            ret.put("result", 4);
        } else {
            try {
                if (("2".equals(type))) {
                    //上传Excel名单
                    List<SlgUserlist> ulist = SlgUtils.uploadfile(request);
                    LOGGER.info("Excel List:" + ulist.size());
                    if (ulist.size() > 0) {
                        if (ulist.size() < 100) {
                            String uid = slgLevelService.getUserID(ulist.get(0).getUname());
                            if (uid != null && uid != "") {
                                list += uid;
                            }
                            if (ulist.size() > 1) {
                                for (int i = 1; i < ulist.size(); i++) {
                                    uid = slgLevelService.getUserID(ulist.get(i).getUname());
                                    if (uid != null && uid != "") {
                                        list += ("," + uid);
                                    }
                                    LOGGER.info("Uname: " + ulist.get(i).getUname());
                                }
                            }
                        } else {
                            ifsend = 1;
                            ret.put("message", "发送用户过多,暂不支持");
                            ret.put("result", 6);
                        }
                    } else {
                        ifsend = 1;
                        ret.put("message", "发送失败,指定名单为空");
                        ret.put("result", 6);
                    }
                } else {
                    List<SlgUserlist> userlist = slgLevelService.getUserList(-1, 0, 0, Integer.valueOf(unionid));
                    LOGGER.info("User list Size: " + userlist.size());
                    if (userlist.size() > 50) {
                        int result = 0;
                        int succ_num = 0;
                        int count = 1;
                        int times = userlist.size() / 50;
                        if(times > 20){
                            int min = times/40;
                            ret.put("message", "系统处理中,请耐心等待(预计"+min+"~"+(min+1)+"分钟)");
                            ServletUtils.responseJson(response, ret);
                        }
                        while (count <= times) {
                            list = String.valueOf(userlist.get((count - 1) * 50).getUid());
                            for (int i = ((count - 1) * 50 + 1); i < count * 50; i++) {
                                list += "," + userlist.get(i).getUid();
                            }
                            result = sendResult(title, content, list, gift);
                            if(result == 0){
                                succ_num += 1;
                            }
                            ret.put("result", result);
                            count += 1;
                        }
                        if (userlist.size() > count * 50) {
                            list = String.valueOf(userlist.get(count * 50).getUid());
                            for (int i = (count * 50 + 1); i < userlist.size(); i++) {
                                list += "," + userlist.get(i).getUid();
                            }
                            if(result == 0){
                                succ_num += 1;
                            }
                            result = sendResult(title, content, list, gift);
                            ret.put("result", result);
                            list = "";
                        }
                        ret.put("message", "已发送 ");
                        ret.put("info",("SendCount:"+count+"SuccNum:"+succ_num));
                        ifsend = 1;
                    } else {
                        if (userlist.size() > 0) {
                            list = String.valueOf(userlist.get(0).getUid());
                            if (userlist.size() > 1) {
                                for (int i = 1; i < userlist.size(); i++) {
                                    list += "," + userlist.get(i).getUid();
                                }
                            }
                        }
                    }
                }
                if (ifsend == 0) {
                    if (StringUtils.isBlank(list)) {
                        ret.put("message", "已发送");
                        ret.put("result", 3);
                    } else {
                        int result = sendResult(title, content, list, gift);
                        ret.put("result", result);
                        if (result == 0) {
                            ret.put("message", "发送成功");
                        } else if (result == 3) {
                            ret.put("message", "已发送");
                        } else {
                            ret.put("message", "发送失败");
                        }
                    }
                }
            } catch (Exception e) {
                ret.put("message", "发送失败,用户信息错误");
                ret.put("result", 5);
                LOGGER.error("发送邮件 获取用户信息: " + e.getMessage());
            }
        }
        ServletUtils.responseJson(response, ret);
    }

    @RequestMapping(value = "kick")
    public void kickUser(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String nick = StringUtils.isNotBlank(request.getParameter("nick")) ? request.getParameter("nick") : "";
        LOGGER.info("踢出玩家昵称:" + nick);
        if (StringUtils.isBlank(nick)) {
            ret.put("message", "玩家昵称不能为空!");
            ret.put("result", 1);
        } else {
            String userid = slgLevelService.getUserID(nick);
            LOGGER.info("踢出玩家ID:" + userid);
            String code = "requestType=kickUser&" + "toUid=" + userid;
            LOGGER.info("踢出玩家 Code:" + code + "&" + Constants.SLG_KEY);
            String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
            LOGGER.info("踢出玩家 Token:" + token);
            String sendInfo = "toUid=" + userid + "&requestType=kickUser" + "&token=" + token;
            LOGGER.info("Send Info: " + sendInfo);
            try {
                String sendGet = HttpUtils.sendGet(Constants.SLG_SERVER, sendInfo, "");
                //sendGet = "{'code':0}";
                LOGGER.info("调用接口返回信息:" + sendGet);
                ret.put("message", "踢出玩家成功!");
                ret.put("msgcode", sendGet);
                ret.put("result", 0);
            } catch (Exception e) {
                LOGGER.error("踢出玩家" + e.getMessage());
                ret.put("message", "踢出玩家失败,接口错误!");
                ret.put("msgcode", 400);
                ret.put("result", 2);
            }
        }
        ServletUtils.responseJson(response, ret);
    }

    public int sendResult(String title, String content, String list, String gift) {
        title = EncodeUtils.urlEncode(title);
        content = EncodeUtils.urlEncode(content);
        LOGGER.info("Title: " + title);
        LOGGER.info("Content: " + content);
        //String code = "contents=" + content + "&gift=" + gift + "&requestType=SendGift&title=" + title + "&toUid=9481000001";//测试
        String code = "contents=" + content + "&gift=" + gift + "&requestType=SendGift&title=" + title + "&toUid=" + list;
        LOGGER.info("Code: " + code);
        String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
        LOGGER.info("Token:" + token);
       // String sendInfo = "requestType=SendGift&toUid=9481000001" + "&contents=" + content + "&title=" + title + "&gift=" + gift + "&token=" + token;//测试
        String sendInfo = "requestType=SendGift&toUid=" + list + "&contents=" + content + "&title=" + title + "&gift=" + gift + "&token=" + token;
        LOGGER.info("Send Info: " + sendInfo);
        int result = SlgUtils.sendResult(sendInfo);
        LOGGER.info(result);
        return result;
    }


}

