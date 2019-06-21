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

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.AccessToInformationAdapter;
import wansun.com.lockble.base.BaseFragment;
import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.entity.AccessToInformationBean;
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
    TextView tv_visit_tobar,tv_start_time,tv_end_time,tv_ble_data,tv_electric_message,tv_time,tv_door_message;
    LinearLayout ll_start_time,ll_end_time;
    ListView lv;
    Button but_qury_message,but_open_lock;
    String startTime,endTime;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "HomeFragment";
    boolean isFlag_qury_message=false;
    boolean isFlag_open_lock=false;
    List quryData;
    AccessToInformationAdapter adapter;
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
                    tv_time.setText("时间信息："+value_time);
                    break;
                case UserCoinfig.DATA_FROM_BLE:  // value_data_from_ble
                    tv_ble_data.setVisibility(View.VISIBLE);
                    Bundle data2= msg.getData();
                    String value_data_from_ble= data2.getString("value_data_from_ble");
                    StringBuffer buffer=new StringBuffer();
                    buffer.append(value_data_from_ble);   //数据的拼接
                    tv_ble_data.setText("蓝牙返回数据："+buffer.toString().trim());
                    break;
                case UserCoinfig.LOCK_ID:   //蓝牙所的id
                    Bundle data3 = msg.getData();
                    String value_lock_id= data3.getString("value_time");
                    tv_door_message.setText(value_lock_id);
                    break;

            }
        }
    };


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
        tv_door_message= (TextView) root.findViewById(R.id.tv_door_message);   //门的编号
    }

    @Override
    public void initEvent() {
        adapter=new AccessToInformationAdapter(getActivity(),quryData);
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
                        String replaceTime = startTime.replace(" ", "-");
                        String replace = replaceTime.replace(":", "-");
                        String[] split = replace.split("-");
                        StringBuffer buf=new StringBuffer();
                        buf.append(split[1]);
                        buf.append(split[2]);
                        buf.append(split[3]);
                        buf.append(split[4]);
                        String s = buf.toString();
                        byte[] bytesStartTime = CommonUtil.hexString2Bytes(s);
                        Log.d("TAG","时间转化"+mBleController.bytesToHexString( bytesStartTime));

                        for (int i = 0; i < split.length; i++) {
                            Log.d("TAG","split:"+split[i]);
                        }


                        String replaceEndTime = endTime.replace(" ", "-");
                        String replace1 = replaceEndTime.replace(":", "-");
                        String[] split1 = replace1.split("-");

                        StringBuffer bufendTimr=new StringBuffer();
                        bufendTimr.append(split1[1]);
                        bufendTimr.append(split1[2]);
                        bufendTimr.append(split1[3]);
                        bufendTimr.append(split1[4]);
                        String s1 = buf.toString();
                        byte[] bytesEndTime = CommonUtil.hexString2Bytes(s1);




             /*           String start_time = CommonUtil.dateToStamp(startTime);
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
*/
                        byte [] head=new byte[5];  //头部信息s
                        head[0]= (byte) 0xAA;
                        head[1]= (byte) 0xBB;
                        head[2]= (byte) 0x09;
                        head[3]= (byte) 0x21;
                        head[4]= (byte) 0x19;
                        byte[] bytes_one = CommonUtil.unitByteArray(head, bytesStartTime);
                      final   byte[] bytes_send_data = CommonUtil.unitByteArray(bytes_one,  bytesEndTime );
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
                                Log.d("TAG","写入蓝牙数据失败"+ mBleController.bytesToHexString(bytes_send_data));
                                mhandler.sendEmptyMessage(UserCoinfig.QURY_MESSAGE);
                            }
                        });

                    } catch (Exception e) {
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
    boolean isflag_ble_id=true;
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
        /**
         * 查询锁的id
         */

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final byte[]bytes={(byte) 0xAA, (byte) 0xBB,0x02 , 0x70};
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {
                        mhandler.sendEmptyMessage(UserCoinfig.WRITE_SUCCESS);

                    }

                    @Override
                    public void onFailed(int state) {

                        mhandler.sendEmptyMessage(UserCoinfig.WRITE_FAIL);

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
                }else if (isflag_electmessage){
                    isflag_electmessage=false;
                    String trim = mBleController.bytesToHexString(value).trim();
                    String[] split = trim.split(" ");
                    StringBuffer buffer=new StringBuffer();
                    buffer.append( split[2]);
                    buffer.append( split[3]);
                    String str = new BigInteger(buffer.toString(), 16).toString(10);
                    double v1 = (Double.parseDouble(str) / 1000)*2/6.4*100;
                    DecimalFormat df = new DecimalFormat("#.00");
                    String numbler = df.format(v1);

                    Message message=new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("value_electmessage", numbler+"%");// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    message.what=UserCoinfig.ELECTRIC_MESSAGE;
                    message.setData(bundle);
                    mhandler.sendMessage(message);

                }else if (isflag_time){
                    isflag_time=false;
                    Message message=new Message();
                    Bundle bundle = new Bundle();
                    String trim = mBleController.bytesToHexString(value).trim();
                    String[] split = trim.split(" ");
                    String data_="2019年"+split[3]+"周"+split[4]+"-"+split[5]+"-"+split[6]+":"+split[7]+":"+split[8];
                    bundle.putString("value_time", data_);// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    message.what=UserCoinfig.TIME_SUCCESS;
                    message.setData(bundle);
                    mhandler.sendMessage(message);
                }else if (isFlag_qury_message){  //查询出入信息
                    isFlag_qury_message=false;
                    quryData.clear();  //先清掉集合里面的数据
                    String trim = mBleController.bytesToHexString(value).trim();
                    String[] strs = trim .split("AA");
                    List<String> list = Arrays.asList(strs);
                   // quryData.add();  // 把数据放在集合里面  16进制
                    for (String s:list){
                        quryData=new ArrayList();
                        AccessToInformationBean bean=new AccessToInformationBean();
                        String[] split = s.split(" ");
                        StringBuffer stringBufferYears=new StringBuffer();   //时间
                        stringBufferYears.append("2019年");
                        stringBufferYears.append(split[1]+"月");
                        stringBufferYears.append(split[2]+"日");
                        stringBufferYears.append(split[3]+":");
                        stringBufferYears.append(split[4]);
                        bean.setOpenLockTime(stringBufferYears.toString());
                        StringBuffer stringBufferICNumbler=new StringBuffer();  //工号
                        stringBufferICNumbler.append(split[12]);
                        stringBufferICNumbler.append(split[13]);
                        stringBufferICNumbler.append(split[14]);
                        stringBufferICNumbler.append(split[15]);
                        stringBufferICNumbler.append(split[16]);
                        stringBufferICNumbler.append(split[17]);
                        stringBufferICNumbler.append(split[18]);
                        stringBufferICNumbler.append(split[19]);
                        stringBufferICNumbler.append(split[20]);
                        stringBufferICNumbler.append(split[21]);
                        stringBufferICNumbler.append(split[22]);
                        bean.setJobNumber(stringBufferICNumbler.toString());
                        StringBuffer stringBufferAuth=new StringBuffer();  //工号
                        stringBufferAuth.append(split[10]);
                        stringBufferAuth.append(split[11]);
                        if (stringBufferAuth.toString().equals("0D00")){
                                bean.setCardType("蓝牙管理员");
                        }else if (  stringBufferAuth.toString().equals("0200")){
                            bean.setCardType("常规出入用户");
                        }else if (  stringBufferAuth.toString().equals("0300")){
                            bean.setCardType("出入日期限制用户");
                        }else if (  stringBufferAuth.toString().equals("0400")){
                            bean.setCardType("出入次数限制用户");
                        }
                        StringBuffer stringBufferOpenDoorType=new StringBuffer();  //开门类型
                        stringBufferOpenDoorType.append(split[6]);
                        stringBufferOpenDoorType.append(split[7]);
                        if (stringBufferOpenDoorType.toString().equals("0300")){
                            bean.setOpenLockType("刷卡开门");
                        }else if (stringBufferOpenDoorType.toString().equals("0100")){
                            bean.setOpenLockType("钥匙开门");
                        }else if (stringBufferOpenDoorType.toString().equals("0200")){
                            bean.setOpenLockType("门内反锁");
                        }
                        quryData.add(bean);
                    }

                    lv.setAdapter(adapter);
                }else if (isflag_ble_id){
                    isflag_ble_id=false;
                    Message message=new Message();
                    Bundle bundle = new Bundle();
                    String trim = mBleController.bytesToHexString(value).trim();
                    String[] split = trim.split(" ");
                    String data_="船锁ID："+split[6]+split[7]+"号";
                    bundle.putString("value_time", data_);// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                    message.what=UserCoinfig.LOCK_ID;
                    message.setData(bundle);
                    mhandler.sendMessage(message);
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

    @Override
    public void onStop() {
        super.onStop();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
        Log.d("TAG","onStop()...");
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
