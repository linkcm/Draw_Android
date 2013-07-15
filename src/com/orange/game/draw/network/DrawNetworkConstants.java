package com.orange.game.draw.network;

import com.orange.game.R;

import android.content.Context;
import android.widget.Toast;

public class DrawNetworkConstants {

	public final static String FORMAT = "format";

	public final static String FORMAT_PB = "pb";
	public final static String FORMAT_JSON = "json";

	public static final int ERROR_NETWORK = 20130000;
	public static final int ERROR_INVALID_PROTOCOL_BUFFER = 20130001;
	public static final int ERROR_NO_DATA_AFTER_PARSING = 20130002;	
	public static final int ERROR_NO_DATA = 20130003;
	public static final int ERROR_FEED_NULL = 20130004;

	public static final String DEVICE_TYPE_ANDROID = "2";
	
	public static void  errorToast(Context context,int errorCode)
	{
		String err = context.getString(R.string.http_load_data_fail);
		Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
	}
	
}
