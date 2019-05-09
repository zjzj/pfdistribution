package cn.edu.sicau.pfdistribution.dao.Impl;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.regionSavaInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class mysqlsavalmpl implements regionSavaInterface {

    @Autowired
    private JdbcTemplate jdbcTemplate;  //这个是系统自带的
    @Override
    public void ksprouteadd(String ksp) {
        jdbcTemplate.update("insert into ksproute(ksp) value(?)",ksp);
    }

    @Override
    public void kspregionadd(String route, double passenger) {
        jdbcTemplate.update("insert into kspregion(route,passenger) value(?,?)",route,passenger);
    }

    @Override
    public void odregion(String kspregion, double passenger) {
        jdbcTemplate.update("insert into odregion(kspregion,passenger) value(?,?)",kspregion,passenger);
    }
}
