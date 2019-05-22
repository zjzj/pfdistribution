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
        Map map = (Map) mainDistribution.getDistribution(swjtu_dto.getStartStation()+" "+swjtu_dto.getEndStation());
        Iterator it=map.keySet().iterator();
        for(int i=1;it.hasNext();i++){
            List<String> odstations=new ArrayList<String>();
            String odstation=it.next().toString();
            odstations=Arrays.asList(odstation.split(","));
            KspSearchResult kspSearchResult = new KspSearchResult(Integer.toString(i),odstations);
            kspSearchResults.add(kspSearchResult);
        }
        return kspSearchResults;
    }
}
