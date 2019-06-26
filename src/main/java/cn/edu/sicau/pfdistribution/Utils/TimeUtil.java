package cn.edu.sicau.pfdistribution.Utils;

import java.util.Date;
public class TimeUtil {
    public static long getUnixTime(String format, String time){
        Date d = new Date(time);
        return d.getTime();
    }
    public static long getCurrentUnixTime(){
        Date date = new Date();
        long unixTime = date.getTime();
        return unixTime;
    }
}
