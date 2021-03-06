package com.tianyou.channel.utils;

import android.app.Activity;
import android.content.Context;

public class ResUtils {

	/**
	 * 通过id获取资源
	 * @param context
	 * @param name 资源ID
	 * @param deftype 资源类型
	 * @return
	 */
	public static int getResById(Context context, String name, String deftype){
		return context.getResources().getIdentifier(name, deftype.toString(), context.getPackageName());
	}
	
	public static String getString(Activity activity, String name){
		return activity.getString(getResById(activity, name, "string"));
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	 * @param context
	 * @param dpValue
	 * @return
	 */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
}
