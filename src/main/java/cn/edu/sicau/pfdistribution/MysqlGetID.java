package cn.edu.sicau.pfdistribution;

import cn.edu.sicau.pfdistribution.dao.Impl.Mysqlsavelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MysqlGetID {
    @Autowired
    private Mysqlsavelmpl mysqlsavelmpl;
    public Map<Integer,Integer> CheZhanID(){
        return mysqlsavelmpl.SelectLineId();
    }
}
