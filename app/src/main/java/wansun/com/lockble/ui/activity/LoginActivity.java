package wansun.com.lockble.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.OnReceiverCallback;
import com.hansion.h_ble.callback.OnWriteCallback;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseActivity;
import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.utils.CommonUtil;
import wansun.com.lockble.utils.ProgresssDialogUtils;
import wansun.com.lockble.utils.ToastUtil;

/**
 * Created by User on 2019/5/29.
 */

public class LoginActivity extends BaseActivity {
    ImageView iv_visit_back;
    TextView tv_visit_tobar,tv_user,tv_write_data;
    EditText et_password;
    Button but_submit,but_cancle;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "LoginActivity";
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mBleController = BleController.getInstance();
        iv_visit_back= (ImageView) findViewById(R.id.iv_visit_back);
        tv_visit_tobar= (TextView) findViewById(R.id.tv_visit_tobar);
        tv_visit_tobar.setText("登录");
        tv_user= (TextView) findViewById(R.id.tv_user);
        et_password= (EditText) findViewById(R.id.et_password);
        but_submit= (Button) findViewById(R.id.but_submit);
        but_cancle= (Button) findViewById(R.id.but_cancle);
        tv_write_data= (TextView) findViewById(R.id.tv_write_data);

    }

    @Override
    public void initEvent() {
        String userType = getIntent().getStringExtra(UserCoinfig.LOGIN_TYPE);
        if (userType.equals(UserCoinfig.LOGIN)){ //普通用户
            tv_user.setText("普通用户：");
        }else if (userType.equals(UserCoinfig.ADMIN_LOGIN)){  //蓝牙用户
            tv_user.setText("蓝牙管理员：");
        }

        iv_visit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取密码  连接蓝牙

                String passWord = et_password.getText().toString().trim();
                if (!TextUtils.isEmpty(passWord)){
                    ProgresssDialogUtils.showProgressDialog("提示","写入数据...",LoginActivity.this);
                byte[] bytesPassWord = CommonUtil.hexString2Bytes(passWord);
                byte [] head={(byte) 0xAA, (byte) 0XBB,0X08, (byte) 0X82};
                final byte[] bytes = CommonUtil.unitByteArray(head, bytesPassWord);

                Log.d("TAG","发送蓝牙数据"+mBleController.bytesToHexString(bytes));
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {
                        tv_write_data.setText("写入蓝牙数据成功："+mBleController.bytesToHexString(bytes));
                        ProgresssDialogUtils.hideProgressDialog();
                    }

                    @Override
                    public void onFailed(int state) {
                        tv_write_data.setText("写入蓝牙数据失败："+mBleController.bytesToHexString(bytes));
                        ProgresssDialogUtils.hideProgressDialog();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                });
                }else {
                    ToastUtil.showToast(LoginActivity.this,"请输入密码");
                }
            }
        });

    }

    @Override
    public void initData() {
        mBleController.registReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY, new OnReceiverCallback() {
            @Override
            public void onRecive(byte[] value) {
              //  ToastUtil.showToast(LoginActivity.this,"蓝牙返回数据："+mBleController.bytesToHexString(value));
                tv_write_data.setText("蓝牙返回数据："+mBleController.bytesToHexString(value));
                String s = mBleController.bytesToHexString(value).trim();

                if ("CC".equals(s)){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }else if("DD".equals(s)){
                    Toast.makeText(LoginActivity.this, "蓝牙执行失败", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
    }


}
