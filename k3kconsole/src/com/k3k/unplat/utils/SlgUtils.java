package com.k3k.unplat.utils;


import com.k3k.unplat.common.Constants;
import com.k3k.unplat.entity.slg.SlgUserlist;
import com.k3k.unplat.utils.excel.ExcelReader;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jef on 2017/2/23.
 */
public class SlgUtils {
    private static final Log LOGGER = LogFactory.getLog(SlgUtils.class);

    public static String getUnionInfo(String db,int enter,int union,int plat) {
        String result = " ";
        String un = "unionId";
        String sub = "subUnionId";
        if(StringUtils.isNotBlank(db)){
            un = db+".unionId";
            sub = db+".subUnionId";
        }
        if(enter != 0){
            if(enter == 1){
                result +=( " and "+un+" in(1,2,4,5,6,7,8,9,10,16) ");
            }else {
                result += (" and "+un+" in(12,13,14,15) ");
            }
        }
        if(plat != 0){
            if(plat ==1){
                result += (" and "+sub+" = 101 ");
            }else {
                result += (" and "+sub+" <> 101 ");
            }
        }
        if(union != 0){
            result += ("and "+un+" ="+union+" ");
        }
        return result;
    }

    //获取游戏入口
    public static String getEntry(int union) {
        String result ="所有";
        if(union == 1){
            result = "列王传奇";
        }else if(union == 2){
            result = "大秦帝国战争";
        }else if(union == 4){
            result = "传奇指挥官";
        }else if(union == 5){
            result = "远征部落";
        }else if(union == 6){
            result = "军团帝国";
        }else if(union == 7){
            result = "帝国传奇战争";
        }else if(union == 8){
            result = "WOK列王之战";
        }else if(union == 9){
            result = "皇室传奇之争";
        }else if(union == 10){
            result = "即刻出兵";
        }else if(union == 12){
            result = "皇室英雄";
        }else if(union == 13){
            result = "皇室传奇之争";
        }else if(union == 14){
            result = "帝国王者";
        }else if(union == 15){
            result = "帝国荣耀";
        }else if(union == 16){
            result = "文明起源";
        }
        return result;
    }

    //获取平台
    public static String getPlatform(int plat) {
        String result = "所有";
        if(plat == 1){
            result = "IOS";
        }else if(plat ==2){
            result = "Android";
        }
        return result;
    }

    //获取渠道
    public static String getUnion(int enter){
        String result = "所有";
        if(enter == 1){
            result ="天游";
        }else if(enter ==2){
            result = "闲趣";
        }
        return result;
    }

    //根据查询的日期判断该查询的数据库表 如:日期为:2016_12_02 ,所查的表为stat_login_2016_12
    public static String getDataBase(long day){
        String dbInfo = new SimpleDateFormat("yyyy_MM").format(new Date(day));
        String str1 = dbInfo.substring(0, 4);
        int year = Integer.valueOf(str1);
        String str2 = dbInfo.substring(dbInfo.length() - 2, dbInfo.length());
        int month = Integer.valueOf(str2);
        dbInfo = String.valueOf(year) + "_" + String.valueOf(month);

        return dbInfo;
    }

    public static int sendResult (String sendinfo){
        int result = 0;
        try {
            String sendGet = HttpUtils.sendGet(Constants.SLG_SERVER, sendinfo, "");
            //String sendGet = "{'code':0}";
            LOGGER.info("SendGet:"+sendGet);
            if(StringUtils.isNotBlank(sendGet)){
                JSONObject obj = JSONObject.fromObject(sendGet);
                if (!obj.get("code").equals(0)) {
                    result = 1;
                }
            }else{
                result = 3;
            }
        } catch (Exception e) {
            result = 2;
        }
        return result;
    }

    public static List<SlgUserlist> uploadfile(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        List<SlgUserlist> list = new ArrayList<SlgUserlist>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            LOGGER.info("指定名单: " + entry.getKey() + " " + entry.getValue().getOriginalFilename());
            MultipartFile file = entry.getValue();
            try {
                InputStream is = file.getInputStream();
                ExcelReader reader = new ExcelReader();
                list = reader.getUserDatas(is);
                LOGGER.info("Get User List Size: " + list.size());
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(getUnionInfo("",1,14,2));
    }

}
