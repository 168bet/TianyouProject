package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2017/2/23.
 */
public class SlgOrder {
    private String ordertime;
    private String account;
    private String nickname;
    private String orderid;
    private String money;
    private String paytype;
    private long uid;
    private String uuid;

    private String DateRange;
    private long sdate;
    private long edate;
    private int union;
    private int subunion;
    private int plat;
    private int enter;
    private String userid;
    private String username;

    public void setPlat(int plate) {
        this.plat = plate;
    }

    public void setUnion(int union) {
        this.union = union;
    }

    public void setEnter(int enter) {
        this.enter = enter;
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

    public String getAccount() {
        return account;
    }

    public String getDateRange() {
        return DateRange;
    }

    public String getMoney() {
        return money;
    }

    public String getNickname() {
        return nickname;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public int getUnion() {
        return union;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDateRange(String dateRange) {
        DateRange = dateRange;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public void setSdate(long sdate) {
        this.sdate = sdate;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setSubunion(int subunion) {
        this.subunion = subunion;
    }

    public int getSubunion() {
        return subunion;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

