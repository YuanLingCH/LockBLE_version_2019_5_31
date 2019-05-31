package wansun.com.lockble.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by User on 2019/5/30.
 */

public class ProgresssDialogUtils {
    private static ProgressDialog progressDialog;
    public static void showProgressDialog(String title, String message , Context context) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, title, message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
