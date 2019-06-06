package wansun.com.lockble.ui.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseFragment;

/**
 * Created by User on 2019/5/15.
 */

public class SecondFragment extends BaseFragment {
    ImageView iv_visit_back;
    TextView tv_visit_tobar;
    Button add_black_list,delect_black_list;
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

    }

    @Override
    public void initEvent() {
        /**
         * 添加黑名单  使用数据库
         */
        add_black_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    }

    @Override
    public void initData() {

    }
}
