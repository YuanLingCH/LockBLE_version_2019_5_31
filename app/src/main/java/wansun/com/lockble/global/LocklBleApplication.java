package wansun.com.lockble.global;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
/**
 * Created by User on 2019/5/15.
 */

public class LocklBleApplication extends Application {
    //提供全局的上下文环境
    public  static  Context  mContext;

     static LocklBleApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();

        app=this;
        CrashReport.initCrashReport(getApplicationContext(), "2e4d3d1ad3", true);
    }
    public static Context getInstanceContext(){

        return mContext;
    }

    public static LocklBleApplication getInstance(){
        return  app;
    }





}
