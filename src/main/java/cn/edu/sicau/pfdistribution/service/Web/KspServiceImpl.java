package cn.edu.sicau.pfdistribution.service.Web;

import cn.edu.sicau.pfdistribution.dao.oracle.OracleGetTransferStations;
import cn.edu.sicau.pfdistribution.entity.KspSearchResult;
import cn.edu.sicau.pfdistribution.entity.SWJTU_DTO;
import cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class KspServiceImpl implements KspService {

    @Autowired
    private MainDistribution mainDistribution;

    @Autowired
    private OracleGetTransferStations oracleGetTransferStations;

    @Override
    public List<KspSearchResult> findKsp(SWJTU_DTO swjtu_dto) {

        List<KspSearchResult> kspSearchResults = new ArrayList<KspSearchResult>();
        Map map = (Map) mainDistribution.getDistribution(swjtu_dto.getStartStation()+" "+swjtu_dto.getEndStation());
//        Map<String,Integer> map=new HashMap<>();
//        map.put("海峡路,南湖,四公里,南坪,南滨路,七星岗,两路口,牛角沱",0);
        Iterator it=map.keySet().iterator();
        for(int i=1;it.hasNext();i++){
            List<String> odstations=new ArrayList<String>();
            String odstation=it.next().toString();
            odstations=Arrays.asList(odstation.split(","));
            System.out.println(odstations);
            List<List<String>> transferstations=oracleGetTransferStations.getTransferStations(odstations);
            KspSearchResult kspSearchResult = new KspSearchResult(Integer.toString(i),odstations,transferstations);
            kspSearchResults.add(kspSearchResult);
        }
        return kspSearchResults;
    }
}
