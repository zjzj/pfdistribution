package cn.edu.sicau.pfdistribution.dao.Impl;

import cn.edu.sicau.pfdistribution.dao.oracle.OracleGetTransferStationsById;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class OracleGetTransferStationsByIdImpl implements OracleGetTransferStationsById {
    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    private String getLineSql="select * from dic_linestation where CZ_ID=";

    @Override
    public List<List<String>> getTransferStations(List<String> odStations) {
/*        List<String> OdStations=getStationsById(odStations);
        for(int i=0;i<odStations.size()-1;i++){
            if(OdStations.get(i).equals(OdStations.get(i+1))){
                odStations.remove(i);
            }
        }*/
        int JudgeTransfer[]=new int[odStations.size()];
        List<List<String>> TransferResult=new ArrayList<>();

        //初始化前一个站点所属线路id
        List pre=jdbcTemplate.queryForList(getLineSql+Integer.parseInt(odStations.get(0)));
        int preLine;
        Iterator preit = pre.iterator();
        Map LineMap_pre = (Map) preit.next();
        preLine = (int) LineMap_pre.get("LINE_ID");
        //初始化当前站点所属线路id
        List mid=jdbcTemplate.queryForList(getLineSql+Integer.parseInt(odStations.get(1)));
        int midLine;
        Iterator midit = mid.iterator();
        Map LineMap_mid = (Map) midit.next();
        midLine = (int) LineMap_mid.get("LINE_ID");
        //标记第一个站和第二个站
        JudgeTransfer[0]=1;
        JudgeTransfer[1]=1;
        //迭代判断每个站点是否是换乘点
        for(int i=1;i<odStations.size()-1;i++){
            //获取到后一个站所属的线路名
            List rear=jdbcTemplate.queryForList(getLineSql+Integer.parseInt(odStations.get(i+1)));
            int rearLine;
            Iterator rearit = rear.iterator();
            Map LineMap_rear = (Map) rearit.next();
            rearLine = (int) LineMap_rear.get("LINE_ID");
            //判断当前站点是否是换乘点
            JudgeTransfer[i]=1;
            if(preLine==rearLine)
                JudgeTransfer[i]=0;
            //更新前、中、后站点所属线路
            preLine=midLine;
            midLine=rearLine;
        }
        JudgeTransfer[odStations.size()-1]=1;
        //换乘站标记完毕，将换乘站列表装入返回结果
        List<String> transferStationsList=new ArrayList<>();
        for(int i=0;i<odStations.size();i++){
            if(JudgeTransfer[i]==1){
                List cz_name=jdbcTemplate.queryForList(getLineSql+Integer.parseInt(odStations.get(i)));
                String Cz_name;
                Iterator czit = cz_name.iterator();
                Map czMap = (Map) czit.next();
                Cz_name = (String) czMap.get("CZ_NAME");
                int len = transferStationsList.size();
                if(len !=0 && Cz_name.equals(transferStationsList.get(len - 1)))
                    continue;
                else transferStationsList.add(Cz_name);
            }
        }
        for(int i=0;i<transferStationsList.size()-1;i++){
            List<String> transferResult=new ArrayList<>();
            transferResult.add(transferStationsList.get(i));
            transferResult.add(transferStationsList.get(i+1));
            TransferResult.add(transferResult);
        }
        return TransferResult;
    }

    @Override
    public List<String> getStationsById(List<String> odStations) {
        List<String> stations=new ArrayList<>();
        for(int i=0;i<odStations.size();i++){
            List cz_name=jdbcTemplate.queryForList(getLineSql+Integer.parseInt(odStations.get(i)));
            String Cz_name;
            Iterator czit = cz_name.iterator();
            Map czMap = (Map) czit.next();
            Cz_name = (String) czMap.get("CZ_NAME");
            int len = stations.size();
            //筛出重复站点
            if(len !=0 && Cz_name.equals(stations.get(len-1)))
                continue;
            else stations.add(Cz_name);
        }
        return stations;
    }
}

