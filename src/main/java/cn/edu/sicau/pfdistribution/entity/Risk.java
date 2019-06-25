package cn.edu.sicau.pfdistribution.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class Risk implements Serializable {
    private List<SectionRisk> sectionRisks;
    private List<StationRisk> stationsRisks;
    public List<SectionRisk> getSectionRisks() {
        return sectionRisks;
    }

    public void setSectionRisks(List<SectionRisk> sectionRisks) {
        this.sectionRisks = sectionRisks;
    }

    public List<StationRisk> getStationsRisks() {
        return stationsRisks;
    }

    public void setStationsRisks(List<StationRisk> stationsRisks) {
        this.stationsRisks = stationsRisks;
    }
}
