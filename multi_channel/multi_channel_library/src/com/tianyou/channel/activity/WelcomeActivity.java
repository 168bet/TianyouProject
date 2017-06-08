package com.tianyou.channel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.BDGameSDKSetting;
import com.baidu.gamesdk.BDGameSDKSetting.Domain;
import com.baidu.gamesdk.BDGameSDKSetting.Orientation;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.ResultCode;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.utils.ConfigHolder;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBDGameSDK();
    }

    private void initBDGameSDK() { // 初始化游戏SDK
    	ChannelInfo channelInfo = ConfigHolder.getChannelInfo(this);
        BDGameSDKSetting mBDGameSDKSetting = new BDGameSDKSetting();
        mBDGameSDKSetting.setAppID(Integer.parseInt(channelInfo.getAppId())); // APPID设置
        mBDGameSDKSetting.setAppKey(channelInfo.getAppKey()); // APPKEY设置
        mBDGameSDKSetting.setDomain(Domain.RELEASE); // 设置为正式模式
        mBDGameSDKSetting.setOrientation(Orientation.LANDSCAPE);
        BDGameSDK.init(this, mBDGameSDKSetting, new IResponse<Void>() {
            @Override
            public void onResponse(int resultCode, String resultDesc, Void extraData) {
                switch (resultCode) {
                    case ResultCode.INIT_SUCCESS:
                        // 初始化成功
						Class<?> mainClass;
						try {
							mainClass = Class.forName("org.cocos2dx.lua.AppActivity");
							Intent intent = new Intent(WelcomeActivity.this, mainClass);
							startActivity(intent);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
                        finish();
                        break;

                    case ResultCode.INIT_FAIL:
                    default:
                        Toast.makeText(WelcomeActivity.this, "启动失败", Toast.LENGTH_LONG).show();
                        finish();
                }

            }
        });
    }

}
