package com.k3k.unplat.entity;


/**
 * Created by jef on 2016/12/10.
 */
public class LoginInfo {
    private int id;
    private String username; //登录用户名
    private String time;       //登录时间
    private String ip;       //登录ip
    private int result;      //登录结果 : 0-成功、1-失败
    private String info;     //登录结果详情: "登录成功!"; "失败:账号不存在", "失败:密码错误"！, "失败:用户账号被锁定!", "失败:登录异常!"
    private String host;     //获取host名 如:adminconsole,console
    private String Country;
    private String Province;
    private String City;
    private String ipinfo;  //如103.238.43.223【中国 江苏 无锡】
    private String loginaddress;

    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getTime(){return time;}
    public void setTime(String time){this.time = time ;}
    public String getIp(){return ip;}
    public void setIp(String ip){this.ip = ip;}
    public int getResult(){return result;}
    public void setResult(int result){this.result = result;}
    public String getInfo(){return info;}
    public void setInfo(String info){this.info = info;}
    public String getHost(){return host;}
    public void setHost(String host){this.host = host;}
    public String getCountry(){return Country;}
    public void setCountry(String County){this.Country = County;}
    public String getProvince(){return Province;}
    public void setProvince(String Province){this.Province = Province;}
    public String getCity(){return City;}
    public void setCity(String City){this.City = City;}
    public String getIpinfo(){return ipinfo;}
    public void setIpinfo(String ipinfo){this.ipinfo = ipinfo;}
    
	public String getLoginaddress() {
		return loginaddress;
	}
	public void setLoginaddress(String loginaddress) {
		this.loginaddress = loginaddress;
	}

    
}
