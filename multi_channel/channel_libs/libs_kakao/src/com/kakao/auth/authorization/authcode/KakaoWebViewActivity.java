package com.kakao.auth.authorization.authcode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.kakao.auth.KakaoSDK;
import com.kakao.android.sdk.R;
import com.kakao.auth.Session;
import com.kakao.auth.exception.KakaoWebviewException;
import com.kakao.auth.receiver.SmsReceiver;
import com.kakao.network.ServerProtocol;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.SystemInfo;
import com.kakao.util.helper.log.Logger;

public class KakaoWebViewActivity extends Activity {

    private static SmsReceiver smsReceiver;

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;

    public static final String KEY_REDIRECT_URL = "key.redirect.url";
    public static final String KEY_EXCEPTION = "key.exception";

    public static final String KEY_URL = "key.url";
    public static final String KEY_EXTRA_HEADERS = "key.extra.headers";
    public static final String KEY_USE_WEBVIEW_TIMERS = "key.use.webview.timers";
    public static final String KEY_USE_SMS_RECEIVER = "key.use.sms.receiver";
    public static final String KEY_RESULT_RECEIVER = "key.result.receiver";

    private String url;
    private Map<String, String> headers = new HashMap<String, String>();
    private boolean useWebViewTimers;
    private boolean useSmsReceiver;
    private ResultReceiver resultReceiver;

    private WebView webView;
    private ProgressBar progressBar;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, KakaoWebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        parseIntent(getIntent());

        setContentView(R.layout.activity_kakao_webview);
        initUi();
        registerSmsReceiverIfNeeded();
    }

    private void parseIntent(Intent intent) {
        url = intent.getStringExtra(KEY_URL);
        useWebViewTimers = intent.getBooleanExtra(KEY_USE_WEBVIEW_TIMERS, false);
        useSmsReceiver = intent.getBooleanExtra(KEY_USE_SMS_RECEIVER, false);
        resultReceiver = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
        Bundle extraHeaders = intent.getParcelableExtra(KEY_EXTRA_HEADERS);
        headers.put(CommonProtocol.KA_HEADER_KEY, SystemInfo.getKAHeader());
        if (extraHeaders != null && !extraHeaders.isEmpty()) {
            for (String key : extraHeaders.keySet()) {
                headers.put(key, extraHeaders.getString(key));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent(intent);

        setProgressBarVisibility(View.VISIBLE);
        webView.loadUrl(url, headers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (useWebViewTimers) {
            webView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (useWebViewTimers) {
            webView.pauseTimers();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        sendCancelToListener();
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        // prevent default animation on some device
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterSmsReceiverIfNeeded();
    }

    private void sendCancelToListener() {
        sendErrorToListener(new KakaoException(KakaoException.ErrorType.CANCELED_OPERATION, "pressed back button or cancel button during requesting auth code."));
    }

    private void sendSuccessToListener(String redirectURL) {
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_REDIRECT_URL, redirectURL);
            resultReceiver.send(RESULT_SUCCESS, bundle);
        }
    }

    private void sendErrorToListener(Throwable error) {
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            KakaoException kakaoException;
            if (error instanceof KakaoException) {
                kakaoException = (KakaoException) error;
            } else {
                kakaoException = new KakaoException(error);
            }
            bundle.putSerializable(KEY_EXCEPTION, kakaoException);
            resultReceiver.send(RESULT_ERROR, bundle);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initUi() {

        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        webView.setBackgroundResource(android.R.color.white);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new KakaoWebViewClient());
        webView.setWebChromeClient(new KakaoWebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(KakaoSDK.getAdapter().getSessionConfig().isSaveFormData());
        webView.getSettings().setSavePassword(false);

        setProgressBarVisibility(View.VISIBLE);
        webView.loadUrl(url, headers);
    }

    private void setProgressBarVisibility(int visibility) {
        if (!isFinishing()) {
            progressBar.setVisibility(visibility);
        }
    }

    private class KakaoWebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.d("Redirect URL: " + url);

            // redirect uri
            if(url.startsWith(Session.REDIRECT_URL_PREFIX) && (url.contains(Session.REDIRECT_URL_POSTFIX) || url.contains(Session.AGEAUTH_REDIRECT_URL_POSTFIX))){
                sendSuccessToListener(url);
                finish();
            } else if(url.contains(ServerProtocol.AUTH_AUTHORITY) || url.contains(ServerProtocol.API_AUTHORITY) || url.contains(ServerProtocol.AGE_AUTH_AUTHORITY)) {
                // 로그인창, 동의창
                webView.loadUrl(url, headers);
            } else {
                //full browser!!!
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            setProgressBarVisibility(View.GONE);
            sendErrorToListener(new KakaoWebviewException(errorCode, description, failingUrl));
            finish();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            setProgressBarVisibility(View.GONE);

            WebView webView = new WebView(KakaoWebViewActivity.this);
            String userAgent = webView.getSettings().getUserAgentString();
            if (userAgent.contains("Chrome/53") || userAgent.contains("Chrome/54")) {
                runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(KakaoWebViewActivity.this);
                        aBuilder.setMessage(R.string.txt_web_view_update)
                                .setCancelable(false)
                                .setPositiveButton(R.string.txt_web_view_update_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                        marketLaunch.setData(Uri.parse("market://details?id=com.google.android.webview"));
                                        startActivity(marketLaunch);
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = aBuilder.create();
                        alertDialog.show();
                    }
                }));
            } else {
                sendErrorToListener(new KakaoWebviewException(ERROR_FAILED_SSL_HANDSHAKE, null, null));
                finish();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.d("Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            setProgressBarVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setProgressBarVisibility(View.GONE);
        }
    }

    /**
     * KakaoWebChromeClient
     */
    private class KakaoWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(KakaoWebViewActivity.this).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            String msg = null;
            String positive = null;
            String negative = null;

            try {
                JSONObject object = new JSONObject(message);

                msg = object.optString("message");
                positive = object.optString("positive");
                negative = object.optString("negative");

            } catch (JSONException e) {
                Logger.e("JSONException: " + e.getMessage());
            } finally {

                msg = TextUtils.isEmpty(msg) ? message : msg;
                positive = TextUtils.isEmpty(positive) ? getString(android.R.string.ok) : positive;
                negative = TextUtils.isEmpty(negative) ? getString(android.R.string.cancel) : negative;

                new AlertDialog.Builder(KakaoWebViewActivity.this)
                        .setMessage(msg)
                        .setPositiveButton(positive, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setNegativeButton(negative, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).setCancelable(false).create().show();
            }

            return true;
        }

        @TargetApi(Build.VERSION_CODES.FROYO)
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Logger.d("KakaoAccountWebView", consoleMessage.message()
                    + " -- (" + consoleMessage.lineNumber() + "/" + consoleMessage.sourceId() + ")");
            return true;
        }

        @Override
        @Deprecated
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Logger.d("KakaoAccountWebView", message
                    + " -- (" + lineNumber + "/" + sourceID + ")");
        }
    }

    private void registerSmsReceiverIfNeeded() {
        if (!useSmsReceiver) {
            return;
        }

        if (smsReceiver != null) {
            return;
        }
        Logger.d("registerSmsReceiver");

        smsReceiver = new SmsReceiver(new SmsReceiver.ISmsReceiver() {
            @Override
            public void onCompleteSms(String code) {
                Logger.d("++ onCompleteSms(%s)", code);
                if (!TextUtils.isEmpty(code)) {
                    final String url = String.format(Locale.US, "javascript:insertSms('%s')", code);
                    Logger.d("++ command : " + url);
                    webView.loadUrl(url);
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(SmsReceiver.ACTION);
        filter.setPriority(999);  // 행아웃에서 SMS수신 가능한 경우에도 SMS를 받으려면 priority를 3보다 높아야 한다.
        registerReceiver(smsReceiver, filter);
    }

    private void unRegisterSmsReceiverIfNeeded() {
        if (smsReceiver != null) {
            try {
                Logger.d("unregisterSmsReceiver");
                unregisterReceiver(smsReceiver);
            } catch (Exception ignore) {
            }
            smsReceiver = null;
        }
    }
}
