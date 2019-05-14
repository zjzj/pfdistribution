package cn.edu.sicau.pfdistribution.service.kspcalculation.ksp.test;


import cn.edu.sicau.pfdistribution.service.kspcalculation.Graph;
import cn.edu.sicau.pfdistribution.service.kspcalculation.ksp.Eppstein;
import cn.edu.sicau.pfdistribution.service.kspcalculation.util.Path;

import java.util.List;

public class TestEppstein {

    public static void main(String args[]) {
        String graphFilename, sourceNode, targetNode;
        int K;
        graphFilename = "data/cd.txt";
        sourceNode = "一品天下-2_7";
        targetNode = "天府广场-4_1";
        K = 2;
        usageExample1(graphFilename,sourceNode,targetNode,K);
    }

    public static void usageExample1(String graphFilename, String source, String target, int k) {
        Graph graph = new Graph(graphFilename);
        List<Path> ksp;
        Eppstein eppsteinAlgorithm = new Eppstein();
        ksp = eppsteinAlgorithm.ksp(graph, source, target, k);
    }
}
