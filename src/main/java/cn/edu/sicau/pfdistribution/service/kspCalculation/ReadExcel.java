package cn.edu.sicau.pfdistribution.service.kspCalculation;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    /**
     * 读取表格，获取车站线路信息
     * @param fileName
     * @return
     */
    public Stations getStationInfo(String fileName){
        Stations stations = new Stations();
        File excelFile = new File(fileName);
        try {
            FileInputStream in = new FileInputStream(excelFile);
            Workbook workbook = new HSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            boolean rowFlag = false;
            for(Row row: sheet){
               if(rowFlag == false)rowFlag = true;
               else{
                   if(!"".equals(row.getCell(1).toString().trim())){
                       String stationName = row.getCell(1).toString().trim();
                       String stationLine = row.getCell(7).toString().trim();
                        stations.addLine(stationName, stationLine);
                   }
               }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    /**
     * 获取区间车站信息
     * @param fileName
     * @return
     */
    public List<Edge> getEdgesFromFile(String fileName){
        List<Edge>edges = new ArrayList<Edge>();
        File excelFile = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(excelFile);
            Workbook workbook = new HSSFWorkbook(in);
            Sheet sheet = workbook.getSheetAt(0);
            boolean rowFlag = false;
            for(Row row: sheet){
                if(rowFlag == false)rowFlag = true;
                else{
                    if(!"".equals(row.getCell(2).toString().trim())){
                        String station1 = row.getCell(2).toString().trim();
                        String station2 = row.getCell(4).toString().trim();
                        String direction = row.getCell(5).toString().trim();
                        double length = Double.parseDouble(row.getCell(9).toString().trim());
                        Edge edge = null;
                        if("下行".equals(direction)){
                            edge= new Edge(station1, station2, length);
                            edges.add(edge);
                        }else{
                            edge= new Edge(station2, station1, length);
                            edges.add(edge);
                        }
                        edges.add(edge);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edges;
    }

    /**
     * 建立完善区间信息，每个车站包含所属线路信息
     * @param stationFileName
     * @param edgeFileName
     * @return
     */
    public List<Edge>buildEdges(String stationFileName, String edgeFileName){
        Stations stations = getStationInfo(stationFileName);
        List<Edge>edges = getEdgesFromFile(edgeFileName);
        List<Edge>formatedEdges = new ArrayList<Edge>();
        Object[] keySet = stations.getStationInfo().keySet().toArray();
        for(int i = 0; i < edges.size(); i = i + 2){
            Edge edge = edges.get(i);
            for(int j =0; j < keySet.length; j++){
                String fromStationName = (String) keySet[j];
                List<String>fromLines = stations.getStationInfo().get(keySet[j]);
                Edge tmp = new Edge();
                if(edge.getFromNode().equals(fromStationName)){
                    String baseFromNode = edge.getFromNode() + "-";
                    String baseToNode = "";
                    double weight = edge.getWeight();
                    for(String line:fromLines)baseFromNode += "_" + line;
                    for(int k = 0; k < keySet.length; k++){
                        String toStationName = (String) keySet[k];
                        List<String>toLines = stations.getStationInfo().get(keySet[k]);
                        if(edge.getToNode().equals(toStationName)){
                            baseToNode = edge.getToNode() + "-";
                            for(String line:toLines)baseToNode += "_" + line;
                        }
                    }
                    tmp.setFromNode(baseFromNode);
                    tmp.setToNode(baseToNode);
                    tmp.setWeight(weight);
                    formatedEdges.add(tmp);
                }
            }
        }
        return formatedEdges;
    }

    /**
     * 从excel文件获取信息构建铁路全网图
     * @param stationFileName
     * @param edgeFileName
     * @return
     */
    public Graph buildGrapgh(String stationFileName, String edgeFileName){
        List<Edge>edges = buildEdges(stationFileName, edgeFileName);
        Graph graph = new Graph();
        for(Edge edge:edges)
            graph.addEdge(edge);
        return graph;
    }
}
