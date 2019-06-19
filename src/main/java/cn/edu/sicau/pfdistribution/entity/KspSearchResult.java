package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class KspSearchResult {
    private String type;
    private List<String> stationList;
    private List<List<String>> transferList;

    public KspSearchResult(String type, List<String> stationList, List<List<String>> transferList) {
        this.type = type;
        this.stationList = stationList;
        this.transferList = transferList;
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

    public List<List<String>> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<List<String>> transferList) {
        this.transferList = transferList;
    }
}

