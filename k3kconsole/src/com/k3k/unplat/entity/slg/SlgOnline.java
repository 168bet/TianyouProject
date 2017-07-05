package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2016/12/8.
 */
public class SlgOnline {
    private String time;
    private int onlinenum;
    private String daytime;

    private int max;
    private int min;
    private int avg;

    //filter
    private String date;
    private String DateRange;
    private long sdate;
    private long edate;
    private int enter;
    private int plat;
    private int union;


    public String getTime(){return time;}
    public void setTime(String time){this.time = time;}
    public int getOnlinenum(){return onlinenum;}
    public void setOnlinenum(int onlinenum){this.onlinenum = onlinenum;}
    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public String getDaytime(){return daytime;}
    public void setDaytime(String daytime){this.daytime = daytime;}

    public void setDateRange(String dateRange) {
        DateRange = dateRange;
    }

    public void setSdate(long sdate) {
        this.sdate = sdate;
    }

    public int getAvg() {
        return avg;
    }

    public void setPlat(int plat) {
        this.plat = plat;
    }

    public int getEnter() {
        return enter;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getPlat() {
        return plat;
    }

    public int getUnion() {
        return union;
    }

    public long getEdate() {
        return edate;
    }

    public long getSdate() {
        return sdate;
    }

    public String getDateRange() {
        return DateRange;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setUnion(int union) {
        this.union = union;
    }

}
