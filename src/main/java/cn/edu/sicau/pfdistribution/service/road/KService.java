package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import cn.edu.sicau.pfdistribution.entity.Risk;
import cn.edu.sicau.pfdistribution.entity.Section;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;


import java.util.List;
import java.util.Map;

public interface KService {
    List<DirectedPath> computeStatic(List<Section>sections, Map<String, List<String>>stationsInfo,String o, String d, String paramType, String resultType);
    List<Path> computeDynamicRemoveAlarmPath(List<Section>sections, Map<String, List<String>>stationsInfo,String o, String d, String paramType, Risk risk);
    Map<String, List<DirectedPath>> computeStatic(Map<String, String> ods, String paramType, String resultType);
    List<DirectedPath> computeDynamic(List<Section>sections, Map<String, List<String>>stationsInfo, String o, String d, String paramType, String resultType, Risk risk);
    List<DirectedPath> computeDynamic(String o, String d, String paramType, String resultType, Risk risk);
    Map<String, List<DirectedPath>> computeDynamic(Map<String, String> ods, String paramType, String resultType, Risk risk);
    String[] stationIdToName(List<Section> sections, String o, String d);
}
