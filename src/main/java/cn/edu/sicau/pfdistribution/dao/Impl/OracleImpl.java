package cn.edu.sicau.pfdistribution.dao.Impl;

import cn.edu.sicau.pfdistribution.dao.oracle.OracleGetod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class OracleImpl implements OracleGetod {
    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    private String pullOdFromOracleSql="select *from ODFLOW_Demand";

    @Override
    public void deleteCare(){
        jdbcTemplate.update("delete   \n" +
                "from ceshi_copy1\n" +
                "WHERE \"票卡种类\" not like '%票%' and \"票卡种类\" not like '%卡%'\n");
    }
    @Override
    public void creatGet(){
        jdbcTemplate.update("CREATE TABLE test_od(   \n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"进站点\" CHAR(100) ,\n" +
                "  \"进站时间\" TIMESTAMP ,\n" +
                "  \"出站点\" CHAR(100) ,\n" +
                "  \"出站时间\" TIMESTAMP \n" +
                ")");
    }
    @Override
    public void creatIn(){
        jdbcTemplate.update("CREATE TABLE test_in (   --创建储存进站数据表\n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"进站点\" CHAR(100) ,\n" +
                "  \"进站时间\" TIMESTAMP ,\n" +
                "\"交易前金额(元)\" CHAR(10)\n" +
                ")");
    }
    @Override
    public void creatOut(){
        jdbcTemplate.update("CREATE TABLE test_out(  \n" +
                "  \"票卡号\" CHAR(20) ,\n" +
                "  \"出站点\" CHAR(100) ,\n" +
                "  \"出站时间\" TIMESTAMP ,\n" +
                "\"交易前金额(元)\" CHAR(10)\n" +
                ")");
    }
    @Override
    public void insertIn(){
        jdbcTemplate.update("insert into test_in(\"票卡号\",\"进站点\",\"进站时间\",\"交易前金额(元)\")  --插入进站数据到进站数据表\n" +
                "select \"票卡号\",\"交易车站\",\"数据接收时间\",\"交易前余额(元)\"\n" +
                "from \"SCOTT\".\"ceshi_copy1\"\n" +
                "where \"交易事件\"='进站'");
    }
    @Override
    public void insertOut(){
        jdbcTemplate.update("insert into test_out(\"票卡号\",\"出站点\",\"出站时间\",\"交易前金额(元)\")  --插入出站数据到出站数据表\n" +
                "select \"票卡号\",\"交易车站\",\"数据接收时间\",\"交易前余额(元)\"\n" +
                "from ceshi_copy1\n" +
                "where \"交易事件\"='出站'");
    }
    @Override
    public void insertOd(){
        jdbcTemplate.update("insert into test_od        --查找OD数据并插入到OD结果数据表\n" +
                "SELECT test_in.\"票卡号\",\"SCOTT\".\"test_in\".\"进站点\",\"SCOTT\".\"test_in\".\"进站时间\",\"SCOTT\".\"test_out\".\"出站点\",\"SCOTT\".\"test_out\".\"出站时间\"\n" +
                "FROM test_in,test_out\n" +
                "WHERE test_in.\"票卡号\"=\"SCOTT\".\"test_out\".\"票卡号\" AND \"SCOTT\".\"test_in\".\"交易前金额(元)\"=\"SCOTT\".\"test_out\".\"交易前金额(元)\"\n" +
                "ORDER BY test_in.\"进站时间\" ASC");
    }
    @Override
    public void deleteNot(){
        jdbcTemplate.update("delete \n" +
                "from test_od\n" +
                "where TO_CHAR(\"出站时间\")<=TO_CHAR(\"进站时间\") ");
    }
    @Override
    public void deleteIn(){
        jdbcTemplate.update("DROP TABLE test_in\n");
    }
    @Override
    public void deleteOut() {
        jdbcTemplate.update("DROP TABLE test_out\n");
    }
    @Override
    public void deleteOd(){
        jdbcTemplate.update("DROP TABLE test_od\n");
    }
    @Override
    public Map<String, Integer> selectOd(String selectOD){
        Map map = new HashMap();
        List rows= jdbcTemplate.queryForList(selectOD);
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            String odIn = (String) userMap.get("进站点");
            String odIn1=odIn.replace(" ", "");
            String odOut = (String) userMap.get("出站点");
            String odOut1=odOut.replace(" ", "");
            //int odPeo = (int) userMap.get("人数");
            int odPeo = Integer.parseInt(userMap.get("人数").toString());
            map.put(odIn1+" "+odOut1,odPeo);
            //strList.add(odIn1+" "+odOut1+" "+odPeo);
        }
        return map;
    }

    @Override
    public void createKspRegionTable() {
        jdbcTemplate.update("CREATE TABLE test_5_28 (   \n" +
                "  \"route\" VARCHAR(500) ,\n" +
                "  \"passenger\" INT \n" +
                ")");
    }

    @Override
    public void kspRegionAdd(String route, int passenger) {
        jdbcTemplate.update("insert into test_5_28 values(?,?)",route,passenger);
    }

    @Override
    public void deleteAllKspRegion() {
        jdbcTemplate.update("delete from test_5_28 where 1=1");
    }

    @Override
    public List<String> odFromOracleToList() {
        List<String> strings=new ArrayList<>();
        List rows= jdbcTemplate.queryForList(pullOdFromOracleSql);
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map odMap = (Map) it.next();
            String startstation = (String) odMap.get("STARTSTATION");
            String odstartstation=startstation.replace(" ", "");
            String endstation = (String) odMap.get("ENDSTATION");
            String odendstation=endstation.replace(" ", "");
            String peoplenum = (String) odMap.get("PEOPLENUM");
            String odresult=odstartstation+" "+odendstation+" "+peoplenum;
            strings.add(odresult);
        }
        return strings;
    }
    public List<String> idTest(){
        List<String> idList=new ArrayList<>();
        List rows= jdbcTemplate.queryForList("SELECT CZ_ID\n" +
                "FROM dic_station");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer ID= Integer.parseInt(userMap.get("CZ_ID").toString());
            String id=ID.toString();
            idList.add(id);
        }
        return idList;
    }
}
