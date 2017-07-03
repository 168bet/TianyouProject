package com.k3k.unplat.common;

public class EnumVar {

	public enum CompareCode {
		GT(1, "&gt;", "大于"), LT(2, "&lt;", "小于"), EQ(3, "&eq;", "等于"), GTEQ(4,
				"&gt;&eq;", "大于等于"), LTEQ(5, "&lt;&eq;", "小于等于");

		int key;
		String code;
		String text;

		private CompareCode(int key, String code, String text) {
			this.key = key;
			this.code = code;
			this.text = text;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public static CompareCode describeOf(String code) {
			if (code.equals(GT.getCode())) {
				return GT;
			} else if (code.equals(LT.getCode())) {
				return LT;
			} else if (code.equals(EQ.getCode())) {
				return EQ;
			} else if (code.equals(GTEQ.getCode())) {
				return GTEQ;
			} else if (code.equals(LTEQ.getCode())) {
				return LTEQ;
			}
			return GT;
		}

		public static CompareCode valueOf(int key) {
			if (key == GT.getKey()) {
				return GT;
			} else if (key == LT.getKey()) {
				return LT;
			} else if (key == EQ.getKey()) {
				return EQ;
			} else if (key == GTEQ.getKey()) {
				return GTEQ;
			} else if (key == LTEQ.getKey()) {
				return LTEQ;
			}
			return GT;
		}
	}

	public enum NoticeType {
		WELCOME(1, "欢迎公告"), POPUP(2, "系统弹窗公告"), UPDATE(3, "更新公告"),EMAIL(4, "邮件公告"), ACTIVITY(5, "活动公告");

		int key;
		String value;

		private NoticeType(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static NoticeType valueOf(int key) {
			if (key == WELCOME.getKey()) {
				return WELCOME;
			} else if (key == POPUP.getKey()) {
				return POPUP;
			} else if (key == UPDATE.getKey()) {
				return UPDATE;
			} else if (key == EMAIL.getKey()) {
				return EMAIL;
			} else if (key == ACTIVITY.getKey()) {
				return ACTIVITY;
			}
			return null;
		}
	}

	public enum ActivityType {
		PAY(1, "充值活动"),
		GIFTBAG(2, "礼包活动"),
		COMMON(3, "普通活动");

		int key;
		String value;

		private ActivityType(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static ActivityType valueOf(int key) {
			if (key == PAY.getKey()) {
				return PAY;
			}else if(key == GIFTBAG.getKey()){
				return GIFTBAG;
			}else if(key == COMMON.getKey()){
				return COMMON;
			}
			return null;
		}
	}

	public enum ActivityStatus {
		NOT_STARTED(1, "未开始"), ON_GOING(2, "进行中"), EXPIRED(3, "已过期");

		int key;
		String value;

		private ActivityStatus(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static ActivityStatus valueOf(int key) {
			if (key == NOT_STARTED.getKey()) {
				return NOT_STARTED;
			} else if (key == ON_GOING.getKey()) {
				return ON_GOING;
			} else if (key == EXPIRED.getKey()) {
				return EXPIRED;
			}
			return null;
		}
	}
	
	public enum ActivityTODO{
		PAY(1, "去充值"),
		MATCH(2, "去比赛");
		
		int key;
		String value;
		private ActivityTODO(int key, String value){
			this.key = key;
			this.value = value;
		}
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public static ActivityTODO valueOf(int key){
			if(key==PAY.getKey()){
				return PAY;
			}else if(key==MATCH.getKey()){
				return MATCH;
			}
			return null;
		}
	}
	
	public enum ActivityPayType{
		FIRSTPAY(1, "首充"),
		ACCUMULATE(2, "累积充值"),
		SPECIAL_ACCUMU(3, "特定时间的累积充值");
		int key;
		String value;
		private ActivityPayType(int key, String value){
			this.key = key;
			this.value = value;
		}
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public static ActivityPayType valueOf(int key){
			if(key==FIRSTPAY.getKey()){
				return FIRSTPAY;
			}else if(key==ACCUMULATE.getKey()){
				return ACCUMULATE;
			}else if(key==SPECIAL_ACCUMU.getKey()){
				return SPECIAL_ACCUMU;
			}
			return null;
		}
	}

	public enum MailStatus {
		DELETED(1, "回收（指被删除）"), READ(2, "已读"), UNREAD(3, "未读");

		int key;
		String value;

		private MailStatus(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static MailStatus valueOf(int key) {
			if (DELETED.getKey() == key) {
				return DELETED;
			} else if (READ.getKey() == key) {
				return READ;
			} else if (UNREAD.getKey() == key) {
				return UNREAD;
			}
			return null;
		}
	}

	public enum MailType {
		COMMON(1, "普通邮件"), PAYAWARD(2, "充值奖励邮件"), NOVICEAWARD(3, "新手奖励邮件"), COMPENSATION(4, "补偿邮件"), DOWNAWARD(5, "下载游戏奖励");

		int key;
		String value;

		private MailType(int key, String value) {
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static MailType valueOf(int key) {
			if (key == COMMON.getKey()) {
				return COMMON;
			} else if (key == PAYAWARD.getKey()) {
				return PAYAWARD;
			} else if (key == NOVICEAWARD.getKey()) {
				return NOVICEAWARD;
			} else if (key == COMPENSATION.getKey()) {
				return COMPENSATION;
			} else if (key == DOWNAWARD.getKey()) {
				return DOWNAWARD;
			} 
			return null;
		}
	}

	public enum YesOrNo {
		YES(1, "是"), // 已领
		NO(0, "否");// 未领

		private YesOrNo(int key, String value) {
			this.key = key;
			this.value = value;
		}

		int key;
		String value;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static YesOrNo valueOf(int key) {
			if (key == YES.getKey()) {
				return YES;
			} else if (key == NO.getKey()) {
				return NO;
			}
			return NO;
		}
	}

	public enum TaskType {
		EVERYDAY(1, "每日任务"),
		RANDOMS(2, "随机任务"),
		ACHIEVE(3, "成就任务"),
		CONTINUOUS(4, "连续任务");

		private TaskType(int key, String value) {
			this.key = key;
			this.value = value;
		}

		int key;
		String value;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static TaskType valueOf(int key) {
			if (key == EVERYDAY.getKey()) {
				return EVERYDAY;
			}else if(key == RANDOMS.getKey()){
				return RANDOMS;
			}else if(key == ACHIEVE.getKey()){
				return ACHIEVE;
			}else if(key == CONTINUOUS.getKey()){
				return CONTINUOUS;
			}
			return null;
		}
	}

	public enum TaskPlayType {
		THREE_AGAINST_FIVE(1, "三人场对战五局"), 
		THREE_WIN_FIVE(2, "三人场赢五局"), 
		SPECIAL_TABLE_AGAINST_ONE(3, "包厢比赛一次"), 
		THREE_WIN_24BLE(4, "三人场24倍"),
		AGAINST_TEN(5, "对战十局"),
		GANGPAI_FIVE(6, "杠牌五次"),
		AGAINST_HU_FIVE(7, "对战中胡牌五局"),
		AGAINST_CONTIHU_THREE(8, "连续胡牌三局"),
		LAIZI_THREE(9, "摸到三个赖子"),
		LAIZI_FOUR(10, "摸到四个赖子"),
		BUYU_GET100(11, "捕鱼100条"),
		BUYU_CHOUKA1(12, "完成一次抽卡"),
		BUYU_WANPAOGETGOLD(13, "万炮场获得金币"),
		BUYU_KUANGBAO1(14, "使用狂暴技能1次"),
		BUYU_JINCHAN1(15, "捕获金蟾1只"),
		BUYU_GETGOLD2000(16, "累积赚取2000金币"),
		BUYU_JINLONG2(17, "捕获金龙2条"),
		BUYU_BOMB(18, "打爆深海炸弹1次"),
		ZHUOJI_JINJI(19, "金鸡一次"),
		GUANDAN_DUIJU10(20, "对局10次"),
		GUANDAN_YING5(21, "赢5局"),
		GUANDAN_GUOA(22, "过A"),
		GUANDAN_YING2000(23, "赢2000"),
		GUANDAN_TONGHUASHUN(24, "同花顺"),
		GUANDAN_TOUYOU(25, "头游"),
		GUANDAN_SHUANGSHANG(26, "双上"),
		WECHAT_SHARE(27, "微信分享一次"),
		NN_DUIJU(28, "对局任务"),
		NN_EVERYDAY(29, "每日任务"),
		NN_QIANGZHUANG(30, "看牌抢庄"),
		NN_ZUOZHUANGWIN(31, "坐庄赢钱"),
		NN_ZHUANJINBI(32, "赚取金币"),
		NN_TONGSHA(33, "通杀模式"),
		JMMJ_DUI_ZHANG10(34, "任意场对局十次"),
		JMMJ_HUPAI_10FAN(35, "十番以上胡牌"),
		JMMJ_BAO_PAI(36, "包牌一次"),
		JMMJ_LIANXV_HUPAI3(37, "连续胡牌三次"),
		JMMJ_QING_YI_SE(38, "清一色胡牌一次"),
		JMMJ_JIANG_YI_SE(39, "将一色胡牌一次"),
		JMMJ_FENG_YI_SE(40, "风一色胡牌一次"),
		JMMJ_PENG_PENG_HU(41, "碰碰胡胡牌一次"),
		JMMJ_QUAN_QIU_REN(42, "全求人胡牌一次"),
		JMMJ_ZI_MO_FOUR(43, "自摸胡牌四次"),
		JMMJ_GANG_FIVE(44, "杠牌五次"),
		JMMJ_AN_GANG_TWO(45, "暗杠两次"),
		JMMJ_MING_GANG_THREE(46, "明杠三次"),
		JMMJ_FANG_PAO_THREE(47, "放炮三次"),
		JMMJ_SHARE_PYQ(48, "分享朋友圈一次"),
		JMMJ_PEAK_RANK_TOP10(49, "进入巅峰榜前十"),
		JMMJ_WIN5(50, "任意场赢五局");
		
		
		private TaskPlayType(int key, String value) {
			this.key = key;
			this.value = value;
		}

		int key;
		String value;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static TaskPlayType valueOf(int key) {
			if (key == THREE_AGAINST_FIVE.getKey()) {
				return THREE_AGAINST_FIVE;
			} else if (key == THREE_WIN_FIVE.getKey()) {
				return THREE_WIN_FIVE;
			} else if (key == THREE_WIN_24BLE.getKey()) {
				return THREE_WIN_24BLE;
			} else if (key == SPECIAL_TABLE_AGAINST_ONE.getKey()) {
				return SPECIAL_TABLE_AGAINST_ONE;
			} else if (key == AGAINST_TEN.getKey()) {
				return AGAINST_TEN;
			} else if (key == GANGPAI_FIVE.getKey()) {
				return GANGPAI_FIVE;
			} else if (key == AGAINST_HU_FIVE.getKey()) {
				return AGAINST_HU_FIVE;
			} else if (key == AGAINST_CONTIHU_THREE.getKey()) {
				return AGAINST_CONTIHU_THREE;
			} else if (key == LAIZI_THREE.getKey()) {
				return LAIZI_THREE;
			} else if (key == LAIZI_FOUR.getKey()) {
				return LAIZI_FOUR;
			} else if (key == BUYU_GET100.getKey()) {
				return BUYU_GET100;
			} else if (key == BUYU_CHOUKA1.getKey()) {
				return BUYU_CHOUKA1;
			} else if (key == BUYU_WANPAOGETGOLD.getKey()) {
				return BUYU_WANPAOGETGOLD;
			} else if (key == BUYU_KUANGBAO1.getKey()) {
				return BUYU_KUANGBAO1;
			} else if (key == BUYU_JINCHAN1.getKey()) {
				return BUYU_JINCHAN1;
			} else if (key == BUYU_GETGOLD2000.getKey()) {
				return BUYU_GETGOLD2000;
			} else if (key == BUYU_JINLONG2.getKey()) {
				return BUYU_JINLONG2;
			} else if (key == BUYU_BOMB.getKey()){
				return BUYU_BOMB;
			} else if (key == ZHUOJI_JINJI.getKey()){
				return ZHUOJI_JINJI;
			} else if (key == GUANDAN_DUIJU10.getKey()){
				return GUANDAN_DUIJU10;
			} else if (key == GUANDAN_YING5.getKey()){
				return GUANDAN_YING5;
			} else if (key == GUANDAN_GUOA.getKey()){
				return GUANDAN_GUOA;
			} else if (key == GUANDAN_YING2000.getKey()){
				return GUANDAN_YING2000;
			} else if (key == GUANDAN_TONGHUASHUN.getKey()){
				return GUANDAN_TONGHUASHUN;
			} else if (key == GUANDAN_TOUYOU.getKey()){
				return GUANDAN_TOUYOU;
			} else if (key == GUANDAN_SHUANGSHANG.getKey()){
				return GUANDAN_SHUANGSHANG;
			} else if (key == WECHAT_SHARE.getKey()){
				return WECHAT_SHARE;
			} else if (key == NN_DUIJU.getKey()){
				return NN_DUIJU;
			} else if (key == NN_EVERYDAY.getKey()){
				return NN_EVERYDAY;
			} else if (key == NN_QIANGZHUANG.getKey()){
				return NN_QIANGZHUANG;
			} else if (key == NN_ZUOZHUANGWIN.getKey()){
				return NN_ZUOZHUANGWIN;
			} else if (key == NN_ZHUANJINBI.getKey()){
				return NN_ZHUANJINBI;
			} else if (key == NN_TONGSHA.getKey()){
				return NN_TONGSHA;
			} else if (key == JMMJ_DUI_ZHANG10.getKey()){
				return JMMJ_DUI_ZHANG10;
			} else if (key == JMMJ_HUPAI_10FAN.getKey()){
				return JMMJ_HUPAI_10FAN;
			} else if (key == JMMJ_BAO_PAI.getKey()){
				return JMMJ_BAO_PAI;
			} else if (key == JMMJ_LIANXV_HUPAI3.getKey()){
				return JMMJ_LIANXV_HUPAI3;
			} else if (key == JMMJ_QING_YI_SE.getKey()){
				return JMMJ_QING_YI_SE;
			} else if (key == JMMJ_JIANG_YI_SE.getKey()){
				return JMMJ_JIANG_YI_SE;
			} else if (key == JMMJ_FENG_YI_SE.getKey()){
				return JMMJ_FENG_YI_SE;
			} else if (key == JMMJ_PENG_PENG_HU.getKey()){
				return JMMJ_PENG_PENG_HU;
			} else if (key == JMMJ_QUAN_QIU_REN.getKey()){
				return JMMJ_QUAN_QIU_REN;
			} else if (key == JMMJ_ZI_MO_FOUR.getKey()){
				return JMMJ_ZI_MO_FOUR;
			}  else if (key == JMMJ_GANG_FIVE.getKey()){
				return JMMJ_GANG_FIVE;
			} else if (key == JMMJ_AN_GANG_TWO.getKey()){
				return JMMJ_AN_GANG_TWO;
			} else if (key == JMMJ_MING_GANG_THREE.getKey()){
				return JMMJ_MING_GANG_THREE;
			} else if (key == JMMJ_FANG_PAO_THREE.getKey()){
				return JMMJ_FANG_PAO_THREE;
			} else if (key == JMMJ_SHARE_PYQ.getKey()){
				return JMMJ_SHARE_PYQ;
			} else if (key == JMMJ_PEAK_RANK_TOP10.getKey()){
				return JMMJ_PEAK_RANK_TOP10;
			} else if (key == JMMJ_WIN5.getKey()){
				return JMMJ_WIN5;
			}
			return null;
		}
	}

	public enum TaskTODO {//任务todo
		COMMON(1, "普通场"), 
		SPECIAL(2, "包厢"),
		MATCH(3, "比赛场"),
		WECHAT_SHARE(4, "微信分享");
		private TaskTODO(int key, String value) {
			this.key = key;
			this.value = value;
		}

		int key;
		String value;

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public static TaskTODO valueOf(int key) {
			if (key == COMMON.getKey()) {
				return COMMON;
			} else if (key == SPECIAL.getKey()) {
				return SPECIAL;
			} else if (key == MATCH.getKey()) {
				return MATCH;
			} else if (key == WECHAT_SHARE.getKey()){
				return WECHAT_SHARE;
			}
			return null;
		}
	}
}
