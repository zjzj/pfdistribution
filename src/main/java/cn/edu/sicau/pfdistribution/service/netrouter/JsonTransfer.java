package cn.edu.sicau.pfdistribution.service.netrouter;

import cn.edu.sicau.pfdistribution.entity.StationAndSection;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.*;

public class JsonTransfer {

    public Map<String,List<String>> stationDataAnalysis(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        Map<String,List<String>> DataMap = new HashMap<>();
        String record_time = jsonObject.optString("Recordtime");
        JSONArray station_loads = jsonObject.getJSONArray("Station_loads");
        JSONArray Section_loads = jsonObject.getJSONArray("Section_loads");
        for(int i = 0;i<station_loads.length();i++){
            List<String>  stationProperties= new ArrayList<>();
            String str = station_loads.getString(i);
            JSONObject s = new JSONObject(str);
           /* System.out.println(s.getString("stationid"));*/
            stationProperties.add(s.getString("crowding_rate"));
            stationProperties.add(s.getString("persengers"));
            stationProperties.add(s.getString("avgvolume"));
            stationProperties.add(s.getString("outvolume"));
            stationProperties.add(s.getString("involume"));
            DataMap.put(s.getString("stationid"),stationProperties);
        }
        for(int i = 0;i<Section_loads.length();i++){
            List<String>  SectionProperties= new ArrayList<>();
            String str = station_loads.getString(i);
            JSONObject s = new JSONObject(str);
           /* System.out.println(s.getString("startid"));*/
            String sectionId = s.getString("startid") + " " +s.getString("endid");
            SectionProperties.add(s.getString("utilization_rate"));
            SectionProperties.add(s.getString("persengers"));
            SectionProperties.add(s.getString("volume"));
            DataMap.put(sectionId,SectionProperties);
        }
        return DataMap;
    }
    public StationAndSection riskDataAnalysis(String data) throws JSONException {
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
        StationAndSection stationAndSection = new StationAndSection(stationList,sectionList);
        return stationAndSection;
    }
}
