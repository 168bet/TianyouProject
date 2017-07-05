package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2016/12/8.
 */
public class SlgUserPreserve {
    private String day;
    private int regnum;
    private int lognum;
    private int active2;
    private int active3;
    private int active4;
    private int active5;
    private int active6;
    private int active7;
    private String rate2;
    private String rate3;
    private String rate4;
    private String rate5;
    private String rate6;
    private String rate7;
    private String channel; //渠道
    private String entry; //入口
    private String platform;//平台

    //filter
    private String dateRange;
    private long sdate;
    private long edate;
    private int enter;
    private int union;
    private int plat;

    public String getDay(){return day;}
    public void setDay(String day){this.day = day;}
    public int getRegnum(){return regnum;}
    public void setRegnum(int regnum){this.regnum = regnum;}
    public int getActive2(){return active2;}
    public void setActive2(int active2){this.active2 = active2;}
    public int getActive3(){return active3;}
    public void setActive3(int active3){this.active3 = active3;}
    public int getActive7(){return active7;}
    public void setActive7(int active7){this.active7 = active7;}
    public String getRate2(){return rate2;}
    public void setRate2(String rate2){this.rate2 = rate2;}
    public String getRate3(){return rate3;}
    public void setRate3(String rate3){this.rate3 = rate3;}
    public String getRate7(){return rate7;}
    public void setRate7(String rate7){this.rate7 = rate7;}

    public String getDateRange(){return dateRange;}
    public void setDateRange(String dateRange){this.dateRange = dateRange;}
    public long getSdate(){return sdate;}
    public void setSdate(long sdate){this.sdate = sdate;}
    public long getEdate(){return edate;}
    public void setEdate(long edate){this.edate = edate;}

    public int getLognum() {
        return lognum;
    }

    public void setLognum(int lognum) {
        this.lognum = lognum;
    }

    public int getActive4() {
        return active4;
    }

    public int getActive5() {
        return active5;
    }

    public int getActive6() {
        return active6;
    }

    public String getRate4() {
        return rate4;
    }

    public String getRate5() {
        return rate5;
    }

    public void setActive4(int active4) {
        this.active4 = active4;
    }

    public void setActive5(int active5) {
        this.active5 = active5;
    }

    public String getRate6() {
        return rate6;
    }

    public void setActive6(int active6) {
        this.active6 = active6;
    }

    public void setRate4(String rate4) {
        this.rate4 = rate4;
    }

    public void setRate5(String rate5) {
        this.rate5 = rate5;
    }

    public void setRate6(String rate6) {
        this.rate6 = rate6;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getEnter() {
        return enter;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public int getPlat() {
        return plat;
    }

    public int getUnion() {
        return union;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public void setUnion(int union) {
        this.union = union;
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
}
