package com.lzx.allenLee.ui;

import java.io.InputStream;
import java.util.Random;

import com.lzx.allenLee.R;
import com.lzx.allenLee.base.BaseActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class Act_start extends BaseActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		initView();
		init();
	}

	@Override
	protected void initView() {
		ImageView iv_img = (ImageView) this.findViewById(R.id.iv_img);
		iv_img.setImageDrawable(getLoadingDrawable());
	}

	private void init() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				gotoActivity(Act_login.class);
				Act_start.this.finish();
			}
		}, 2 * 1000);
	}

	/**
	 * 加载图片
	 * 
	 * @return
	 */
	private Drawable getLoadingDrawable() {
		Random rand = new Random();
		int num = rand.nextInt(21);
		String startPgn = "sys/start" + num + ".png";
		Drawable localDrawable;
		try {
			InputStream localInputStream = getAssets().open(startPgn);
			if (localInputStream == null) {
				localDrawable = null;
			} else {
				localDrawable = Drawable.createFromStream(localInputStream, startPgn);
				localInputStream.close();
			}
		} catch (OutOfMemoryError localOutOfMemoryError) {
			System.gc();
			localDrawable = null;
		} catch (Exception localException) {
			localDrawable = null;
		}
		return localDrawable;
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
}
