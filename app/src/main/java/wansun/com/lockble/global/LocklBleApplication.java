package wansun.com.lockble.global;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tencent.bugly.crashreport.CrashReport;

import wansun.com.lockble.greendao.gen.DaoMaster;
import wansun.com.lockble.greendao.gen.DaoSession;

/**
 * Created by User on 2019/5/15.
 */

public class LocklBleApplication extends Application {
    //提供全局的上下文环境
    public  static  Context  mContext;
    private DaoMaster.DevOpenHelper dbHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        initDatabass();
    }
    public static Context getInstanceContext(){

        return mContext;
    }
    private void initDatabass() {
        //这里之后会修改，关于升级数据库
        dbHelper = new DaoMaster.DevOpenHelper(this, "visit-db", null);
        db = dbHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();    // 2e4d3d1ad3
        CrashReport.initCrashReport(getApplicationContext(), "2e4d3d1ad3", true);


    }


    public DaoSession getSession(){
        return mDaoSession;
    }
    public SQLiteDatabase getDb(){
        return db;
    }
}
