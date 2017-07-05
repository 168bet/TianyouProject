package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2017/2/24.
 */
public class SlgLevel {
    private int rank;
    private String level;
    private long uid;
    private String nickname;

    //filter
    private int enter;
    private int type;

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public int getEnter() {
        return enter;
    }

    public int getRank() {
        return rank;
    }

    public String getNickname() {
        return nickname;
    }

    public void setEnter(int enter) {
        this.enter = enter;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
