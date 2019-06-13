package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.Constants;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.DirectedEdge;
import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.entity.Station;
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
    private PathCheckService pathCheckService;

    /**
     * 静态k路径计算：
     * 1.先获取数据库区间组成，建立一个图
     * 2.调用KSPUTIL计算k为一的一条路径，从这条路径拿到有几个换乘点，
     * 3.再搜索一次k路径，k为换乘点个数 + 1
     * 4.由于存在搜的路径有往返情况，用Map表示一条路径的站点出现次数，如果出现两次以上，则证明这条路径是多余的，要去掉。
     * @param o
     * @param d
     * @param resultType 用于确定返回路径是id还是name
     * @param paramType 用于参数od的类型，为id还是name
     * @return
     */
    @Override
    public List<DirectedPath> computeStatic(String o, String d, String paramType,String resultType) {
        //返回路径为id，站名为name
        if(Constants.RETURN_EDGE_ID.equals(resultType) && Constants.PARAM_NAME.equals(paramType)){
            String[]od = stationNameToId(o, d);
            o = od[0];
            d = od[1];

        }//返回路径为name，od为id
        else if(Constants.RETURN_EDGE_NAME.equals(resultType) && Constants.PARAM_ID.equals(paramType)){
            String[] od = stationIdToName(o, d);
            o = od[0];
            d = od[1];
        }
        List<Edge>edges = getIdOrNameEdges(resultType);
        Graph graph = new Graph();
        graph.addEdges(edges);
        KSPUtil kspUtil = new KSPUtil();
        kspUtil.setEdges(edges);
        kspUtil.setGraph(graph);
        List<Path>paths = kspUtil.computeODPath(o, d, 1);
        if(paths == null)return null;

        LinkedList<Edge> path = paths.get(0).getEdges();
        //路径经过的站点
        List<String> stations = new ArrayList<>();
        stations.add(path.getFirst().getFromNode());
        for(int i = 1; i < path.size(); i++)
            stations.add(path.get(i).getToNode());

        int k = getPathAboveK(stations);

        paths = kspUtil.computeODPath(o, d, k + 1);
        List<Path>newPaths = removeWrongPaths(paths);
        List<DirectedPath>directedPath = convertPathToDirectedPath(newPaths, resultType);
        return directedPath;
    }


    /**
     * 从通号院获取废弃区间的动态计算,可额外添加废弃区间
     * @param o 为id
     * @param d 为id
     * @return
     */
    @Override
    public List<DirectedPath> computeDynamic(String o, String d, String paramType, String resultType) {
        List<DirectedPath> paths = null;
        paths = computeStatic(o, d, paramType, resultType);

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
    /**
     * 从通号院获取废弃区间的动态计算，计算以集合形式的od的k路径搜索
     * @param ods 键为o，值为d
     * @return
     */
    @Override
    public Map<String, List<DirectedPath>> computeDynamic(Map<String, String> ods, String paramType, String resultType) {
        if(ods == null) return null;
        Map<String, List<DirectedPath>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        while(it.hasNext()){
            String o = it.next();
            String d = ods.get(o);
            List<DirectedPath>paths = computeDynamic(o, d, paramType, resultType);
            odsPaths.put(o + " " + d, paths);
        }
        return odsPaths;
    }

    /**
     * 通过判断od的最短一条路径来确定换成站点数，以换乘站点数作为odk路径搜索的k
     * @param stations 一条路径的站点组成
     * @return
     */
    private int getPathAboveK(List<String> stations){
        List<Station>stationInfo = roadDistributionDao.getAllStationInfo();
        int k = 0;
        for(int i = 0; i < stations.size(); i++){
            String station = stations.get(i);
            for(int j = 0; j < stationInfo.size(); j++){
                if(station.equals(stationInfo.get(j).getName()) && stationInfo.get(j).getLines().size() >=2 )
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
    private List<Edge> getIdOrNameEdges(String edgeType){
        List<Section> sections = roadDistributionDao.getAllSection();
        List<Edge>edges = new ArrayList<>();
        if(Constants.RETURN_EDGE_ID.equals(edgeType)){
            for(Section section:sections){
                Edge edge = new Edge();
                String fromId = section.getFromId().toString();
                String toId = null;
                toId = section.getToId().toString();
                edge.setFromNode(fromId);
                edge.setToNode(toId);
                edge.setWeight(section.getWeight());
                edges.add(edge);
            }
        }else if(Constants.RETURN_EDGE_NAME.equals(edgeType)){
            for(Section section:sections){
                Edge edge = new Edge();
                String fromName = section.getFromName().toString();
                String toName = null;
                toName = section.getToName().toString();
                edge.setFromNode(fromName);
                edge.setToNode(toName);
                edge.setWeight(section.getWeight());
                edges.add(edge);
            }
        }
        return edges;
    }
    private String[] stationNameToId(String o, String d){
        List<Section> sections = roadDistributionDao.getAllSection();
        for(Section section:sections){
            if(section.getFromName().equals(o))o = section.getFromId().toString();
            if(section.getFromName().equals(d))d = section.getFromId().toString();
            if(section.getToName().equals(o))o = section.getToId().toString();
            if(section.getToName().equals(d))d = section.getToId().toString();
        }
        String[]od = new String[2];
        od[0] = o;
        od[1] = d;
        return od;
    }
    private String[] stationIdToName(String o, String d){
        List<Section> sections = roadDistributionDao.getAllSection();
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
    private List<DirectedPath>convertPathToDirectedPath(List<Path>paths, String pathType){
        List<Section> sections = roadDistributionDao.getAllSection();
        List<DirectedPath>directedPaths = new ArrayList<>();
        //name
        if(Constants.RETURN_EDGE_NAME.equals(pathType)){
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
        }
        else if(Constants.RETURN_EDGE_ID.equals(pathType)){
            for(Path path:paths){
                List<Edge>singlePathEdges = path.getEdges();
                //一条路径
                LinkedList<DirectedEdge>singlePathDirectedEdges = new LinkedList<>();
                for(Edge edge : singlePathEdges){
                    for(Section section:sections){
                        if(section.getFromId().equals(edge.getFromNode()) &&
                                section.getToId().equals(edge.getToNode())){
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
        }
        return directedPaths;
    }
}
