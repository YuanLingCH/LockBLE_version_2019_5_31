package wansun.com.lockble.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具
 * Created by User on 2019/5/22.
 */

public class ToastUtil {
  static Toast toast=null;
    public static void showToast(Context context,String msg){
        if (toast==null){
            toast=Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
        }
        toast.show();
    }

}
