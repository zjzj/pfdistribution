package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.Impl.Mysqlsavelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xieyuan
 */
@Service
public class MysqlGetID implements Serializable{
    transient
    @Autowired
    private Mysqlsavelmpl mysqlsavelmpl;

    public Map<Integer,Integer> carID(){
        Map<Integer,Integer> idLine=mysqlsavelmpl.selectLineId();
        return idLine;
    }
    public Map<Integer, List<String>> idTime(){
        Map<Integer, List<String>> time=mysqlsavelmpl.selectTime();
        return time;
    }
    public Map<String ,Integer> test_CQ_od(String da,String ti){
        return mysqlsavelmpl.get_CQ_od(da,ti);
    }
}
