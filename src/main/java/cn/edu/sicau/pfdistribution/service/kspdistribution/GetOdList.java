package cn.edu.sicau.pfdistribution.service.kspdistribution;

import cn.edu.sicau.pfdistribution.MysqlGetID;
import cn.edu.sicau.pfdistribution.OracleLink;
import cn.edu.sicau.pfdistribution.dao.Impl.Mysqlsavelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class GetOdList implements Serializable {

    transient
    @Autowired
    OracleLink odData;
    transient
    @Autowired
    MysqlGetID getOD;
    transient
    @Autowired
    Mysqlsavelmpl saveLmpl;
    //测试存储AFC区间分配结果
    public void saveOD(String ksp,Double peo,String day,String hour){
        saveLmpl.odRegion(ksp,peo,day,hour);
    }

    public Map<String,Integer> getOdMap(String inTime, long time) throws Exception {
        Map<String, Integer> strMap=odData.SelectOD(inTime,time);
        return strMap;
    }
    public Map<String,Integer> test_od(String day,String hour){
        return getOD.test_CQ_od(day,hour);
    }
    //OD矩阵路径分配结果操作
    public void createKspRegionTable()
    {
        odData.createKspRegionTable();
    }
    public void kspRegionAdd(String route, int passenger){
        odData.kspRegionAdd(route,passenger);
    }
    public void deleteAllKspRegion(){
        odData.deleteAllKspRegion();
    }
    //将仿真得到的OD结果从oracle拉取出来并组成需要格式
    public List<String> odFromOracleToList(){
        return odData.odFromOracleToList();
    }
}
