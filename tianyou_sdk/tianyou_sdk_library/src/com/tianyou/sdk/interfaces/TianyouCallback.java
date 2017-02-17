package com.tianyou.sdk.interfaces;

public interface TianyouCallback {

	/**
	 * sdk操作成功回调
	 */
	public void onSuccess(String msg);

	/**
	 * sdk操作失败回调
	 */
	public void onFailed(String msg);
	
	public interface LoginCallback {
		/**
		 * sdk操作成功回调
		 */
		public void onSuccess(String userId, String userToken);

		/**
		 * sdk操作失败回调
		 */
		public void onFailed(String msg);
	}
}
