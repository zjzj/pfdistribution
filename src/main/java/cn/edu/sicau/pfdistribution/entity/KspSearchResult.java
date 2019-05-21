package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class KspSearchResult {
    private String type;
    private List<String> stationList;

    public KspSearchResult(String type, List<String> stationList) {
        this.type = type;
        this.stationList = stationList;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }
}
