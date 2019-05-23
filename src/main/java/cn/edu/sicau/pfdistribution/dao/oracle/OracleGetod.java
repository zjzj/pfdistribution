package cn.edu.sicau.pfdistribution.dao.oracle;

import java.util.List;

public interface OracleGetod {
    public void deleteCare();
    public void creatGet();
    public void creatIn();
    public void creatOut();
    public void insertIn();
    public void insertOut();
    public void insertOd();
    public void deleteNot();
    public void deleteIn();
    public void deleteOut();
    public void deleteOd();
    List<String> selectOd(String selectOD);
}
