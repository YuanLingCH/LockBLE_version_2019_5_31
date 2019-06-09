package wansun.com.lockble.ui.fragment;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.BlackListAdapter;
import wansun.com.lockble.base.BaseFragment;
import wansun.com.lockble.entity.BlackListBean;
import wansun.com.lockble.global.LocklBleApplication;

/**
 * Created by User on 2019/5/15.
 */

public class SecondFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar;
    Button add_black_list,delect_black_list;
    BlackListAdapter adapter;
    List<BlackListBean> data;
    ListView lv_black;
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

    }

    @Override
    public void initEvent() {
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
                lv_black.setAdapter(adapter);
            }
        });
        /**
         * 删除黑名单
         */
        delect_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    @Override
    public void initData() {

    }
}
