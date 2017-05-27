package com.kakao.game;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.kakao.auth.Session;
import com.kakao.auth.exception.KakaoWebviewException;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import com.kakao.android.sdk.R;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by house.dr on 2017. 1. 23..
 */

public class CommunityWebViewActivity extends Activity {
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;
    public static final String KEY_RESULT_RECEIVER = "key.result.receiver";
    public static final String KEY_EXCEPTION = "key.exception";

    private String WEB_URL = "https://playgame.kakao.com/bridge/auth/capri";
//    private String WEB_URL = "https://beta-playgame.kakao.com/bridge/auth/capri";
    private WebView webView;
    private ProgressDialog progressDialog;
    private ResultReceiver resultReceiver;
    private static final int FILECHOOSER_REQ_CODE = 1887;
    private static final String DEFAULT_ACCEPT_TYPE = "image/*";
    protected ValueCallback<Uri> uploadMessage = null;
    protected ValueCallback<Uri[]> uploadMessages = null;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CommunityWebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultReceiver = getIntent().getParcelableExtra(KEY_RESULT_RECEIVER);
        setContentView(R.layout.game_webview);
        webView = (WebView) findViewById(R.id.game_webview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0 (2.x)
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                clearFileChooser();
                uploadMessage = uploadMsg;
                CommunityWebViewActivity.this.openFileChooser();

            }

            // For Android 3.x, 4.0
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                clearFileChooser();
                uploadMessage = uploadMsg;
                CommunityWebViewActivity.this.openFileChooser(acceptType);
            }

            // For Android 4.1+
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                clearFileChooser();
                uploadMessage = uploadMsg;
                CommunityWebViewActivity.this.openFileChooser(acceptType);
            }

            // For Android 4.4+
            @SuppressLint("NewApi")
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                clearFileChooser();
                uploadMessages = filePathCallback;
                CommunityWebViewActivity.this.openFileChooser(fileChooserParams.createIntent(), fileChooserParams.getTitle());
                return true;
            }
        });
        webView.setWebViewClient(new GameWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        View backView = findViewById(R.id.dialog_web_topbar);
        backView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        View lineView = findViewById(R.id.game_topbar_line);
        lineView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nativeAppKey", Session.getCurrentSession().getAppKey());
        hashMap.put("accessToken", Session.getCurrentSession().getAccessToken());
        hashMap.put("enableLogout", "true");
        JSONObject jsonObject = new JSONObject(hashMap);
        webView.postUrl(WEB_URL, EncodingUtils.getBytes(jsonObject.toString(), "BASE64"));
    }

    private class GameWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressDialog();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals("zinny://closeView")) {
                sendSuccessToListener();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            hideProgressDialog();
            sendErrorToListener(new KakaoWebviewException(errorCode, description, failingUrl));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            hideProgressDialog();
            sendErrorToListener(new KakaoWebviewException(ERROR_FAILED_SSL_HANDSHAKE, null, null));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        sendSuccessToListener();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendSuccessToListener() {
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            resultReceiver.send(RESULT_SUCCESS, bundle);
            finish();
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
            finish();
        }
    }

    private void showProgressDialog() {
        hideProgressDialog();
        progressDialog = ProgressDialog.show(this, null, "Loading...");
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_REQ_CODE) {
            Uri result = (data == null || resultCode != Activity.RESULT_OK) ? null : data.getData();

            if (result != null) {
                grantUriPermission(getPackageName(), result, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }

            if (uploadMessages != null) {
                Uri[] uris = null;

                if (result != null) {
                    uris = new Uri[] {
                            result
                    };
                }

                uploadMessages.onReceiveValue(uris);
                uploadMessages = null;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearFileChooser() {
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
            uploadMessage = null;
        }

        if (uploadMessages != null) {
            uploadMessages.onReceiveValue(null);
            uploadMessages = null;
        }
    }

    private void openFileChooser() {
        openFileChooser(DEFAULT_ACCEPT_TYPE);
    }

    private void openFileChooser(String acceptType) {

        if (TextUtils.isEmpty(acceptType)) {
            acceptType = DEFAULT_ACCEPT_TYPE;
        }
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // 선택할 파일 타입
        intent.setType(acceptType);

        openFileChooser(intent, null);
    }

    @TargetApi(19)
    private void openFileChooser(Intent intent, CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            // 파일을 선택하기 위한 앱을 고르는 팝업에서 보여지는 타이틀
            title = "File Chooser";
        }

        if (Build.VERSION.SDK_INT >= 19) {
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final Intent chooserIntent = Intent.createChooser(intent, title);

        startActivityForResult(chooserIntent, FILECHOOSER_REQ_CODE);
    }
}
