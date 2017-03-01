package com.tianyou.sdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.Gson;
import com.tianyou.sdk.base.FloatControl;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

/**
 * 显示悬浮小球菜单
 * @author itstrong
 */
public class FloatMenu implements OnClickListener, OnTouchListener {

	private Activity mActivity;
	private View mView;
	private int mWidth;
	
	private PopupWindow mLogoPopupWindow;
	private PopupWindow mMenupopupWindow;
	private boolean isRight = false;		//悬浮球是否在右边
	private boolean isHideState = true;		//是否是隐藏状态
	private boolean isShowFloat = true;		//当前显示的是悬浮球还是悬浮菜单
	private int isHidePopup = 0;
	
	public FloatMenu(Activity activity) {
		this.mActivity = activity;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.mWidth = dm.widthPixels;
		
		if (mView == null) {
			mView = new View(mActivity);
			FrameLayout layout = new FrameLayout(mActivity);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layout.addView(mView);
			mActivity.addContentView(layout, params);
		}
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isHidePopup > 4) {
					isHideState = true;
					if (mMenupopupWindow != null) {
						mMenupopupWindow.dismiss();
					}
					mImgPopupLogo.setImageResource(isRight ? ResUtils.getResById(mActivity, "ty_float_logo_right", "drawable")
							: ResUtils.getResById(mActivity, "ty_float_logo_left", "drawable"));
				} else {
					isHidePopup++;
				}
				handler.postDelayed(this, 1000);
			}
		}, 1000);
	}
	
	public void createLogoPopupWindow() {
		if (isShowFloat) {
			mLogoPopupWindow = new PopupWindow(null, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			View contentView = LayoutInflater.from(mActivity).inflate(ResUtils.getResById(mActivity, "item_popup_logo", "layout"), null);
			mImgPopupLogo = (ImageView) contentView.findViewById(ResUtils.getResById(mActivity, "img_popup_logo", "id"));
			mImgPopupLogo.setOnClickListener(this);
			mImgPopupLogo.setOnTouchListener(this);
			mLogoPopupWindow.setContentView(contentView);
			mLogoPopupWindow.setFocusable(false);
			try {
				mLogoPopupWindow.showAtLocation(mView, Gravity.LEFT, 0, 0);
			} catch (Exception e) {
				LogUtils.w("mLogoPopupWindow异常...");
			}
			mLogoPopupWindow.update();
		}
	}
	
	private void showMenuPopupWindow() {
		isHidePopup = 0;
		if (!isShowFloat) {
			mMenupopupWindow = new PopupWindow();
			final View contentView  = LayoutInflater.from(mActivity).inflate(isRight ? ResUtils.getResById(mActivity, "item_popup_menu_right", "layout") : ResUtils.getResById(mActivity, "item_popup_menu_left", "layout"), null);
			mImgMenuLogo = (ImageView)contentView.findViewById(ResUtils.getResById(mActivity, "item_menu_logo", "id"));
			mImgMenuLogo.setOnClickListener(this);
			
			View menu0 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_0", "id"));
			View menu1 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_1", "id"));
			View menu2 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_2", "id"));
			View menu3 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_3", "id"));
			View menu4 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_4", "id"));
			View menu5 = contentView.findViewById(ResUtils.getResById(mActivity, "popup_menu_5", "id"));
			
			menu0.setOnClickListener(this);
			menu1.setOnClickListener(this);
			menu2.setOnClickListener(this);
			menu3.setOnClickListener(this);
			menu4.setOnClickListener(this);
			menu5.setOnClickListener(this);
			
			String response = SPHandler.getString(mActivity, SPHandler.SP_FLOAT_CONTROL);
			LogUtils.d("response:" + response);
			FloatControl control = new Gson().fromJson(response, FloatControl.class);
			FloatControl.ResultBean.FrameinfoBean frameinfo = control.getResult().getFrameinfo();
			menu0.setVisibility(frameinfo.getAccount() == 1 ? View.VISIBLE : View.GONE);
			menu1.setVisibility(frameinfo.getMore() == 1 ? View.VISIBLE : View.GONE);
			menu2.setVisibility(frameinfo.getGift() == 1 ? View.VISIBLE : View.GONE);
			menu3.setVisibility(frameinfo.getBbs() == 1 ? View.VISIBLE : View.GONE);
			menu4.setVisibility(frameinfo.getHelp() == 1 ? View.VISIBLE : View.GONE);
			menu5.setVisibility(frameinfo.getLogout() == 1 ? View.VISIBLE : View.GONE);
			
			mMenupopupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			mMenupopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			mMenupopupWindow.setContentView(contentView);
			mMenupopupWindow.setFocusable(true);
			mMenupopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			mMenupopupWindow.showAtLocation(mView, Gravity.LEFT, isRight ? mWidth : 0, -dy);
			mMenupopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					mImgPopupLogo.setVisibility(View.VISIBLE);
					isShowFloat = true;
				}
			});
		}
	}
	
	int lastX = 0;
	int lastY = 0;
	int dx = 0;
	int dy = 0;
	int mScreenX = 0;
	int mScreenY = 0;
	private ImageView mImgPopupLogo;
	private ImageView mImgMenuLogo;
	private Handler handler;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isHideState || !isShowFloat) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isHidePopup = 0;
			lastX = (int)event.getRawX();
			lastY = (int)event.getRawY();
			break;

		case MotionEvent.ACTION_MOVE:
			dx = (int)event.getRawX() - lastX + mScreenX;
			dy = lastY - (int)event.getRawY() + mScreenY;
			mLogoPopupWindow.update(dx, -dy, -1, -1);
			break;
			
		case MotionEvent.ACTION_UP:
			mScreenX = dx;
            mScreenY = dy;
            LogUtils.d("dy:" + dy);
            if (dx != 0 && dy != 0) {
            	if (event.getRawX() > mWidth / 2) {
    				mLogoPopupWindow.update(mWidth, -dy, -1, -1);
    				mScreenX = mWidth;
    				isRight = true;
    			} else {
    				mLogoPopupWindow.update(0, -dy, -1, -1);
    				mScreenX = 0;
    				isRight = false;
    			}
			} 
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == ResUtils.getResById(mActivity, "img_popup_logo", "id")) {
			mImgPopupLogo.setImageResource(ResUtils.getResById(mActivity, "ty_popup_menu_logo", "drawable"));
			isShowFloat = false;
			mImgPopupLogo.setVisibility(View.INVISIBLE);
			showMenuPopupWindow();
			isHideState = false;
			return;
		} else {
			if (id != ResUtils.getResById(mActivity, "item_menu_logo", "id")) {
				if (!ConfigHolder.userIsLogin) {
					ToastUtils.show(mActivity, "请先登录");
				} else {
					Intent intent = new Intent(mActivity, MenuActivity.class);
					if (id == ResUtils.getResById(mActivity, "popup_menu_0", "id")) {
						intent.putExtra("menu_type", MenuActivity.POPUP_MENU_0);
					} else if (id == ResUtils.getResById(mActivity, "popup_menu_1", "id")) {
						intent.putExtra("menu_type", MenuActivity.POPUP_MENU_1);
					} else if (id == ResUtils.getResById(mActivity, "popup_menu_2", "id")) {
						intent.putExtra("menu_type", MenuActivity.POPUP_MENU_2);
					} else if (id == ResUtils.getResById(mActivity, "popup_menu_3", "id")) {
						intent.putExtra("menu_type", MenuActivity.POPUP_MENU_3);
					} else if (id == ResUtils.getResById(mActivity, "popup_menu_4", "id")) {
						intent.putExtra("menu_type", MenuActivity.POPUP_MENU_4);
					} else if (id == ResUtils.getResById(mActivity, "popup_menu_5", "id")) {
						ConfigHolder.userIsLogin = false;
						Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
						mMenupopupWindow.dismiss();
						isShowFloat = true;
						return;
					}
					mActivity.startActivity(intent);
				}
			}
			mMenupopupWindow.dismiss();
			isShowFloat = true;
		}
	}
}
