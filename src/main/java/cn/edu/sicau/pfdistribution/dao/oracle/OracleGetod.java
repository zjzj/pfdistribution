package cn.edu.sicau.pfdistribution.dao.oracle;


import java.util.List;
import java.util.Map;

public interface OracleGetod {
    public void deleteCare();
    public void creatGet();
    public void creatIn();
    public void creatOut();
    public void insertIn();
    public void insertOut();
    public void insertOd();
    public void deleteNot();
    public void deleteIn();
    public void deleteOut();
    public void deleteOd();
    Map<String,Integer> selectOd(String selectOD);
    //路径分配结果存取部分
    public void createKspRegionTable();
    public void kspRegionAdd(String route,int passenger);
    public void deleteAllKspRegion();
    //将仿真得到的OD结果从oracle拉取出来并组成需要格式
    public List<String> odFromOracleToList();
}

