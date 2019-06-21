package cn.edu.sicau.pfdistribution.dao.oracle;

import java.util.List;

public interface OracleGetTransferStationsById {
    public List<List<String>> getTransferStations(List<String> odStations);
    public List<String> getStationsById(List<String> odStations);
}
