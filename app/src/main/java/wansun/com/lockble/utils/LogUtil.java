package wansun.com.lockble.utils;

import android.util.Log;

/**
 * LogUtil 工具类
 * Created by User on 2019/5/22.
 */

public class LogUtil {
    public static boolean isShow=true; //开发环境
  //  public static boolean isSow=false;//生产环境
    public  static  String TAG="tag";

    /**
     * 对应verbose级别
     * @param msg
     */
    public static void v(String msg){
        if (isShow==true){
            Log.v(TAG,msg);
        }
    }

    /**
     * 对应debug模式
     * @param msg
     */
    public static void d(String msg){
        if (isShow==true){
            Log.d(TAG,msg);
        }
    }

    /**
     * 对应错误级别
     * @param msg
     */
    public static void  e(String msg){
        if (isShow==true){
            Log.v(TAG,msg);
        }
    }

    /**
     * 对应应用级别
     * @param msg
     */
    public static void i(String msg){
        if (isShow==true){
            Log.i(TAG,msg);
        }
    }

    /**
     * 对应警告级别
     * @param msg
     */
    public  static void w(String msg){
        if (isShow==true){
            Log.w(TAG,msg);
        }
    }


}
