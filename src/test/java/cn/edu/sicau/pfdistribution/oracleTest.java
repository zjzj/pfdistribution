package cn.edu.sicau.pfdistribution;


import cn.edu.sicau.pfdistribution.dao.Impl.Mysqlsavelmpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class oracleTest implements Serializable {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    MysqlGetID GetID;
    @Autowired
    Mysqlsavelmpl save;
    @Test
    public void test()throws Exception{
        Map<String,String> time=save.SelectAvgTime();
        Map<String,Integer> peo=save.SelectAvgPeo();
        double peoCount=0,timeCount=0;
        for(String test : time.keySet()){
            peoCount=peoCount + Double.valueOf(peo.get(test));
            timeCount=timeCount+Double.valueOf(time.get(test))*Double.valueOf(peo.get(test));
        }
        System.out.println("20180904 星期三 乘车出行平均时间："+(timeCount/peoCount));

        //save.odRegion("武汉 北京",2.3,"20180309","6");
        /*Map idTime = new HashMap();
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
        }*/
        System.out.println("###########################################################");
        System.out.println("ok");
        System.out.println("###########################################################");
       /* Map<Integer,Integer> idLine=GetID.carID();
        System.out.println("###########################################################");
        System.out.println(idLine);
        System.out.println("###########################################################");*/
        //jdbcTemplate.update("insert into \"SCOTT\".\"user\" values(?, ?)",  "bbb", 30);
        /*Map LineId1 = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT LINE_ID,CZ_ID\n" +
                "from dic_linestation");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer careID1 = (Integer) userMap.get("CZ_ID");
            Integer lineID1 = (Integer) userMap.get("LINE_ID");
            LineId1.put(careID1,lineID1);
        }
        System.out.println("###########################################################");
        System.out.println(LineId1);
        System.out.println("###########################################################");
        System.out.println(LineId1.get(7));
        System.out.println("###########################################################");*/
    }
}
