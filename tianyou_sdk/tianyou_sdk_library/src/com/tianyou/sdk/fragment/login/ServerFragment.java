package com.tianyou.sdk.fragment.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;

import android.view.View;
import android.widget.TextView;

public class ServerFragment extends BaseFragment {

	private TextView mTextTips;
	
	private boolean mIsPhone;
	private String mAccount;
	private List<TextView> mTextViews;
	
	public ServerFragment(boolean isPhone, String account) {
		this.mIsPhone = isPhone;
		this.mAccount = account;
	}
	
	@Override
	protected String setContentView() { return "fragment_login_server"; }

	@Override
	protected void initView() {
		mTextViews = new ArrayList<TextView>();
		mTextViews.add((TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_info_0", "id")));
		mTextViews.add((TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_info_1", "id")));
		mTextViews.add((TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_info_2", "id")));
		mTextViews.add((TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_info_3", "id")));
		mTextViews.add((TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_info_4", "id")));
		mTextTips = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_tips", "id"));
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle(mIsPhone ? "联系客服" : ConfigHolder.isOverseas?"Handle service":"人工申诉");
		mTextTips.setVisibility(mIsPhone ? View.GONE : View.VISIBLE);
		mTextTips.setText((ConfigHolder.isOverseas?"Your current account ":"你的当前账号 ") + mAccount + 
				(ConfigHolder.isOverseas?" has no password protection,please contact customer service staff at work time to coordinate the problem ":"未设置过密码保护，请您在工作时间联系客服工作人员协调解决此类问题"));
		Map<String,String> map = new HashMap<String, String>();
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId));
		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_SERVER_INFO, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONObject result = jsonObject.getJSONObject("result");
					JSONArray setinfo = result.getJSONArray("setinfo");
					for (int i = 0; i < setinfo.length(); i++) {
						mTextViews.get(i).setVisibility(View.VISIBLE);
						mTextViews.get(i).setText(setinfo.getString(i));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) { }
}
