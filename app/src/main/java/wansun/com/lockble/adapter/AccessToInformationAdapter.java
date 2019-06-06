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
        AccessToInformationBean bean = data.get(position);
        number.setText(bean.getNumber());
        openLockTime.setText(bean.getOpenLockTime());
        jobNumber.setText(bean.getJobNumber());
        openLockType.setText(bean.getOpenLockType());
        cardType.setText(bean.getCardType());
        return holder.getConvertView();
    }
}
