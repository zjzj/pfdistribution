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




    public Risk riskDataAnalysis(JSONArray dataArray) throws JSONException {
        List<SectionRisk>sectionRisks = new ArrayList<>();
        List<StationRisk>stationRisks = new ArrayList<>();
        for(int i = 0; i < dataArray.length(); i++){
            JSONObject tmp = dataArray.getJSONObject(i);
            if(tmp.has(Constants.STATION_ID)){
                StationRisk stationRisk = new StationRisk();
                stationRisk.setStationId(tmp.getInt(Constants.STATION_ID));
                stationRisk.setAlarmLevel(tmp.getInt(Constants.ALARM_LEVEL));
                stationRisks.add(stationRisk);
            }else if(tmp.has(Constants.SECTION_ID)){
                SectionRisk sectionRisk = new SectionRisk();
                sectionRisk.setSectionId(tmp.getInt(Constants.SECTION_ID));
                sectionRisk.setAlarmLevel(tmp.getInt(Constants.ALARM_LEVEL));
            }
        }
        Risk risk = new Risk();
        risk.setSectionRisks(sectionRisks);
        risk.setStationsRisks(stationRisks);
        return risk;
    }
}
