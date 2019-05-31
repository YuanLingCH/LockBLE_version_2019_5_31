package wansun.com.lockble.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by User on 2019/5/15.
 */

public class LocklBleApplication extends Application {
    //提供全局的上下文环境
    public  static  Context  mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();

    }
    public static Context getInstanceContext(){

        return mContext;
    }

}
