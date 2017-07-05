package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2017/1/16.
 */
public class SlgUser {
    private int channel;
    private String username;  //账号
    private int server;      //区服
    private Long createtime;
    private String role;       //角色
    private int viplevel;
    private String nickname;
    private String userid;
    private int level;       //任务等级
    private int roomlevel;  //太守府等级
    private long food;     //粮食
    private long wood;    //木头
    private long gunmetal; //青铜
    private long iron;     //铁矿
    private long gold;     //金币
    private long power;   //战力
    private String guild;  //行会
    private String accomplish; //成就完成度
    private int enemykill;  //消灭敌军数
    private int medal;  //勋章获得数
    private int state; //状态
    private String regtime;
    private String bantime;
    private String logtime;
    private int unionid;
    private int subunionid;
    private String union;
    private String entry;
    private String cstate;

    private String keyword;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRoomlevel() {
        return roomlevel;
    }

    public void setRoomlevel(int roomlevel) {
        this.roomlevel = roomlevel;
    }

    public int getEnemykill() {
        return enemykill;
    }

    public int getServer() {
        return server;
    }

    public int getViplevel() {
        return viplevel;
    }

    public int getMedal() {
        return medal;
    }

    public long getFood() {
        return food;
    }

    public long getGold() {
        return gold;
    }

    public long getGunmetal() {
        return gunmetal;
    }

    public long getIron() {
        return iron;
    }

    public long getPower() {
        return power;
    }

    public String getUserid() {
        return userid;
    }

    public long getWood() {
        return wood;
    }

    public String getAccomplish() {
        return accomplish;
    }

    public int getChannel() {
        return channel;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public String getGuild() {
        return guild;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setAccomplish(String accomplish) {
        this.accomplish = accomplish;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public void setEnemykill(int enemykill) {
        this.enemykill = enemykill;
    }

    public void setFood(long food) {
        this.food = food;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public void setGunmetal(long gunmetal) {
        this.gunmetal = gunmetal;
    }

    public void setIron(long iron) {
        this.iron = iron;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMedal(int medal) {
        this.medal = medal;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setViplevel(int viplevel) {
        this.viplevel = viplevel;
    }

    public void setWood(long wood) {
        this.wood = wood;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getSubunionid() {
        return subunionid;
    }

    public int getUnionid() {
        return unionid;
    }

    public String getLogtime() {
        return logtime;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
    }

    public void setSubunionid(int subunionid) {
        this.subunionid = subunionid;
    }

    public void setUnionid(int unionid) {
        this.unionid = unionid;
    }

    public String getEntry() {
        return entry;
    }

    public String getUnion() {
        return union;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getBantime() {
        return bantime;
    }

    public void setBantime(String bantime) {
        this.bantime = bantime;
    }

    public String getCstate() {
        return cstate;
    }

    public void setCstate(String cstate) {
        this.cstate = cstate;
    }
}
