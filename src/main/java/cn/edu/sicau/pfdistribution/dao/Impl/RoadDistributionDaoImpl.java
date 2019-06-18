package cn.edu.sicau.pfdistribution.dao.Impl;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.entity.SimpleStation;
import cn.edu.sicau.pfdistribution.entity.Station;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoadDistributionDaoImpl implements RoadDistributionDao {
    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    /**
     * 获取所有站点的所属的线路信息
     * @return
     */
    @Override
    public Map<String, List<String>> getAllStationInfo() {
        String sql = "SELECT station.CZ_NAME stationName, station.LJM line FROM dic_station station";
        RowMapper rowMapper = new BeanPropertyRowMapper(SimpleStation.class);
        List<SimpleStation>stations = jdbcTemplate.query(sql, rowMapper);
        Map<String, List<String>> stationList= new HashMap<>();
        for(int i = 0; i < stations.size(); i++){
            String stationName = stations.get(i).getStationName();
            if(stationList.containsKey(stationName)){
                List<String>lines = stationList.get(stationName);
                lines.add(stations.get(i).getLine());
                stationList.put(stationName, lines);
            }else{
                List<String>lines = new ArrayList<>();
                lines.add(stations.get(i).getLine());
                stationList.put(stationName, lines);
            }
        }
        return stationList;
    }

    /**
     * 获取所有的区间信息
     * @return
     */
    @Override
    public List<Section> getAllSection() {
        String sql = "SELECT QJ_ID sectionId, `CZ1_ID` fromId, CZ1_NAME fromName, \n" +
                "\t`CZ2_ID` toId, CZ2_NAME toName, \n" +
                "\t`QJ_SXX` direction, QJ_LENGTH weight FROM dic_section";
        RowMapper<Section>rowMapper = new BeanPropertyRowMapper<Section>(Section.class);
        List<Section>sections = jdbcTemplate.query(sql, rowMapper);
        return sections;
    }
}