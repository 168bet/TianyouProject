package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.interfaces.TianyouxiCallback;
import com.tianyou.sdk.interfaces.TianyouxiSdk;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;

public class TianyouSdkService extends BaseSdkService {
    
	private TianyouxiCallback mTianyouxiCallback = new TianyouxiCallback() {
		@Override
		public void onResult(int code, String msg) {
			switch (code) {
			case TianyouxiCallback.CODE_LOGIN_SUCCESS:
				try {
					JSONObject jsonObject = new JSONObject(msg);
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, jsonObject.getString("uid"));
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.show(mActivity, "uid解析异常");
				}
				break;
			case TianyouxiCallback.CODE_LOGIN_FAILED:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, msg);
				break;
			case TianyouxiCallback.CODE_LOGIN_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, msg);
				break;
			case TianyouxiCallback.CODE_LOGOUT:
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);
				break;
			case TianyouxiCallback.CODE_PAY_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
				break;
			case TianyouxiCallback.CODE_PAY_FAILED:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, msg);
				break;
			case TianyouxiCallback.CODE_PAY_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, msg);
				break;
			case TianyouxiCallback.CODE_QUIT_SUCCESS:
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, msg);
				break;
			case TianyouxiCallback.CODE_QUIT_CANCEL:
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, msg);
				break;
			}
		}
	};
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		TianyouxiSdk.getInstance().applicationInit(context, mChannelInfo.getAppId(), 
				mChannelInfo.getAppSecret(), mChannelInfo.getGameId(), true);
	}
    
    @Override
    public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
    	super.doActivityInit(activity, tianyouCallback);
    	TianyouxiSdk.getInstance().activityInit(mActivity, mTianyouxiCallback);
    	mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
    }
    
    @Override
    public void doLogin() {
    	super.doLogin();
    	TianyouxiSdk.getInstance().login();
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
		TianyouxiSdk.getInstance().createRole(info);
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
    
    private void doUpdateRoleInfo() {
    	com.tianyou.sdk.bean.RoleInfo roleInfo = new com.tianyou.sdk.bean.RoleInfo();
		roleInfo.setServerId(mRoleInfo.getServerId());
		roleInfo.setServerName(mRoleInfo.getServerName());
		roleInfo.setRoleId(mRoleInfo.getRoleId());
		roleInfo.setRoleName(mRoleInfo.getRoleName());
		roleInfo.setProfession(mRoleInfo.getProfession());
		roleInfo.setLevel(mRoleInfo.getRoleLevel());
		roleInfo.setSociaty(mRoleInfo.getParty());
		TianyouxiSdk.getInstance().updateRoleInfo(roleInfo);
	}
    
    @Override
    public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
    	super.doChannelPay(payInfo, orderInfo);
    	PayInfo payParam = new PayInfo();
    	payParam.setRoleId(mRoleInfo.getRoleId());
    	payParam.setMoney(mPayInfo.getMoney());
    	payParam.setServerId(mRoleInfo.getServerId());
    	payParam.setServerName(mRoleInfo.getServerName());
    	payParam.setProductId(mPayInfo.getProductId());
    	payParam.setProductName(mPayInfo.getProductName());
    	payParam.setCustomInfo(payInfo.getCustomInfo());
    	payParam.setGameName(mChannelInfo.getGameId());
    	TianyouxiSdk.getInstance().pay(payParam);
    }
    
    @Override
    public void doExitGame() {
    	TianyouxiSdk.getInstance().exitGame();
    }
}
