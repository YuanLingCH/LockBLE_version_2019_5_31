package wansun.com.lockble.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.OnReceiverCallback;
import com.hansion.h_ble.callback.OnWriteCallback;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseActivity;
import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.utils.ProgresssDialogUtils;
import wansun.com.lockble.utils.ToastUtil;

/**
 * Created by User on 2019/5/29.
 */

public class WelcomeActivity extends BaseActivity {
    Button but_login,but_admin_login,but_modify_admin_password;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "WelcomeActivity";
    TextView te_modify_admin_password;
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        but_login= (Button) findViewById(R.id.but_login);
        but_admin_login= (Button) findViewById(R.id.but_admin_login);
        but_modify_admin_password= (Button) findViewById(R.id.but_modify_admin_password);
        te_modify_admin_password= (TextView) findViewById(R.id.te_modify_admin_password);
        mBleController = BleController.getInstance();
    }

    @Override
    public void initEvent() {
        /**
         * 普通用户
         */
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
                intent.putExtra("login", UserCoinfig.LOGIN);
                startActivity(intent);
            }
        });
        /**
         * 蓝牙管理员
         */
        but_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
                intent.putExtra("login", UserCoinfig.ADMIN_LOGIN);
                startActivity(intent);
            }
        });
        /**
         * 修改蓝牙管理员密码
         */
        but_modify_admin_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgresssDialogUtils.showProgressDialog("请稍后", "正在连接设备...",WelcomeActivity .this);
                final byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x08 , (byte) 0x82,0x06 ,0x06, 0x06 ,0x06 ,0x06 ,0x06};
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showToast(WelcomeActivity.this,"写入蓝牙数据成功："+mBleController.bytesToHexString(bytes));
                    }

                    @Override
                    public void onFailed(int state) {
                        ToastUtil.showToast(WelcomeActivity.this,"写入蓝牙数据失败："+mBleController.bytesToHexString(bytes));
                        ProgresssDialogUtils.hideProgressDialog();
                    }
                });
            }
        });
    }

    @Override
    public void initData() {
            mBleController.registReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY, new OnReceiverCallback() {
                @Override
                public void onRecive(byte[] value) {
                    ToastUtil.showToast(WelcomeActivity.this,"蓝牙返回数据："+mBleController.bytesToHexString(value));
                    te_modify_admin_password.setText("蓝牙返回数据："+mBleController.bytesToHexString(value));
                    ProgresssDialogUtils.hideProgressDialog();
                }
            });
    }
}
