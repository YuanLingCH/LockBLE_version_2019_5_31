package wansun.com.lockble.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hansion.h_ble.BleController;
import com.hansion.h_ble.callback.OnReceiverCallback;
import com.hansion.h_ble.callback.OnWriteCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.BlackListAdapter;
import wansun.com.lockble.base.BaseFragment;
import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.entity.BlackListBean;
import wansun.com.lockble.global.LocklBleApplication;
import wansun.com.lockble.utils.CommonUtil;
import wansun.com.lockble.utils.ProgresssDialogUtils;
import wansun.com.lockble.utils.ToastUtil;

/**
 * Created by User on 2019/5/15.
 */

public class SecondFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar,tv_write_data,tv_data_ble_read;
    Button add_black_list,delect_black_list,but_time_check,but_modify_ble_password,but_modify_ble_id;
    BlackListAdapter adapter;
    List<BlackListBean> data;
    ListView lv_black;
    EditText ed_count,et_modify_ble_password,et_modify_ble_numbler,et_modify_ble_floor,et_modify_ble_localtion,et_modify_ble_id;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "SecondFragment";
    boolean isFlagCheckTime=false;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UserCoinfig.TIME_CHECK_OUT:
                    Bundle data = msg.getData();
                    if (data!=null){
                        String timeCheckOut = data.getString("timeCheckOut");
                        tv_write_data.setText("蓝牙返回数据："+timeCheckOut);
                    }
                    break;
                case UserCoinfig.WRITE_SUCCESS:
                    Bundle data1 = msg.getData();
                    if (data1!=null){
                        String writeSuccess = data1.getString("writeSuccess");
                        tv_write_data.setText("写入蓝牙数据成功："+writeSuccess);
                    }
                    break;
                case UserCoinfig.WRITE_FAIL:
                    Bundle data2 = msg.getData();
                    if (data2!=null){
                        String writeFail = data2.getString("writeFail");
                        tv_write_data.setText("写入蓝牙数据失败："+writeFail);
                    }
                    break;
                case UserCoinfig.DATA_FROM_BLE:
                    Bundle data3 = msg.getData();
                    if (data3!=null){
                        String writeFail = data3.getString("bleFromData");
                        tv_data_ble_read.setText("蓝牙返回数据："+writeFail);
                    }
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_second_layout;
    }

    @Override
    public void initView() {
        iv_visit_back= (ImageView) root.findViewById(R.id.iv_visit_back);
        iv_visit_back.setVisibility(View.INVISIBLE);
        tv_visit_tobar= (TextView) root.findViewById(R.id.tv_visit_tobar);
        tv_visit_tobar.setText("黑名单管理");
        add_black_list= (Button) root.findViewById(R.id.add_black_list);
        delect_black_list= (Button) root.findViewById(R.id.delect_black_list);
        lv_black= (ListView) root.findViewById(R.id.lv_black);
        but_time_check= (Button) root.findViewById(R.id.but_time_check);
        tv_write_data= (TextView) root.findViewById(R.id.tv_write_data);
        ed_count= (EditText) root.findViewById(R.id.ed_count);
        et_modify_ble_password= (EditText) root.findViewById(R.id.et_modify_ble_password);
        but_modify_ble_password= (Button) root.findViewById(R.id.but_modify_ble_password);
        but_modify_ble_id= (Button) root.findViewById(R.id.but_modify_ble_id);
        et_modify_ble_numbler= (EditText) root.findViewById(R.id.et_modify_ble_numbler);
        et_modify_ble_floor= (EditText) root.findViewById(R.id.et_modify_ble_floor);
        et_modify_ble_localtion= (EditText) root.findViewById(R.id.et_modify_ble_localtion);
        et_modify_ble_id= (EditText) root.findViewById(R.id.et_modify_ble_id);
        tv_data_ble_read= (TextView) root.findViewById(R.id.tv_data_ble_read); //读蓝牙的值

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBleController = BleController.getInstance();


    }
    @Override
    public void initEvent() {
        onReceiverData();
        data= new ArrayList();
       adapter=new  BlackListAdapter(LocklBleApplication.getInstanceContext(),data);
        /**
         * 添加黑名单  使用数据库
         */
        add_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count= ed_count.getText().toString().trim();
                byte [] head={(byte) 0XAA, (byte) 0XBB,0X0C, (byte) 0XB1};
                sendBleData(count,head);
                BlackListBean bean=new BlackListBean();
                bean.setIcNumbler(count);
                data.add(bean);
                lv_black.setAdapter(adapter);

            }
        });
        /**
         * 删除黑名单
         */
        delect_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count= ed_count.getText().toString().trim();
                byte [] head={(byte) 0XAA, (byte) 0XBB,0X0C, (byte) 0XB0};
                sendBleData(count,head);
            }

        });
        /**
         * listView Item 的点击事件
         */
        lv_black.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tv_black_list);
                String count = tv.getText().toString().trim();
                Log.d("TAG","item点击事件"+ count);
                //TODO 写入蓝牙数据
                byte [] head={(byte) 0XAA, (byte) 0XBB,0X0C, (byte) 0XB0};
                sendBleData(count,head);
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        /**
         * 蓝牙锁的时间校验
         */
        but_time_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 获取手机的当前时间
                isFlagCheckTime=true;
                Date date=new Date();
                String weekOfDate = CommonUtil.getWeekOfDate(date);
                SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
                String format = sdf.format(date);
                Log.d("TAG","时间"+format);
                byte [] head={(byte) 0xAA, (byte) 0XBB,0X09, (byte) 0X93,0X19};
                byte[] bytesWeek = CommonUtil.hexString2Bytes(weekOfDate);
                byte[] bytesHeadWeek = CommonUtil.unitByteArray(head, bytesWeek);
                Log.d("TAG","时间"+mBleController.bytesToHexString(bytesHeadWeek));
                byte[] bytesfromat = CommonUtil.hexString2Bytes(format);
                final byte[] bytes = CommonUtil.unitByteArray(bytesHeadWeek, bytesfromat);
                Log.d("TAG","时间最后发送数据"+mBleController.bytesToHexString( bytes));
                mBleController.writeBuffer(bytes, new OnWriteCallback() {
                    @Override
                    public void onSuccess() {

                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("writeSuccess",mBleController.bytesToHexString( bytes));
                        msg.setData(bundle);
                        msg.what= UserCoinfig.WRITE_SUCCESS;
                        mHandler.sendMessage(msg);

                    }

                    @Override
                    public void onFailed(int state) {
                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("writeFail",mBleController.bytesToHexString( bytes));
                        msg.setData(bundle);
                        msg.what= UserCoinfig.WRITE_FAIL;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
        /**
         * 修改蓝牙管理员密码
         */
        but_modify_ble_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = et_modify_ble_password.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)){
                 //   ProgresssDialogUtils.showProgressDialog("请稍后", "正在修改蓝牙数据...",getActivity());
                    Log.d("TAG","修改蓝牙密码"+ trim);
                    String replace = trim.replace("", "0");
                    String pas=null;
                    pas =  replace.substring(0,  replace.length() - 1);  //16进制的字符串
                     byte[] bytes = CommonUtil.toByteArray(pas);
                    Log.d("TAG","进制"+ pas);
                    byte[]head={(byte) 0xAA, (byte) 0xBB,0x08 , (byte) 0x82};
                    final byte[] senData= CommonUtil.unitByteArray(head, bytes);
                    Log.d("TAG","最后写入数据"+mBleController.bytesToHexString(senData));
                    mBleController.writeBuffer(senData, new OnWriteCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.showToast(getActivity(),"写入蓝牙数据成功："+mBleController.bytesToHexString(senData));
                        }

                        @Override
                        public void onFailed(int state) {
                            ToastUtil.showToast(getActivity(),"写入蓝牙数据失败："+mBleController.bytesToHexString(senData));
                            ProgresssDialogUtils.hideProgressDialog();
                        }
                    });
                }else {
                    ToastUtil.showToast(getActivity(),"请输入修改蓝牙密码");
                }
            }
        });
        /**
         * 修改蓝牙锁的ID
         */
        but_modify_ble_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // et_modify_ble_numbler  et_modify_ble_floor  et_modify_ble_localtion et_modify_ble_id
                String numbler = et_modify_ble_numbler.getText().toString().trim();
                if (TextUtils.isEmpty(numbler)){
                    ToastUtil.showToast(getActivity(),"请输入船号");
                    return;
                }
                String floor = et_modify_ble_floor.getText().toString().trim();
                if (TextUtils.isEmpty(floor)){
                    ToastUtil.showToast(getActivity(),"请输入楼层");
                    return;
                }
                String localtion = et_modify_ble_localtion.getText().toString().trim();
                if (TextUtils.isEmpty(localtion)){
                    ToastUtil.showToast(getActivity(),"请输入船位置");
                    return;
                }
                String id = et_modify_ble_id.getText().toString().trim();
                if (TextUtils.isEmpty(id)){
                    ToastUtil.showToast(getActivity(),"请输入船锁ID");
                    return;
                }
                byte [] head={(byte) 0XAA, (byte) 0XBB,0X08,0X71};  //写入数据的头部
                if (!TextUtils.isEmpty(numbler)&&!TextUtils.isEmpty(floor)
                        &&!TextUtils.isEmpty(localtion)&&!TextUtils.isEmpty(id)){
                    int n = Integer.parseInt(numbler);
                    String s=null;
                    byte[] bytes;
                    if (n<10){
                        String replace = numbler.replace("", "0");
                        s =  replace.substring(0,  replace.length() - 1);  //16进制的字符串
                        bytes = CommonUtil.toByteArray(s);
                    }else {
                        s = CommonUtil.intToHex(n);
                        bytes = CommonUtil.hexString2Bytes(s);
                    }
                    byte []onesupp={0x00};
                    byte[] sendData_numbler= CommonUtil.unitByteArray(bytes, onesupp);
                    byte[] sendNumbler= CommonUtil.unitByteArray(head, sendData_numbler);   // 头部拼接船号

                    String replace_floor = floor.replace("", "0");
                    String  s_floor =  replace_floor.substring(0,  replace_floor.length() - 1);  //16进制的字符串
                    byte[] bytes_floor = CommonUtil.toByteArray(s_floor);
                    byte[] sendNumbler_floor= CommonUtil.unitByteArray(sendNumbler, bytes_floor);   // 头部拼接船号 楼层

                    String replace_localtion = localtion.replace("", "0");
                    String  s_localtion =  replace_localtion.substring(0,  replace_localtion.length() - 1);  //16进制的字符串
                    byte[] bytes_localtion = CommonUtil.toByteArray(s_localtion);
                    byte[] sendNumbler_localtion= CommonUtil.unitByteArray(sendNumbler_floor, bytes_localtion);   // 头部拼接船号 船位置


                    int n_id = Integer.parseInt(id);    //船锁ID
                    String s_id=null;
                    byte[] bytes_id;
                    if (n_id<10){
                        String replace_id = id.replace("", "0");
                        s_id =  replace_id.substring(0,  replace_id.length() - 1);  //16进制的字符串
                        bytes_id = CommonUtil.toByteArray(s_id);
                    }else {
                        s_id = CommonUtil.intToHex(n_id);
                        bytes_id = CommonUtil.hexString2Bytes(s_id);
                    }

                    byte[] sendData_sup_id= CommonUtil.unitByteArray(onesupp,bytes_id);
                    final byte[] sendNumbler_id= CommonUtil.unitByteArray(sendNumbler_localtion, sendData_sup_id);   // 头部拼接船号 船位置 锁id
                    Log.d("TAG","数字转化"+s );

                    Log.d("TAG","数字"+ mBleController.bytesToHexString(sendNumbler_id) );

                      mBleController.writeBuffer(sendNumbler_id, new OnWriteCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtil.showToast(getActivity(),"写入蓝牙数据成功："+mBleController.bytesToHexString(sendNumbler_id));
                            Message msg=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putString("writeSuccess",mBleController.bytesToHexString(sendNumbler_id));
                            msg.setData(bundle);
                            msg.what= UserCoinfig.WRITE_SUCCESS;
                            mHandler.sendMessage(msg);

                        }

                        @Override
                        public void onFailed(int state) {
                            ToastUtil.showToast(getActivity(),"写入蓝牙数据失败："+mBleController.bytesToHexString(sendNumbler_id));
                            Message msg=new Message();
                            Bundle bundle=new Bundle();
                            bundle.putString("writeFail",mBleController.bytesToHexString(sendNumbler_id));
                            msg.setData(bundle);
                            msg.what= UserCoinfig.WRITE_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    });
                }

            }
        });
    }

    private void sendBleData(String count,byte [] head) {
        if (!TextUtils.isEmpty(count)){
            String replace = count.replace("", "0");
            String pas=null;
            pas =  replace.substring(0,  replace.length() - 1);  //16进制的字符串
            final byte[] bytes = CommonUtil.toByteArray(pas);
            Log.d("TAG","进制"+ pas);
            final byte[] senData= CommonUtil.unitByteArray(head, bytes);
            mBleController.writeBuffer(senData, new OnWriteCallback() {
                @Override
                public void onSuccess() {
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("writeSuccess",mBleController.bytesToHexString( senData));
                    msg.setData(bundle);
                    msg.what= UserCoinfig.WRITE_SUCCESS;
                    mHandler.sendMessage(msg);

                }

                @Override
                public void onFailed(int state) {
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("writeFail",mBleController.bytesToHexString( senData));
                    msg.setData(bundle);
                    msg.what= UserCoinfig.WRITE_FAIL;
                    mHandler.sendMessage(msg);
                }
            });


        }else {
            ToastUtil.showToast(getActivity(),"请输入账号");
        }
    }

    @Override
    public void initData() {

    }

    /**
     * 接受蓝牙数据
     */
    private void onReceiverData() {
        mBleController.registReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY, new OnReceiverCallback() {
            @Override
            public void onRecive(byte[] value) {

                Message msg1=new Message();
                Bundle bundle1=new Bundle();
                bundle1.putString("bleFromData",mBleController.bytesToHexString(value));
                msg1.setData(bundle1);
                msg1.what= UserCoinfig.DATA_FROM_BLE;
                mHandler.sendMessage(msg1);

                if (isFlagCheckTime){
                    isFlagCheckTime=false;
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("timeCheckOut",mBleController.bytesToHexString(value));
                    msg.setData(bundle);
                    msg.what= UserCoinfig.TIME_CHECK_OUT;
                    mHandler.sendMessage(msg);
                }


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
    }

}
