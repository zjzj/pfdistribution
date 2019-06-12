package cn.edu.sicau.pfdistribution.dao.Impl;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.plugin.javascript.navig.Array;

import java.math.BigDecimal;
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
    /*@Override
    public Map<BigDecimal,BigDecimal> SelectLineid(){
        Map LineId = new HashMap();
        List rows= jdbcTemplate.queryForList("select ");
        Iterator it = rows.iterator();
        while(it.hasNext()) {
            Map userMap = (Map) it.next();
            BigDecimal CZ_ID = (BigDecimal) userMap.get("CZ_ID");
            BigDecimal LINE_ID = (BigDecimal) userMap.get("LINE_ID");
            LineId.put(CZ_ID,LINE_ID);
        }
        return LineId;
    }*/
}
