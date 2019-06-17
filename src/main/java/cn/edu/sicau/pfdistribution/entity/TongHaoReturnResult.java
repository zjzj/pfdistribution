package cn.edu.sicau.pfdistribution.entity;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
@Service
public class TongHaoReturnResult implements Serializable {
    private String dataTime;
    private ArrayList<TongHaoPathType> pathDistribution;

    public TongHaoReturnResult(ArrayList<TongHaoPathType> pathDistribution) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        this.dataTime = df.format(new Date());
        this.pathDistribution = pathDistribution;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public ArrayList<TongHaoPathType> getPathDistribution() {
        return pathDistribution;
    }

    public void setPathDistribution(ArrayList<TongHaoPathType> pathDistribution) {
        this.pathDistribution = pathDistribution;
    }
}
