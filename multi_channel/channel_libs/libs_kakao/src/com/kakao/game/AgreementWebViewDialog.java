package com.kakao.game;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.TextView;

import com.kakao.android.sdk.R;
import com.kakao.game.exception.GameWebViewException;
import com.kakao.util.exception.KakaoException;

/**
 * Created by house.dr on 2016. 10. 24..
 */

public class AgreementWebViewDialog extends Dialog {
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;
    public static final String KEY_PARAMS = "key.params";
    public static final String KEY_EXCEPTION = "key.exception";
    public static final String KAKAOGAME_AGREEMENT_SCHEME = "kakaogame://AgreementOk?";

    private String WEB_URL = "https://terms-zinny3.game.kakao.com/agreement/form?";
    private ProgressDialog progressDialog;
    private String url;
    private Activity activity;
    private WebView webView;
    private TextView titleView;
    private View backView;
    private ResultReceiver resultReceiver;

    public AgreementWebViewDialog(Activity activity, String parameters, ResultReceiver resultReceiver) {
        super(activity, android.R.style.Theme_NoTitleBar_Fullscreen);
        this.activity = activity;
        url = WEB_URL + parameters;
        this.resultReceiver = resultReceiver;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView = inflater.inflate(R.layout.game_webview, null);
        setContentView(mainView);

        int webviewID = mainView.getResources().getIdentifier("game_webview", "id", this.activity.getPackageName());
        webView = (WebView) mainView.findViewById(webviewID);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new GameWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        int titleViewID = mainView.getResources().getIdentifier("webview_topbar_title", "id", this.activity.getPackageName());
        titleView = (TextView) mainView.findViewById(titleViewID);
        int backViewID = mainView.getResources().getIdentifier("webview_topbar_back", "id", this.activity.getPackageName());
        backView = mainView.findViewById(backViewID);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    backView.setVisibility(View.INVISIBLE);
                }
            }
        });

        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showProgressDialog() {
        hideProgressDialog();
        progressDialog = ProgressDialog.show(getContext(), null, "Loading...");
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void sendSuccessToListener(String url) {
        if (resultReceiver != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_PARAMS, url.replace(KAKAOGAME_AGREEMENT_SCHEME, ""));
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

    private class GameWebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(KAKAOGAME_AGREEMENT_SCHEME)) {
                sendSuccessToListener(url);
                dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            hideProgressDialog();
            sendErrorToListener(new GameWebViewException(errorCode, description, failingUrl));
            dismiss();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            hideProgressDialog();

            WebView webView = new WebView(activity);
            String userAgent = webView.getSettings().getUserAgentString();
            if (userAgent.contains("Chrome/53") || userAgent.contains("Chrome/54")) {
                activity.runOnUiThread(new Thread(new Runnable() {
                    public void run() {
                        AlertDialog.Builder aBuilder = new AlertDialog.Builder(activity);
                        aBuilder.setMessage(R.string.txt_web_view_update)
                                .setCancelable(false)
                                .setPositiveButton(R.string.txt_web_view_update_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                                        marketLaunch.setData(Uri.parse("market://details?id=com.google.android.webview"));
                                        activity.startActivity(marketLaunch);
                                        dismiss();
                                    }
                                });
                        AlertDialog alertDialog = aBuilder.create();
                        alertDialog.show();
                    }
                }));
            } else {
                sendErrorToListener(new GameWebViewException(ERROR_FAILED_SSL_HANDSHAKE, null, null));
                dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgressDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressDialog();
            if (backView != null) {
                if (view.canGoBack()) {
                    backView.setVisibility(View.VISIBLE);
                } else {
                    backView.setVisibility(View.INVISIBLE);
                }
            }

            if (titleView != null) {
                titleView.setText(view.getTitle());
            }
        }
    }
}
