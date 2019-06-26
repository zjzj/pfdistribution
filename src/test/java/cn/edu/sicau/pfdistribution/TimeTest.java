package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.Utils.TimeUtil;
import org.junit.Test;

public class TimeTest {
    @Test
    public void unixTimeTest(){
        String time = "2019/5/31 19:33:31";
        long t = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, time);
        System.out.println(t);
    }
}
