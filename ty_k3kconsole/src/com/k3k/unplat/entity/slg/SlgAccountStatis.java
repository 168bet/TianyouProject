package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2016/12/7.
 */
public class SlgAccountStatis {
    private int accountnum;
    private int regnum;
    private int loginnum;
    private int activenum;
    private int newusernum;
    private int usernum;
    private String day;
    private String channel; //渠道
    private String entry; //入口
    private String platform;//平台

    private long sdate;
    private long edate;
    private String dateRange;
    private int enter;
    private int union;
    private int plat;


    public int getAccountnum(){return accountnum;}
    public void setAccountnum(int accountnum){this.accountnum = accountnum;}
    public int getRegnum(){return regnum;}
    public void setRegnum(int regnum){this.regnum = regnum;}
    public int getLoginnum(){return loginnum;}
    public void setLoginnum(int loginnum){this.loginnum = loginnum;}
    public int getActivenum(){return activenum;}
    public void setActivenum(int activenum){this.activenum = activenum;}
    public int getNewusernum(){return newusernum;}
    public void setNewusernum(int newusernum){this.newusernum = newusernum;}
    public int getUsernum(){return usernum;}
    public void setUsernum(int usernum){this.usernum = usernum;}
    public String getDay(){return day;}
    public void setDay(String day){this.day = day;}

    public long getSdate(){return sdate;}
    public void setSdate(long sdate){this.sdate = sdate;}
    public long getEdate(){return edate;}
    public void setEdate(long edate){this.edate = edate;}
    public String getDateRange(){return dateRange;}
    public void setDateRange(String dateRange){this.dateRange = dateRange;}

    public void setUnion(int union) {
        this.union = union;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public int getUnion() {
        return union;
    }

    public int getPlat() {
        return plat;
    }

    public int getEnter() {
        return enter;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public String getEntry() {
        return entry;
    }

    public String getPlatform() {
        return platform;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
