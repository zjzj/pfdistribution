package cn.edu.sicau.pfdistribution;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import cn.edu.sicau.pfdistribution.entity.Station;
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
    /*    @Test
        public void sectionTest(){
            List<Section>sections = roadDistributionDao.getAllSection();
            System.out.println(sections);
        }*/
    @Test
    public void kspTest(){
        List<DirectedPath>paths = null;
        // id -> id
/*        paths = kService.computeStatic("20", "42", Constants.PARAM_ID, Constants.RETURN_EDGE_ID);
        System.out.println(paths);
        //name -> name
        paths = kService.computeStatic("较场口", "牛角沱", Constants.PARAM_NAME, Constants.RETURN_EDGE_NAME);
        System.out.println(paths);
        // id -> name
        paths = kService.computeStatic("20", "42", Constants.PARAM_ID, Constants.RETURN_EDGE_NAME);
        System.out.println(paths);*/
        paths = kService.computeStatic("唐家院子", "陈家桥", Constants.PARAM_NAME, Constants.RETURN_EDGE_ID);
        System.out.println(paths);
    }
}
