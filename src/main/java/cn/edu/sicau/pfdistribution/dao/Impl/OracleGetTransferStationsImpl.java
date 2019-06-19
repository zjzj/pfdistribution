package cn.edu.sicau.pfdistribution.dao.Impl;

import cn.edu.sicau.pfdistribution.dao.oracle.OracleGetTransferStations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class OracleGetTransferStationsImpl implements OracleGetTransferStations {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    public String getLineListSql="select * from dic_linestation where CZ_NAME=";
    public String getSection1ListSql="SELECT * FROM dic_linesection WHERE CZ1_NAME=";
    public String getSection2ListSql="SELECT * FROM dic_linesection WHERE CZ2_NAME=";

    @Override
    public List<List<String>> getTransferStations(List<String> odStations) {
        int JudgeTransfer[]=new int[odStations.size()];
        List<List<String>> TransferResult=new ArrayList<>();
        //初始化前一个站点所属线路List和当前站点所属线路List
        List pre=jdbcTemplate.queryForList(getLineListSql+"'"+odStations.get(0)+"'");
        List<Integer> preLines=new ArrayList<>();
        Iterator preit = pre.iterator();
        while(preit.hasNext()) {
            Map LineMap = (Map) preit.next();
            Integer line_id = (int) LineMap.get("LINE_ID");
            preLines.add(line_id);
        }
        List mid=jdbcTemplate.queryForList(getLineListSql+"'"+odStations.get(1)+"'");
        List<Integer> midLines=new ArrayList<>();
        Iterator midit = mid.iterator();
        while(midit.hasNext()) {
            Map LineMap = (Map) midit.next();
            Integer line_id = (int) LineMap.get("LINE_ID");
            midLines.add(line_id);
        }
        //确定前一个站所属线路
        int JudgeOrnot=0;
        for(int i=0;i<preLines.size();i++){
            for(int a=0;a<midLines.size();a++){
                if(preLines.get(i).equals(midLines.get(a))){
                    List<String> sectionResult=new ArrayList<>();
                    //获取在当前定位到的线路下，前一个站点的相邻站点List并判断当前站点是否在这个List里面，
                    //如果在，则说明当前定位到的线路就是前一个站点所在线路，否则不是，继续迭代
                    List sections1=jdbcTemplate.queryForList(getSection1ListSql+"'"+odStations.get(0)+"'"+" and LINE_ID="+preLines.get(i));
                    List<String> sectionList1=new ArrayList<>();
                    Iterator sectionit1 = sections1.iterator();
                    Map LineMap1 = (Map) sectionit1.next();
                    String section1 = (String)LineMap1.get("CZ2_NAME");
                    sectionResult.add(section1);
                    List sections2=jdbcTemplate.queryForList(getSection2ListSql+"'"+odStations.get(0)+"'"+" and LINE_ID="+preLines.get(i));
                    List<String> sectionList2=new ArrayList<>();
                    Iterator sectionit2 = sections2.iterator();
                    Map LineMap2 = (Map) sectionit2.next();
                    String section2 = (String)LineMap2.get("CZ1_NAME");
                    sectionResult.add(section2);
                    for(int b=0;b<sectionResult.size();b++){
                        if(odStations.get(1).equals(sectionResult.get(b))){
                            preLines.clear();
                            preLines.add(midLines.get(a));
                            JudgeOrnot=1;
                            break;
                        }
                    }
                }
                if(JudgeOrnot==1)
                    break;
            }
            if(JudgeOrnot==1)
                break;
        }
        //标记起点和第一个站点
        JudgeTransfer[0]=1;
        JudgeTransfer[1]=1;
        //迭代判断每个站点是否是换乘点
        for(int i=1;i<odStations.size()-1;i++){
            //获取到后一个站所属的线路List
            List rear=jdbcTemplate.queryForList(getLineListSql+"'"+odStations.get(i+1)+"'");
            List<Integer> rearLines=new ArrayList<>();
            Iterator rearit = rear.iterator();
            while(rearit.hasNext()) {
                Map LineMap = (Map) rearit.next();
                Integer line_id = (int) LineMap.get("LINE_ID");
                rearLines.add(line_id);
            }
            //判断当前站点是否是换乘点
            JudgeTransfer[i]=1;
            for(int a=0;a<rearLines.size();a++){
                if(preLines.get(0).equals(rearLines.get(a))){
                    JudgeTransfer[i]=0;
                }
            }
            //更新前、中、后所属线路List
            JudgeOrnot=0;
            for(int c=0;c<midLines.size();c++){
                for(int d=0;d<rearLines.size();d++){
                    if(midLines.get(c).equals(rearLines.get(d))){
                        List<String> sectionResult=new ArrayList<>();
                        //获取在当前定位到的线路下，前一个站点的相邻站点List并判断当前站点是否在这个List里面，
                        //如果在，则说明当前定位到的线路就是前一个站点所在线路，否则不是，继续迭代
                        List sections1=jdbcTemplate.queryForList(getSection1ListSql+"'"+odStations.get(i)+"'"+" and LINE_ID="+midLines.get(c));
                        List<String> sectionList1=new ArrayList<>();
                        Iterator sectionit1 = sections1.iterator();
                        Map LineMap1 = (Map) sectionit1.next();
                        String section1 = (String)LineMap1.get("CZ2_NAME");
                        sectionResult.add(section1);
                        List sections2=jdbcTemplate.queryForList(getSection2ListSql+"'"+odStations.get(i)+"'"+" and LINE_ID="+midLines.get(c));
                        List<String> sectionList2=new ArrayList<>();
                        Iterator sectionit2 = sections2.iterator();
                        Map LineMap2 = (Map) sectionit2.next();
                        String section2 = (String)LineMap2.get("CZ1_NAME");
                        sectionResult.add(section2);
                        for(int b=0;b<sectionResult.size();b++){
                            if(odStations.get(i+1).equals(sectionResult.get(b))){
                                midLines.clear();
                                midLines.add(rearLines.get(d));
                                JudgeOrnot=1;
                                break;
                            }
                        }
                    }
                    if(JudgeOrnot==1)
                        break;
                }
                if(JudgeOrnot==1)
                    break;
            }
            preLines.clear();
            preLines.add(midLines.get(0));
            midLines.clear();
            for(int e=0;e<rearLines.size();e++){
                midLines.add(rearLines.get(e));
            }
        }
        //标记终点
        JudgeTransfer[odStations.size()-1]=1;
        //换乘站标记完毕，将换乘站列表装入返回结果
        List<String> transferStationsList=new ArrayList<>();
        for(int i=0;i<odStations.size();i++){
            if(JudgeTransfer[i]==1){
                transferStationsList.add(odStations.get(i));
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
}