//package cn.uc.gamesdk.demo;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.support.annotation.UiThread;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.uc.gamesdk.UCGameSdk;
//import cn.uc.gamesdk.demo.account.AccountInfo;
//import cn.uc.gamesdk.demo.aligames.R;
//import cn.uc.gamesdk.demo.util.APNUtil;
//import cn.uc.gamesdk.even.SDKEventKey;
//import cn.uc.gamesdk.even.SDKEventReceiver;
//import cn.uc.gamesdk.even.Subscribe;
//import cn.uc.gamesdk.exception.AliLackActivityException;
//import cn.uc.gamesdk.exception.AliNotInitException;
//import cn.uc.gamesdk.open.GameParamInfo;
//import cn.uc.gamesdk.open.OrderInfo;
//import cn.uc.gamesdk.open.UCOrientation;
//import cn.uc.gamesdk.param.SDKParamKey;
//import cn.uc.gamesdk.param.SDKParams;
//
///**
// * 游戏主程序。包含了对UCGameSDK以下接口的调用：<br>
// * <p/>
// * 1 初始化<br>
// * 2 登录<br>
// * 3 个人中心<br>
// *
// * @author chenzh
// */
//public class GameActivity extends Activity {
//
//    private static final String TAG = "GameActivity";
//    private Handler handler;
//
//    @BindView(R.id.btnLogin)
//    Button btnLogin;
//
//    @BindView(R.id.btnEnterPay)
//    Button btnPay;
//
//    @BindView(R.id.btnLogout)
//    Button btnLogout;
//
//    @BindView(R.id.btnRole)
//    Button btnSubmit;
//
//    @BindView(R.id.btnExit)
//    Button btnExit;
//
//
//    public boolean mRepeatCreate = false;
//    
//    public void onCreate(Bundle b) {
//        Log.d("GameActivity", "----------onCreate---------");
//        super.onCreate(b);
//        this.setContentView(R.layout.game);
//
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            Log.i(TAG, "onCreate with flag FLAG_ACTIVITY_BROUGHT_TO_FRONT");
//            mRepeatCreate = true;
//            finish();
//            return;
//        }
//        ButterKnife.bind(this);
//        ucNetworkAndInitUCGameSDK(getPullupInfo(getIntent()));
//        handler = new Handler(Looper.getMainLooper());
//        UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onStart is repeat activity!");
//            return;
//        }
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onRestart is repeat activity!");
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onStop is repeat activity!");
//            return;
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onNewIntent is repeat activity!");
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (this.mRepeatCreate) {
//            Log.i("onResume", "is repeat activity!");
//            return;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "AppActivity:onPause is repeat activity!");
//            return;
//        }
//    }
//
//    private String getPullupInfo(Intent intent) {
//        if(intent == null){
//            return null;
//        }
//        String pullupInfo = intent.getDataString();
//        return pullupInfo;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onActivityResult is repeat activity!");
//            return;
//        }
//        ucNetworkAndInitUCGameSDK(null);
//    }
//
//    public void ucNetworkAndInitUCGameSDK(String pullUpInfo) {
//        //!!!在调用SDK初始化前进行网络检查
//        //当前没有拥有网络
//        if (false == APNUtil.isNetworkAvailable(this)) {
//            AlertDialog.Builder ab = new AlertDialog.Builder(this);
//            ab.setMessage("网络未连接,请设置网络");
//            ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent("android.settings.SETTINGS");
//                    startActivityForResult(intent, 0);
//                }
//            });
//            ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    System.exit(0);
//                }
//            });
//            ab.show();
//        } else {
//            ucSdkInit(pullUpInfo);//执行UCGameSDK初始化
//        }
//    }
//
//    private void ucSdkInit(String pullUpInfo) {
//        GameParamInfo gameParamInfo = new GameParamInfo();
//        //gameParamInfo.setCpId(UCSdkConfig.cpId);已废用
//        gameParamInfo.setGameId(UCSdkConfig.gameId);
//        //gameParamInfo.setServerId(UCSdkConfig.serverId);已废用
//        gameParamInfo.setOrientation(UCOrientation.PORTRAIT);
//
//        SDKParams sdkParams = new SDKParams();
//
//        sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
//        sdkParams.put(SDKParamKey.PULLUP_INFO,pullUpInfo);
//
//
//        //联调环境已经废用
//      //  sdkParams.put(SDKParamKey.DEBUG_MODE, UCSdkConfig.debugMode);
//
//        try {
//            UCGameSdk.defaultSdk().initSdk(this, sdkParams);
//        } catch (AliLackActivityException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void startGame() {
//        ucSdkLogin();
//    }
//    private void paintGame(){
//        this.setContentView(R.layout.game);
//    }
//
//
//    protected void onDestroy() {
//        super.onDestroy();
//        if (this.mRepeatCreate) {
//            Log.i(TAG, "onDestroy is repeat activity!");
//            return;
//        }
//        Log.d("GameActivity", "----------onDestroy---------");
//        UCGameSdk.defaultSdk().unregisterSDKEventReceiver(receiver);
//        receiver = null;
//    }
//    @OnClick(R.id.btnLogin)
//    void ucSdkLogin() {
//        try {
//            UCGameSdk.defaultSdk().login(this, null);
//        } catch (AliLackActivityException e) {
//            e.printStackTrace();
//        } catch (AliNotInitException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @OnClick(R.id.btnRole)
//    void submit() {
//        SDKParams sdkParams = new SDKParams();
//        sdkParams.put(SDKParamKey.STRING_ROLE_ID, "11");
//        sdkParams.put(SDKParamKey.STRING_ROLE_NAME, "小小");
//        sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, Long.valueOf("23"));
//        sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, getRoleCreateTime());
//        sdkParams.put(SDKParamKey.STRING_ZONE_ID, "1");
//        sdkParams.put(SDKParamKey.STRING_ZONE_NAME, "1区-天下第一");
//
//        try {
//            UCGameSdk.defaultSdk().submitRoleData(this, sdkParams);
//            Toast.makeText(GameActivity.this, "数据已提交，查看数据是否正确，请到开放平台接入联调工具查看", Toast.LENGTH_SHORT).show();
//        } catch (AliNotInitException e) {
//            e.printStackTrace();
//        } catch (AliLackActivityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private long getRoleCreateTime() {
//        //TODO  此处需要从游戏服务器中获取到角色的创建时间返回，以秒为单位
//        return System.currentTimeMillis() / 1000;
//    }
//
//
//    @OnClick(R.id.btnLogout)
//    void ucSdkLogout() {
//
//        try {
//            UCGameSdk.defaultSdk().logout(this, null);
//        } catch (AliLackActivityException e) {
//            e.printStackTrace();
//        } catch (AliNotInitException e) {
//            e.printStackTrace();
//        }
//    }
//    @OnClick(R.id.btnExit)
//    void ucSdkExit(){
//        try {
//            UCGameSdk.defaultSdk().exit(this, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @OnClick({R.id.btnEnterPay})
//    @UiThread
//    void ucSdkPay() {
//            Map<String, String> paramMap = new HashMap<String, String>();
//            paramMap.put(SDKParamKey.CALLBACK_INFO, "DDD");
//            paramMap.put(SDKParamKey.NOTIFY_URL, "http://pay.uctest2.ucweb.com:8039/result.jsp");
//            paramMap.put(SDKParamKey.AMOUNT, "1");
//            paramMap.put(SDKParamKey.CP_ORDER_ID, "123");
//            paramMap.put(SDKParamKey.ACCOUNT_ID, requestAccountId());
//            paramMap.put(SDKParamKey.SIGN_TYPE, "MD5");
//
//            SDKParams sdkParams = new SDKParams();
//
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.putAll(paramMap);
//            sdkParams.putAll(map);
//
//            String sign = sign(paramMap);
//            sdkParams.put(SDKParamKey.SIGN, sign);
//            System.out.println("sdkParams:"+sdkParams.toString());
//            try {
//                UCGameSdk.defaultSdk().pay(this, sdkParams);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(GameActivity.this, "charge failed - Exception: "+e.toString(), Toast.LENGTH_SHORT).show();
//            }
//    }
//    /**
//     * 向SDK服务器获取当前登录账号的AccountId
//     *
//     * @return
//     */
//    private String requestAccountId() {
//        //TODO
//        return "117201900";//测试账号，密码为123456789
//    }
//    /**
//     * 签名方法
//     *
//     * @param reqMap
//     * @return
//     */
//    private static String sign(Map<String, String> reqMap) {
//        //TODO 游戏服务器需要提供签名接口，参考服务端接入包合集内PayChargeSignService签名实现
//        return "71a67060ea322937a4be242709978a93";
//    }
//    private String formatDate(long time, String format) {
//        SimpleDateFormat dateformat = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
//        dateformat.applyPattern(format);
//        return dateformat.format(time);
//    }
//
//    private void dumpOrderInfo(OrderInfo orderInfo) {
//        if (orderInfo != null) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(String.format("'orderId':'%s'", orderInfo.getOrderId()));
//            sb.append(String.format("'orderAmount':'%s'", orderInfo.getOrderAmount()));
//            sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
//            sb.append(String.format("'payWayName':'%s'", orderInfo.getPayWayName()));
//
//            Log.i(TAG, "callback orderInfo = " + sb);
//        }
//    }
//
//    public void onBackPressed() {
//            ucSdkExit();
//    }
//
//    SDKEventReceiver receiver = new SDKEventReceiver() {
//        @Subscribe(event = SDKEventKey.ON_INIT_SUCC)
//        private void onInitSucc() {
//            //初始化成功
//            handler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    startGame();
//                }
//            });
//        }
//
//        @Subscribe(event = SDKEventKey.ON_INIT_FAILED)
//        private void onInitFailed(String data) {
//            //初始化失败
//            Toast.makeText(GameActivity.this, "init failed", Toast.LENGTH_SHORT).show();
//            ucNetworkAndInitUCGameSDK(null);
//        }
//
//        @Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
//        private void onLoginSucc(String sid) {
//            Toast.makeText(GameActivity.this, "login succ,sid=" + sid, Toast.LENGTH_SHORT).show();
//            final GameActivity me = GameActivity.this;
//            AccountInfo.instance().setSid(sid);
//            handler.post(new Runnable() {
//
//                @Override
//                public void run() {
//                    paintGame();
//                }
//            });
//
//        }
//
//        @Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
//        private void onLoginFailed(String desc) {
//            Toast.makeText(GameActivity.this,desc, Toast.LENGTH_SHORT).show();
////            printMsg(desc);
//        }
//
//        @Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
//        private void onCreateOrderSucc(OrderInfo orderInfo) {
//            dumpOrderInfo(orderInfo);
//            if (orderInfo != null) {
//                String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
//                Toast.makeText(GameActivity.this, "订单已生成，获取支付结果请留意服务端回调"+txt, Toast.LENGTH_SHORT).show();
//            }
//            Log.i(TAG, "pay create succ");
//        }
//
//        @Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
//        private void onPayUserExit(OrderInfo orderInfo) {
//            dumpOrderInfo(orderInfo);
//            if (orderInfo != null) {
//                String txt = orderInfo.getOrderAmount() + "," + orderInfo.getOrderId() + "," + orderInfo.getPayWay();
//                Toast.makeText(GameActivity.this, "支付界面关闭"+txt, Toast.LENGTH_SHORT).show();
//            }
//            Log.i(TAG, "pay exit");
//        }
//
//
//        @Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
//        private void onLogoutSucc() {
//            Toast.makeText(GameActivity.this, "logout succ", Toast.LENGTH_SHORT).show();
//            AccountInfo.instance().setSid("");
//            ucSdkLogin();
//        }
//
//        @Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
//        private void onLogoutFailed() {
//            Toast.makeText(GameActivity.this, "logout failed", Toast.LENGTH_SHORT).show();
////            printMsg("注销失败");
//        }
//
//        @Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
//        private void onExit(String desc) {
//            Toast.makeText(GameActivity.this, desc, Toast.LENGTH_SHORT).show();
//
//            GameActivity.this.finish();
//
//            // 退出程序
////            Intent intent = new Intent(Intent.ACTION_MAIN);
////            intent.addCategory(Intent.CATEGORY_HOME);
////            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(intent);
//            android.os.Process.killProcess(android.os.Process.myPid());
////            printMsg(desc);
//        }
//
//        @Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
//        private void onExitCanceled(String desc) {
//            Toast.makeText(GameActivity.this, desc, Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    };
//}