package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

import java.util.List;
import java.util.Map;

public interface KService {
    List<Path> computeStatic(String o, String d);
    List<Path> computeStatic(String o, String d, List<Edge>abandonEdges);
    List<Path> computeDynamic(String o, String d);
    List<Path> computeDynamic(String o, String d, List<Edge> abandonEdges);
    Map<String, List<Path>> computeDynamic(Map<String, String> ods);
    Map<String, List<Path>> computeDynamic(Map<String, String>ods, List<Edge> abandonEdges);
}
