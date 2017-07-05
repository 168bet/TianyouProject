package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2016/12/8.
 */
public class SlgLevelStatis {
    private String userlv; //玩家等级对应(1~5级)、(6~10级).....、(46~50级),或者为对应的战斗力(1~99999)。
    private int usernum;
    private String powerid;
    private String uid;

    private String type;    //1为玩家等级 , 2为玩家战斗力 ,3为建筑统计

    private String bdlv; //id为1~10,分布对应(1~5级)、(6~10级).....、(46~50级)。
    private String bdid;
    private int bdnum;
    private String bdname;

    //filter
    private String dateRange;
    private String date;

    public String getUserlv(){return userlv;}
    public void setUserlv(String userlv){this.userlv = userlv;}
    public int getUsernum(){return usernum;}
    public void setUsernum(int usernum){this.usernum = usernum;}
    public String getPowerID(){return powerid;}
    public void setPowerID(String powerid){this.powerid = powerid;}
    public String getUid(){return uid;}
    public void setUid(String uid){this.uid = uid;}
    public String getType(){return type;}
    public void setType(String type){this.type = type;}
    public String getBdlv(){return bdlv;}
    public void setBdlv(String bdlv){this.bdlv = bdlv;}
    public String getBdid(){return bdid;}
    public void setBdid(String bdid){this.bdid = bdid;}
    public int getBdnum(){return bdnum;}
    public void setBdnum(int bdnum){this.bdnum = bdnum;}
    public String getBdname(){return bdname;}
    public void setBdname(String bdname){this.bdname = bdname;}
    public String getDateRange(){return dateRange;}
    public void setDateRange(String dateRange){this.dateRange = dateRange;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}


}
