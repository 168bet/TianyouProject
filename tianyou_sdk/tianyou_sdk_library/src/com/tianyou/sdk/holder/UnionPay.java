package com.tianyou.sdk.holder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.tianyou.sdk.utils.LogUtils;
import com.unionpay.UPPayAssistEx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

/**
 * 银联支付接口
 * Created by itstrong on 2016/7/3.
 */
public class UnionPay implements Runnable {

    private Activity activity;
    private Handler mHandler = null;
    private static ProgressDialog mLoadingDialog = null;

    public static final int PLUGIN_VALID = 0;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;

    public final static String mMode = "00";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    private String tn;
    private boolean repeatFlag = false;

    public UnionPay (Activity activity, String tn) {
        this.activity = activity;
        this.tn = tn;
    }

    public void doUnionPay () {
    	LogUtils.d("---tn1:" + tn);
    	if (!repeatFlag) {
    		repeatFlag = true;
    		UPPayAssistEx.startPay(activity, null, null, tn, UnionPay.mMode);
    		repeatFlag = false;
		}
    	LogUtils.d("---tn2:" + tn);
    }

    @Override
    public void run() {
    	String tn = null;
        InputStream is;
        try {
            String url = TN_URL_01;
            URL myURL = new URL(url);
            URLConnection ucon = myURL.openConnection();
            ucon.setConnectTimeout(120000);
            is = ucon.getInputStream();
            int i = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((i = is.read()) != -1) {
                baos.write(i);
            }

            tn = baos.toString();
            is.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = mHandler.obtainMessage();
        msg.what = 2;
        msg.obj = tn;
        mHandler.sendMessage(msg);
    }

	public static void dismissDialog() {
		if (mLoadingDialog.isShowing()) {
          mLoadingDialog.dismiss();
      }
	}
}
