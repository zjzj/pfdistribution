package cn.edu.sicau.pfdistribution.service.netrouter;

import cn.edu.sicau.pfdistribution.Constants;
import cn.edu.sicau.pfdistribution.entity.*;
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

    @Autowired

    public Risk risk;

    public boolean stationDataAnalysis(JSONObject jsonObject){
        try {
            //JSONObject jsonObject = new JSONObject(data);
            //        String record_time = jsonObject.optString("Recordtime");
            JSONArray station_loads = jsonObject.getJSONArray("StationLoads");
            JSONArray Section_loads = jsonObject.getJSONArray("SectionLoads");
            Map<String, List<String>> stationP = new HashMap<>();
            Map<String, List<String>> sectionP = new HashMap<>();
            for (int i = 0; i < station_loads.length(); i++) {
                List<String> stationPassengers = new ArrayList<>();
                String str = station_loads.getString(i);
                JSONObject s = new JSONObject(str);
                //            stationPassengers.add(s.getString("stationid"));
                stationPassengers.add(s.getString("CrowdingRate"));
                stationPassengers.add(s.getString("Passengers"));
                stationPassengers.add(s.getString("AvgVolume"));
                stationPassengers.add(s.getString("OutVolume"));
                stationPassengers.add(s.getString("InVolume"));
                stationP.put(s.getString("StationId"), stationPassengers);
            }
            for (int i = 0; i < Section_loads.length(); i++) {
                List<String> sectionPassengers = new ArrayList<>();
                String str = station_loads.getString(i);
                JSONObject s = new JSONObject(str);
                sectionPassengers.add(s.getString("UtilizationRate"));
                sectionPassengers.add(s.getString("Passengers"));
                sectionPassengers.add(s.getString("Volume"));
                sectionP.put(s.getString("StartId") + " " + s.getString("EndId"), sectionPassengers);
            }
            stationAndSectionPassengers.setStationP(stationP);
            stationAndSectionPassengers.setSectionP(sectionP);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public void riskDataAnalysis(JSONArray dataArray) throws JSONException {
        List<SectionRisk>sectionRisks = new ArrayList<>();
        List<StationRisk>stationRisks = new ArrayList<>();
        for(int i = 0; i < dataArray.length(); i++){
            JSONObject tmp = dataArray.getJSONObject(i);
            if(tmp.has(Constants.STATION_ID)){
                StationRisk stationRisk = new StationRisk();
                stationRisk.setStationId(tmp.getInt(Constants.STATION_ID));
                stationRisk.setAlarmLevel(tmp.getInt(Constants.ALARM_LEVEL));
                stationRisk.setStartTime(tmp.getString(Constants.ALARM_START_TIME));
                stationRisk.setEndTime(tmp.getString(Constants.ALARM_END_TIME));
                stationRisks.add(stationRisk);
            }else if(tmp.has(Constants.SECTION_ID)){
                SectionRisk sectionRisk = new SectionRisk();
                sectionRisk.setSectionId(tmp.getInt(Constants.SECTION_ID));
                sectionRisk.setStartTime(tmp.getString(Constants.ALARM_START_TIME));
                sectionRisk.setEndTime(tmp.getString(Constants.ALARM_END_TIME));
                sectionRisk.setAlarmLevel(tmp.getInt(Constants.ALARM_LEVEL));
            }
        }
        risk.setSectionRisks(sectionRisks);
        risk.setStationsRisks(stationRisks);
    }
}
