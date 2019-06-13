package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.entity.DirectedPath;


import java.util.List;
import java.util.Map;

public interface KService {
    List<DirectedPath> computeStatic(String o, String d, String paramType, String resultType);
    List<DirectedPath> computeDynamic(String o, String d, String paramType, String resultType);
    Map<String, List<DirectedPath>> computeDynamic(Map<String, String> ods, String paramType, String resultType);
}
