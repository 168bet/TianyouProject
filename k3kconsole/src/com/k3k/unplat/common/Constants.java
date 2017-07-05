package com.k3k.unplat.common;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public abstract class Constants {


    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final DateFormat DAYID_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

    public static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00");

    public static final String CONFIG_FILE = "config.properties";

    public static final String PAGE_SIZE = "page.pageSize";

    public static final String CURRENT_USER = "current_user";



    /********************************
     * SEM
     ******************************************/

    public static final String[] ALIYUN_CDN_DOMAIN_ARRAY = new String[]{"dasdadadas.adsdasdak.com"};

    public static final String ALIYUN_CDN_ACCESS_KEY_ID = "iasdasdasdad";

    public static final String ALIYUN_CDN_ACCESS_KEY_SECRET = "Uasdasdasdasda";



    /***************************************
     * SLG
     ******************************/

    public static final String SLG_KEY = "da04D11141A655a9cb85F356bcC8dA69";


    public static final String SLG_SERVER = "http://121.42.230.216:8181/web/gm";//正式
    //public static final String SLG_SERVER = "http://139.224.35.230:8181/web/gm"; //测试



}
