package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class StationAndSectionRisk {
    private List<List<String>> stationRisk;
    private List<List<String>> sectionRisk;

    public StationAndSectionRisk(List<List<String>> stationRisk, List<List<String>> sectionRisk) {
        this.stationRisk = stationRisk;
        this.sectionRisk = sectionRisk;
    }

    public List<List<String>> getStationRisk() {
        return stationRisk;
    }

    public void setStationRisk(List<List<String>> stationRisk) {
        this.stationRisk = stationRisk;
    }

    public List<List<String>> getSectionRisk() {
        return sectionRisk;
    }

    public void setSectionRisk(List<List<String>> sectionRisk) {
        this.sectionRisk = sectionRisk;
    }
}
