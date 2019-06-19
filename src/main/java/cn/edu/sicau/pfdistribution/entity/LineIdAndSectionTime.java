package cn.edu.sicau.pfdistribution.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class LineIdAndSectionTime implements Serializable {
    private Map<Integer,Integer> stationIdToLineId;
    private Map<Integer, List<String>> sectionTime;

    public Map<Integer, List<String>> getSectionTime() {
        return sectionTime;
    }

    public void setSectionTime(Map<Integer, List<String>> sectionTime) {
        this.sectionTime = sectionTime;
    }

    public Map<Integer, Integer> getStationIdToLineId() {
        return stationIdToLineId;
    }

    public void setStationIdToLineId(Map<Integer, Integer> stationIdToLineId) {
        this.stationIdToLineId = stationIdToLineId;
    }
}
