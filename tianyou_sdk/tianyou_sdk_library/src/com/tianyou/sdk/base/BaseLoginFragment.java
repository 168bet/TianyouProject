package com.tianyou.sdk.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.bean.LoginInfo.ResultBean;
import com.tianyou.sdk.fragment.login.BindingFragment;
import com.tianyou.sdk.fragment.login.NoQQFragment;
import com.tianyou.sdk.fragment.login.PhoneFragment;
import com.tianyou.sdk.fragment.login.QQBindingFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

/**
 * Created by itstrong on 2016/7/1.
 */
public abstract class BaseLoginFragment extends Fragment implements OnClickListener {

    protected LoginActivity mActivity;
    protected View mContentView;
    protected LoginHandler mLoginHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (LoginActivity)getActivity();
        mActivity.setFragmentTag(getTag());
        mLoginHandler = LoginHandler.getInstance(mActivity,mActivity.getHandler());
        mContentView = inflater.inflate(ResUtils.getResById(mActivity, setContentView(), "layout"), container, false);
        initView();
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    
    /**
     * 设置布局文件
     * @return 布局文件id
     */
    protected abstract String setContentView();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && data != null) {
			mLoginHandler.onLoginProcess((ResultBean) data.getSerializableExtra("login_result"));
		}
	}
    
 	// 一键登录
 	protected void doOneKeyLogin() {
		final ProgressDialog dialog = new ProgressDialog(mActivity);
		dialog.setTitle("一键登录");
		dialog.setMessage("正在发送短信...");
		dialog.setIndeterminate(false);
		dialog.setMax(100);
		dialog.incrementProgressBy(30);
		dialog.incrementSecondaryProgressBy(70);
		dialog.setCancelable(false);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				String number = AppUtils.getPhoneNumber(mActivity);
				if (number.isEmpty()) {
					ToastUtils.show(mActivity, "验证失败，请使用其他方式登录！");
					mActivity.switchFragment(new PhoneFragment(), "PhoneFragment");
				} else {
					Map<String,String> map = new HashMap<String, String>();
					map.put("username", number);
					map.put("imei", AppUtils.getPhoeIMEI(mActivity));
					map.put("ip", AppUtils.getIP());
					map.put("channel", ConfigHolder.CHANNEL_ID);
					map.put("appID", ConfigHolder.GAME_ID);
					map.put("type", "android");
					HttpUtils.post(mActivity, URLHolder.URL_KEY_LOGIN, map, new HttpsCallback() {
						@Override
						public void onSuccess(String response) {
							LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
							if (info.getResult().getCode() == 200) {
								mLoginHandler.onLoginSuccess(info.getResult());
							} else {
								ToastUtils.show(mActivity, info.getResult().getMsg());
							}
						}
					});
				}
				
			}
		}, 1000);
	}
 	
 	// 快速注册
	protected void doQuickRegister() {
		Map<String,String> map = new HashMap<String, String>();
		String phoeImei = AppUtils.getPhoeIMEI(mActivity);
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("imei", phoeImei);
		map.put("isgenerate", "1");
		map.put("channel", ConfigHolder.CHANNEL_ID);
		map.put("ip", AppUtils.getIP());
		map.put("type", "android");
		String url = (ConfigHolder.IS_OVERSEAS ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.URL_LOGIN_QUICK;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
				final ResultBean result = info.getResult();
				if (result.getCode() == 200) {
					showTipDialog(result);
				} else {
					ToastUtils.show(mActivity, result.getMsg());
				}
			}
		});
	}
	
	private void showTipDialog(final ResultBean result) {
		View view = View.inflate(mActivity, ResUtils.getResById(mActivity, "dialog_login_quick", "layout"), null);
		final AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setView(view);
		dialog.show();
		setDialogWindowAttr(dialog, mActivity);
		view.findViewById(ResUtils.getResById(mActivity, "text_dialog_menu_0", "id")).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mActivity.switchFragment(BindingFragment.getInstance(result.getUserid(), 
						result.getUsername(), result.getPassword(), result.getToken()), "BandingFragment");
				dialog.dismiss();
			}
		});
		view.findViewById(ResUtils.getResById(mActivity, "text_dialog_menu_1", "id")).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				mLoginHandler.showWelComePopup(result);
			}
		});
	}
	
	public static void setDialogWindowAttr(Dialog dlg,Context ctx){
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = (int) ctx.getResources().getDimension(ResUtils.getResById(ctx, "dialog_login_tip", "dimen"));//宽高可设置具体大小
        lp.height = LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(lp);
    }
	
	// QQ登录
	protected void doQQLogin() {
		List<Map<String, String>> loginInfo = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ);
		if (loginInfo == null || loginInfo.size() == 0) {
			mActivity.switchFragment(new NoQQFragment(), "NoQQFragment");
		} else {
			mActivity.switchFragment(new QQBindingFragment(), "QQBindingFragment");
		}
	}
}
