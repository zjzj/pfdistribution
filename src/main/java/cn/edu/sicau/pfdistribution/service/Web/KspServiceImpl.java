package cn.edu.sicau.pfdistribution.service.Web;

import cn.edu.sicau.pfdistribution.Entity.KspSearchResult;
import cn.edu.sicau.pfdistribution.Entity.SWJTU_DTO;
import cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class KspServiceImpl implements KspService {

    @Autowired
    private MainDistribution mainDistribution;

    @Override
    public List<KspSearchResult> findKsp(SWJTU_DTO swjtu_dto) {

        List<KspSearchResult> kspSearchResults = new ArrayList<KspSearchResult>();
        Map resultMap=mainDistribution.getDistribution(swjtu_dto.getStartStation()+" "+swjtu_dto.getEndStation());
        Iterator it = resultMap.keySet().iterator();
        for(int i=1;it.hasNext();i++){
            List result= Arrays.asList(resultMap.get(it.next()));
            KspSearchResult kspSearchResult = new KspSearchResult(Integer.toString(i),result);
            kspSearchResults.add(kspSearchResult);
        }
        return kspSearchResults;
    }
}
