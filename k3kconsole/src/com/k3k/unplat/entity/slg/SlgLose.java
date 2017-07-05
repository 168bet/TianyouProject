package com.k3k.unplat.entity.slg;

/** 流失率
 * Created by jef on 2017/2/27.
 */
public class SlgLose {
    private int regnum;
    private int lognum;
    private int losenum2;
    private int losenum3;
    private int losenum4;
    private int losenum5;
    private int losenum6;
    private int losenum7;

    private String loserate2;
    private String loserate3;
    private String loserate4;
    private String loserate5;
    private String loserate6;
    private String loserate7;

    private String date;
    private String channel;

    //filter
    private int plat;
    private int enter;
    private int union;
    private String DateRange;
    private long sdate;
    private long edate;
    private String buildinglevel;

    public int getLognum() {
        return lognum;
    }

    public int getLosenum2() {
        return losenum2;
    }

    public int getLosenum3() {
        return losenum3;
    }

    public int getLosenum4() {
        return losenum4;
    }

    public int getLosenum5() {
        return losenum5;
    }

    public int getLosenum6() {
        return losenum6;
    }

    public int getLosenum7() {
        return losenum7;
    }

    public int getRegnum() {
        return regnum;
    }

    public String getLoserate2() {
        return loserate2;
    }

    public String getLoserate3() {
        return loserate3;
    }

    public String getLoserate4() {
        return loserate4;
    }

    public String getLoserate5() {
        return loserate5;
    }

    public String getLoserate6() {
        return loserate6;
    }

    public String getLoserate7() {
        return loserate7;
    }

    public void setLognum(int lognum) {
        this.lognum = lognum;
    }

    public void setLosenum2(int losenum2) {
        this.losenum2 = losenum2;
    }

    public void setLosenum3(int losenum3) {
        this.losenum3 = losenum3;
    }

    public void setLosenum4(int losenum4) {
        this.losenum4 = losenum4;
    }

    public void setLosenum5(int losenum5) {
        this.losenum5 = losenum5;
    }

    public void setLosenum6(int losenum6) {
        this.losenum6 = losenum6;
    }

    public void setLosenum7(int losenum7) {
        this.losenum7 = losenum7;
    }

    public void setLoserate2(String loserate2) {
        this.loserate2 = loserate2;
    }

    public void setLoserate3(String loserate3) {
        this.loserate3 = loserate3;
    }

    public void setLoserate4(String loserate4) {
        this.loserate4 = loserate4;
    }

    public void setLoserate5(String loserate5) {
        this.loserate5 = loserate5;
    }

    public void setLoserate6(String loserate6) {
        this.loserate6 = loserate6;
    }

    public void setLoserate7(String loserate7) {
        this.loserate7 = loserate7;
    }

    public void setRegnum(int regnum) {
        this.regnum = regnum;
    }

    public int getPlat() {
        return plat;
    }

    public int getEnter() {
        return enter;
    }

    public long getEdate() {
        return edate;
    }

    public long getSdate() {
        return sdate;
    }

    public String getChannel() {
        return channel;
    }

    public String getDate() {
        return date;
    }

    public String getDateRange() {
        return DateRange;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDateRange(String dateRange) {
        DateRange = dateRange;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public void setSdate(long sdate) {
        this.sdate = sdate;
    }

    public String getBuildinglevel() {
        return buildinglevel;
    }

    public void setBuildinglevel(String buildinglevel) {
        this.buildinglevel = buildinglevel;
    }

    public int getUnion() {
        return union;
    }

    public void setUnion(int union) {
        this.union = union;
    }
}
