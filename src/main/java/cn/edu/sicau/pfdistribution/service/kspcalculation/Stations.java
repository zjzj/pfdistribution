package cn.edu.sicau.pfdistribution.service.kspcalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stations {
    private Map<String, List<String>> stationInfo = new HashMap<String, List<String>>();

    public void addLine(String stationName, String line){
        if(stationInfo.get(stationName) != null){
            List<String>lines = stationInfo.get(stationName);
            lines.add(line);
            stationInfo.put(stationName, lines);
        }else{
            List<String>lines = new ArrayList<String>();
            lines.add(line);
            stationInfo.put(stationName, lines);
        }
    }
    public Map<String, List<String>> getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(Map<String, List<String>> stationInfo) {
        this.stationInfo = stationInfo;
    }
}