package cn.edu.sicau.pfdistribution.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static long getUnixTime(String format, String time){
        DateFormat df = new SimpleDateFormat(format);
        long unixTime = 0;
        try {
             unixTime= df.parse(time).getTime();
            return unixTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTime;
    }
    public static long getCurrentUnixTime(){
        Date date = new Date();
        long unixTime = date.getTime();
        return unixTime;
    }
}
