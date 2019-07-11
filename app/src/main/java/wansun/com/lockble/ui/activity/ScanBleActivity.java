package wansun.com.lockble.ui.activity;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.ConnectCallback;
import com.hansion.h_ble.callback.OnReceiverCallback;
import com.hansion.h_ble.callback.OnWriteCallback;
import com.hansion.h_ble.callback.ScanCallback;

import java.util.ArrayList;
import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.DeviceListAdapter;
import wansun.com.lockble.base.BaseActivity;
import wansun.com.lockble.utils.ProgresssDialogUtils;

import static wansun.com.lockble.utils.ProgresssDialogUtils.hideProgressDialog;
import static wansun.com.lockble.utils.ProgresssDialogUtils.showProgressDialog;

/**
 * Created by User on 2019/5/30.
 */

public class ScanBleActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_ENABLE_BT =0x01 ;
    private BleController mBleController;
    private static String LOGTAG = "AppCompatActivity";
    //搜索结果列表
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
    private ListView mDeviceList;
    TextView tv_visit_tobar,tv_scan_ble;
    ImageView iv_visit_back;
    Button but_scan_ble;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "ScanBleActivity";
    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_ble;
    }

    @Override
    public void initView() {
        mDeviceList = (ListView) findViewById(R.id.mDeviceList);
        tv_visit_tobar= (TextView) findViewById(R.id.tv_visit_tobar);
        tv_visit_tobar.setText("蓝牙扫描");
        iv_visit_back= (ImageView) findViewById(R.id.iv_visit_back);
        iv_visit_back.setVisibility(View.INVISIBLE);
        but_scan_ble= (Button) findViewById(R.id.but_scan_ble);
        tv_scan_ble= (TextView) findViewById(R.id.tv_scan_ble);
        // TODO  第一步：初始化
        mBleController = BleController.getInstance().init(this);
    }

    @Override
    public void initEvent() {
        but_scan_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO  第二步：搜索设备，获取列表后进行展示
                checkBluetoothPermission();
            }
        });
    }

    @Override
    public void initData() {
            mBleController.registReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY, new OnReceiverCallback() {
                @Override
                public void onRecive(byte[] value) {
                    //接受数据
                    String s = mBleController.bytesToHexString(value).trim();
                    tv_scan_ble.setText("蓝牙模块返回数据："+s );
                    if ("CC".equals(s)){
                        startActivity(new Intent(ScanBleActivity.this,WelcomeActivity.class));
                    }else if ("DD".equals(s)){
                        Toast.makeText(ScanBleActivity.this, "蓝牙连接失败，请重新连接", Toast.LENGTH_SHORT).show();
                    }

                }
            });
    }


    private void scanDevices() {
        ProgresssDialogUtils.showProgressDialog("请稍后", "正在搜索设备...",this);
        mBleController.scanBle(0, new ScanCallback() {
            @Override
            public void onSuccess() {
                hideProgressDialog();
                if (bluetoothDevices.size() > 0) {
                    mDeviceList.setAdapter(new DeviceListAdapter(ScanBleActivity.this, bluetoothDevices));
                    mDeviceList.setOnItemClickListener(ScanBleActivity.this);
                } else {
                    Toast.makeText(ScanBleActivity.this, "未搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onScanning(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showProgressDialog("请稍后", "正在连接设备...",this);

        // TODO 第三步：点击条目后,获取地址，根据地址连接设备
        String address = bluetoothDevices.get(i).getAddress();
        mBleController.connect(0, address, new ConnectCallback() {
            @Override
            public void onConnSuccess() {
                hideProgressDialog();
                Toast.makeText(ScanBleActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
              //  startActivity(new Intent(ScanBleActivity.this,SendAndReciveActivity.class))
                //连接成功后，就要立即进行密码校验
                final byte[]bytes={(byte) 0xaa,(byte) 0xbb,0x08,0x11,0x00 ,0x01, 0x02 ,0x03 ,0x04 ,0x05};

                        mBleController.writeBuffer(bytes, new OnWriteCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ScanBleActivity.this, "密码校验数据写入完成"+bytes.toString(), Toast.LENGTH_SHORT).show();
                                tv_scan_ble.setText("写入蓝牙模块数据成功为："+mBleController.bytesToHexString(bytes));
                                startActivity(new Intent(ScanBleActivity.this,WelcomeActivity.class));
                            }

                            @Override
                            public void onFailed(int state) {
                                Toast.makeText(ScanBleActivity.this, "密码校验数据写入失败"+state, Toast.LENGTH_SHORT).show();
                                tv_scan_ble.setText("写入蓝牙模块数据失败为："+mBleController.bytesToHexString(bytes));
                            }
                        });
            }

            @Override
            public void onConnFailed() {
                hideProgressDialog();
                Toast.makeText(ScanBleActivity.this, "连接超时，请重试", Toast.LENGTH_SHORT).show();
     //startActivity(new Intent(ScanBleActivity.this,WelcomeActivity.class));
       //  startActivity(new Intent(ScanBleActivity.this,GreenDaoActivity.class));
            }
        });
    }

    /*
    校验蓝牙权限
   */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(ScanBleActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanBleActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ENABLE_BT );
            }else{//权限已打开
                scanDevices();
            }
        }else{//小于23版本直接使用
            scanDevices();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//蓝牙权限开启成功
                scanDevices();
            }else{
                Toast.makeText(ScanBleActivity.this, "蓝牙权限未开启,请设置", Toast.LENGTH_SHORT).show();
                mBleController.openBle();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
