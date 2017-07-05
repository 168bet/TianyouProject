package com.k3k.unplat.controller.slgMail;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.slg.SlgUserlist;
import com.k3k.unplat.service.slg.SlgLevelService;
import com.k3k.unplat.utils.*;
import net.sf.json.JSONObject;
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
import java.util.List;
import java.util.Map;

/**
 * Created by jef on 2017/1/16.
 */
@Controller
@RequestMapping(value = "slgmail")
public class MailController {
    private static final Log LOGGER = LogFactory.getLog(MailController.class);


    @Autowired
    private SlgLevelService slgLevelService;

    @RequestMapping(value = "add")
    public ModelAndView slgNewMail() {
        /**
         * 查看SLG新增邮件，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        LOGGER.info("SJL New Mail Start");
        mav.setViewName("admin/SLG_Mail/slg_newmail");
        return mav;
    }

    @RequestMapping(value = "list")
    public ModelAndView slgMailList() {
        /**
         * 查看SLG新增邮件，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        LOGGER.info("SJL Mail List Start");
        mav.setViewName("admin/SLG_Mail/slg_mailist");
        return mav;
    }

    @RequestMapping(value = "notice")
    public ModelAndView slgNotice() {
        /**
         * 查看SLG新增公告，可以根据相应条件筛选
         */
        ModelAndView mav = new ModelAndView();
        LOGGER.info("SJL Notice Start");
        mav.setViewName("admin/SLG_Mail/slg_notice");
        return mav;
    }

    @RequestMapping(value = "send", method = RequestMethod.POST)
    public void send(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String server = StringUtils.isNotBlank(request.getParameter("server")) ? request.getParameter("server") : "0";
        String level = StringUtils.isNotBlank(request.getParameter("level")) ? request.getParameter("level") : "-1";
        String viplevel = StringUtils.isNotBlank(request.getParameter("viplevel")) ? request.getParameter("viplevel") : "-1";
        String title = StringUtils.isNotBlank(request.getParameter("title")) ? request.getParameter("title") : "";
        String content = StringUtils.isNotBlank(request.getParameter("content")) ? request.getParameter("content") : "";
        String mailid = StringUtils.isNotBlank(request.getParameter("mailid")) ? request.getParameter("mailid") : "";
        String unionid = StringUtils.isNotBlank(request.getParameter("union")) ? request.getParameter("union") : "0";
        LOGGER.info("新增邮件信息: " + server + ";" + level + ";" + viplevel + ";" + title + ";" + content + ";" + mailid);
        String list = "";
        int ifsend = 0;  //判断用户数量是否正常,允许发送。 0-允许
        int sendType = 0;
        try {
            //上传Excel名单
            List<SlgUserlist> ulist = SlgUtils.uploadfile(request);
            LOGGER.info("Excel List:" + ulist.size());
            if (ulist.size() > 0) {
                sendType = 1;
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
            } else if (StringUtils.isBlank(mailid)) {
                int slevel = 0;
                int elevel = 0;
                sendType = 1;
                if (!("-1").equals(level)) {
                    slevel = Integer.valueOf(level) * 10 + 1;
                    elevel = (Integer.valueOf(level) + 1) * 10;
                }
                List<SlgUserlist> userlist = slgLevelService.getUserList(Integer.valueOf(viplevel), slevel, elevel, Integer.valueOf(unionid));
                LOGGER.info("User list Size: " + userlist.size());
                if (userlist.size() > 50) {
                    int result = 0;
                    int count = 1;
                    int succ_num = 0;
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
                        result = sendResult(title,content,list,sendType,"");
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
                        result = sendResult(title, content, list, sendType,"");
                        if(result == 0){
                            succ_num += 1;
                        }
                        ret.put("result", result);
                    }
                    LOGGER.info("User List Send Times:"+count);
                    ret.put("message", "已发送");
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
                    LOGGER.info("User list Info: " + list);
                }
            }
            if (ifsend == 0) {
                LOGGER.info("Send Type: " + sendType);
                int result = sendResult(title, content, list, sendType, mailid);
                ret.put("result", result);
                if (result == 0) {
                    ret.put("message", "发送成功");
                } else if (result == 3) {
                    ret.put("message", "已发送");
                } else {
                    ret.put("message", "发送失败");
                }
            }
        } catch (Exception e) {
            ret.put("message", "发送失败");
            ret.put("result", 5);
            LOGGER.error("发送邮件" + e.getMessage());
        }
        ServletUtils.responseJson(response, ret);
    }


    @RequestMapping(value = "sendNotice", method = RequestMethod.POST)
    public void sendNotice(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String msg = StringUtils.isNotBlank(request.getParameter("msg")) ? request.getParameter("msg") : "";
        LOGGER.info("公告内容:" + msg);
        if (StringUtils.isBlank(msg)) {
            ret.put("message", "发送公告失败,内容不能为空");
            ret.put("result", 1);
        } else {
            msg = EncodeUtils.urlEncode(msg);
            String code = "msg=" + msg + "&requestType=pushNotice&toUid=0";
            LOGGER.info("发送公告 code:" + code);
            String token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
            LOGGER.info("发送公告 token:" + token);
            String sendinfo = code + "&token=" + token;
            LOGGER.info("发送公告 SendInfo:" + sendinfo);
            try {
                String sendGet = HttpUtils.sendGet(Constants.SLG_SERVER, sendinfo, "");
                LOGGER.info("发送公告 调用接口返回信息:" + sendGet);
                if (StringUtils.isNotBlank(sendGet)) {
                    JSONObject obj = JSONObject.fromObject(sendGet);
                    if (!obj.get("code").equals(0)) {
                        ret.put("message", "发送失败 " + sendGet);
                        ret.put("result", 2);
                    } else {
                        ret.put("message", "发送成功");
                        ret.put("result", 0);
                    }
                } else {
                    ret.put("message", "已发送");
                    ret.put("result", 1);
                }
            } catch (Exception e) {
                LOGGER.error("发送公告 " + e.getMessage());
                ret.put("message", "发送失败");
                ret.put("result", 3);
            }
        }
        ServletUtils.responseJson(response, ret);
    }

    public int sendResult(String title, String content, String list, int sendType, String mailid) {
        String code;
        String token;
        String sendInfo;
        if (sendType == 1) {
            title = EncodeUtils.urlEncode(title);
            content = EncodeUtils.urlEncode(content);
            LOGGER.info("Title: " + title);
            LOGGER.info("Content: " + content);
           // code = "contents=" + content + "&requestType=SendMail&title=" + title + "&toUid=9481000001" ; //测试
            code = "contents=" + content + "&requestType=SendMail&title=" + title + "&toUid=" + list;
            token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
           // sendInfo = "requestType=SendMail&toUid=9481000001" + "&contents=" + content + "&title=" + title + "&token=" + token;//测试
            sendInfo = "requestType=SendMail&toUid=" + list + "&contents=" + content + "&title=" + title + "&token=" + token;
        } else {
            code = "mailId=" + mailid + "&requestType=pushMail&toUid=0";
            token = MD5Utils.string2MD5(code + "&" + Constants.SLG_KEY);
            sendInfo = "mailId=" + mailid + "&requestType=pushMail&toUid=0&token=" + token;
        }
        LOGGER.info("Code: " + code);
        LOGGER.info("Token:" + token);
        LOGGER.info("Send Info: " + sendInfo);

        int result = SlgUtils.sendResult(sendInfo);

        return result;
    }


}

