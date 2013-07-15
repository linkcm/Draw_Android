/**  
        * @title CommonImageLoaderActivity.java  
        * @package com.orange.game.draw.activity.bbs  
        * @description   
        * @author liuxiaokun  
        * @update 2013-5-9 下午2:32:06  
        * @version V1.0  
 */
package com.orange.game.draw.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.orange.game.R;

/**  
 * @description   
 * @version 1.0  
 * @author liuxiaokun  
 * @update 2013-5-9 下午2:32:06  
 */

public class CommonImageLoaderActivity extends CommonDrawActivity
{

	@Override
	protected boolean isRegisterServerConnect()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isRegisterGameMessage()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void handleServiceConnected()
	{
		// TODO Auto-generated method stub

	}

	
	
	private static final String IMAGE_URL = "imageURL";
	
	public static void newInstance(Context context,String imageURL){
		Intent intent = new Intent();
		intent.setClass(context, CommonImageLoaderActivity.class);
		intent.putExtra(IMAGE_URL, imageURL);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_image_loader);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(backOnClickListener);
		ImageView imageView = (ImageView) findViewById(R.id.common_image);
		String imageURL = getIntent().getStringExtra(IMAGE_URL);
		ImageLoader.getInstance().displayImage(imageURL, imageView);
	}
	
	
}
