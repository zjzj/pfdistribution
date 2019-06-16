package cn.edu.sicau.pfdistribution.service.kspdistribution;

import cn.edu.sicau.pfdistribution.MysqlGetID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class GetLineID implements Serializable{
    transient
    @Autowired
    MysqlGetID GetID;

    public Map<Integer,Integer> GetCZ_ID(){
        return GetID.CheZhanID();
    }
    public Map<Integer, List<String>> sectionTime(){
        return GetID.idTime();
    }


}
