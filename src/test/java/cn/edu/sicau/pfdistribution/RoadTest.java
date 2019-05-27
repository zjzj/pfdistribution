package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.Station;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import cn.edu.sicau.pfdistribution.service.road.KService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoadTest {
    @Autowired
    private RoadDistributionDao roadDistributionDao;
    @Autowired
    private KService kService;
    @Test
    public void sationInfoTest(){
        List<Station> stations = roadDistributionDao.getAllStationInfo();
        for(int i = 0; i < stations.size(); i++){
            System.out.println(stations.get(i));
        }
    }
    @Test
    public void sectionTest(){
        List<Edge>sections = roadDistributionDao.getAllSection();
        System.out.println(sections);
    }
    @Test
    public void kspTest(){
        List<Edge>abandonEdges = new ArrayList<>();
        Edge abadonEdge = new Edge();
        abadonEdge.setFromNode("幸福广场");
        abadonEdge.setToNode("人和");
        abadonEdge.setWeight(1.0);
        abandonEdges.add(abadonEdge);
        List<Path>paths = null;
        paths = kService.computeStatic("璧山", "较场口");
        System.out.println("长度:" + paths.size());
        System.out.println(paths);
        paths = kService.computeStatic("璧山", "较场口", abandonEdges);
        System.out.println("长度:" + paths.size());
        System.out.println(paths);
    }
}
