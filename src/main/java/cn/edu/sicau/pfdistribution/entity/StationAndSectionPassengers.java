package cn.edu.sicau.pfdistribution.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class StationAndSectionPassengers implements Serializable {

    private Map<String, List<String>> stationP;
    private Map<String, List<String>> sectionP;

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
