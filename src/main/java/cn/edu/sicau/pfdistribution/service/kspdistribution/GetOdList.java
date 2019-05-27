package cn.edu.sicau.pfdistribution.service.kspdistribution;

import cn.edu.sicau.pfdistribution.OracleLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public class GetOdList implements Serializable {

    transient
    @Autowired
    OracleLink odData;

    public Map<String,Integer> getOdMap(String inTime, long time) throws Exception {
        Map<String, Integer> strMap=odData.SelectOD(inTime,time);
        return strMap;
    }
}
