//package com.tianyou.channel.activity;
//
//import java.util.ArrayList;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.res.Resources.NotFoundException;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.tianyou.channel.utils.LogUtils;
//import com.tianyou.channel.utils.ResUtils;
//
//public class SplashActivity extends Activity implements OnRequestPermissionsResultCallback{
//	private Activity mActivity;
//	private AlertDialog confirmDialog;
//	private AlertDialog agreeDialog;
//	private ArrayList<String> list;
//	private String[] permissions = new String[5];//{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION
////			,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.GET_ACCOUNTS};
//
//	@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		mActivity = this;
//		list = new ArrayList<String>();
//		addDataToList();
//		getPermission();
//		Log.d("TAG", "---------------");
//    }
//	
//	private void setContentView() {
////		setContentView(ResUtils.getResById(this, "activity_splash", "layout"));
////		new Handler().postDelayed(new Runnable() {
////			@Override
////			public void run() {
//				try {
//					Class<?> mainClass = Class.forName("org.cocos2dx.lua.AppActivity");
//					startActivity(new Intent(getApplicationContext(), mainClass));
//					finish();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (NotFoundException e) {
//					e.printStackTrace();
//				}
////			}
////		}, 500);
//	}
//
//	private void getPermission() {
//		if (Build.VERSION.SDK_INT >= 23) {
//        	if (isShowDialog()) {
//        		View view = getConfirmDialog();
//        		view.findViewById(ResUtils.getResById(mActivity, "tv_permisson_confirm", "id")).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						confirmDialog.dismiss();
////						if (isPhoneAllow() && !isLocationAllow()) {
////							permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
////						} else if (!isPhoneAllow() && isLocationAllow()) {
////							permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
////						} else if (!isPhoneAllow() && !isLocationAllow()) {
////							permissions = permissions;
////						}
//						makePermission();
//						
//						ActivityCompat.requestPermissions(mActivity, permissions, 1);
//					}
//				});
//        	} else {
//        		setContentView();
//			}
//	  } else {
//		  setContentView();
//	  }
//	}
//
//	private boolean isShowDialog(){
////		if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE)
////				== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
////				== PackageManager.PERMISSION_GRANTED) {
//		if (isAccounts() && isAudio() && isPhoneAllow() && isExtra() && isLocationAllow()){
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
//	private boolean isPhoneAllow(){
//		return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_PHONE_STATE)
//				== PackageManager.PERMISSION_GRANTED;
//	}
//	
//	private boolean isLocationAllow(){
//		return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
//				== PackageManager.PERMISSION_GRANTED;
//	}
//	
//	private boolean isAudio(){
//		return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
//				== PackageManager.PERMISSION_GRANTED;
//	}
//	
//	private boolean isExtra(){
//		return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//				== PackageManager.PERMISSION_GRANTED;
//	}
//	
//	private boolean isAccounts(){
//		return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.GET_ACCOUNTS)
//				== PackageManager.PERMISSION_GRANTED;
//	}
//	
//	private View getConfirmDialog(){
//		View view = View.inflate(mActivity, ResUtils.getResById(mActivity, "ty_confirm_dialog", "layout"), null);
//    	AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,ResUtils.getResById(mActivity, "Theme_Transparent", "style"));
//    	builder.setView(view);
//    	confirmDialog = builder.create();//.show();
//    	confirmDialog.setCancelable(false);
//    	confirmDialog.show();
//    	return view;
//	}
//	
//	private void showAgreeDialog(){
//		View view = View.inflate(mActivity, ResUtils.getResById(mActivity, "ty_agree_dialog", "layout"), null);
//    	AlertDialog.Builder builder = new AlertDialog.Builder(mActivity,ResUtils.getResById(mActivity, "Theme_Transparent", "style"));
//    	builder.setView(view);
//    	final AlertDialog agreeDialog = builder.create();//.show();
//    	agreeDialog.setCancelable(false);
//    	agreeDialog.show();
//    	view.findViewById(ResUtils.getResById(mActivity, "tv_permission_setting", "id")).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				agreeDialog.dismiss();
//				Log.d("TAG", "phone= "+ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_PHONE_STATE));
//				Log.d("TAG", "location= "+ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION));
//				if (isShowDialog()) {
//					if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_PHONE_STATE)
//							|| !ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) ||
//							!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
//							!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECORD_AUDIO)||
//							!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.GET_ACCOUNTS)
//							){
//						showDetail();
//					} else {
//						makePermission();
////						if (isPhoneAllow() && !isLocationAllow()) {
////							permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
//////							ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, 4);
////						} else if (!isPhoneAllow() && isLocationAllow()) {
////							permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
////						} else if (!isPhoneAllow() && !isLocationAllow()) {
////							permissions = permissions;
////						}
//						
////						if (!isLocationAllow()) {
////							ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
////						} 
////						if (!isPhoneAllow() && !isLocationAllow()) {
//							ActivityCompat.requestPermissions(mActivity, permissions, 1);
////						}
//						
//					}
//				}
//			}
//		});
//    	
//    	view.findViewById(ResUtils.getResById(mActivity, "tv_permission_close", "id")).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				LogUtils.d("quit--------------------");
////				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
//				mActivity.finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
//				LogUtils.d("quit end--------------");
//			}
//		});
//	}
//	
//	private void showDetail(){
//		Intent intent = new Intent();
//		intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//		intent.setData(Uri.fromParts("package", mActivity.getPackageName(), null)); 
//		mActivity.startActivityForResult(intent, 3);
//	}
//
//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//			@NonNull int[] grantResults) {
//		if (requestCode == 1) {
//			Log.d("TAG", "length= "+grantResults.length);
//			boolean flag = true;
//			if (grantResults.length >= 1) {
//				Log.d("TAG", "length>=2");
//				for (int i=0;i<grantResults.length;i++) {
//					Log.d("TAG", "for-----------"+i);
//					if (PackageManager.PERMISSION_GRANTED != grantResults[i] && !permissions[i].equals("")) {
//						Log.d("TAG", "grantResults[i] == "+grantResults[i]);
//						flag = false;
//						break;
//					} 
//				}
//				
//				if (flag) {
//					Log.d("TAG", "flag is tur");
//					setContentView();
//				} else {
//					Log.d("TAG", "flag is false");
//					showAgreeDialog();
//				}
////				if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
////					setContentView();
////				} else {
////					showAgreeDialog();
////				}
//			} else {
//				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//					setContentView();
//				} else {
//					showAgreeDialog();
//				}
//			}
//		}
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 3) {
//			if (!isPhoneAllow() || !isLocationAllow() || !isAccounts() || !isAudio() || !isExtra()){
//				getPermission();
//			} else {
//				setContentView();
//			}
//		}
//	}
//	
//	private void addDataToList(){
//		list.add(Manifest.permission.READ_PHONE_STATE);
//		list.add(Manifest.permission.ACCESS_FINE_LOCATION);
//		list.add(Manifest.permission.RECORD_AUDIO);
//		list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//		list.add(Manifest.permission.GET_ACCOUNTS);
//	}
//
//	
//	private void makePermission(){
//		list.clear();
//		addDataToList();
//		if (isPhoneAllow()) {
//			for (int i=0;i<list.size();i++){
//				if (list.get(i) == Manifest.permission.READ_PHONE_STATE) {
////					list.remove(i);
//					list.set(i, "");
//				}
//			}
//		}
//		
//		if (isLocationAllow()) {
//			for (int i=0;i<list.size();i++){
//				if (list.get(i) == Manifest.permission.ACCESS_FINE_LOCATION) {
////					list.remove(i);
//					list.set(i, "");
//				}
//			}
//		}
//		
//		if (isAccounts()) {
//			for (int i=0;i<list.size();i++){
//				if (list.get(i) == Manifest.permission.GET_ACCOUNTS) {
////					list.remove(i);
//					list.set(i, "");
//				}
//			}
//		}
//		
//		if (isAudio()) {
//			for (int i=0;i<list.size();i++){
//				if (list.get(i) == Manifest.permission.RECORD_AUDIO) {
////					list.remove(i);
//					list.set(i, "");
//				}
//			}
//		}
//		
//		if (isExtra()) {
//			for (int i=0;i<list.size();i++){
//				if (list.get(i) == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
////					list.remove(i);
//					list.set(i, "");
//				}
//			}
//		}
//		
//		if (list.size() >0){
//			for (int i=0;i<list.size();i++){
//				permissions[i] = list.get(i);
//			}
//		}
//	}
//}
