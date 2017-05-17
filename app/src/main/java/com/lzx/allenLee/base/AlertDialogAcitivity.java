package com.lzx.allenLee.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.lzx.allenLee.R;

/***
 * 异常退出捕获提示
 * @author Allen.li
 */
public class AlertDialogAcitivity extends BaseActivity {
	public static final String ALERT_MSG = "ALERT_MSG"; 
	private String alert;
	Button btn_exit_ok;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setFinishOnTouchOutside(true);

		// 为获取屏幕宽、高
		LayoutParams p = getWindow().getAttributes(); //
		
		p.dimAmount = 0.0f;// 除去背景遮盖

		getWindow().setAttributes(p); //

		getWindow().setGravity(Gravity.CENTER); //

		setContentView(R.layout.activity_exception_dialog);
		initData();
		initView();
	}

	protected void initView() {
		btn_exit_ok = (Button)this.findViewById(R.id.btn_exit_ok);
		TextView tv_alert = (TextView)this.findViewById(R.id.tv_alert);
		if(alert!=null&&alert.length()!=0){
			tv_alert.setText(alert);
		}
		btn_exit_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialogAcitivity.this.finish();
				myApplication.exitApp();
			}
		});
	}

	@Override
	protected void initData() {
		alert = getIntent().getStringExtra(ALERT_MSG);
	}

}
