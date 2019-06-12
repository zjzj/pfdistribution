package cn.edu.sicau.pfdistribution.entity;

import java.util.List;
import java.util.Map;

public class StationAndSectionPassengers {
    private Map<String, List<String>> stationP;
    private Map<String, List<String>> sectionP;

    public StationAndSectionPassengers(Map<String, List<String>> stationP, Map<String, List<String>> sectionP) {
        this.stationP = stationP;
        this.sectionP = sectionP;
    }

    public Map<String, List<String>> getStationP() {
        return stationP;
    }

    public void setStationP(Map<String, List<String>> stationP) {
        this.stationP = stationP;
    }

    public Map<String, List<String>> getSectionP() {
        return sectionP;
    }

    public void setSectionP(Map<String, List<String>> sectionP) {
        this.sectionP = sectionP;
    }
}
