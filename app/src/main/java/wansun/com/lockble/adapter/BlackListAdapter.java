package wansun.com.lockble.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.entity.BlackListBean;


public class BlackListAdapter extends BaseAdapter {

    private List<BlackListBean> blackListData = new ArrayList<BlackListBean>();
    private Context mContext;

    public BlackListAdapter(Context context, List<BlackListBean> bluetoothDevices) {
        mContext = context;
        this.blackListData = bluetoothDevices;
    }

    @Override
    public int getCount() {
        return blackListData.size();
    }

    @Override
    public Object getItem(int position) {
        return blackListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BaseViewHolder holder = BaseViewHolder.
                getViewHolder(mContext, convertView, viewGroup, R.layout.black_list_item, position);
        TextView name = holder.getView(R.id.tv_black_list);

        name.setText(blackListData.get(position).icNumbler);

        return holder.getConvertView();
    }
}
