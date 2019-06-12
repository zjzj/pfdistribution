package cn.edu.sicau.pfdistribution.dao.mysqlsave;

import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.entity.Station;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadDistributionDao {
    List<Station> getAllStationInfo();
    List<Section>getAllSection();
}
