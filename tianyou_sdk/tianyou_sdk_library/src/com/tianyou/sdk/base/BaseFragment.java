package com.tianyou.sdk.base;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.bean.LoginWay;
import com.tianyou.sdk.bean.LoginWay.ResultBean;
import com.tianyou.sdk.fragment.login.UnionRegisterFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
 		Intent intent = new Intent(mActivity, WebViewAvtivity.class);
		intent.putExtra("title", ResUtils.getString(mActivity, "ty_qq_login"));
		intent.putExtra("url", URLHolder.URL_QQ_WEB);
		startActivityForResult(intent, 100);
 	}
 	
 	// 快速注册开关
 	protected void quickRegisterSwitch() {
 		if (ConfigHolder.isOverseas || ConfigHolder.isUnion) {
 			String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
 			LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
 			ResultBean result = loginWay.getResult();
 			if (result.getCode() == 200 && result.getCustominfo().getReg_quick() == 1) {
 				if (ConfigHolder.isUnion) {
 					mActivity.switchFragment(new UnionRegisterFragment());
 				} else {
 					mLoginHandler.doQuickRegister();
 				}
 			} else {
 				ToastUtils.show(mActivity, "暂未开放");
 			}
 		} else {
 			mLoginHandler.doQuickRegister();
 		}
 	}
}
