package cn.edu.sicau.pfdistribution.entity;

import cn.edu.sicau.pfdistribution.service.kspcalculation.Edge;

import java.io.Serializable;

public class DirectedEdge implements Serializable {
    private Edge edge;
    private String direction;

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return "edge:{" +
                "edge=" + edge +
                ", direction='" + direction + '\'' +
                '}';
    }
}
