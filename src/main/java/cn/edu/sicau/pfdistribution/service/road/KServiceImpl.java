package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.Constants;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.*;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Graph;
import cn.edu.sicau.pfdistribution.service.kspcalculation.KSPUtil;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import scala.Serializable;

import java.util.*;

@Service
public class KServiceImpl implements KService, Serializable {

    transient
    @Autowired
    private RoadDistributionDao roadDistributionDao;

    transient
    @Autowired
    private PathCheckService pathCheckService;

    /**
     * 静态k路径计算：
     * 1.先获取数据库区间组成，建立一个图
     * 2.调用KSPUTIL计算k为一的一条路径，从这条路径拿到有几个换乘点，
     * 3.再搜索一次k路径，k为换乘点个数 + 1
     * 4.由于存在搜的路径有往返情况，用Map表示一条路径的站点出现次数，如果出现两次以上，则证明这条路径是多余的，要去掉。
     * 5.需要注意的是，不存在od为站点名，而resultType为 id的k路径计算。因为站点名转id不唯一
     * @param o
     * @param d
     * @param resultType 用于确定返回路径是id还是name
     * @param paramType 用于参数od的类型，为id还是name
     * @return
     */
    @Override
    public List<DirectedPath> computeStatic(List<Section>sections, Map<String, List<String>>stationsInfo, String o, String d, String paramType,String resultType) {
        String newOD[] = null;
        if(Constants.PARAM_ID.equals(paramType)){
            newOD = stationIdToName(sections, o, d);
        }
        List<Edge>edges = getIdOrNameEdges(sections, Constants.RETURN_EDGE_NAME);
        Graph graph = new Graph();
        graph.addEdges(edges);
        KSPUtil kspUtil = new KSPUtil();
        kspUtil.setEdges(edges);
        kspUtil.setGraph(graph);
        List<Path> paths = null;
        if(newOD == null){
            paths = kspUtil.computeODPath(o, d, 1);
        }else{
            paths = kspUtil.computeODPath(newOD[0], newOD[1], 1);
        }
        if(paths == null)return null;

        List<String>stations = getShortestPathStations(paths.get(0).getEdges());
        int k = getPathAboveK(stations);
        if(newOD == null){
            paths = kspUtil.computeODPath(o, d, k + 1);
        }else{
            paths = kspUtil.computeODPath(newOD[0], newOD[1], k + 1);
        }
        List<Path>newPaths = removeWrongPaths(paths);
        List<DirectedPath>directedPath = null;
        if(Constants.RETURN_EDGE_NAME.equals(resultType)){
            directedPath = convertPathToDirectedNamePath(newPaths);
        }else if(Constants.RETURN_EDGE_ID.equals(resultType)){
            directedPath = convertPathToDirectedIdPathTest(sections, stationsInfo, newPaths);
        }
        return directedPath;
    }


    /**
     * 从通号院获取废弃区间的动态计算,可额外添加废弃区间
     * @param o 为id
     * @param d 为id
     * @return
     */
    @Override
    public List<DirectedPath> computeDynamic(List<Section>sections, Map<String, List<String>>stationsInfo, String o, String d, String paramType, String resultType) {
        List<DirectedPath> paths = null;
        paths = computeStatic(sections, stationsInfo, o, d, paramType, resultType);

        List<Integer>interruptedIndex = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(!pathCheckService.checkPath(paths.get(i)))
            interruptedIndex.add(i);
        }
        List<DirectedPath>newPaths = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            if(interruptedIndex.indexOf(i) == -1)
                newPaths.add(paths.get(i));
        }
        return newPaths;
    }

    @Override
    public List<DirectedPath> computeDynamic(String o, String d, String paramType, String resultType) {
        List<Section>sections = roadDistributionDao.getAllSection();
        Map<String, List<String>>stationsInfo = roadDistributionDao.getAllStationInfo();
        return computeDynamic(sections, stationsInfo, o, d, paramType, resultType);
    }

    /**
     * 从通号院获取废弃区间的动态计算，计算以集合形式的od的k路径搜索
     * @param ods 键为o，值为d
     * @return
     */
    @Override
    public Map<String, List<DirectedPath>> computeDynamic(Map<String, String> ods, String paramType, String resultType) {
        List<Section>sections = roadDistributionDao.getAllSection();
        Map<String, List<String>>stationsInfo = roadDistributionDao.getAllStationInfo();
        if(ods == null) return null;
        Map<String, List<DirectedPath>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        List<String> tmpOD = new ArrayList<>();
        System.out.println("ods条数:" + ods.keySet().size());
        int count = 0;
        while(it.hasNext()){
            String[] odStr = it.next().split(" ");
            /*String o = it.next();
            String d = ods.get(o);*/
            String o = odStr[0];
            String d = odStr[1];
            if(o.equals(d)){
                tmpOD.add(o + "->" + d);
                continue;
            }
            String[] tmp = stationIdToName(sections, o, d);
            if(tmp[0].equals(tmp[1])){
                tmpOD.add(o + "->" + d);
                continue;
            }
            count++;
            List<DirectedPath>paths = computeDynamic(sections, stationsInfo, o, d, paramType, resultType);
            odsPaths.put(o + " " + d, paths);
        }
        System.out.println("无效od条数:" + tmpOD.size());
        System.out.println(tmpOD);
        System.out.println("======================================================");
        System.out.println("有效路径条数:" + count);
        return odsPaths;
    }

    /**
     * 通过判断od的最短一条路径来确定换成站点数，以换乘站点数作为odk路径搜索的k
     * @param stations 一条路径的站点组成
     * @return
     */
    private int getPathAboveK(List<String> stations){
        Map<String, List<String>>stationInfo = roadDistributionDao.getAllStationInfo();
        int k = 0;
        for(int i = 0; i < stations.size(); i++){
            String station = stations.get(i);
            Iterator<String>it = stationInfo.keySet().iterator();
            while(it.hasNext()){
                if(station.equals(it.next()) && stationInfo.get(station).size() >=2 )
                    k++;
            }
        }
        return k;
    }

    /**
     * 去掉k路径的错误路径(一个站点在一条路径中出现多次即为错误路径)
     * @param paths
     * @return
     */
    private List<Path>removeWrongPaths(List<Path>paths){
        List<Path>newPaths = new ArrayList<>();
        for(int i = 0; i < paths.size(); i++){
            boolean flag = true;
            List<Edge> p = paths.get(i).getEdges();
            Map<String, Integer> deletePathMap = new HashMap<>();
            deletePathMap.put(p.get(0).getFromNode(), 1);
            for(int j = 1; j < p.size(); j++){
                if(deletePathMap.containsKey(p.get(j).getToNode())){
                    flag = false;
                }else
                    deletePathMap.put(p.get(j).getToNode(), 1);
            }
            if(flag == true)newPaths.add(paths.get(i));
        }
        return newPaths;
    }
    private List<Edge> getIdOrNameEdges(List<Section> sections, String edgeType){
        List<Edge>edges = new ArrayList<>();
        if(Constants.RETURN_EDGE_ID.equals(edgeType)){
            for(Section section:sections){
                Edge edge = new Edge();
                String fromId = section.getFromId().toString();
                String toId = section.getToId().toString();
                edge.setFromNode(fromId);
                edge.setToNode(toId);
                edge.setWeight(section.getWeight());
                edges.add(edge);
            }
        }else if(Constants.RETURN_EDGE_NAME.equals(edgeType)){
            for(Section section:sections){
                Edge edge = new Edge();
                String fromName = section.getFromName();
                String toName = section.getToName();
                edge.setFromNode(fromName);
                edge.setToNode(toName);
                edge.setWeight(section.getWeight());
                edges.add(edge);
            }
        }
        return edges;
    }
    private String[] stationIdToName(List<Section> sections, String o, String d){
        for(Section section:sections){
            if(section.getFromId().toString().equals(o))o = section.getFromName();
            if(section.getFromId().toString().equals(d))d = section.getFromName();
            if(section.getToId().toString().equals(o))o = section.getToName();
            if(section.getToId().toString().equals(d))d = section.getToName();
        }
        String[]od = new String[2];
        od[0] = o;
        od[1] = d;
        return od;
    }
    private List<DirectedPath>convertPathToDirectedNamePath(List<Path>paths){
        List<Section> sections = roadDistributionDao.getAllSection();
        List<DirectedPath>directedPaths = new ArrayList<>();
        for(Path path:paths){
            List<Edge>singlePathEdges = path.getEdges();
            //一条路径
            LinkedList<DirectedEdge>singlePathDirectedEdges = new LinkedList<>();
            for(Edge edge : singlePathEdges){
                for(Section section:sections){
                    if(section.getFromName().equals(edge.getFromNode()) &&
                            section.getToName().equals(edge.getToNode())){
                        DirectedEdge directedEdge = new DirectedEdge();
                        directedEdge.setEdge(edge);
                        directedEdge.setDirection(section.getDirection());
                        singlePathDirectedEdges.add(directedEdge);
                        break;
                    }
                }
            }
            DirectedPath directedPath = new DirectedPath();
            directedPath.setEdges(singlePathDirectedEdges);
            directedPath.setTotalCost(path.getTotalCost());
            directedPaths.add(directedPath);
        }
        return directedPaths;
    }
    private List<DirectedPath>convertPathToDirectedIdPathTest(List<Section>sections, Map<String, List<String>>stations,List<Path>paths){
        List<DirectedPath>directedPaths = new ArrayList<>();
        for(Path path:paths){
            List<Edge>pathEdges = path.getEdges();
            LinkedList<DirectedEdge>directedEdges = new LinkedList<>();
            for(Edge edge:pathEdges){
                for(Section section: sections){
                    if(section.getFromName().equals(edge.getFromNode()) && section.getToName().equals(edge.getToNode())){
                        Edge newEdge = new Edge();
                        newEdge.setFromNode(section.getFromId().toString());
                        newEdge.setToNode(section.getToId().toString());
                        DirectedEdge directedEdge = new DirectedEdge();
                        directedEdge.setEdge(newEdge);
                        directedEdge.setDirection(section.getDirection());
                        if(directedEdges.size()>=1){
                            if(!directedEdges.getLast().getEdge().getToNode().equals(directedEdge.getEdge().getFromNode())){
                                DirectedEdge changeDirectedEdge = new DirectedEdge();
                                Edge changeEdge = new Edge();
                                changeEdge.setFromNode(directedEdges.getLast().getEdge().getToNode());
                                changeEdge.setToNode(directedEdge.getEdge().getFromNode());
                                changeDirectedEdge.setEdge(changeEdge);
                                changeDirectedEdge.setDirection(Constants.CHANGE_STATION);
                                directedEdges.add(changeDirectedEdge);
                            }
                        }
                        directedEdges.add(directedEdge);
                        break;
                    }
                }
            }
            DirectedPath directedPath = new DirectedPath();
            directedPath.setEdges(directedEdges);
            directedPath.setTotalCost(path.getTotalCost());
            directedPaths.add(directedPath);
        }
        return directedPaths;
    }
    private List<String> getShortestPathStations(LinkedList<Edge> path){
        //路径经过的站点
        List<String> stations = new ArrayList<>();
        stations.add(path.getFirst().getFromNode());
        stations.add(path.getFirst().getToNode());
        for(int i = 1; i < path.size(); i++)
            stations.add(path.get(i).getToNode());
        return stations;
    }

}
