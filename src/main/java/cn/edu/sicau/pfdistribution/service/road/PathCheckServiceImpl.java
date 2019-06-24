package cn.edu.sicau.pfdistribution.service.road;

import cn.edu.sicau.pfdistribution.entity.DirectedEdge;
import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class PathCheckServiceImpl implements PathCheckService, Serializable {

    /**
     * 返回false表示路径不通
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(DirectedPath path) {
        List<DirectedEdge> edges = path.getEdges();
        boolean flag = true;
        for(int i = 0; i < edges.size(); i++){
            if(!checkSection(edges.get(i))){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 返回false表示区间不通
     * @param section
     * @return
     */
    @Override
    public boolean checkSection(DirectedEdge section) {
        return true;
    }
}
