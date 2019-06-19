package cn.edu.sicau.pfdistribution.dao.oracle;

import java.util.List;

public interface OracleGetTransferStations {
    public List<List<String>> getTransferStations(List<String> odStations);
}

