package wansun.com.lockble.ui.activity;

import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,ScanBleActivity.class));
                finish();
            }
        },3000);
    }
}
