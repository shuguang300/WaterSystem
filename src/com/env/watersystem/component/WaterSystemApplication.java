package com.env.watersystem.component;

import java.io.File;
import java.io.IOException;

import com.env.component.PatrolApplication;
import com.env.dcwater.component.WaterApplication;
import com.env.dcwater.util.SystemMethod;
import com.env.utils.DataBaseUtil;
import com.env.utils.HttpUtil;
import com.env.utils.SystemMethodUtil;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;


public class WaterSystemApplication extends Application{
	
	public static final String SYSTEMFOLDER_STRING = "WaterSystem";
	public static final String LOGFOLDER_STRING	= "log";
	
	private Context context;
	
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(context);
		iniWaterApplication();
		iniPatrolApplication();
	}
	
	private void iniWaterApplication(){
		SharedPreferences sp = getSharedPreferences(WaterApplication.PREFERENCE_STRING, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			File cache = new File(SystemMethod.getDownloadPngFolderPath());
			File files = new File(SystemMethod.getDownloadFileFolderPath());
			File logs = new File(SystemMethod.getLogFileFolderPath());
			if (!cache.exists()) {
				cache.mkdirs();
			}
			if (!files.exists()) {
				files.mkdirs();
			}
			if(!logs.exists()){
				logs.mkdirs();
			}
		}
		if (sp.getBoolean(WaterApplication.PREFERENCE_FIRSTRUN_STRING, true)) {
			String path = SystemMethod.getInternalDataBasePath(context);
			try {
				SystemMethod.copyDataBase(path, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		editor.putBoolean(WaterApplication.PREFERENCE_FIRSTRUN_STRING, false);
		editor.commit();
	}
	
	private void iniPatrolApplication(){
		SharedPreferences sp = getSharedPreferences(PatrolApplication.PREFS_NAME, MODE_PRIVATE);
		HttpUtil.getInstance(context).setIPAddr(sp.getString(PatrolApplication.IP_Addr,PatrolApplication.IP));
		
		
		String path = DataBaseUtil.getPath()+File.separator+DataBaseUtil.DATABASE_NAME;
		SystemMethodUtil.copyDB(context,path);
		
		try {
			Thread.sleep(1000);
			Intent startService = new Intent();
			startService.setAction("com.env.component.DataService");
			context.startService(startService);
			
			Intent keepDataService = new Intent();
			keepDataService.setAction("com.env.component.TaskService");
			
			AlarmManager  alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			PendingIntent pendingIntent = PendingIntent.getService(context, 0, keepDataService, 0);
			alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, 5*60*1000, pendingIntent);
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
