package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.Impl.Mysqlsavelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MysqlGetID implements Serializable {
    @Autowired
    private Mysqlsavelmpl mysqlsavelmpl;
    public Map<Integer,Integer> CheZhanID(){
        return mysqlsavelmpl.SelectLineId();
    }
    public Map<Integer, List<String>> idTime(){
        return mysqlsavelmpl.selectTime();
    }
}
