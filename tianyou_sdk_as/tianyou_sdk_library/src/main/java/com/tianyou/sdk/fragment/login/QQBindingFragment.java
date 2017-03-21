package com.tianyou.sdk.fragment.login;

import java.util.List;
import java.util.Map;

import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.ResUtils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * QQ登陆绑定列表
 * @author itstrong
 * 
 */
public class QQBindingFragment extends BaseLoginFragment {

	private TextView mTextName;
	private TextView mTextAccount;
	private View mLayoutUser;
	
	private PopupWindow mPopupWindow;
	private LoginAdapter mAdapter;
	private ListView mListView;
	private String mUserName;
	private String mUserPass;
	private List<Map<String, String>> mLoginInfos;

	@Override
	protected String setContentView() { return "fragment_login_qq_binding"; }

	@Override
	protected void initView() {
		mTextName = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_binding_name", "id"));
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_binding_account", "id"));
		mLayoutUser = mContentView.findViewById(ResUtils.getResById(mActivity, "layouyt_binding_user", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_binding_entry", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_binding_add", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "img_binding_pull", "id")).setOnClickListener(this);
		mListView = new ListView(mActivity);
        mListView.setBackgroundResource(ResUtils.getResById(mActivity, "listview_background", "drawable"));
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("QQ账号绑定列表");
		mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ);
		if (mLoginInfos.size() == 0) return;
		mTextName.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_NICKNAME));
		mUserPass = mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD);
		mUserName = mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT);
		mTextAccount.setText("天游用户名：" + mUserName);
		mAdapter = new LoginAdapter();
        mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_binding_entry", "id")) {
			mUserPass = mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD);
			mUserName = mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT);
			mLoginHandler.doUserLogin(mUserName, mUserPass, false);
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_binding_add", "id")) {
			Intent intent = new Intent(mActivity, WebViewAvtivity.class);
			intent.putExtra("title", ResUtils.getString(mActivity, "ty_qq_login"));
			intent.putExtra("url", URLHolder.URL_QQ_WEB);
			startActivityForResult(intent, 100);
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_binding_pull", "id")) {
			showPopupWindow();
		}
	}
	
	// 用户登录下拉弹窗
	private void showPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mListView, 0, 0);
			mPopupWindow.setWidth(mLayoutUser.getWidth());
			mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			mPopupWindow.setContentView(mListView);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(ResUtils.getResById(mActivity, "shape_btn_gray", "drawable")));
			mPopupWindow.setFocusable(true);
		}
		mPopupWindow.showAsDropDown(mLayoutUser, 0, 0);
	}
	
	class LoginAdapter extends BaseAdapter {

	    @Override
	    public int getCount() {
	    	return mLoginInfos.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return position;
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        ViewHolder holder;
	        if (convertView == null) {
	            holder = new ViewHolder();
	            convertView = View.inflate(mActivity, ResUtils.getResById(mActivity, "item_list_login", "layout"), null);
	            holder.textAccount = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_item_account", "id"));
	            holder.textServer = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_item_server", "id"));
	            holder.imgPoint = convertView.findViewById(ResUtils.getResById(mActivity, "img_item_point", "id"));
	            holder.layoutInfo = convertView.findViewById(ResUtils.getResById(mActivity, "layout_item_info", "id"));
	            holder.imgClose = convertView.findViewById(ResUtils.getResById(mActivity, "img_item_close", "id"));
	            convertView.setTag(holder);
	        } else {
	            holder = (ViewHolder) convertView.getTag();
	        }
	        final Map<String, String> info = mLoginInfos.get(position);
	        holder.textAccount.setText(info.get(LoginInfoHandler.USER_ACCOUNT));
	        holder.textServer.setText(info.get(LoginInfoHandler.USER_SERVER));
	        holder.imgPoint.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
	        holder.layoutInfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mTextName.setText(mLoginInfos.get(position).get(LoginInfoHandler.USER_NICKNAME));
					mTextAccount.setText("天游用户：" + mLoginInfos.get(position).get(LoginInfoHandler.USER_ACCOUNT));
					LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ, mLoginInfos.get(position));
					mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ);
					mAdapter.notifyDataSetChanged();
					mPopupWindow.dismiss();
				}
			});
	        holder.imgClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder = new Builder(mActivity);
					builder.setMessage("确定要删除账号" + info.get(LoginInfoHandler.USER_ACCOUNT) + "？");
					builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
							mPopupWindow.dismiss();
							mLoginInfos.remove(position);
							LoginInfoHandler.saveLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ, mLoginInfos);
							mAdapter.notifyDataSetChanged();
							if (mLoginInfos.size() == 0) {
								mActivity.switchFragment(new NoQQFragment(), "NoQQFragment");
							} else {
								mTextName.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_NICKNAME));
							}
						}
					});
					builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
					builder.create().show();
				}
			});
	        return convertView;
	    }

	    class ViewHolder{
	    	View imgPoint;
	    	View layoutInfo;
	    	View imgClose;
	        TextView textAccount;
	        TextView textServer;
	    }
	}
}
