package com.multi.channel;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;

import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.TianyouSdk;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;

public class ChannelService extends BaseSdkService {
    
	private TianyouCallback mTianyouxiCallback = new TianyouCallback() {
		@Override
		public void onResult(int code, String msg) {
			switch (code) {
			case TianyouCallback.CODE_LOGIN_SUCCESS:
				try {
					JSONObject jsonObject = new JSONObject(msg);
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, jsonObject.getString("uid"));
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.show(mActivity, "uid解析异常");
				}
				break;
			case TianyouCallback.CODE_LOGIN_FAILED:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, msg);
				break;
			case TianyouCallback.CODE_LOGIN_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, msg);
				break;
			case TianyouCallback.CODE_LOGOUT:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);
				break;
			case TianyouCallback.CODE_PAY_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
				break;
			case TianyouCallback.CODE_PAY_FAILED:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, msg);
				break;
			case TianyouCallback.CODE_PAY_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, msg);
				break;
			case TianyouCallback.CODE_QUIT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, msg);
				break;
			case TianyouCallback.CODE_QUIT_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, msg);
				break;
			}
		}
	};
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		TianyouSdk.getInstance().applicationInit(context, mChannelInfo.getGameId(), 
				mChannelInfo.getGameToken(), mChannelInfo.getGameName(), true);
	}
    
    @Override
    public void doActivityInit(Activity activity, com.tianyou.channel.interfaces.TianyouCallback tianyouCallback) {
    	super.doActivityInit(activity, tianyouCallback);
    	TianyouSdk.getInstance().activityInit(mActivity, mTianyouxiCallback);
    	mTianyouCallback.onResult(com.tianyou.channel.interfaces.TianyouCallback.CODE_INIT, "");
    }
    
    @Override
    public void doLogin() {
    	super.doLogin();
    	TianyouSdk.getInstance().login();
    }
    
    @Override
    public void doCreateRole(RoleInfo roleInfo) {
    	super.doCreateRole(roleInfo);
    	com.tianyou.sdk.bean.RoleInfo info = new com.tianyou.sdk.bean.RoleInfo();
    	info.setRoleId(roleInfo.getRoleId());
    	info.setRoleName(roleInfo.getRoleName());
    	info.setServerId(roleInfo.getServerId());
    	info.setServerName(roleInfo.getServerName());
    	info.setProfession(roleInfo.getProfession());
    	info.setLevel(roleInfo.getRoleLevel());
    	info.setSociaty(roleInfo.getParty());
		TianyouSdk.getInstance().createRole(info);
    }
    
    @Override
    public void doEntryGame() {
    	super.doEntryGame();
    	doUpdateRoleInfo();
    }
    
    @Override
    public void doUploadRoleInfo(RoleInfo roleInfo) {
    	super.doUploadRoleInfo(roleInfo);
    	doUpdateRoleInfo();
    }
    
    @Override
    public void doUpdateRoleInfo(RoleInfo roleInfo) {
    	super.doUpdateRoleInfo(roleInfo);
    	doUpdateRoleInfo();
    }
    
    private void doUpdateRoleInfo() {
    	com.tianyou.sdk.bean.RoleInfo roleInfo = new com.tianyou.sdk.bean.RoleInfo();
		roleInfo.setServerId(mRoleInfo.getServerId());
		roleInfo.setServerName(mRoleInfo.getServerName());
		roleInfo.setRoleId(mRoleInfo.getRoleId());
		roleInfo.setRoleName(mRoleInfo.getRoleName());
		roleInfo.setProfession(mRoleInfo.getProfession());
		roleInfo.setLevel(mRoleInfo.getRoleLevel());
		roleInfo.setSociaty(mRoleInfo.getParty());
		roleInfo.setAmount("1");
		roleInfo.setBalance(mRoleInfo.getBalance());
		roleInfo.setVipLevel(mRoleInfo.getVipLevel());
		TianyouSdk.getInstance().updateRoleInfo(roleInfo);
	}
    
    @Override
    public void doPay(PayParam payInfo) {
    	LogUtils.d("调用支付接口:" + payInfo);
		if (mRoleInfo == null) {
			LogUtils.d("调用支付接口1");
			ToastUtils.show(mActivity, "请先上传角色信息");
			return;
		}
		LogUtils.d("调用支付接口2");
		mPayInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		if (mPayInfo == null) {
			ToastUtils.show(mActivity, "需打入渠道资源");
		} else {
			PayInfo payParam = new PayInfo();
	    	payParam.setRoleId(mRoleInfo.getRoleId());
	    	payParam.setMoney(mPayInfo.getMoney());
	    	payParam.setServerId(mRoleInfo.getServerId());
	    	payParam.setServerName(mRoleInfo.getServerName());
	    	payParam.setProductId(mPayInfo.getProductId());
	    	payParam.setProductName(mPayInfo.getProductName());
	    	payParam.setCustomInfo(payInfo.getCustomInfo());
	    	payParam.setGameName(mChannelInfo.getGameId());
	    	TianyouSdk.getInstance().pay(payParam);
		}
    }
    
    @Override
    public void doExitGame() {
    	TianyouSdk.getInstance().exitGame();
    }
}
