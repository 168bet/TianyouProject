//package com.tianyou.channel.utils.vivo;
//
//import java.util.Map;
//
//import org.apache.http.NameValuePair;
//import org.json.JSONObject;
//
//import com.vivo.sdkplugin.aidl.VivoUnionManager;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//
//public class InitialPayTask extends AsyncTask<NameValuePair, Integer, String> {
//
//	private Activity mActivity;
//	private VivoUnionManager mUnionManager;
//	private Map<String, String> channelParam;
//	private ProgressDialog mProgress = null;
//	
//	public InitialPayTask(Activity activity,VivoUnionManager unionManager,Map<String, String> channelParam){
//		this.mActivity = activity;
//		this.mUnionManager = unionManager;
//		this.channelParam = channelParam;
//	}
//	
//	@Override
//	protected void onPreExecute() {
//		mProgress = UtilTool.showProgress(mActivity,null,"正在初始化支付，请稍等...", false, true);
//		mProgress.show();
//	}
//	
//	@Override
//	protected String doInBackground(NameValuePair... nameValuePairs) {
//		NetworkRequestAgent networkRequestAgent = new NetworkRequestAgent();
//		String resultInfo = networkRequestAgent
//				.initialPayment(nameValuePairs);
//		Log.d("TAG","doInBackground resultInfo=" + resultInfo+ ",getPackageName="+ mActivity.getPackageName());
//		return resultInfo;
//	}
//	
//	@Override
//	protected void onPostExecute(String result) {
//		try {
//			if (null != mProgress) {
//				mProgress.dismiss();
//				mProgress = null;
//			}
//			if (!UtilTool.checkStringIsNull(result)) {
//				Log.d("TAG", "result=" + result);
//				boolean isSignOk = UtilTool.checkSignatrue(result);
//				if (isSignOk) {
//					JSONObject jsonVo = new JSONObject(result);
//					String respCode = jsonVo
//							.getString(Constant.RESPONE_RESP_CODE);
//					if ("200".equals(respCode)) {
//						String transNo = jsonVo.getString(Constant.RESPONE_VIVO_ORDER);
//						String accessKey = jsonVo.getString(Constant.RESPONE_VIVO_SIGNATURE);
//						Bundle localBundle = new Bundle();
//						String packageName = mActivity.getPackageName();
//						localBundle.putString(Constants.PAY_PARAM_TRANSNO, transNo);
//						localBundle.putString(Constant.RESPONE_VIVO_SIGNATURE, accessKey);
//						localBundle.putString(Constants.PAY_PARAM_APPID, Constant.VIVO_APP_ID);
//						localBundle.putString(Constants.PAY_PARAM_PRODUCT_NAME, channelParam.get("product_name"));
//						localBundle.putString(Constants.PAY_PARAM_PRODUCT_DEC, channelParam.get("product_des"));
//						localBundle.putLong(Constants.PAY_PARAM_PRICE, Long.parseLong(channelParam.get("price")));
//						localBundle.putString("blance", "100元宝");
//						localBundle.putString("vip", "vip2");
//						localBundle.putInt("level", 35);
//						localBundle.putString("party", "工会");
//						localBundle.putString("roleId", "角色id");
//						localBundle.putString("roleName", "角色名称角色名称角色名称");
//						localBundle.putString("serverName", "区服信息");
//						localBundle.putString("extInfo", "扩展参数");
//						localBundle.putBoolean("logOnOff", true);
//						mUnionManager.payment(mActivity, localBundle);
//						Log.d("TAG", "transNo=" + transNo);
//						Log.d("TAG", "localBundle="+localBundle.toString());
//					} else {
//						Toast.makeText(mActivity, "初始化支付失败", Toast.LENGTH_SHORT).show();
////						showPayMessage(R.string.ini_install_fail);
//					}
//				} else {
//					Toast.makeText(mActivity, "交易信息被篡改", Toast.LENGTH_SHORT).show();
////					showPayMessage(R.string.ini_sign_fail);
//				}
//			} else {
//				Toast.makeText(mActivity, "初始化支付失败", Toast.LENGTH_SHORT).show();
////				showPayMessage(R.string.ini_install_fail);
//			}
//		} catch (Exception e) {
//			Toast.makeText(mActivity, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
////			showPayMessage(R.string.network_error);
//			e.printStackTrace();
//		}
//	}
//}
