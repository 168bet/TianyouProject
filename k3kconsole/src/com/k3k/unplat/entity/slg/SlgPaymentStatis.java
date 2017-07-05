package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2016/12/9.
 */
public class SlgPaymentStatis {
    private int newnum;   //新增用户
    private int ordernum;  //订单数
    private double allmoney;  //总金额
    private int accountnum; //充值账号
    private String arppu;  //充值总金额/充值账号
    private int loginnum;  //登录账号
    private String payrate; //付费率
    private String arpu;  //充值总金额/登录账号
    private double paymoney; //玩家充值金额
    private long uid; //玩家id
    private String uname; //玩家名称
    private int rank;//排名
    private String channel; //渠道
    private String entry; //入口
    private String platform;//平台
    private String name;
    private int level;
    private String userid;
    private String username;


    //filter
    private String date;
    private String DateRange;
    private long sdate;
    private long edate;
    private String type;
    private int union;
    private int plat;
    private int enter;

    public int getNewnum(){return newnum;}
    public void setNewnum(int newnum){this.newnum = newnum;}
    public int getOrdernum(){return ordernum;}
    public void setOrdernum(int ordernum){this.ordernum = ordernum;}
    public double getAllmoney(){return allmoney;}
    public void setAllmoney(double allmoney){this.allmoney = allmoney;}
    public int getAccountnum(){return accountnum;}
    public void setAccountnum(int accountnum){this.accountnum = accountnum;}
    public String getArppu(){return arppu;}
    public void setArppu(String arppu){this.arppu = arppu;}
    public int getLoginnum(){return loginnum;}
    public void setLoginnum(int loginnum){this.loginnum = loginnum;}
    public String getArpu(){return arpu;}
    public void setArpu(String arpu){this.arpu = arpu;}
    public double getPaymoney(){return paymoney;}
    public void setPaymoney(double paymoney){this.paymoney = paymoney;}
    public long getUid(){return uid;}
    public void setUid(long uid){this.uid = uid;}
    public String getUname(){return uname;}
    public void setUname(String uname){this.uname = uname;}
    public String getPayrate(){return payrate;}
    public void setPayrate(String payrate){this.payrate = payrate;}

    public String getDate(){return date;}
    public void setDate(String date){this.date = date;}
    public String getType(){return type;}
    public void setType(String type){this.type = type;}
    public int getRank(){return rank;}
    public void setRank(int rank){this.rank = rank;}

    public String getDateRange() {
        return DateRange;
    }

    public void setDateRange(String dateRange) {
        DateRange = dateRange;
    }

    public long getEdate() {
        return edate;
    }

    public long getSdate() {
        return sdate;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public void setSdate(long sdate) {
        this.sdate = sdate;
    }

    public int getEnter() {
        return enter;
    }

    public int getUnion() {
        return union;
    }

    public int getPlat() {
        return plat;
    }

    public void setEnter(int enter) {
        this.enter = enter;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
