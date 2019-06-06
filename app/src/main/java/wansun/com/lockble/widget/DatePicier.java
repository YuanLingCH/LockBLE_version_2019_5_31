package wansun.com.lockble.widget;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wansun.com.lockble.constant.UserCoinfig;
import wansun.com.lockble.event.EventMessage;

/**
 * Created by yuanling on 2018/4/25.
 */

public class DatePicier {

    private static CustomDatePicker customDatePicker1, customDatePicker2;
    public DatePicier() {
    }

    public static CustomDatePicker getCustomDatePicker1() {
        return customDatePicker1;
    }

    public static CustomDatePicker getCustomDatePicker2() {
        return customDatePicker2;
    }

    public static void initDatePicker(final TextView currentDate , final TextView  currentTime, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
      // currentDate.setText(now.split(" ")[0]);
        currentDate.setText(now);
        currentTime.setText(now);
        String kaiTime=null;
        String jieshuTime=null;
        kaiTime=now;
        jieshuTime="2025-01-01 00:00";
      /*  if (adminUserId.equals(uid)){
            kaiTime=now;
            jieshuTime="2025-01-01 00:00";
        }else if (!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)&&!startTime.equals(endTime)){

                String substringStart = startTime.substring(0, startTime.length() - 5);
                String substringendTime  = endTime .substring(0, endTime .length() - 5);
                kaiTime=substringStart;
                jieshuTime=substringendTime;


        } else if (!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
            if (startTime.equals(endTime)){
                kaiTime=now;
                jieshuTime="2025-01-01 00:00";
            }

        }
*/




        customDatePicker2 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                currentTime.setText(time);
                Log.d("TAG","开始时间"+time);

                EventBus.getDefault().post(new EventMessage(time, UserCoinfig.START_TIME));


            }
        },  kaiTime,jieshuTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(true); // 显示时和分
        customDatePicker2.setIsLoop(true); // 允许循环滚动



        customDatePicker1 = new CustomDatePicker(context, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                currentDate.setText(time);
                Log.d("TAG","结束时间"+time);

                EventBus.getDefault().post(new EventMessage(time, UserCoinfig.END_TIME));
          /*      losetimeMessage message=new losetimeMessage();
                message.setTime(time);
                EventBus.getDefault().post(message);
*/

            }
        }, kaiTime,jieshuTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 显示时和分
        customDatePicker1.setIsLoop(true); // 允许循环滚动
    }

}
