package wansun.com.lockble.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.entity.AccessToInformationBean;

/**
 *
 *
 * 出入信息的adapter
 * Created by User on 2019/5/31.
 */

public class AccessToInformationAdapter extends BaseAdapter {
    List<AccessToInformationBean> data;
    Context mContext;
    LayoutInflater inflater;
    public AccessToInformationAdapter(Context mContext,List<AccessToInformationBean> data){
        this.mContext=mContext;
        this.data=data;
        inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.
                getViewHolder(mContext, convertView, parent, R.layout.access_to_info_item, position);
        TextView number = holder.getView(R.id.tv_number);
        TextView openLockTime = holder.getView(R.id.tv_open_lock_time);
        TextView jobNumber = holder.getView(R.id.tv_job_number);
        TextView openLockType = holder.getView(R.id.tv_open_lock_typer);
        TextView cardType = holder.getView(R.id.tv_open_card_type);
       String bleData = String.valueOf(data.get(position));
        AccessToInformationBean bean=new AccessToInformationBean();
        String[] split = bleData.split(" ");
        StringBuffer stringBufferYears=new StringBuffer();   //时间
        stringBufferYears.append("2019年");
        stringBufferYears.append(split[1]+"月");
        stringBufferYears.append(split[2]+"日");
        stringBufferYears.append(split[3]+":");
        stringBufferYears.append(split[4]);
        bean.setOpenLockTime(stringBufferYears.toString());
        StringBuffer stringBufferICNumbler=new StringBuffer();  //工号
        stringBufferICNumbler.append(split[12].split("0")[1]);
        stringBufferICNumbler.append(split[13].split("0")[1]);
        stringBufferICNumbler.append(split[14].split("0")[1]);
        stringBufferICNumbler.append(split[15].split("0")[1]);
        stringBufferICNumbler.append(split[16].split("0")[1]);
        stringBufferICNumbler.append(split[17].split("0")[1]);
        stringBufferICNumbler.append(split[18].split("0")[1]);
        stringBufferICNumbler.append(split[19].split("0")[1]);
        stringBufferICNumbler.append(split[20].split("0")[1]);
        stringBufferICNumbler.append(split[21].split("0")[1]);
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





      //  number.setText(bean.getNumber());   数量没有这个返回值
        openLockTime.setText(bean.getOpenLockTime());
        jobNumber.setText(bean.getJobNumber());
        openLockType.setText(bean.getOpenLockType());
        cardType.setText(bean.getCardType());
        return holder.getConvertView();
    }
}
