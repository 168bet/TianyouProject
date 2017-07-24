package com.k3k.unplat.entity.payment;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.k3k.unplat.common.Constants;
import com.k3k.unplat.common.EnumVar.MailStatus;
import com.k3k.unplat.common.EnumVar.MailType;
import com.k3k.unplat.common.EnumVar.YesOrNo;

public class Mail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2105712763420413189L;

	private String mailid;
	private String title;
	private String content;
	private Date collectdate;
	private String collectdateFormat;
	private String state;// 状态
	private String stateText;
	private String type;// 邮件类型
	private String typeText;
	private String giftscore;
	private String giftvip;
	private String giftday;
	private String userid;
	private String isget;
	private String isgetText;
	private Date getdate;
	private String getdateFormat;
	private String kindid;
	private String gameText;
	private String downKindid;
	private String machineSerial;
	private String isnotice;
	private String noticeid;

	public String getMailid() {
		return mailid;
	}

	public void setMailid(String mailid) {
		this.mailid = mailid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCollectdate() {
		return collectdate;
	}

	public void setCollectdate(Date collectdate) {
		this.collectdate = collectdate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGiftscore() {
		return giftscore;
	}

	public void setGiftscore(String giftscore) {
		this.giftscore = giftscore;
	}

	public String getGiftvip() {
		return giftvip;
	}

	public void setGiftvip(String giftvip) {
		this.giftvip = giftvip;
	}

	public String getGiftday() {
		return giftday;
	}

	public void setGiftday(String giftday) {
		this.giftday = giftday;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIsget() {
		return isget;
	}

	public void setIsget(String isget) {
		this.isget = isget;
	}

	public String getKindid() {
		return kindid;
	}

	public void setKindid(String kindid) {
		this.kindid = kindid;
	}

	public String getGameText() {
		return gameText;
	}

	public void setGameText(String gameText) {
		this.gameText = gameText;
	}

	public String getCollectdateFormat() {
		if (StringUtils.isBlank(collectdateFormat) && collectdate != null) {
			collectdateFormat = Constants.TIME_FORMAT.format(collectdate);
		}
		return collectdateFormat;
	}

	public void setCollectdateFormat(String collectdateFormat) {
		this.collectdateFormat = collectdateFormat;
	}

	public String getStateText() {
		if (StringUtils.isBlank(stateText) && StringUtils.isNotBlank(state)) {
			stateText = MailStatus.valueOf(Integer.parseInt(state)).getValue();
		}
		return stateText;
	}

	public void setStateText(String stateText) {
		this.stateText = stateText;
	}

	public String getTypeText() {
		if (StringUtils.isBlank(typeText) && StringUtils.isNotBlank(type)) {
			typeText = MailType.valueOf(Integer.parseInt(type)).getValue();
		}
		return typeText;
	}

	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}

	public String getIsgetText() {
		if (StringUtils.isBlank(isgetText) && StringUtils.isNotBlank(isget)) {
			isgetText = YesOrNo.valueOf(Integer.parseInt(isget)).getValue();
		}
		return isgetText;
	}

	public Date getGetdate() {
		return getdate;
	}

	public void setGetdate(Date getdate) {
		this.getdate = getdate;
	}

	public String getGetdateFormat() {
		if (StringUtils.isBlank(getdateFormat) && getdate != null) {
			getdateFormat = Constants.TIME_FORMAT.format(getdate);
		}
		return getdateFormat;
	}

	public String getDownKindid() {
		return downKindid;
	}

	public void setDownKindid(String downKindid) {
		this.downKindid = downKindid;
	}

	public String getIsnotice() {
		return isnotice;
	}

	public void setIsnotice(String isnotice) {
		this.isnotice = isnotice;
	}

	public String getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}

	public String getMachineSerial() {
		return machineSerial;
	}

	public void setMachineSerial(String machineSerial) {
		this.machineSerial = machineSerial;
	}

}