package cn.edu.sicau.pfdistribution.service.road;

        import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
        import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

public interface PathCheckService {
    boolean checkPath(Path path);
    boolean checkSection(Edge section);
}
