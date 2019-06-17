package cn.edu.sicau.pfdistribution.service.netrouter;

import cn.edu.sicau.pfdistribution.entity.StationAndSectionPassengers;
import cn.edu.sicau.pfdistribution.entity.StationAndSectionRisk;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.*;

public class JsonTransfer {

    public boolean stationDataAnalysis(JSONObject jsonObject){
        try {
          /*  JSONObject jsonObject = new JSONObject(data);*/
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
                /*sectionPassengers.add(s.getString("startid"));
                sectionPassengers.add(s.getString("startid"));*/
                sectionPassengers.add(s.getString("utilization_rate"));
                sectionPassengers.add(s.getString("persengers"));
                sectionPassengers.add(s.getString("volume"));
                sectionP.put(s.getString("startid") + " " + s.getString("startid"), sectionPassengers);
            }
            new StationAndSectionPassengers(stationP, sectionP);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    public void riskDataAnalysis(String data) throws JSONException {
        String data1 = data.substring(1);
        String data2 = "{data:"+ data1;
        JSONObject jsonObject = new JSONObject(data2);
        List<List<String>> stationList = new ArrayList<>();
        List<List<String>> sectionList = new ArrayList<>();
        JSONArray dataArray = jsonObject.getJSONArray("data");
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
        new StationAndSectionRisk(stationList,sectionList);
    }
}
