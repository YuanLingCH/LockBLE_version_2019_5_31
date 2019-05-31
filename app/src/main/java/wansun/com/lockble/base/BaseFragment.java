package wansun.com.lockble.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基类baseFragement的封装
 * Created by User on 2019/5/15.
 */

public abstract class BaseFragment extends Fragment {
    protected  View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root=inflater.inflate(getLayoutId(),null);
        initView();
        initEvent();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 获取布局文件的Id
     * @return
     */
    public abstract  int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 事件的监听
     */
    public  abstract  void initEvent();

    /**
     * 初始化数据
     */
    public  abstract void initData();
}
