package cn.edu.sicau.pfdistribution.service;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class ParamContextService {
    @Autowired
    RoadDistributionDao roadDistributionDao;

    private List<Section> sections;
    private Map<String, List<String>> stations;
    @PostConstruct
    public void initContext(){
        sections = roadDistributionDao.getAllSection();
        stations = roadDistributionDao.getAllStationInfo();
    }

    public RoadDistributionDao getRoadDistributionDao() {
        return roadDistributionDao;
    }

    public void setRoadDistributionDao(RoadDistributionDao roadDistributionDao) {
        this.roadDistributionDao = roadDistributionDao;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public Map<String, List<String>> getStations() {
        return stations;
    }

    public void setStations(Map<String, List<String>> stations) {
        this.stations = stations;
    }
}
