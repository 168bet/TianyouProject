package com.tianyou.channel.interfaces;

import android.content.Context;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.business.AipuSdkService;
import com.tianyou.channel.business.AiqiyiSdkService;
import com.tianyou.channel.business.AnzhiSdkService;
import com.tianyou.channel.business.AyxSdkService;
import com.tianyou.channel.business.BaiduSdkService;
import com.tianyou.channel.business.CCSdkService;
import com.tianyou.channel.business.DouquSdkService;
import com.tianyou.channel.business.CaoxieSdkService;
import com.tianyou.channel.business.DownJoySdkService;
import com.tianyou.channel.business.DuoquSdkService;
import com.tianyou.channel.business.ErWuOUSdkService;
import com.tianyou.channel.business.HSZSdkService;
import com.tianyou.channel.business.HaiMaSdkService;
import com.tianyou.channel.business.HanfengService;
import com.tianyou.channel.business.HuaWeiSdkService;
import com.tianyou.channel.business.JinliSdlService;
import com.tianyou.channel.business.KupaiSdkService;
import com.tianyou.channel.business.LenovoSdkService;
import com.tianyou.channel.business.LeshiSdkService;
import com.tianyou.channel.business.LeyouSdkService;
import com.tianyou.channel.business.M4399SdkService;
import com.tianyou.channel.business.MeizuSdkService;
import com.tianyou.channel.business.TestYingyongbaoSdkService;
import com.tianyou.channel.business.YijieSdkService;
import com.tianyou.channel.business.MoguSdkService;
import com.tianyou.channel.business.OppoSdkService;
import com.tianyou.channel.business.PYWSdkService;
import com.tianyou.channel.business.PangGooglepaySdkService;
import com.tianyou.channel.business.PangSdkService;
import com.tianyou.channel.business.QihooSdkService;
import com.tianyou.channel.business.ShanyouSdkService;
import com.tianyou.channel.business.ShenqiSdkService;
import com.tianyou.channel.business.SogouSdkService;
import com.tianyou.channel.business.TTSdkService;
import com.tianyou.channel.business.TianTianSdkService;
import com.tianyou.channel.business.TianyouSdkService;
import com.tianyou.channel.business.UCSdkService;
import com.tianyou.channel.business.VivoSdkService;
import com.tianyou.channel.business.WandoujiaSdkService;
import com.tianyou.channel.business.WuyouwanSdkService;
import com.tianyou.channel.business.XianquChSdkService;
import com.tianyou.channel.business.XiaoMiSdkService;
import com.tianyou.channel.business.YingyongbaoSdkService;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;

public class TianyouSdk {

	private static BaseSdkService sdkService;
	
	private TianyouSdk() { }

	public static BaseSdkService getInstance(Context context) {
		if (sdkService != null) return sdkService;
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		LogUtils.d("channelInfo:" + channelInfo);
		if (channelInfo == null) {
			sdkService = new BaseSdkService();
		} else if ("ty000".equals(channelInfo.getChannelId())) {
			sdkService = new TianyouSdkService();
		} else if ("ty001".equals(channelInfo.getChannelId()) || "ty055".equals(channelInfo.getChannelId())) {
			sdkService = new XiaoMiSdkService();
		} else if ("ty002".equals(channelInfo.getChannelId())) {
			sdkService = new HuaWeiSdkService();
		} else if ("ty003".equals(channelInfo.getChannelId())) {
			sdkService = new QihooSdkService();
		} else if ("ty004".equals(channelInfo.getChannelId())) {
			sdkService = new VivoSdkService();
		} else if ("ty005".equals(channelInfo.getChannelId())) {
			sdkService = new UCSdkService();
		} else if ("ty006".equals(channelInfo.getChannelId())) {
			sdkService = new MeizuSdkService();
		} else if ("ty007".equals(channelInfo.getChannelId())) {
			sdkService = new DownJoySdkService();
		} else if ("ty008".equals(channelInfo.getChannelId())) {
			sdkService = new OppoSdkService();
		} else if ("ty009".equals(channelInfo.getChannelId())) {
			sdkService = new JinliSdlService();
		} else if ("ty010".equals(channelInfo.getChannelId())) {
			sdkService = new AnzhiSdkService();
		} else if ("ty011".equals(channelInfo.getChannelId())) {
			sdkService = new LeshiSdkService();
		} else if ("ty012".equals(channelInfo.getChannelId())) {
			sdkService = new LenovoSdkService();
		} else if ("ty013".equals(channelInfo.getChannelId())) {
			sdkService = new BaiduSdkService();
		} else if ("ty014".equals(channelInfo.getChannelId())) {
			sdkService = new WandoujiaSdkService();
		} else if ("ty015".equals(channelInfo.getChannelId())) {
			sdkService = new HaiMaSdkService();
		} else if ("ty016".equals(channelInfo.getChannelId())) {
			sdkService = new TTSdkService();
		} else if ("ty017".equals(channelInfo.getChannelId())) {
			sdkService = new KupaiSdkService();
		} else if ("ty018".equals(channelInfo.getChannelId())) {
			sdkService = new CCSdkService();
		} else if ("ty020".equals(channelInfo.getChannelId())) {
			sdkService = new PYWSdkService();
		} else if ("ty021".equals(channelInfo.getChannelId())) {
			sdkService = new ErWuOUSdkService();
		} else if ("ty022".equals(channelInfo.getChannelId())) {
			sdkService = new AiqiyiSdkService();
		} else if ("ty023".equals(channelInfo.getChannelId())) {
			sdkService = new AyxSdkService();
		} else if ("ty024".equals(channelInfo.getChannelId())) {
			sdkService = new SogouSdkService();
		} else if ("ty025".equals(channelInfo.getChannelId())) {
			sdkService = new M4399SdkService();
		} else if ("ty026".equals(channelInfo.getChannelId()) || "ty060".equals(channelInfo.getChannelId())) {
			sdkService = new YingyongbaoSdkService();
		} else if ("ty027".equals(channelInfo.getChannelId())) {
			sdkService = new HSZSdkService();
		} else if ("ty028".equals(channelInfo.getChannelId())) {
			sdkService = new ShenqiSdkService();
		} else if ("ty029".equals(channelInfo.getChannelId())) {
			sdkService = new AipuSdkService();
		} else if ("ty032".equals(channelInfo.getChannelId())) {
			sdkService = new TianTianSdkService();
		} else if ("ty033".equals(channelInfo.getChannelId())) {
			sdkService = new MoguSdkService();
		} else if ("ty034".equals(channelInfo.getChannelId())  || "ty044".equals(channelInfo.getChannelId())  || 
				"ty045".equals(channelInfo.getChannelId())  || "ty046".equals(channelInfo.getChannelId())  || 
				"ty051".equals(channelInfo.getChannelId()) || "ty062".equals(channelInfo.getChannelId())) { 
			sdkService = new WuyouwanSdkService();
		} else if ("ty035".equals(channelInfo.getChannelId())) {
			sdkService = new ShanyouSdkService();
		} else if ("bm102".equals(channelInfo.getChannelId()) || "bm102_test".equals(channelInfo.getChannelId())) {
			sdkService = new PangSdkService();
		} else if ("bm103".equals(channelInfo.getChannelId())) {
			sdkService = new PangGooglepaySdkService();
		} else if ("ty039".equals(channelInfo.getChannelId()) || "ty040".equals(channelInfo.getChannelId())) {
			sdkService = new XianquChSdkService();
		} else if ("ty052".equals(channelInfo.getChannelId())) {
			sdkService = new YijieSdkService();
		} else if ("ty063".equals(channelInfo.getChannelId())) {
			sdkService = new DouquSdkService();
		} else if ("ty057".equals(channelInfo.getChannelId())) {
			sdkService = new CaoxieSdkService();
		} else if ("ty100".equals(channelInfo.getChannelId())) {
			sdkService = new TestYingyongbaoSdkService();
		} else if ("ty065".equals(channelInfo.getChannelId())) {
			sdkService = new DuoquSdkService();
		} else if ("ty066".equals(channelInfo.getChannelId())) {
			sdkService = new HanfengService();
		} else if ("ty067".equals(channelInfo.getChannelId())) {
			sdkService = new LeyouSdkService();
		}
		return sdkService;
	}

	public static String getChannelName(Context context) {
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		return channelInfo == null ? "没有配置文件" : channelInfo.getChannelId();
	}
}
