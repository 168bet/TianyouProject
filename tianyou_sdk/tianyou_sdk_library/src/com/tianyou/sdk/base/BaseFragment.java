package com.tianyou.sdk.base;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.bean.LoginWay;
import com.tianyou.sdk.bean.LoginWay.ResultBean;
import com.tianyou.sdk.bean.PhoneCode;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by itstrong on 2016/7/1.
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

    protected BaseActivity mActivity;
    protected View mContentView;
    protected PayActivity mPayActivity;
    protected LoginHandler mLoginHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity)getActivity();
        mActivity.setFragmentTag(getTag());
        mLoginHandler = LoginHandler.getInstance(mActivity, mActivity.mHandler);
        mContentView = inflater.inflate(ResUtils.getResById(mActivity, setContentView(), "layout"), container, false);
        try {
        	((LoginActivity)mActivity).setRegisterTitle(false);
        	((LoginActivity)mActivity).setBackBtnVisible(false);
        	((LoginActivity)mActivity).setCloseViw(true);
        	((LoginActivity)mActivity).setBgHeight(false);
		} catch (Exception e) {
			LogUtils.d("转化异常...");
		}
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
    
    // QQ登录
 	protected void doQQLogin() {
 		String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
		LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
		ResultBean result = loginWay.getResult();
		if (result.getCode() == 200 && result.getCustominfo().getQq_quick() == 0) {
			ToastUtils.show(mActivity, "暂未开放");
		} else {
			Intent intent = new Intent(mActivity, WebViewAvtivity.class);
			intent.putExtra("title", ResUtils.getString(mActivity, "ty_qq_login"));
			intent.putExtra("url", URLHolder.URL_QQ_WEB);
			startActivityForResult(intent, 100);
		}
 	}
 	
// 	public String mVerifiCode;
 	private TextView mTextCode;
 	
 	protected void getVerifiCode(EditText editText, TextView textVie, String sendType) {
		String phone = editText.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			getVerifiCode(phone, textVie, sendType);
		}
	}
 	
 	public class SendType {
 		public static final String SEND_TYPE_REGISTER = "register";
 		public static final String SEND_TYPE_FIND_PASS = "findpass";
 		public static final String SEND_TYPE_REMOVE_PHONE = "removephone";
 		public static final String SEND_TYPE_BIND_PHONE = "bindphone";
 		public static final String SEND_TYPE_VERIFI = "verification";
 		public static final String SEND_TYPE_IDENTITY = "identity";
 		public static final String SEND_TYPE_UPDATE_PHONE = "updatephone";
 	}
 	
 	// 获取验证码
 	protected void getVerifiCode(String phone, TextView textView, String sendType) {
 		mTextCode = textView;
 		if (phone.isEmpty()) {
 			ToastUtils.show(mActivity, "手机号不能为空");
 		} else if (!AppUtils.verifyPhoneNumber(phone)) {
 			ToastUtils.show(mActivity, (ConfigHolder.isOverseas? "Phone number format error" : "手机号格式错误"));
 		} else {
 			Map<String, String> map = new HashMap<String, String>();
 			map.put("mobile", phone);
             map.put("send_code", AppUtils.MD5(phone));
             map.put("send_type", sendType);
             map.put("type", "android");
             map.put("imei", AppUtils.getPhoeIMEI(mActivity));
             map.put("sign", AppUtils.MD5(phone + "register" + "android" + AppUtils.getPhoeIMEI(mActivity)));
 			HttpUtils.post(mActivity, URLHolder.URL_GET_CODE, map, new HttpsCallback() {
 				@Override
 				public void onSuccess(String response) {
 					PhoneCode code = new Gson().fromJson(response, PhoneCode.class);
 					if (code.getResult().getCode() == 200) {
// 						mVerifiCode = code.getResult().getMobile_code();
 						mTextCode.setClickable(false);
 						createDelayed();
 					}
 					ToastUtils.show(mActivity, code.getResult().getMsg());
 				}
 			});
 		}
 	}
 	
 	// 验证验证码
  	protected void verifiCode(String phone, String verify) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", phone);
        map.put("verify", verify);
        map.put("type", "android");
        map.put("imei", AppUtils.getPhoeIMEI(mActivity));
        map.put("sign", AppUtils.MD5(phone + ConfigHolder.gameId));
		HttpUtils.post(mActivity, URLHolder.URL_VERIFY_CODE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				PhoneCode code = new Gson().fromJson(response, PhoneCode.class);
				if (code.getResult().getCode() == 200) {
//					mVerifiCode = code.getResult().getMobile_code();
					mTextCode.setClickable(false);
					createDelayed();
				}
				ToastUtils.show(mActivity, code.getResult().getMsg());
			}
		});
  	}
 	
 	public int mCodeTime;	//验证码倒计时
 	
 	// 创建定时器
 	public void createDelayed() {
 		mCodeTime = 60;
 		final Handler handler = new Handler();
 		handler.postDelayed(new Runnable() {
 			@Override
 			public void run() {
 				if (mCodeTime != 0) {
 					mTextCode.setText("重新发送(" + mCodeTime-- + ")");
 					handler.postDelayed(this, 1000);
 				} else {
 					mTextCode.setText("获取验证码");
 					mTextCode.setClickable(true);
 					handler.removeCallbacks(this);
 				}
 			}
 		}, 1000);
 	}
 	
 	// 快速注册开关
// 	protected void quickRegisterSwitch() {
// 		if (ConfigHolder.isUnion) {
// 			ToastUtils.show(mActivity, "暂未开放");
//		} else {
//			String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
//			LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
//			ResultBean result = loginWay.getResult();
//			if (result.getCode() == 200 && result.getCustominfo().getReg_quick() == 1) {
//				if (ConfigHolder.isUnion) {
//					mActivity.switchFragment(new UnionRegisterFragment());
//				} else {
//					mLoginHandler.doQuickRegister();
//				}
//			} else {
//				ToastUtils.show(mActivity, "暂未开放");
//			}
//		}
// 	}
}
