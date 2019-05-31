package wansun.com.lockble.ui.activity;

import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseActivity;
import wansun.com.lockble.global.LocklBleApplication;
import wansun.com.lockble.ui.fragment.HomeFragment;
import wansun.com.lockble.ui.fragment.OtherFragment;
import wansun.com.lockble.ui.fragment.SecondFragment;


public class MainActivity extends BaseActivity {
    View fragment_content;
    FragmentTabHost tabhost;
    LayoutInflater inflater;

    private  String [] tabItems={"首页","记录","我的"};
    Class [] fragments={
            HomeFragment.class,
            SecondFragment.class,
            OtherFragment.class
    };
    private int []imgIds={
            R.drawable.tab_home_sel,
            R.drawable.tab_message_sel,
            R.drawable.tab_center_sel
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        inflater=LayoutInflater.from(LocklBleApplication.getInstanceContext());
        fragment_content=findViewById(R.id.fragment_content);
        tabhost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this,getSupportFragmentManager(),R.id.fragment_content);
        for (int i = 0; i < fragments.length; i++) {
            TabHost.TabSpec tabItem=tabhost.newTabSpec(i+"");
            tabItem.setIndicator(getItemView(i));//设置一个指示器
            tabhost.addTab(tabItem,fragments[i],null); //添加tab项
        }
    }

    private View getItemView(int index) {
        View view=inflater.inflate(R.layout.tabhost_item_layout,null);
        ImageView iv = (ImageView) view.findViewById(R.id.tab_img);
        iv.setImageResource(imgIds[index]);
        TextView tv= (TextView) view.findViewById(R.id.tab_tv);
        tv.setText(tabItems[index]);
        return view;
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
