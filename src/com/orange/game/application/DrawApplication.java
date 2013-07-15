package com.orange.game.application;
import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.orange.common.android.utils.FileUtil;
import com.orange.game.R;
import com.orange.game.draw.model.user.UserPermissionManager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class DrawApplication extends Application {

	private static final String TAG = "DrawApplication";
	
	private static DrawApplication drawApplication;
	
	public static DrawApplication getInstance() {
		
		return drawApplication;
	}

	private UserPermissionManager userPermissionManager = new UserPermissionManager();
	
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		drawApplication = this;
		/*CrashHandler crashHandler = CrashHandler.getInstance();  
        // 注册crashHandler  
        crashHandler.init(getApplicationContext()); */
		
		
		
		// Get singletone instance of ImageLoader
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.unloadbg)
        .showImageForEmptyUri(R.drawable.unloadbg)
        .cacheInMemory()
        //.bitmapConfig(Bitmap.Config.ARGB_8888)
        .cacheOnDisc()
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        .build();
		
		String cachePath = FileUtil.getSDCardCachePath(getApplicationContext(),getString(R.string.app_name));
		File cacheDir = new File(cachePath);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		           
		            //.memoryCache(new UsingFreqLimitedMemoryCache(6 * 1024 * 1024)) // You can pass your own memory cache implementation
		           .discCache(new UnlimitedDiscCache(cacheDir)) // default
		           .discCacheSize(30 * 1024 * 1024)
		           .discCacheFileCount(50)
		            .defaultDisplayImageOptions(options)
		            //.enableLogging()
		            .build();
		// Initialize ImageLoader with created configuration. Do it once on Application start.
		imageLoader.init(config);
		
		
	}

	public UserPermissionManager getUserPermissionManager()
	{
		return userPermissionManager;
	}

	
	

}
