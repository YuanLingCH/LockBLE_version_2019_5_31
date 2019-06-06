package wansun.com.lockble.event;

/**
 * 定义发布事件
 * Created by User on 2019/6/4.
 */

public class EventMessage {
   public String time;  //发布时间
  public   String state; //开始时间或者结束时间
    public EventMessage(String time, String state){
        this.time=time;
        this.state=state;
    }

}
