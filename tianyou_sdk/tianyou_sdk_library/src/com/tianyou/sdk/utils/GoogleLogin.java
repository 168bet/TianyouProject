package com.tianyou.sdk.utils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by MrV on 2016/12/30.
 */
public class GoogleLogin implements ConnectionCallbacks,OnConnectionFailedListener, com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks{

    public int requestCode = 10 ;
    private FragmentActivity activity ;
//    public GoogleSignInOptions gso;
    public GoogleApiClient mGoogleApiClient ;
    public GoogleApiClient.OnConnectionFailedListener listener ;
    private GoogleSignListener googleSignListener ;
//    private String clientid_google = "738790003955-6lgcm4hf42tnmh982kb3i0g647jm4tbs.apps.googleusercontent.com";

    
    private ConnectionResult mConnectionResult;
    public GoogleLogin(FragmentActivity activity){// , GoogleApiClient.OnConnectionFailedListener listener ){
             this.activity = activity ;
             this.listener = listener ;

             //初始化谷歌登录服务
//             gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                     .requestEmail()
//                     .requestId()
//                     .requestIdToken(AppUtils.getMetaDataValue(activity,"google_client_id"))
//                     .requestProfile()
//                     .build();
//
//             // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
//             mGoogleApiClient = new GoogleApiClient.Builder( activity )
//                     .enableAutoManage( activity , listener )
//                     .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                     .build();
             PlusClient plusClient = new PlusClient.Builder(activity, this, this)
             .setScopes("PLUS_LOGIN")
             .build();
             
             mGoogleApiClient = new GoogleApiClient.Builder(activity)
             .addConnectionCallbacks(this)
             .addOnConnectionFailedListener(this)
             .addApi(Plus.API,Plus.PlusOptions.builder().setServerClientId("775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com").build())
             .addScope(Plus.SCOPE_PLUS_LOGIN)
             .build();
             
         }
        /**
           * 登录
           */
    public void signIn() {
    	int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
//    	if (available != ConnectionResult.SUCCESS) {
//    		showDialog(1);
//    		return;
//    	}
    	try {
    		mConnectionResult.startResolutionForResult(activity,1);
		} catch (Exception e) {
			mGoogleApiClient.connect();
		}
    	
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        activity.startActivityForResult(signInIntent, requestCode);
    }
    /**
          * 退出登录
           */
    public void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//        new ResultCallback<Status>() {
//        @Override
//        public void onResult(Status status) {
//            if ( status.isSuccess() ){
//                if ( googleSignListener != null ){ googleSignListener.googleLogoutSuccess(); }
//
//            }else {
//                if ( googleSignListener!= null ){ googleSignListener.googleLogoutFail(); }
//            }
//        }
//        });
    }

//     public String handleSignInResult(GoogleSignInResult result) {
//        String res = "" ;
//        Log.d("TAG",result.isSuccess()+"");
//        if (result.isSuccess()) {
//             //登录成功
//             GoogleSignInAccount acct = result.getSignInAccount();
//             Log.d("TAG", "res:"+res);
//             if ( googleSignListener != null ){
//                     googleSignListener.googleLoginSuccess(acct.getId(),acct.getIdToken());
//             }
//         } else {
//             // Signed out, show unauthenticated UI.
//             res = "-1" ;  //-1代表用户退出登录了 ， 可以自定义
//             if ( googleSignListener != null ){
//                    googleSignListener.googleLoginFail();
//             }
//         }
//          return res ;
//    }

    public void setGoogleSignListener( GoogleSignListener googleSignListener ){
            this.googleSignListener = googleSignListener ;
    }

    public interface GoogleSignListener {
        void googleLoginSuccess(String id, String token);
        void googleLoginFail() ;
        void googleLogoutSuccess();
        void googleLogoutFail() ;
    }
    
    public void onStart(){
    	mGoogleApiClient.connect();
    }
    
    public void onStop(){
    	mGoogleApiClient.disconnect();
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		person.getId();
		person.getPlusOneCount();
		Log.d("TAG", "id= "+person.getId()
			+",token= "+person.getPlusOneCount()
			);
	}
	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
	@Override
	public void onDisconnected() {
		
	}
}
