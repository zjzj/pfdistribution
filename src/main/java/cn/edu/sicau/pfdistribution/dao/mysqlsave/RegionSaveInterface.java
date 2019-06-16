package cn.edu.sicau.pfdistribution.dao.mysqlsave;


import java.util.List;
import java.util.Map;

public interface RegionSaveInterface {
    //保存路径
    public void routeAdd(String ksp);
    //保存路径和分配的人数
    public void kspRegionAdd(String route,double passenger);
    //保存区间和分配的人数
    public void odRegion(String kspregion,double passenger);
    //查询出车站所在的线路ID
    public Map<Integer, Integer> SelectLineId();
    //查询车站ID对应的车站名
    public String selectStationName(Integer id);
    public Map<Integer, List<String>> selectTime();
}
