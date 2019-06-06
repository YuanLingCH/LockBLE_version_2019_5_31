package wansun.com.lockble.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wansun.com.lockble.R;


public class BlackListAdapter extends BaseAdapter {

    private List<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();
    private Context mContext;

    public BlackListAdapter(Context context, List<BluetoothDevice> bluetoothDevices) {
        mContext = context;
        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        BaseViewHolder holder = BaseViewHolder.
                getViewHolder(mContext, convertView, viewGroup, R.layout.black_list_item, position);
        TextView name = holder.getView(R.id.tv_black_list);

        name.setText(bluetoothDevices.get(position).getName());

        return holder.getConvertView();
    }
}
