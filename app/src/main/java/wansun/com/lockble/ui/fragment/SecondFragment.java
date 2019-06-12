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
import wansun.com.lockble.utils.ToastUtil;

/**
 * Created by User on 2019/5/15.
 */

public class SecondFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar,tv_write_data;
    Button add_black_list,delect_black_list,but_time_check;
    BlackListAdapter adapter;
    List<BlackListBean> data;
    ListView lv_black;
    EditText ed_count;
    private BleController mBleController;
    public static final String REQUESTKEY_SENDANDRECIVEACTIVITY = "SecondFragment";
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
        BlackListBean bean=null;
        for (int i = 0; i < 5; i++) {
            bean=new BlackListBean();
            bean.setIcNumbler("0123456"+i);
            data.add(bean);
        }

       adapter=new  BlackListAdapter(LocklBleApplication.getInstanceContext(),data);
        /**
         * 添加黑名单  使用数据库
         */
        add_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // lv_black.setAdapter(adapter);
                String count= ed_count.getText().toString().trim();
                byte [] head={(byte) 0XAA, (byte) 0XBB,0X0C, (byte) 0XB1};
                sendBleData(count,head);
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
                Log.d("TAG","item点击事件"+tv.getText().toString().trim());
                //TODO 写入蓝牙数据

            }
        });
        /**
         * 蓝牙锁的时间校验
         */
        but_time_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 获取手机的当前时间
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
                Message msg=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("timeCheckOut",mBleController.bytesToHexString(value));
                msg.setData(bundle);
                msg.what= UserCoinfig.TIME_CHECK_OUT;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mBleController.unregistReciveListener(REQUESTKEY_SENDANDRECIVEACTIVITY);
    }

}
