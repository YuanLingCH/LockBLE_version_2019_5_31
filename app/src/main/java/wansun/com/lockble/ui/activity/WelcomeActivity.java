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
    Button but_login_four,but_admin_login,but_modify_admin_password,but_login_two,but_login_three;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "WelcomeActivity";
    TextView te_modify_admin_password,tv_visit_tobar;
    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        but_login_two= (Button) findViewById(R.id.but_login_two);
        but_admin_login= (Button) findViewById(R.id.but_admin_login);
        but_modify_admin_password= (Button) findViewById(R.id.but_modify_admin_password);
        te_modify_admin_password= (TextView) findViewById(R.id.te_modify_admin_password);
        mBleController = BleController.getInstance();
        but_login_four= (Button) findViewById(R.id.but_login_four);
        but_login_three= (Button) findViewById(R.id.but_login_three);
        tv_visit_tobar= (TextView) findViewById(R.id.tv_visit_tobar);
        tv_visit_tobar.setText("用户选择");
    }

    /**
     * 权限登录 写入蓝牙数据 控制蓝牙芯片的权限等级
     */

    @Override
    public void initEvent() {
        /**
         * 二级权限登录
         */
        but_login_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                intent.putExtra(UserCoinfig.LOGIN_TYPE,UserCoinfig.LOGIN);   //普通用户
                startActivity(intent);

            }
        });
        /**
         * 三级权限登录
         */
        but_login_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final byte []bytes_three={(byte) 0xAA, (byte) 0XBB,0x08,0x03, 0x00 };
             mBleController.writeBuffer(bytes_three, new OnWriteCallback() {
                @Override
                public void onSuccess() {
                    ToastUtil.showToast(WelcomeActivity.this,"写入蓝牙数据成功："+mBleController.bytesToHexString(bytes_three));
                }

                @Override
                public void onFailed(int state) {

                }
            });
            }
        });
        /**
         * 四级权限登录
         */
        but_login_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         * 蓝牙管理员
         */
        but_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                intent.putExtra(UserCoinfig.LOGIN_TYPE,UserCoinfig.ADMIN_LOGIN);   //蓝牙用户
                startActivity(intent);

            }
        });
        /**
         * 修改蓝牙管理员密码
         */
        but_modify_admin_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ProgresssDialogUtils.showProgressDialog("请稍后", "正在修改蓝牙数据...",WelcomeActivity .this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
    }
}
