package wansun.com.lockble.constant;

/**
 * Created by User on 2019/5/29.
 */

public class UserCoinfig {
    public static final  String LOGIN="login";   //普通用户
    public static final  String LOGIN_TYPE="login_type";
    public static final  String ADMIN_LOGIN="admin_login";   //蓝牙管理员说
    public static final String START_TIME="satrt_time";
    public static final String END_TIME="end_time";
    public  static  final  int open_lock=0x01;
    public  static  final  int open_lock_over_or_fail=0x02;
    public static final int TIME_CHECK=0x03;
    public static final int TIME_CHECK_FAIL=0x04;
    public static final int QURY_MESSAGE=0x05;
    public static final int ELECTRIC_MESSAGE=0x06;
    public static final int TIME_SUCCESS=0x07;
    public static final int DATA_FROM_BLE=0x08;
    public static final int TIME_CHECK_OUT=0x09;  //锁的时间校验
    public static final int WRITE_SUCCESS=0x10;
    public static final int WRITE_FAIL=0x11;
}
