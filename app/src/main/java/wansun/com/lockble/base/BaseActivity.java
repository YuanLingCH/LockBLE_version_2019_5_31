package wansun.com.lockble.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * BaseActivity基类的封装
 * Created by User on 2019/5/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initEvent();
        initData();
    }

    public abstract int getLayoutId();  //获取布局文件
    public  abstract void initView();   //加载视图
    public abstract void initEvent();    //S事件
    public  abstract  void  initData();   //获取数据
}
