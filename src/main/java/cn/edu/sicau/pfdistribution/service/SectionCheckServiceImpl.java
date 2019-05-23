package cn.edu.sicau.pfdistribution.service;

import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionCheckServiceImpl implements SectionCheckService{

    @Override
    public boolean checkPath(Path path) {
        List<Edge> edges = path.getEdges();
        return true;
    }
}
