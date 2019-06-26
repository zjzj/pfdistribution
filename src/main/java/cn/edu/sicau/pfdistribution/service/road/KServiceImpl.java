package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.Constants;
import cn.edu.sicau.pfdistribution.Utils.TimeUtil;
import cn.edu.sicau.pfdistribution.dao.mysqlsave.RoadDistributionDao;
import cn.edu.sicau.pfdistribution.entity.*;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Graph;
import cn.edu.sicau.pfdistribution.service.kspcalculation.KSPUtil;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import com.google.gson.JsonArray;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;
import scala.Serializable;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class KServiceImpl implements KService, Serializable {
    transient
    @Autowired
    private RoadDistributionDao roadDistributionDao;

    transient
    @Autowired
    private PathCheckService pathCheckService;

    public static List<Section> sections;

    public static Map<String, List<String>> stationInfo;
    @PostConstruct
    public void init(){
        sections = roadDistributionDao.getAllSection();
        stationInfo = roadDistributionDao.getAllStationInfo();
    }

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
            directedPath = convertPathToDirectedIdPathTest(sections, newPaths);
        }
        return directedPath;
    }

    @Override
    public List<Path> computeDynamicRemoveAlarmPath(List<Section> sections, Map<String, List<String>> stationsInfo, String o, String d, String paramType, Risk risk) {
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
        if(risk == null)return newPaths;
        newPaths = removeAlarmPath(newPaths, sections, risk);
        return newPaths;
    }

    @Override
    public Map<String, List<DirectedPath>> computeStatic(Map<String, String> ods, String paramType, String resultType) {
        if(ods == null) return null;
        Map<String, List<DirectedPath>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        while(it.hasNext()){
            String[] odStr = it.next().split(" ");
            String o = odStr[0];
            String d = odStr[1];
            if(o.equals(d)){
                continue;
            }
            if(Constants.PARAM_ID.equals(paramType)) {
                String[] tmp = stationIdToName(sections, o, d);
                if(tmp[0].equals(tmp[1])){
                    DirectedPath directedPath = new DirectedPath();
                    Edge edge = new Edge();DirectedEdge directedEdge = new DirectedEdge();
                    edge.setFromNode(o);edge.setToNode(d);edge.setWeight(0);
                    directedEdge.setEdge(edge);directedEdge.setDirection(Constants.CHANGE_STATION);
                    LinkedList<DirectedEdge>edges = new LinkedList<>();
                    edges.add(directedEdge);
                    directedPath.setEdges(edges);directedPath.setTotalCost(Constants.CHANGE_LENGTH);
                    List<DirectedPath>directedPaths = new ArrayList<>();
                    directedPaths.add(directedPath);
                    odsPaths.put(o + " " + d, directedPaths);
                    continue;
                }
            }
            List<DirectedPath>paths = computeStatic(sections, stationInfo, o, d, paramType, resultType);
            odsPaths.put(o + " " + d, paths);
        }
        return odsPaths;
    }


    /**
     * 从通号院获取废弃区间的动态计算,可额外添加废弃区间
     * @param o 为id
     * @param d 为id
     * @return
     */
    @Override
    public List<DirectedPath> computeDynamic(List<Section>sections, Map<String, List<String>>stationsInfo, String o, String d, String paramType, String resultType, Risk risk) {
        List<Path> paths = null;
        paths = computeDynamicRemoveAlarmPath(sections, stationsInfo, o, d, paramType, risk);
        if(paths == null)return null;
        List<DirectedPath>directedPaths = null;
        if(Constants.RETURN_EDGE_NAME.equals(resultType)){
            directedPaths = convertPathToDirectedNamePath(paths);
        }else if(Constants.RETURN_EDGE_ID.equals(resultType)){
            directedPaths = convertPathToDirectedIdPathTest(sections, paths);
        }
        return directedPaths;
    }

    @Override
    public List<DirectedPath> computeDynamic(String o, String d, String paramType, String resultType, Risk risk) {
        return computeDynamic(sections, stationInfo, o, d, paramType, resultType,risk);
    }

    /**
     * 从通号院获取废弃区间的动态计算，计算以集合形式的od的k路径搜索
     * @param ods 键为o，值为d
     * @return
     */
    @Override
    public Map<String, List<DirectedPath>> computeDynamic(Map<String, String> ods, String paramType, String resultType, Risk risk) {
        if(ods == null) return null;
        Map<String, List<DirectedPath>>odsPaths = new HashMap<>();
        Iterator<String> it = ods.keySet().iterator();
        while(it.hasNext()){
            String[] odStr = it.next().split(" ");
            String o = odStr[0];
            String d = odStr[1];
            if(o.equals(d) && Constants.PARAM_NAME.equals(paramType)){
                continue;
            }
            if(o.equals(d) && Constants.PARAM_ID.equals(paramType)) {
                String[] tmp = stationIdToName(sections, o, d);
                if(tmp[0].equals(tmp[1])){
                    DirectedPath directedPath = new DirectedPath();
                    Edge edge = new Edge();DirectedEdge directedEdge = new DirectedEdge();
                    edge.setFromNode(o);edge.setToNode(d);edge.setWeight(0);
                    directedEdge.setEdge(edge);directedEdge.setDirection(Constants.CHANGE_STATION);
                    LinkedList<DirectedEdge>edges = new LinkedList<>();
                    edges.add(directedEdge);
                    directedPath.setEdges(edges);directedPath.setTotalCost(Constants.CHANGE_LENGTH);
                    List<DirectedPath>directedPaths = new ArrayList<>();
                    directedPaths.add(directedPath);
                    odsPaths.put(o + " " + d, directedPaths);
                    return odsPaths;
                    //continue;
                }
            }
            List<DirectedPath>paths = computeDynamic(sections, stationInfo, o, d, paramType, resultType,risk);
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
    @Override
    public String[] stationIdToName(List<Section> sections, String o, String d){
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
    private List<DirectedPath>convertPathToDirectedIdPathTest(List<Section>sections,List<Path>paths){
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
                        newEdge.setWeight(section.getWeight());
                        DirectedEdge directedEdge = new DirectedEdge();
                        directedEdge.setEdge(newEdge);
                        directedEdge.setDirection(section.getDirection());
                        if(directedEdges.size() == 0){
                            //
                        }else{
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

    private List<Path>removeAlarmPath(List<Path>paths, List<Section>sections, Risk risk){
        List<Path>newPaths = new ArrayList<>();
        List<Integer>removeIndex = null;
            removeIndex = getAlarmIndexFromNamePath(paths, sections, risk);
        if(removeIndex == null)return paths;
        for(int i = 0; i < paths.size(); i++){
            if((removeIndex.indexOf(i)) == -1){
                newPaths.add(paths.get(i));
            }
        }
        return newPaths;
    }

    //section index
    private List<Integer> getAlarmSectionIdxWithNamePath(List<Path>paths, List<Edge>alarmSection){
        if(alarmSection == null)return null;
        List<Integer>index = new ArrayList<>();
        int i = 0;
        for(Path path:paths){
            List<Edge>edges = path.getEdges();
            for(Edge edge:edges){
                for(Edge alarmEdge:alarmSection){
                     if(alarmEdge.getFromNode().equals(edge.getFromNode()) &&
                             alarmEdge.getToNode().equals(edge.getToNode())){
                         index.add(i);break;
                     }
                }
            }
            i++;
        }
        return index;
    }

    //station index
    private List<Integer> getAlarmStationIdxWithNamePath(List<Path>paths,List<Section>sections, List<StationRisk>stationRisks){
        if(stationRisks == null)return null;
        List<String>alarmNameStations = getStationNameFromAlarmStationId(sections, stationRisks);
        int i = 0;
        List<Integer>alarmStationIndex = new ArrayList<>();
        for(Path path:paths){
            List<Edge>edges = path.getEdges();
            for(Edge edge:edges){
                for(String station:alarmNameStations){
                    if(edge.getFromNode().equals(station) ||
                            edge.getToNode().equals(station)){
                        alarmStationIndex.add(i);
                        break;
                    }
                }
            }
            i++;
        }
        return alarmStationIndex;
    }

    //alarm edge
    private List<Edge> getNameEdgeFromAlarmSectionId(List<Section>sections, List<SectionRisk>sectionRisks){
        if(sectionRisks == null)return null;
        List<Edge>alarmNameEdge = new ArrayList<>();
        for(SectionRisk sectionRisk:sectionRisks){
            long currentTime = TimeUtil.getCurrentUnixTime();
            long alarmStart = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, sectionRisk.getStartTime());
            long alarmEnd = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, sectionRisk.getEndTime());
            if(currentTime >=alarmStart && currentTime < alarmEnd){
                for(Section section:sections){
                    if(sectionRisk.getAlarmLevel() == 1 && sectionRisk.getSectionId() == section.getSectionId()){
                        Edge edge = new Edge();
                        edge.setFromNode(section.getFromName());
                        edge.setToNode(section.getToName());
                        alarmNameEdge.add(edge);
                        break;
                    }
                }
            }
        }
        return alarmNameEdge;
    }

    //alarm station
    private List<String> getStationNameFromAlarmStationId(List<Section>sections, List<StationRisk>stationRisks){
        if(stationRisks == null)return null;
        List<String>alarmStation = new ArrayList<>();
        for(StationRisk stationRisk:stationRisks){
            long currentTime = TimeUtil.getCurrentUnixTime();
            long alarmStart = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, stationRisk.getStartTime());
            long alarmEnd = TimeUtil.getUnixTime(Constants.ALARM__TIME_FORMAT, stationRisk.getEndTime());
            //currentTime >=alarmStart && currentTime < alarmEnd
            if(currentTime >=alarmStart && currentTime < alarmEnd){
                for(Section section:sections){
                    if(stationRisk.getAlarmLevel() == 1 && stationRisk.getStationId() == section.getFromId()){
                        String stationName = section.getFromName();
                        alarmStation.add(stationName);
                        break;
                    }
                    if(stationRisk.getAlarmLevel() == 1 && stationRisk.getStationId() == section.getToId()){
                        String stationName = section.getToName();
                        alarmStation.add(stationName);
                        break;
                    }
                }
            }
        }
        return alarmStation;
    }

    //remove namePath index
    private List<Integer>getAlarmIndexFromNamePath(List<Path>paths, List<Section>sections, Risk risk){
        //section
        List<Edge> alarmNameEdges = getNameEdgeFromAlarmSectionId(sections, risk.getSectionRisks());
        List<Integer>tmp1 = getAlarmSectionIdxWithNamePath(paths, alarmNameEdges);
        //station
        List<Integer>tmp2 = getAlarmStationIdxWithNamePath(paths, sections, risk.getStationsRisks());
        if(tmp1 == null && tmp2 == null)return null;
        else if(tmp1 == null)return tmp2;
        else if(tmp2 == null)return tmp1;
        else{
            for(Integer tmp:tmp2){
                tmp1.add(tmp);
            }
            return tmp1;
        }
    }
}
