package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.Station;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Graph;
import cn.edu.sicau.pfdistribution.service.kspcalculation.KSPUtil;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Serializable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class KServiceImpl implements KService, Serializable {

    transient
    @Autowired
    private RoadDistributionDao roadDistributionDao;

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
                if(station.equals(stationInfo.get(i).getName()) && stationInfo.get(i).getLines().size() >=2 )
                    k++;
            }
        }

        paths = kspUtil.computeODPath(o, d, k + 1);
        return paths;
    }

    @Override
    public List<Path> computeDynamic(String o, String d) {
        List<Path> paths = computeStatic(o, d);
        return null;
    }
}
