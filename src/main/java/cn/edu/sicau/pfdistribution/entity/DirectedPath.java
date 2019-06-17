package cn.edu.sicau.pfdistribution.entity;



import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DirectedPath implements Serializable {
    private LinkedList<DirectedEdge> edges;

    private double totalCost;
    public DirectedPath() {
        edges = new LinkedList<DirectedEdge>();
        totalCost = 0;
    }

    public LinkedList<DirectedEdge> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<DirectedEdge> edges) {
        this.edges = edges;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(edges.get(0).getEdge().getFromNode());
        sb.append("+" + edges.get(0).getEdge().getToNode());
        for(int i = 1; i < edges.size(); i++){
            sb.append("+" + edges.get(i).getEdge().getToNode());
        }
        return "{" +
                "path:" + sb +
                ", totalCost:" + totalCost +
                '}';
    }
    public List<String> getNodes() {
        LinkedList<String> nodes = new LinkedList<String>();

        for (DirectedEdge directedEdge : edges) {
            nodes.add(directedEdge.getEdge().getFromNode());
        }

        DirectedEdge lastDirectedEdge = edges.getLast();
        if (lastDirectedEdge != null) {
            nodes.add(lastDirectedEdge.getEdge().getToNode());
        }

        return nodes;
    }
}
