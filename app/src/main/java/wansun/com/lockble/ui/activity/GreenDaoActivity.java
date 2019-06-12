package wansun.com.lockble.ui.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import wansun.com.lockble.R;
import wansun.com.lockble.base.BaseActivity;
import wansun.com.lockble.entity.UserBean;
import wansun.com.lockble.global.LocklBleApplication;
import wansun.com.lockble.greendao.gen.UserBeanDao;

/**
 * GreenDao3.0测试
 * Created by User on 2019/6/10.
 */

public class GreenDaoActivity extends BaseActivity {
    Button but_add,but_qury,but_delect;
    UserBeanDao dao;

    @Override
    public int getLayoutId() {
        return R.layout.activity_green_dao;
    }

    @Override
    public void initView() {
          dao = LocklBleApplication.getInstance().getSession().getUserBeanDao();
          but_add= (Button) findViewById(R.id.but_add);
           but_qury= (Button) findViewById(R.id.but_qury);
        but_delect= (Button) findViewById(R.id.but_delect);


    }

    @Override
    public void initEvent() {
        /**
         * 添加数据
         */
        but_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 5; i++) {
                    UserBean bean=new UserBean();
                    bean.setId((long) i);
                    bean.setName("张飞"+i);
                    dao.insert(bean);  //插入一条数据
                }


            }
        });
        /**
         * 查询数据
         */
        but_qury.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UserBean> userBeen = dao.loadAll();  //查询全部数据
                for (UserBean us: userBeen ){
                    Log.d("TAG","查询数据"+us.getName());
                }

            }
        });
        /**
         * 删除数据
         *  删除数据的方法 1.全部删除实例    2.根据id  3.全部删除
         */
        but_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = "AA BB 0A 47 CC 02 05 06 AA 05 09 06 04 03 AA 03 04 08 07 09 07 05";
                String[] strs = trim .split("AA");
                List<String> list = Arrays.asList(strs);
                for (String s:list){
                    Log.d("TAG","数据转换"+ s);
                }




            }
        });
    }

    @Override
    public void initData() {

    }
}
