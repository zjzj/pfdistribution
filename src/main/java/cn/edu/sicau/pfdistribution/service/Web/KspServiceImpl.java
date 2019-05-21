package cn.edu.sicau.pfdistribution.service.Web;

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

    @Override
    public List<KspSearchResult> findKsp(SWJTU_DTO swjtu_dto) {

        List<KspSearchResult> kspSearchResults = new ArrayList<KspSearchResult>();
        Map resultMap=mainDistribution.getDistribution(swjtu_dto.getStartStation()+" "+swjtu_dto.getEndStation());
        int i=0;
        Set<Array> set=resultMap.keySet();
        for(List odStations:set){
            
            KspSearchResult kspSearchResult = new KspSearchResult(Integer.toString(i),odStations);
            kspSearchResults.add(kspSearchResult);
        }
        return kspSearchResults;
    }
}
