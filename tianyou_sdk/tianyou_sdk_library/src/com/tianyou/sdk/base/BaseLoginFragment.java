package com.tianyou.sdk.base;

import java.util.List;
import java.util.Map;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.fragment.login.NoQQFragment;
import com.tianyou.sdk.fragment.login.QQBindingFragment;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.utils.ResUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
