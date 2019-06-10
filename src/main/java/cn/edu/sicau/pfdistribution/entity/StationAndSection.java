package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class StationAndSection {
    private List<List<String>> station;
    private List<List<String>> section;

    public StationAndSection(List<List<String>> station, List<List<String>> section) {
        this.station = station;
        this.section = section;
    }

    public List<List<String>> getStation() {
        return station;
    }

    public void setStation(List<List<String>> station) {
        this.station = station;
    }

    public List<List<String>> getSection() {
        return section;
    }

    public void setSection(List<List<String>> section) {
        this.section = section;
    }
}
