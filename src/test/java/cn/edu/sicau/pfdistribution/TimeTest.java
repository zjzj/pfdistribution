package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.Utils.TimeUtil;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTest {
    @Test
    public void unixTimeTest() throws ParseException {
        String time = "2019/6/26 18:06:31";
        long t1 = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, time);
        DateFormat df = new SimpleDateFormat("yyyy/MM/DD hh:mm:ss");
        Date d1 = new Date(time);
        Date d2 = new Date();
        long t2 = d2.getTime();
        System.out.println(d1.getTime());
        System.out.println(t2);
    }
}
