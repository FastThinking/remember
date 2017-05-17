package com.lzx.allenLee.util.createShotCut;

import com.lzx.allenLee.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;


public class CreateShotCut {
	private Context context;
	private SharedPreferences sharedPref;
	private String PREFERENCE_KEY_SHORTCUT_EXISTS = "PREFERENCE_KEY_SHORTCUT_EXISTS";
	public CreateShotCut(Context context){
		this.context = context;
	}
    public void createShortCut(){

        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean exists = sharedPref.getBoolean(PREFERENCE_KEY_SHORTCUT_EXISTS, false);
        
        if(!exists){          
            Intent broadcastIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");            
            broadcastIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
            broadcastIntent.putExtra("duplicate", false);
            
//            Intent targetIntent = new Intent();
//            ComponentName cn = new ComponentName(getPackageName(),this.getClass().getName());
//            targetIntent.setComponent(cn);

            Intent targetIntent2 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            
            broadcastIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent2);
            Parcelable icon = Intent.ShortcutIconResource.fromContext(context,R.drawable.ic_launcher);
            
            broadcastIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            //duplicate created            
            context.sendBroadcast(broadcastIntent);
            
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(PREFERENCE_KEY_SHORTCUT_EXISTS,true);
            editor.commit();
        }
}
}
