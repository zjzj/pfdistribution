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
    public Map<Integer, Integer> selectLineId();
    //查询车站ID对应的车站名
    public String selectStationName(Integer id);
    //查询id对应的车站名
    public Map<Integer, List<String>> selectTime();
    //查询区间对应的时间
    public Map<String,List<Integer>> czNameToID(String Name);
    //查询车站名对应的id
    public Map<Integer,Integer> select_CQ_LineId();
    //重庆id线路数据查询
    public Map<String,Integer> get_CQ_od(String day,String hour);
    //重庆id测试
}
