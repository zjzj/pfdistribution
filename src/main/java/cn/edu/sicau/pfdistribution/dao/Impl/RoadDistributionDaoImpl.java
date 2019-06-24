package cn.edu.sicau.pfdistribution.dao.Impl;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.ODPath;
import cn.edu.sicau.pfdistribution.entity.ODPathWithJson;
import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.entity.SimpleStation;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoadDistributionDaoImpl implements RoadDistributionDao, Serializable {
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

    @Override
    public boolean insertIDOd(List<ODPath> odPaths) {
        final List<ODPath>finalOds = odPaths;
        String sql = "insert into id_to_all_odPath values(?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

                preparedStatement.setString(1, finalOds.get(i).getO() + " " + finalOds.get(i).getD());
                preparedStatement.setString(2, finalOds.get(i).getIdPaths().toString() + "&&" + finalOds.get(i).getNamePaths().toString());
            }

            @Override
            public int getBatchSize() {
                return finalOds.size();
            }
        });
        return true;
    }

    @Override
    public boolean insertNameOd(List<ODPath> odPaths) {
        final List<ODPath>finalOds = odPaths;
        String sql = "insert into name_to_all_odPath values(?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, finalOds.get(i).getO() + " " + finalOds.get(i).getD());
                preparedStatement.setString(2, finalOds.get(i).getIdPaths().toString() + "&&" + finalOds.get(i).getNamePaths().toString());
            }
            @Override
            public int getBatchSize() {
                return finalOds.size();
            }
        });
        return true;
    }

    @Override
    public List<ODPathWithJson> getAllODPathById() {
        String sql = "select od , path pathWithNameAndId from id_to_all_odPath";
        RowMapper<ODPathWithJson> rowMapper = new BeanPropertyRowMapper<>(ODPathWithJson.class);
        List<ODPathWithJson> odPathWithJsons = jdbcTemplate.query(sql, rowMapper);
        return odPathWithJsons;
    }

    @Override
    public List<ODPathWithJson> getAllODPathByName() {
        String sql = "select od , path pathWithNameAndId from name_to_all_odPath";
        RowMapper<ODPathWithJson> rowMapper = new BeanPropertyRowMapper<>(ODPathWithJson.class);
        List<ODPathWithJson> odPathWithJsons = jdbcTemplate.query(sql, rowMapper);
        return odPathWithJsons;
    }
}