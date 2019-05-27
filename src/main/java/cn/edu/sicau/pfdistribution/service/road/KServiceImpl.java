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

    transient
    @Autowired
    private RoadDistributionDao roadDistributionDao;

    transient
    @Autowired
    private PathCheckService sectionCheckService;

    /**
     * 有废弃区间的静态计算
     * @param o
     * @param d
     * @param abandonEdges
     * @return
     */
    @Override
    public List<Path> computeStatic(String o, String d, List<Edge> abandonEdges) {
        List<Edge> sections = roadDistributionDao.getAllSection();
        List<Edge>newSections = new ArrayList<Edge>();
        Graph graph = new Graph();
        if(abandonEdges != null){
            for(int i = 0; i < sections.size(); i++){
                boolean flag = true;
                for(int j = 0; j < abandonEdges.size(); j++){
                    if(sections.get(i).getFromNode().equals(abandonEdges.get(j).getFromNode()) && sections.get(i).getToNode().equals(abandonEdges.get(j).getToNode())){
                        flag = false;
                    }
                }
                if(flag == true)newSections.add(sections.get(i));
            }
            sections = newSections;
        }
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

    /**
     * 无废弃区间的静态计算
     * @param o
     * @param d
     * @return
     */
    @Override
    public List<Path> computeStatic(String o, String d) {
        List<Path> path = computeStatic(o, d, null);
        return path;
    }

    /**
     * 从通号院获取废弃区间的动态计算,可额外添加废弃区间
     * @param o
     * @param d
     * @return
     */
    @Override
    public List<Path> computeDynamic(String o, String d, List<Edge>abandonEdges) {
        List<Path> paths = null;
        if(abandonEdges != null){
            paths = computeStatic(o, d, abandonEdges);
        }else{
            paths = computeStatic(o, d);
        }
        List<Integer>interuptedIndex = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(!sectionCheckService.checkPath(paths.get(i)))
            interuptedIndex.add(i);
        }
        List<Path>newPaths = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(interuptedIndex.indexOf(i) == -1)
                newPaths.add(paths.get(i));
        }
        return newPaths;
    }
    /**
     * 从通号院获取废弃区间的动态计算
     * @param o
     * @param d
     * @return
     */
    @Override
    public List<Path> computeDynamic(String o, String d) {
        List<Path>paths = computeDynamic(o, d, null);
        return paths;
    }
    /**
     * 从通号院获取废弃区间的动态计算，计算以集合形式的od的k路径搜索
     * @param ods 键为o，值为d
     * @return
     */
    @Override
    public Map<String, List<Path>> computeDynamic(Map<String, String> ods) {
        if(ods == null) return null;
        Map<String, List<Path>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        while(it.hasNext()){
            String o = it.next();
            String d = ods.get(o);
            List<Path>paths = computeDynamic(o, d);
            odsPaths.put(o + " " + d, paths);
        }
        return odsPaths;
    }

    /**
     * 从通号院获取废弃区间的动态计算.可额外设置废弃区间
     * @param ods
     * @param abandonEdges
     * @return
     */
    @Override
    public Map<String, List<Path>> computeDynamic(Map<String, String> ods, List<Edge> abandonEdges) {
        Map<String, List<Path>> odsPaths= new HashMap<>();
        Iterator<String>it = ods.keySet().iterator();
        while(it.hasNext()){
            String o = it.next();
            String d = ods.get(o);
            List<Path> path = computeDynamic(o, d, abandonEdges);
            odsPaths.put(o + d, path);
        }
        return odsPaths;
    }


}
