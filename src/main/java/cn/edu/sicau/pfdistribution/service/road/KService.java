package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

import java.util.List;

public interface KService {
    List<Path> computeStatic(String o, String d);
    List<Path> computeDynamic(String o, String d);
}
