package wansun.com.lockble.entity;

/**
 * Created by User on 2019/5/31.
 */

public class AccessToInformationBean {
    private  int number;
    private String openLockTime;
    private  String jobNumber;
    private String openLockType;
    private  String cardType;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOpenLockTime() {
        return openLockTime;
    }

    public void setOpenLockTime(String openLockTime) {
        this.openLockTime = openLockTime;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getOpenLockType() {
        return openLockType;
    }

    public void setOpenLockType(String openLockType) {
        this.openLockType = openLockType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
