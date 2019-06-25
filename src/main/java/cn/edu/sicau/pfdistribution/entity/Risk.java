package cn.edu.sicau.pfdistribution.entity;

import java.util.List;

public class Risk {
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
