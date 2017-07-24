package com.k3k.unplat.entity.slg;

/**
 * Created by jef on 2017/1/16.
 */
public class SlgMail {
    private int id;
    private String title;
    private String addtime;
    private String admin;
    private String content;

    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}
    public String getAddtime(){return addtime;}
    public void setAddtime(String addtime){this.addtime = addtime;}
    public String getAdmin(){return admin;}
    public void setAdmin(String admin){this.admin = admin;}
    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}
}
