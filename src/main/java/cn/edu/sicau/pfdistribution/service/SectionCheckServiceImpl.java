package cn.edu.sicau.pfdistribution.service;

import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionCheckServiceImpl implements PathCheckService{

    /**
     * 返回false表示路径不通
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(Path path) {
        List<Edge> edges = path.getEdges();
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
    public boolean checkSection(Edge section) {
        return true;
    }
}
