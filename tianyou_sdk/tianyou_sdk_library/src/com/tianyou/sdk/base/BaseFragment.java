package com.tianyou.sdk.base;

import com.tianyou.sdk.activity.PayActivity;
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
public abstract class BaseFragment extends Fragment implements OnClickListener {

    protected BaseActivity mActivity;
    protected View mContentView;
    protected PayActivity mPayActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity)getActivity();
        mActivity.setFragmentTag(getTag());
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
}
