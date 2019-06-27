package cn.edu.sicau.pfdistribution;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.*;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import cn.edu.sicau.pfdistribution.service.road.KService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoadTest {
    @Autowired
    private RoadDistributionDao roadDistributionDao;
    @Autowired
    private KService kService;
    private List<Section>sections;
    private Map<String, List<String>>stationInfo;
    @PostConstruct
    public void init(){
        sections = roadDistributionDao.getAllSection();
        stationInfo = roadDistributionDao.getAllStationInfo();
    }
    @Test
    public void sationInfoTest(){

    }
    @Test
    public void nameOdKspTest(){
        List<Section> sections = roadDistributionDao.getAllSection();
        Map<String, List<String>> stations = roadDistributionDao.getAllStationInfo();
        int i, j;
        List<String>odMatrix = new ArrayList<>();
        for(i = 1; i <=10; i++){
            for(j = i + 1; j <=293; j++){
                String[]od = kService.stationIdToName(sections, ""+i, ""+j);
                if(od[0].equals(od[1]))continue;
                odMatrix.add(od[0] + " " + od[1]);
                odMatrix.add(od[1] + " " + od[0]);
            }
        }
        List<ODPath>odPaths = new ArrayList<>();
        for(String od: odMatrix){
            String[]fields = od.split("\\s+");
            List<DirectedPath>idPaths = kService.computeStatic(sections, stations, fields[0], fields[1], Constants.PARAM_NAME, Constants.RETURN_EDGE_ID);
            List<DirectedPath>namePaths = kService.computeStatic(sections, stations, fields[0], fields[1], Constants.PARAM_NAME, Constants.RETURN_EDGE_NAME);
            ODPath odPath = new ODPath();
            odPath.setO(fields[0]);
            odPath.setD(fields[1]);
            odPath.setNamePaths(namePaths);
            odPath.setIdPaths(idPaths);
            odPaths.add(odPath);
        }
        System.out.println("插入数据过程中.....");
        roadDistributionDao.insertNameOd(odPaths);
    }
    @Test
    public void idOdKspTest(){
        List<Section> sections = roadDistributionDao.getAllSection();
        Map<String, List<String>> stations = roadDistributionDao.getAllStationInfo();
        int i, j;
        List<String>odMatrix = new ArrayList<>();
        int count = 0;
        for(i = 1; i <=293; i++){
            for(j = i + 1; j <=293; j++){
                String[] fields = kService.stationIdToName(sections, ""+i, ""+j);
                if(fields[0].equals(fields[1]))continue;
                odMatrix.add(i + " " + j);
                odMatrix.add(j + " " + i);
            }
        }
        System.out.println("over");
        List<ODPath>odPaths = new ArrayList<>();
        for(String od: odMatrix){
            count++;
            String[]fields = od.split("\\s+");
            List<DirectedPath>idPaths = kService.computeStatic(sections, stations, fields[0], fields[1], Constants.PARAM_ID, Constants.RETURN_EDGE_ID);
            List<DirectedPath>namePaths = kService.computeStatic(sections, stations, fields[0], fields[1], Constants.PARAM_ID, Constants.RETURN_EDGE_NAME);
            ODPath odPath = new ODPath();
            odPath.setO(fields[0]);
            odPath.setD(fields[1]);
            odPath.setNamePaths(namePaths);
            odPath.setIdPaths(idPaths);
            odPaths.add(odPath);
            if(count%293 == 0) System.out.println("=======================================");
        }
        System.out.println("插入数据过程中.....");
        roadDistributionDao.insertIDOd(odPaths);
    }

    @Test
    public void kspTest(){
        List<Section>sections = roadDistributionDao.getAllSection();
        Map<String, List<String>>stations = roadDistributionDao.getAllStationInfo();
        List<DirectedPath>path = kService.computeStatic(sections, stations, "174", "9", Constants.PARAM_ID, Constants.RETURN_EDGE_ID);
        System.out.println(path);
    }

    @Test
    public void AlarmTest(){
        Risk risk = new Risk();
//        StationRisk stationRisk = new StationRisk();
//        List<StationRisk>stationRisks = new ArrayList<>();
//        stationRisk.setStationId(199);
//        stationRisk.setAlarmLevel(1);
//        stationRisk.setStartTime("2019/6/26 6:33:44");
//        stationRisk.setEndTime("2019/6/26 22:53:44");
//        stationRisks.add(stationRisk);
//        risk.setStationsRisks(stationRisks);
//
//        List<SectionRisk>sectionRisks = new ArrayList<>();
//        SectionRisk sectionRisk = new SectionRisk();
//        sectionRisk.setAlarmLevel(1);
//        sectionRisk.setSectionId(575);
//        sectionRisk.setStartTime("2019/6/26 6:33:44");
//        sectionRisk.setEndTime("2019/6/26 22:53:44");
//        sectionRisks.add(sectionRisk);
//        risk.setSectionRisks(sectionRisks);
//        risk.setStationsRisks(stationRisks);

        String o = "198",d = "12";
        //List<DirectedPath>directedPathLIst = kService.computeDynamic(sections, stationInfo, o, d, Constants.PARAM_ID, Constants.RETURN_EDGE_NAME, risk);
        //System.out.println(directedPathLIst);
        List<DirectedPath>staticPath = kService.computeDynamic(sections, stationInfo, o, d, Constants.PARAM_ID, Constants.RETURN_EDGE_NAME, null);
        System.out.println(staticPath);
    }
}
