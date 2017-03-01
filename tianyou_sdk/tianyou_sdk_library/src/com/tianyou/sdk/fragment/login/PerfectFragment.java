package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 完善账号信息
 * @author itstrong
 *
 */
public class PerfectFragment extends BaseLoginFragment {

	private EditText mEditName;
	private EditText mEditNick;
	private EditText mEditPass;
	private TextView mTextTip;
	private TextView mTextText;
	private View mViewLine0;
	private View mViewLine1;
	private View mViewLine2;

	@Override
	protected String setContentView() { return "fragment_login_perfect"; }
	
//	public static Fragment getInstall(String userName, String userPass, String userId, String nickName) {
//		PerfectFragment fragment = new PerfectFragment();
//		Bundle bundle = new Bundle();
//		bundle.putString("user_name", userName);
//		bundle.putString("user_pass", userPass);
//		bundle.putString("user_id", userId);
//		bundle.putString("user_nickname", nickName);
//		fragment.setArguments(bundle);
//		return fragment;
//	}

	@Override
	protected void initView() {
		mEditName = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_name", "id"));
		mEditNick = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_nick", "id"));
		mEditPass = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_pass", "id"));
		mTextTip = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_perfect_tip", "id"));
		mTextText = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_perfect_text", "id"));
		mViewLine0 = mContentView.findViewById(ResUtils.getResById(mActivity, "view_perfect_line_0", "id"));
		mViewLine1 = mContentView.findViewById(ResUtils.getResById(mActivity, "view_perfect_line_1", "id"));
		mViewLine2 = mContentView.findViewById(ResUtils.getResById(mActivity, "view_perfect_line_2", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_perfect_entry", "id")).setOnClickListener(this);
		mEditPass.addTextChangedListener(mTextWatcher);
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
		
		@Override
		public void afterTextChanged(Editable text) {
			String password = text.toString();
			if (password.length() < 6) {
				mViewLine0.setVisibility(View.INVISIBLE);
				mViewLine1.setVisibility(View.INVISIBLE);
				mViewLine2.setVisibility(View.INVISIBLE);
				mTextTip.setVisibility(View.GONE);
			} else if (password.matches("^[a-zA-Z]+$") || password.matches("^[\\d]+$")) {
				switchState(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, "危险", Color.RED);
			} else if (password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$")) {
				switchState(View.VISIBLE, View.VISIBLE, View.INVISIBLE, "一般", Color.parseColor("#FF8C00"));
			} else {
				switchState(View.VISIBLE, View.VISIBLE, View.VISIBLE, "安全", Color.GREEN);
			}
		}
	};
	
	private void switchState(int v0, int v1, int v2, String text, int color) {
		mViewLine0.setVisibility(v0);
		mViewLine1.setVisibility(v1);
		mViewLine2.setVisibility(v2);
		mViewLine0.setBackgroundColor(color);
		mViewLine1.setBackgroundColor(color);
		mViewLine2.setBackgroundColor(color);
		mTextTip.setVisibility(View.VISIBLE);
		mTextTip.setText(text);
		mTextTip.setTextColor(color);
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("完善账号信息");
		mEditName.setText(mLoginHandler.mResultBean.getUsername());
		mTextText.setText("亲爱的" + mLoginHandler.mResultBean.getNickname() + "，请完善以下账号信息");
	}

	@Override
	public void onClick(View v) {
		final String password = mEditPass.getText().toString();
		final String nickname = mEditNick.getText().toString();
		if (nickname.length() < 6 || nickname.length() > 16) {
			ToastUtils.show(mActivity, "用户名长度不能小于6位或大于16位！");
			return;
		} else if (!nickname.matches("^[A-Za-z0-9]+$")) {
			ToastUtils.show(mActivity, "用户名只能为字母或数字！");
			return;
		} else if (password.isEmpty() || nickname.isEmpty()) {
			ToastUtils.show(mActivity, "用户名或密码不能为空！");
			return;
		} else if (password.length() < 6 || password.length() > 20) {
			ToastUtils.show(mActivity, "密码长度不能小于6位或大于20位！");
			return;
		}
		if (v.getId() == ResUtils.getResById(mActivity, "btn_perfect_entry", "id")) {
			LoginHandler.getInstance(mActivity).doPerfectAccountInfo(nickname,password);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mActivity.finish();
	}
}
