package cn.edu.sicau.pfdistribution.service.road;

        import cn.edu.sicau.pfdistribution.entity.DirectedEdge;
        import cn.edu.sicau.pfdistribution.entity.DirectedPath;
        import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
        import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

public interface PathCheckService {
    boolean checkPath(DirectedPath path);
    boolean checkSection(DirectedEdge section);
}
