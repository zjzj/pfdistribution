package cn.edu.sicau.pfdistribution.dao.Impl;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RegionSaveInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class  Mysqlsavelmpl implements RegionSaveInterface {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Override
    public void routeAdd(String ksp) {
        jdbcTemplate.update("insert into ksproute(ksp) value(?)",ksp);
    }


    @Override
    public void kspRegionAdd(String route, double passenger) {
        jdbcTemplate.update("insert into kspregion(route,passenger) value(?,?)",route,passenger);
    }

    @Override
    public void odRegion(String kspregion, double passenger) {
        jdbcTemplate.update("insert into odregion(kspregion,passenger) value(?,?)",kspregion,passenger);
    }

}
