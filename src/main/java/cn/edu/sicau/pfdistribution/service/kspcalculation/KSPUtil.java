package cn.edu.sicau.pfdistribution.service.kspcalculation;

import cn.edu.sicau.pfdistribution.service.kspcalculation.ksp.Eppstein;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

import java.util.List;

public class KSPUtil {
    private Graph graph;

    /**
     * 计算一个od对的k短路径
     * @param source 起点
     * @param target 终点
     * @param k 路径条数
     * @return
     */
    public List<Path> computeODPath(String source, String target, int k){
        Eppstein eppsteinAlgorithm = new Eppstein();
        List<Path>ksp = eppsteinAlgorithm.ksp(graph, source, target, k);
        return ksp;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }


//    public void initialGraph(String fileName, List<String> abandonNodes){
//        Graph graph = new Graph(fileName, abandonNodes);
//        this.graph = graph;
//    }
}
