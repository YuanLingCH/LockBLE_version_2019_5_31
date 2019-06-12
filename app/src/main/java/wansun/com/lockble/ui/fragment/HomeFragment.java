package wansun.com.lockble.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.OnReceiverCallback;
import com.hansion.h_ble.callback.OnWriteCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.AccessToInformationAdapter;
import wansun.com.lockble.base.BaseFragment;
import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.event.EventMessage;
import wansun.com.lockble.utils.CommonUtil;
import wansun.com.lockble.utils.ProgresssDialogUtils;
import wansun.com.lockble.utils.ToastUtil;
import wansun.com.lockble.widget.DatePicier;

/**
 * Created by User on 2019/5/15.
 */

public class HomeFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar,tv_start_time,tv_end_time,tv_ble_data,tv_electric_message,tv_time;
    LinearLayout ll_start_time,ll_end_time;
    ListView lv;
    Button but_qury_message,but_open_lock;
    String startTime,endTime;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "HomeFragment";
    boolean isFlag_qury_message=false;
    boolean isFlag_open_lock=false;
    Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UserCoinfig.open_lock:
                    ProgresssDialogUtils.showProgressDialog("请稍后", "正在开锁...", getActivity());
                    break;
                case UserCoinfig.open_lock_over_or_fail:  //开锁成功或者失败都要取消对话框
                    ProgresssDialogUtils.hideProgressDialog();
                    break;
                case UserCoinfig.TIME_CHECK:
                    byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x02 , 0x31};
                    ToastUtil.showToast(getActivity(),"写入蓝牙数据成功："+mBleController.bytesToHexString(bytes));
                    tv_ble_data.setVisibility(View.VISIBLE);
                    tv_ble_data.setText("写入蓝牙数据："+mBleController.bytesToHexString(bytes));
                    isflag_time=true;
                    break;
                case UserCoinfig.TIME_CHECK_FAIL:
                    byte[]bytes1={(byte) 0xAA, (byte) 0xBB,0x02 , 0x31};
                    ToastUtil.showToast(getActivity(),"写入蓝牙数据失败："+mBleController.bytesToHexString(bytes1));
                    break;
                case UserCoinfig.QURY_MESSAGE:
                    ProgresssDialogUtils.hideProgressDialog();
                    break;
                case UserCoinfig.ELECTRIC_MESSAGE:
                    Bundle data = msg.getData();
                    String value_electmessage = data.getString("value_electmessage");
                    tv_electric_message.setText("电量信息为:"+value_electmessage );
                    break;
                case UserCoinfig.TIME_SUCCESS:
                    Bundle data1 = msg.getData();
                    String value_time= data1.getString("value_time");
                    tv_time.setText("时间信息："+"2019年"+value_time);
                    break;
                case UserCoinfig.DATA_FROM_BLE:  // value_data_from_ble
                    tv_ble_data.setVisibility(View.VISIBLE);
                    Bundle data2= msg.getData();
                    String value_data_from_ble= data2.getString("value_data_from_ble");
                    tv_ble_data.setText("蓝牙返回数据："+value_data_from_ble);
                    break;

            }
        }
    };

    AccessToInformationAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void initView() {

        iv_visit_back= (ImageView) root.findViewById(R.id.iv_visit_back);
        iv_visit_back.setVisibility(View.INVISIBLE);
        tv_visit_tobar= (TextView) root.findViewById(R.id.tv_visit_tobar);
        tv_visit_tobar.setText(R.string.text_lock_message);
        ll_start_time= (LinearLayout) root.findViewById(R.id.ll_start_time);
        ll_end_time= (LinearLayout) root.findViewById(R.id.ll_end_time);
        tv_start_time= (TextView) root.findViewById(R.id.tv_start_time);
        tv_end_time= (TextView) root.findViewById(R.id.tv_end_time);
        lv= (ListView) root.findViewById(R.id.lv);
        but_qury_message= (Button) root.findViewById(R.id.but_qury_message);
        tv_ble_data= (TextView) root.findViewById(R.id.tv_ble_data);   //蓝牙写数据和返回数据
        but_open_lock= (Button) root.findViewById(R.id.but_open_lock);
        tv_electric_message= (TextView) root.findViewById(R.id.tv_electric_message);
        tv_time= (TextView) root.findViewById(R.id.tv_time);
    }

    @Override
    public void initEvent() {

        DatePicier.initDatePicker(tv_end_time, tv_start_time, getContext());
        /**
         * 开始时间
         */
        ll_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicier.getCustomDatePicker2().show(tv_start_time.getText().toString());
            }
        });
        /**
         * 结束时间
         */
        ll_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicier.getCustomDatePicker1().show(tv_end_time.getText().toString());
            }
        });
        /**
         * 出入信息查询
         */
        but_qury_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlag_qury_message=true;
                ProgresssDialogUtils.showProgressDialog("请稍后", "正在查询出入信息...", getActivity());
                Log.d("TAG","startTime:"+startTime);
                Log.d("TAG","endTime:"+endTime);
                if (TextUtils.isEmpty(startTime)){
                    ToastUtil.showToast(getActivity(),"请输入开始时间");
                return;
                }
                if (TextUtils.isEmpty(endTime)){
                    ToastUtil.showToast(getActivity(),"请输入结束时间");
                    return;
                }

                    try {

                        String start_time = CommonUtil.dateToStamp(startTime);
                        long l_start_time = Long.parseLong(start_time) / 1000;
                        String hexString_start_time = String.format("%08X", l_start_time);
                        String end_time = CommonUtil.dateToStamp(endTime);
                        long l_end_time = Long.parseLong(end_time) / 1000;
                        String hexString_l_end_time = String.format("%08X", l_end_time);
                        byte[] bytes_start_time = CommonUtil.hexString2Bytes(hexString_start_time );
                        byte[] bytes_end_time = CommonUtil.hexString2Bytes(hexString_l_end_time );
                        Log.d("TAG","hexString_start_time"+hexString_start_time);
                        Log.d("TAG","hexString_l_end_time"+hexString_l_end_time );
                        for (int i = 0; i < bytes_start_time.length; i++) {
                            Log.d("TAG","hexString_start_time>>"+ bytes_start_time[i]);
                        }

                        byte [] head=new byte[3];  //头部信息s
                        head[0]= (byte) 0xAA;
                        head[1]= (byte) 0xBB;
                        head[2]= (byte) 0x09;
                        byte[] bytes_one = CommonUtil.unitByteArray(head, bytes_start_time);
                      final   byte[] bytes_send_data = CommonUtil.unitByteArray(bytes_one, bytes_end_time);
                        mBleController.writeBuffer(bytes_send_data, new OnWriteCallback() {
                            @Override
                            public void onSuccess() {

                                ToastUtil.showToast(getActivity(),"写入蓝牙数据成功："+mBleController.bytesToHexString(bytes_send_data));
                                tv_ble_data.setVisibility(View.VISIBLE);
                                tv_ble_data.setText("写入蓝牙返回数据："+mBleController.bytesToHexString(bytes_send_data));
                                mhandler.sendEmptyMessage(UserCoinfig.QURY_MESSAGE);
                            }

                            @Override
                            public void onFailed(int state) {
                                ToastUtil.showToast(getActivity(),"写入蓝牙数据失败："+mBleController.bytesToHexString(bytes_send_data));

                                mhandler.sendEmptyMessage(UserCoinfig.QURY_MESSAGE);
                            }
                        });

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




            }
        });
        /**
         * 发送开锁命令
         */
        but_open_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFlag_open_lock=true;
                mhandler.sendEmptyMessage(UserCoinfig.open_lock);
                final byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x08 , (byte) 0x0A};
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showToast(getActivity(),"写入蓝牙数据成功："+mBleController.bytesToHexString(bytes));
                        tv_ble_data.setVisibility(View.VISIBLE);
                        tv_ble_data.setText("写入蓝牙数据："+mBleController.bytesToHexString(bytes));
                    }

                    @Override
                    public void onFailed(int state) {
                        ToastUtil.showToast(getActivity(),"写入蓝牙数据失败："+mBleController.bytesToHexString(bytes));

                        mhandler.sendEmptyMessage(UserCoinfig.open_lock_over_or_fail);
                    }
                });
            }
        });

    sendBleData();
    }

    boolean isflag_electmessage=true;
    boolean isflag_time=true;
    private void sendBleData() {
        /**
         * 检测蓝牙的电量和时间
         */
        final byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x02 , 0x41};
        mBleController.writeBuffer(bytes, new OnWriteCallback() {
            @Override
            public void onSuccess() {

           tv_ble_data.setVisibility(View.VISIBLE);
            tv_ble_data.setText("写入蓝牙数据："+mBleController.bytesToHexString(bytes));

            }

            @Override
            public void onFailed(int state) {



                mhandler.sendEmptyMessage(UserCoinfig.open_lock_over_or_fail);
            }
        });
        /**
         * 时钟查询
         */
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x02 , 0x31};
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {
                        mhandler.sendEmptyMessage(UserCoinfig.TIME_CHECK);

                    }

                    @Override
                    public void onFailed(int state) {

                        mhandler.sendEmptyMessage(UserCoinfig.TIME_CHECK_FAIL);

                    }
                });
            }
        },1000);

    }

    @Override
    public void initData() {
        mBleController.registReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY, new OnReceiverCallback() {
            @Override
            public void onRecive(byte[] value) {

                Message message1=new Message();
                Bundle bundle1 = new Bundle();
                bundle1.putString("value_data_from_ble", mBleController.bytesToHexString(value));// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                message1.what=UserCoinfig.DATA_FROM_BLE;
                message1.setData(bundle1);
                mhandler.sendMessage(message1);
                if (isFlag_open_lock){
                    isFlag_open_lock=false;
                    mhandler.sendEmptyMessage(UserCoinfig.open_lock_over_or_fail);
                }

                if (isflag_electmessage){
                    isflag_electmessage=false;
                    byte [] electByte=new byte[2];
                    electByte[0]=value[2];
                    electByte[1]=value[3];
                    Message message=new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("value_electmessage", mBleController.bytesToHexString(value));// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    message.what=UserCoinfig.ELECTRIC_MESSAGE;
                    message.setData(bundle);
                    mhandler.sendMessage(message);

                }
                if (isflag_time){
                    isflag_time=false;
                    Message message=new Message();
                    Bundle bundle = new Bundle();
                    byte []data=new byte[6];
                    data[0]=value[3];
                    data[1]=value[4];
                    data[2]=value[5];
                    data[3]=value[6];
                    data[4]=value[7];
                    data[5]=value[8];
                    String data_="2019年"+data[0]+"周"+data[1]+"-"+data[2]+"-"+data[3]+":"+data[4]+":"+data[5];
                    bundle.putString("value_time", data_);// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    message.what=UserCoinfig.TIME_SUCCESS;
                    message.setData(bundle);
                    mhandler.sendMessage(message);


                }
                if (isFlag_qury_message){  //查询出入信息
                    isFlag_qury_message=false;
                }

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBleController = BleController.getInstance();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message){
        //接收到发布者发布的事件后，进行相应的处理操作
        switch (message.state){
            case UserCoinfig.START_TIME:
                startTime=message.time;
                Log.d("TAG","接收到开始时间数据"+message.time);
                break;
            case UserCoinfig.END_TIME:
                endTime=message.time;
                Log.d("TAG","接收到结束时间数据"+message.time);
                break;
        }
    }
}
