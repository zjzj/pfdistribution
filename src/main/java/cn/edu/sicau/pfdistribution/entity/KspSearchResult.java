package cn.edu.sicau.pfdistribution.entity;

public class KspSearchResult {
    private String type;
    private String stationList;

    public KspSearchResult(String type, String stationList) {
        this.type = type;
        this.stationList = stationList;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStationList() {
        return stationList;
    }

    public void setStationList(String stationList) {
        this.stationList = stationList;
    }
}
