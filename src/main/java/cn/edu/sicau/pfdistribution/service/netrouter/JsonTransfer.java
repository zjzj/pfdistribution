package cn.edu.sicau.pfdistribution.service.netrouter;

import cn.edu.sicau.pfdistribution.entity.StationAndSectionPassengers;
import cn.edu.sicau.pfdistribution.entity.StationAndSectionRiskLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsonTransfer {
    @Autowired
    public StationAndSectionPassengers stationAndSectionPassengers;

    public boolean stationDataAnalysis(JSONObject jsonObject){
        try {
            //JSONObject jsonObject = new JSONObject(data);
            //        String record_time = jsonObject.optString("Recordtime");
            JSONArray station_loads = jsonObject.getJSONArray("Station_loads");
            JSONArray Section_loads = jsonObject.getJSONArray("Section_loads");
            Map<String, List<String>> stationP = new HashMap<>();
            Map<String, List<String>> sectionP = new HashMap<>();
            for (int i = 0; i < station_loads.length(); i++) {
                List<String> stationPassengers = new ArrayList<>();
                String str = station_loads.getString(i);
                JSONObject s = new JSONObject(str);
                //            stationPassengers.add(s.getString("stationid"));
                stationPassengers.add(s.getString("crowding_rate"));
                stationPassengers.add(s.getString("persengers"));
                stationPassengers.add(s.getString("avgvolume"));
                stationPassengers.add(s.getString("outvolume"));
                stationPassengers.add(s.getString("involume"));
                stationP.put(s.getString("stationid"), stationPassengers);
            }
            for (int i = 0; i < Section_loads.length(); i++) {
                List<String> sectionPassengers = new ArrayList<>();
                String str = station_loads.getString(i);
                JSONObject s = new JSONObject(str);
                sectionPassengers.add(s.getString("utilization_rate"));
                sectionPassengers.add(s.getString("persengers"));
                sectionPassengers.add(s.getString("volume"));
                sectionP.put(s.getString("startid") + " " + s.getString("startid"), sectionPassengers);
            }
            stationAndSectionPassengers.setStationP(stationP);
            stationAndSectionPassengers.setSectionP(sectionP);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    public void riskDataAnalysis(JSONArray dataArray) throws JSONException {
        List<List<String>> stationList = new ArrayList<>();
        List<List<String>> sectionList = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            String str = dataArray.getString(i);
            JSONObject s = new JSONObject(str);
            if(s.getString("StationId") != ""){ //!!!!!""
                List<String> message = new ArrayList<>();
                message.add(s.getString("StationId"));
                message.add(s.getString("AlarmLevel"));
                message.add(s.getString("StartTime"));
                message.add(s.getString("EndTime"));
                stationList.add(message);
            } else if(s.getString("SectionId") != ""){ //!!!!!""
                List<String> message = new ArrayList<>();
                message.add(s.getString("SectionId"));
                message.add(s.getString("AlarmLevel"));
                message.add(s.getString("StartTime"));
                message.add(s.getString("EndTime"));
                sectionList.add(message);
            }
        }
        StationAndSectionRiskLevel data = new StationAndSectionRiskLevel(stationList,sectionList);
    }
}
