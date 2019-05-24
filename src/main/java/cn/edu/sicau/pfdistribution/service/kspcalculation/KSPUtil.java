package cn.edu.sicau.pfdistribution.service.kspcalculation;

import cn.edu.sicau.pfdistribution.service.kspcalculation.ksp.Eppstein;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.List;

public class KSPUtil{
    transient
    private Graph graph;//路网图
    transient
    private List<Edge>edges;//构建路网图的边

    /**
     * 计算一个od对的k短路径
     * @param source 起点
     * @param target 终点
     * @param abandonEdges 废弃的区间
     * @param k 路径条数
     * @return
     */
    public List<Path> computeDynamicODPath(String source, String target, List<Edge>abandonEdges, int k){
        Eppstein eppsteinAlgorithm = new Eppstein();
        initialGraph(abandonEdges);
        List<Path>ksp = eppsteinAlgorithm.ksp(graph, source, target, k);
        return ksp;
    }
    public List<Path> computeODPath(String source, String target, int k){
        Eppstein eppsteinAlgorithm = new Eppstein();
        List<Path>ksp = eppsteinAlgorithm.ksp(graph, source, target, k);
        return ksp;
    }


    public boolean initAllSection(List<Edge> abandonEdges){
        //初始化全网拓扑图
        initialGraph(abandonEdges);
        //初始化静态k路径
        //1.从数据库获取全网区间信息
        //2.建立graph
        //3.初始化静态k路径


        return true;
    }
    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    /**
     * 初始化路径拓扑图
     * 需要从数据库读取全网节点信息
     * 命令提供废弃节点，如果没提供，默认为没有废弃节点
     */
    private void initialGraph(List<Edge>abandonEdges){

    }
}
