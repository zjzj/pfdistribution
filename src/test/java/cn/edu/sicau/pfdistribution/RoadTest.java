package cn.edu.sicau.pfdistribution;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import cn.edu.sicau.pfdistribution.entity.ODPath;
import cn.edu.sicau.pfdistribution.entity.ODPathWithJson;
import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.service.road.KService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    //param  edge   edge
    //(id -> name) ->name ->id
    //id -> id and name
    @Test
    public void getOdIdPathFromDB() throws JSONException, IOException {
        List<ODPathWithJson> odPathWithJsons = roadDistributionDao.getAllODPathById();
        ODPathWithJson path = odPathWithJsons.get(0);
        String idPath = path.getPathWithNameAndId().split("&&")[0];
        String namePath = path.getPathWithNameAndId().split("&&")[1];
        JSONArray nameJson = new JSONArray(namePath);
        JSONArray idJson = new JSONArray(idPath);
        System.out.println(nameJson.toString());
        System.out.println(idJson);
    }
    @Test
    public void getOdNamePathFromDB() throws JSONException, IOException {
        List<ODPathWithJson> odPathWithJsons = roadDistributionDao.getAllODPathByName();
        ODPathWithJson path = odPathWithJsons.get(0);
        String idPath = path.getPathWithNameAndId().split("&&")[0];
        String namePath = path.getPathWithNameAndId().split("&&")[1];
        JSONArray nameJson = new JSONArray(namePath);
        JSONArray idJson = new JSONArray(idPath);
        System.out.println(nameJson.toString());
        System.out.println(idJson);
    }
}
