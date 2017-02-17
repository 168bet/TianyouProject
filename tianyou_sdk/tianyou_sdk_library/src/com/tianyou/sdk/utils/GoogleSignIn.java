package com.tianyou.sdk.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by MrV on 2016/12/30.
 */
public class GoogleSignIn implements ConnectionCallbacks,OnConnectionFailedListener{

    public int requestCode = 10 ;
    private FragmentActivity activity ;
//    public GoogleSignInOptions gso;
    public GoogleApiClient mGoogleApiClient ;
    public GoogleApiClient.OnConnectionFailedListener listener ;
//    private String clientid_google = "738790003955-6lgcm4hf42tnmh982kb3i0g647jm4tbs.apps.googleusercontent.com";
    
    private ConnectionResult mConnectionResult;
    public GoogleSignIn(FragmentActivity activity){
         mGoogleApiClient = new GoogleApiClient.Builder(activity)
         .addConnectionCallbacks(this)
         .addOnConnectionFailedListener(this)
         .addApi(Plus.API)
         .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }
        /**
           * 登录
           */
    public void signIn() {
    	try {
    		mConnectionResult.startResolutionForResult(activity, 1);
		} catch (Exception e) {
			mGoogleApiClient.connect();
		}
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
		Log.d("TAG", "id= "+person.getId()+",token= "+person.getPlusOneCount());
	}
	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
}
