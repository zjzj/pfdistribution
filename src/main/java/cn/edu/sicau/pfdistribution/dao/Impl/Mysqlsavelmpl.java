package cn.edu.sicau.pfdistribution.dao.Impl;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
    public void odRegion(String kspregion, double passenger) {
        jdbcTemplate.update("insert into odregion(kspregion,passenger) values(?,?)",kspregion,passenger);
    }
    @Override
    public Map<Integer, Integer> SelectLineId(){
        Map LineId = new HashMap();
        List rows= jdbcTemplate.queryForList("SELECT LINE_ID,CZ_ID\n" +
                "from dic_linestation");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            Integer CZ_ID = (Integer) userMap.get("CZ_ID");
            Integer LINE_ID = (Integer) userMap.get("LINE_ID");
            LineId.put(CZ_ID,LINE_ID);
        }
        return LineId;
    }
}
