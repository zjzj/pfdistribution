package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.Station;
import cn.edu.sicau.pfdistribution.service.PathCheckService;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Graph;
import cn.edu.sicau.pfdistribution.service.kspcalculation.KSPUtil;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Serializable;

import java.util.*;

@Service
public class KServiceImpl implements KService, Serializable {


    @Autowired
    private RoadDistributionDao roadDistributionDao;


    @Autowired
    private PathCheckService sectionCheckService;

    @Override
    public List<Path> computeStatic(String o, String d) {
        List<Edge> sections = roadDistributionDao.getAllSection();

        Graph graph = new Graph();
        graph.addEdges(sections);
        KSPUtil kspUtil = new KSPUtil();
        kspUtil.setEdges(sections);
        kspUtil.setGraph(graph);
        List<Path>paths = kspUtil.computeODPath(o, d, 1);
        if(paths == null)return null;

        LinkedList<Edge> path = paths.get(0).getEdges();
        //路径经过的站点
        List<String> stations = new ArrayList<>();
        stations.add(path.getFirst().getFromNode());
        for(int i = 1; i < path.size(); i++)
            stations.add(path.get(i).getToNode());

        //获取k
        List<Station>stationInfo = roadDistributionDao.getAllStationInfo();
        int k = 0;
        for(int i = 0; i < stations.size(); i++){
            String station = stations.get(i);
            for(int j = 0; j < stationInfo.size(); j++){
                if(station.equals(stationInfo.get(j).getName()) && stationInfo.get(j).getLines().size() >=2 )
                    k++;
            }
        }

        paths = kspUtil.computeODPath(o, d, k + 1);
        return paths;
    }

    @Override
    public List<Path> computeDynamic(String o, String d) {
        List<Path> paths = computeStatic(o, d);
        List<Integer>interunptedIndx = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(!sectionCheckService.checkPath(paths.get(i)))
            interunptedIndx.add(i);
        }
        List<Path>newPaths = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(interunptedIndx.indexOf(i) == -1)
                newPaths.add(paths.get(i));
        }
        return newPaths;
    }

    /**
     * 计算以集合形式的od的k路径搜索
     * @param ods 键为o，值为d
     * @return
     */
    @Override
    public Map<String, List<Path>> computeDynamic(Map<String, String> ods) {
        if(ods == null)return null;
        Map<String, List<Path>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        while(it.hasNext()){
            String o = it.next();
            String d = ods.get(o);
            List<Path>paths = computeDynamic(o, d);
            odsPaths.put(o + "-" + d, paths);
        }
        return odsPaths;
    }
}
