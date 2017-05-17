package com.lzx.allenLee.util;

import java.lang.reflect.Field;

import com.lzx.allenLee.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;


public class UIHelper {

	public static final int DIRECTION_NEXT = 101;
	public static final int DIRECTION_PREVIOUS = 201;

	/**
	 * 跳转到指定num的view
	 * 
	 * @param context
	 * @param flipper
	 * @param num
	 *            -
	 */
	public static void flipNum(Context context, ViewFlipper flipper, int direction, int num) {
		if (direction == DIRECTION_NEXT) {
			flipper.setInAnimation(context, R.anim.flipper_right_in);
			flipper.setOutAnimation(context, R.anim.flipper_left_out);
		} else if (direction == DIRECTION_PREVIOUS) {
			flipper.setInAnimation(context, R.anim.flipper_left_in);
			flipper.setOutAnimation(context, R.anim.flipper_right_out);
		}
		flipper.setDisplayedChild(num);
	}

	public static void flipPrevious(Context context, ViewFlipper flipper) {
		flipper.setInAnimation(context, R.anim.flipper_left_in);
		flipper.setOutAnimation(context, R.anim.flipper_right_out);
		flipper.showPrevious();
	}

	public static void flipNext(Context context, ViewFlipper flipper) {
		flipper.setInAnimation(context, R.anim.flipper_right_in);
		flipper.setOutAnimation(context, R.anim.flipper_left_out);
		flipper.showNext();
	}

	/**
	 * 设置动画效果
	 * 
	 * @param context
	 * @param animator
	 * @param rightInLeftOut
	 *            右进左出 反之 左进右出
	 */
	public static void setAnimation(Context context, ViewAnimator animator, boolean rightInLeftOut) {
		if (rightInLeftOut) {
			animator.setInAnimation(context, R.anim.flipper_right_in);
			animator.setOutAnimation(context, R.anim.flipper_left_out);
		} else {
			animator.setInAnimation(context, R.anim.flipper_left_in);
			animator.setOutAnimation(context, R.anim.flipper_right_out);
		}

	}

	/**
	 * 设置为全屏模式，即隐藏标题栏和系统操作栏
	 */
	public static void setFullScreen(Activity activity) {
		// Window window = activity.getWindow();
		// 隐藏标题栏
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏操作栏
		// activity.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
		// 隐藏4.2顶部的状态栏
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// // Hide status bar
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// // Show status bar
		// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 隐藏/显示 系统操作栏
	 */
	public static void toogleSystemBar(Activity activity) {
		Window window = activity.getWindow();
		if (View.GONE == window.getDecorView().getSystemUiVisibility()) {
			window.getDecorView().setSystemUiVisibility(View.VISIBLE);
		} else {
			window.getDecorView().setSystemUiVisibility(View.GONE);
		}
	}

	/**
	 * 隐藏和显示view
	 * 
	 * @param view
	 * @param space
	 *            is still takes up space for layout purposes
	 */
	public static void toogleVisibility(View view, boolean space) {
		if (view.getVisibility() == View.VISIBLE) {
			if (space) {
				view.setVisibility(View.INVISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}

		} else {
			view.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Toast提示消息
	 * 
	 * @param context
	 * @param text
	 * @param isShort
	 *            LENGTH_SHORT
	 */
	public static void makeToast(Context context, CharSequence text, boolean isShort) {
		int duration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

		Toast.makeText(context, text, duration).show();
	}

	public static void makeToast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast提示消息
	 * 
	 * @param context
	 * @param resId
	 * @param isShort
	 *            LENGTH_SHORT
	 */
	public static void makeToast(Context context, int resId, boolean isShort) {
		int duration = isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;

		Toast.makeText(context, resId, duration).show();
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) (dpValue * metrics.density + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (int) (pxValue / metrics.density + 0.5f);
	}

	/**
	 * 单位是dp
	 * 
	 * @param context
	 * @param button
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public static void setPadding(Context context, Button button, float left, float top, float right, float bottom) {
		button.setPadding(button.getPaddingLeft() + dip2px(context, left), button.getPaddingTop() + dip2px(context, top), button.getPaddingRight() + dip2px(context, right), button.getPaddingBottom() + dip2px(context, bottom));
	}

	public static void setPadding(Context context, EditText textView, float left, float top, float right, float bottom) {
		textView.setPadding(dip2px(context, left), dip2px(context, top), dip2px(context, right), dip2px(context, bottom));
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/** 获取屏幕宽 */
	public static int getDisplayWidth(Activity activity) {
		final DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);// 获取分辨率
		return metric.widthPixels;
	}

	/**
	 * 获取屏幕高 px
	 */
	public static int getDisplayHeight(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();// 获取分辨率
		return metric.heightPixels;
	}

	/**
	 * 获取屏幕高 px
	 */
	public static int getDisplayWidth(Context context) {
		DisplayMetrics metric = context.getResources().getDisplayMetrics();// 获取分辨率
		return metric.widthPixels;
	}

	/**
	 * @author Tim 获取屏幕高
	 */
	public static int getDisplayHeight(Activity activity) {
		final DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);// 获取分辨率
		return metric.heightPixels;
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param activity
	 */
	public static void hideSoftInputFromWindow(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @param view
	 */
	public static void hideSoftInputFromWindow(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 设置文本选中颜色 选中时为白色 反正为黑色
	 * 
	 * @param selected
	 *            是否选中
	 * @param views
	 */
	public static void setTextColor(boolean selected, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(selected ? Color.WHITE : Color.BLACK);
		}
	}

	/**
	 * 设置列表项选中
	 * 
	 * @param selected
	 * @param parent
	 * @param views
	 */
	public static void setListItemSelected(boolean selected, View parent, TextView... views) {
		if (selected) {
			parent.setBackgroundResource(R.color.list_item_focused);
		} else {
			parent.setBackgroundResource(R.color.full_transparent);
		}
		setTextColor(selected, views);
	}

	/**
	 * 设置表格选中的背景色 选中时为绿色 反正为白色
	 * 
	 * @param context
	 * @param selected
	 *            是否选中
	 * @param views
	 */
	public static void setBackgroundColor(Context context, boolean selected, TextView... views) {
		int color = Color.WHITE;
		if (selected) {
			color = context.getResources().getColor(R.color.list_item_focused);
		}
		for (int i = 0; i < views.length; i++) {
			views[i].setBackgroundColor(color);
		}
	}

	public static void keepDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void distoryDialog(DialogInterface dialog) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 分享文本信息
	 * 
	 * @param title
	 * @param value
	 */
	public static void shareText(Context context, String title, String value) {
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, value);
		// sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		sendIntent.setType("text/plain");
		context.startActivity(Intent.createChooser(sendIntent, title));
	}

	public static void openExplorer(Context context, String uriString) {
		Uri uri = Uri.parse(uriString);
		context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}
	
	private Runnable runnable;

	public void setButtonDrawableCenter(final Context context, final Button button, final int imageID, final int spacing) {
		final Handler handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run() {
				if (button.getMeasuredWidth() == 0) {
					handler.postDelayed(runnable, 0);
				} else {
					Drawable drawable = context.getResources().getDrawable(imageID);
					button.measure(0, 0);
					int width = button.getMeasuredWidth();
					// int height = button.getMeasuredHeight();

					Rect bounds = new Rect();
					Paint textPaint = button.getPaint();
					textPaint.getTextBounds(button.getText().toString(), 0, button.getText().length(), bounds);
					// int txt_height = bounds.height();
					int txt_width = bounds.width();

					int img_width = drawable.getIntrinsicWidth();
					// int img_height = drawable.getIntrinsicHeight();
					// int content_height = txt_height + img_height + spacing;
					int content_width = txt_width + img_width + spacing;

					int padding_w = 0;
					if (width >= content_width)
						padding_w = width / 2 - content_width / 2;
					// int padding_h = height / 2 - content_height / 2;

					// button.setCompoundDrawablesWithIntrinsicBounds(drawable,
					// null, null, null);
					button.setPadding(padding_w, 0, 0, 0);
					button.setCompoundDrawablePadding(-padding_w);
				}
			}
		};
		handler.postDelayed(runnable, 0);
	}
	// public static boolean isFastDoubleClick(View v) {
	// Object obj = v.getTag(R.id.lastClickTime);
	// long time = System.currentTimeMillis();
	// System.err.println("current:"+time);
	// if (obj == null) {
	// // v.setTag(R.id.lastClickTime, time);
	// return false;
	// }
	// long lastClickTime = Long.valueOf(obj.toString());
	// long timeD = time - lastClickTime;
	// System.err.println("timeD:"+timeD);
	// if (0 < timeD && timeD < 1000) {
	// return true;
	// } else {
	// // v.setTag(R.id.lastClickTime, time);
	// return false;
	// }
	// }
	
}
