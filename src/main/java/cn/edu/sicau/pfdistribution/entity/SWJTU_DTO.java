package cn.edu.sicau.pfdistribution.entity;

public class SWJTU_DTO {
    private String startTime;
    private String endTime;
    private String startStation;
    private String endStation;

    public SWJTU_DTO(String startTime, String endTime, String startStation, String endStation) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
}
