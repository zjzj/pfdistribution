package cn.edu.sicau.pfdistribution.dao.mysqlsave;

import cn.edu.sicau.pfdistribution.entity.ODPath;
import cn.edu.sicau.pfdistribution.entity.ODPathWithJson;
import cn.edu.sicau.pfdistribution.entity.Section;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository
public interface RoadDistributionDao{
    Map<String, List<String>> getAllStationInfo();
    List<Section>getAllSection();
    boolean insertIDOd(List<ODPath> odPaths);
    boolean insertNameOd(List<ODPath> odPaths);
    List<ODPathWithJson>getAllODPathById();
    List<ODPathWithJson>getAllODPathByName();
}
