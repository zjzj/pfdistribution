package cn.edu.sicau.pfdistribution.Utils;

import cn.edu.sicau.pfdistribution.entity.DirectedEdge;
import cn.edu.sicau.pfdistribution.entity.DirectedPath;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonToObjectUtil {
    public static final String path = "path";
    public static final String edge = "edge";
    public static final String edgeFromNode = "fromNode";
    public static final String edgeToNode = "toNode";
    public static final String edgeWeight = "weight";
    public static final String edgeDirection = "direction";
    public static List<DirectedPath> jsonPathToDirectedPath(JSONArray jsonArray) throws JSONException {
        List<DirectedPath>paths = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            JSONArray edges = obj.getJSONArray(path);
        }
        return null;
    }
    private List<DirectedEdge> getDirectedEdges(JSONArray edges){
        return null;
    }
    private DirectedPath getDirectedPath(){
        return null;
    }
}
