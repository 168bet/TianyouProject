package com.tianyou.sdk.base;

import java.util.List;
import java.util.Map;

import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.utils.ResUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LoginAdapter extends BaseAdapter {

	private Activity mActivity;
	private AdapterCallback mCallback;
	private List<Map<String, String>> mLoginInfos;
	
	public LoginAdapter(Activity activity, List<Map<String, String>> infos, AdapterCallback callback) {
		this.mActivity = activity;
		this.mCallback = callback;
		this.mLoginInfos = infos;
	}
	
    @Override
    public int getCount() {
    	return mLoginInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, ResUtils.getResById(mActivity, "item_list_login", "layout"), null);
            holder.textAccount = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_item_account", "id"));
            holder.textServer = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_item_server", "id"));
            holder.imgPoint = convertView.findViewById(ResUtils.getResById(mActivity, "img_item_point", "id"));
            holder.layoutInfo = convertView.findViewById(ResUtils.getResById(mActivity, "layout_item_info", "id"));
            holder.imgClose = convertView.findViewById(ResUtils.getResById(mActivity, "img_item_close", "id"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Map<String, String> info = mLoginInfos.get(position);
        holder.textAccount.setText(info.get(LoginInfoHandler.USER_ACCOUNT));
        holder.textServer.setText(info.get(LoginInfoHandler.USER_SERVER));
        holder.imgPoint.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
        holder.layoutInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Map<String, String> map = mLoginInfos.get(position);
				mLoginInfos = mCallback.userClick(map);
				notifyDataSetChanged();
			}
		});
        holder.imgClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new Builder(mActivity);
				final AlertDialog dialog = builder.create();
				builder.setMessage(mActivity.getString(ResUtils.getResById(mActivity, "ty_confirm_delete", "string"))
						+ info.get(LoginInfoHandler.USER_ACCOUNT) + "？");
				builder.setPositiveButton(mActivity.getString(ResUtils.getResById(mActivity, "ty_confirm", "string")), new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mLoginInfos.remove(position);
						mCallback.confirmDelete();
						dialog.dismiss();
						notifyDataSetChanged();
					}
				});
				builder.setNegativeButton(mActivity.getString(ResUtils.getResById(mActivity, "ty_confirm_cancel", "string")), new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mCallback.cancelDelete();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
        return convertView;
    }

    class ViewHolder{
    	View imgPoint;
    	View layoutInfo;
    	View imgClose;
        TextView textAccount;
        TextView textServer;
    }
    
    public interface AdapterCallback {

    	List<Map<String, String>> userClick(Map<String, String> infos);

		void confirmDelete();
		
		void cancelDelete();
	}
}
