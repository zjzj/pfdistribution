package cn.edu.sicau.pfdistribution.dao.Impl;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class  Mysqlsavelmpl implements RegionSaveInterface {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public void routeAdd(String ksp) {
        jdbcTemplate.update("insert into ksproute(ksp) values(?)",ksp);
    }


    @Override
    public void kspRegionAdd(String route, double passenger) {
        jdbcTemplate.update("insert into kspregion(route,passenger) values(?,?)",route,passenger);
    }

    @Override
    public void odRegion(String kspregion, double passenger,String time_day,String time_hour) {
        jdbcTemplate.update("insert into odregion(kspregion,passenger,time_day,time_hour) values(?,?,?,?)",kspregion,passenger,time_day,time_hour);
    }
    @Override
    public Map<Integer, Integer> selectLineId(){
        Map LineId = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT LINE_ID,CZ_ID\n" +
                "from dic_linestation");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer careID = (Integer) userMap.get("CZ_ID");
            Integer lineID = (Integer) userMap.get("LINE_ID");
            LineId.put(careID,lineID);
        }
        return LineId;
    }
    @Override
    public Map<Integer,Integer> select_CQ_LineId(){
        Map CQ_LineId = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT STATIONID,LINENAME\n" +
                "FROM chongqing_stations_nm");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer careID = (Integer) userMap.get("STATIONID");
            String lineName = (String) userMap.get("LINENAME");
            CQ_LineId.put(careID,lineName);
        }
        return CQ_LineId;
    }
    @Override
    public Map<String,Integer> get_CQ_od(String day,String hour){
        Map CQ_od = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT GETIN_STATION,GETOUT_STATION,VOLUME\n" +
                "FROM chongqinod\n" +
                "WHERE GETIN_DAY='"+day+"' AND GETIN_60MIN='"+hour+"'");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            String getInStation = (String) userMap.get("GETIN_STATION");
            String getOutStation= (String) userMap.get("GETOUT_STATION");
            int passengers= (int) userMap.get("VOLUME");
            CQ_od.put(getInStation+" "+getOutStation,passengers);
        }
        return CQ_od;
    }
    @Override
    public String selectStationName(Integer id){
        Map stationNameMap=jdbcTemplate.queryForMap("SELECT LINE_NAME\n" +
                "from dic_linestation\n" +
                "WHERE CZ_ID="+id+"");
        String stationName= (String) stationNameMap.get("LINE_NAME");
        return stationName;
    }
    @Override
    public Map<Integer,List<String>> selectTime(){
        Map idTime = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT CZ_ID,ARR_TIME,DEP_TIME\n" +
                "from base_khsk");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer CZ_ID = (int) userMap.get("CZ_ID");
            String ARR_TIME= (String) userMap.get("ARR_TIME");
            String ARR_TIME1=ARR_TIME.replace(" ", "");
            String DEP_TIME= (String) userMap.get("DEP_TIME");
            String DEP_TIME1=DEP_TIME.replace(" ", "");
            List<String> timeList= Arrays.asList(ARR_TIME1, DEP_TIME1);
            idTime.put(CZ_ID,timeList);
        }
        return idTime;
    }
    @Override
    public Map<String,List<Integer>> czNameToID(String Name){
        Map<String,List<Integer>> nameToID = new HashMap();
        List<Integer> idList=new ArrayList<>();
        List rows= jdbcTemplate.queryForList("SELECT CZ_ID\n" +
                "FROM dic_linestation\n" +
                "WHERE CZ_NAME='"+Name+"'");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer CZ_ID = (Integer) userMap.get("CZ_ID");
            idList.add(CZ_ID);
        }
        nameToID.put(Name,idList);
        return nameToID;
    }
    public Map<String,String> SelectAvgTime(){
        Map peoMap = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT GETIN_STATION,GETOUT_STATION,AVGTIME\n" +
                "FROM chongqinod\n" +
                "WHERE GETIN_DAY='20180905'");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            String in= (String) userMap.get("GETIN_STATION");
            String out= (String) userMap.get("GETOUT_STATION");
            String time = (String) userMap.get("AVGTIME");
            peoMap.put(in+" "+out,time);
        }
        return peoMap;
    } public Map<String,Integer> SelectAvgPeo(){
        Map timeMap = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT GETIN_STATION,GETOUT_STATION,VOLUME\n" +
                "FROM chongqinod\n" +
                "WHERE GETIN_DAY='20180905'");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            String in= (String) userMap.get("GETIN_STATION");
            String out= (String) userMap.get("GETOUT_STATION");
            Integer pessengers = (Integer) userMap.get("VOLUME");
            timeMap.put(in+" "+out,pessengers);
        }
        return timeMap;
    }
    public List<String> idGet(){
        List<String> idList1=new ArrayList<>();
        List rows= jdbcTemplate.queryForList("SELECT CZ_ID\n" +
                "FROM dic_station");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer id= Integer.parseInt(userMap.get("CZ_ID").toString());
            String ID=id.toString();
            idList1.add(ID);
        }
        return idList1;
    }
}
