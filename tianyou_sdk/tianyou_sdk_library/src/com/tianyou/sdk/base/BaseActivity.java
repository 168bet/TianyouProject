package com.tianyou.sdk.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.utils.ResUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

	protected FragmentManager mFragmentManager;
	protected String mFragmentTag;
	protected TextView mTextTitle;
	protected Activity mActivity;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ConfigHolder.isLandscape ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE 
				: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(setContentView());
        mActivity = this;
        PushAgent.getInstance(this).onAppStart();
        mFragmentManager = getFragmentManager();
        mTextTitle = (TextView) findViewById(ResUtils.getResById(this, "text_title", "id"));
        initView();
		initData();
	}
	
	/**
	 * 切换Fragment
	 * @param fragment
	 * @param TAG
	 */
	public void switchFragment(Fragment fragment, String TAG) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(ResUtils.getResById(this, "layout_content", "id"), fragment, TAG);
        transaction.addToBackStack(TAG);
        transaction.commitAllowingStateLoss();
    }
	
	/**
	 * 设置title
	 * @param title
	 */
	public void setFragmentTitle(String title) {
    	mTextTitle.setText(title);
    }
    
    /**
     * 设置setFragmentTag
     * @param TAG
     */
	public void setFragmentTag(String TAG) {
    	mFragmentTag = TAG;
    }
    
	/**
	 * 处理返回按钮
	 */
    @Override
    public void onBackPressed() {
    	if ("HomeFragment".equals(mFragmentTag) || "OneKeyFragment".equals(mFragmentTag)) {
			finish();
		} else {
			super.onBackPressed();
		}
    }
    
    /**
     * 设置布局文件
     * @return 布局文件id
     */
    protected abstract int setContentView();
    
    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
    
    @Override
    protected void onResume() {
    	super.onResume();
    	// 友盟统计
    	MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	// 友盟统计
    	MobclickAgent.onPause(this);
    }
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	if (ev.getAction() == MotionEvent.ACTION_DOWN) {
    		View v = getCurrentFocus();
    		if (isShouldHideKeyboard(v,ev)) {
    			hideKeyboard(v.getWindowToken());
    		}
    	}
    	return super.dispatchTouchEvent(ev);
    }
    
    private boolean isShouldHideKeyboard(View v,MotionEvent ev) {
    	if (v != null && (v instanceof EditText)) {
    		int[] l = {0,0};
    		v.getLocationInWindow(l);
    		int left = l[0],top = l[1],bottom = top + v.getHeight(),right = left + v.getWidth();
    		if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
    			return false;
    		} else {
    			return true;
    		}
    	}
    	return false;
    }
    
    private void hideKeyboard (IBinder token) {
    	if (token != null) {
    		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    		im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    	}
    }
}
