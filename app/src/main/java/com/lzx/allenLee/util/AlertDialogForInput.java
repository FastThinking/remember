package com.lzx.allenLee.util;

import com.lzx.allenLee.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * 当输入校验有误时，不取消弹出框。
 * 
 * @author Allen.li
 * 
 */
public class AlertDialogForInput {
	private Context ctx;
	private AlertDialog.Builder builder;
	private InputValidityListener mListener;

	public AlertDialogForInput(Context context, InputValidityListener listener) {

		this.ctx = context;
		this.mListener = listener;
		builder = new AlertDialog.Builder(context);

		builder.setNegativeButton(
				ctx.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						UIHelper.distoryDialog(dialog);
					}

				});
		builder.setPositiveButton(ctx.getResources().getString(R.string.sure),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mListener != null) {
							if (mListener.validity()) {
								UIHelper.distoryDialog(dialog);
								mListener.onValidityOk();
							} else {
								UIHelper.keepDialog(dialog);
							}
						} else {
							UIHelper.distoryDialog(dialog);
						}

					}

				});

	}

	public void setTitle(String title) {
		builder.setTitle(title);// 设置自定义布局的标题
	}

	public void setContentView(View contentView) {
		builder.setView(contentView);
	}

	public void show() {
		builder.show().setCanceledOnTouchOutside(false);// 点击窗口外不取消窗口
	}

	public interface InputValidityListener {
		/**
		 * 校验条件
		 * 
		 * @return
		 */
		boolean validity();

		/**
		 * 校验ok
		 */
		void onValidityOk();
	}

}
