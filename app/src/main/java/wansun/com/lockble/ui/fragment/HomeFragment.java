package wansun.com.lockble.ui.fragment;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import wansun.com.lockble.R;
import wansun.com.lockble.adapter.AccessToInformationAdapter;
import wansun.com.lockble.base.BaseFragment;
import wansun.com.lockble.widget.DatePicier;

/**
 * Created by User on 2019/5/15.
 */

public class HomeFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar,tv_start_time,tv_end_time;
    LinearLayout ll_start_time,ll_end_time;
    ListView lv;

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

    }

    @Override
    public void initData() {

    }
}
