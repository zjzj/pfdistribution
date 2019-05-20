package cn.edu.sicau.pfdistribution.service.Web;

import cn.edu.sicau.pfdistribution.Entity.KspSearchResult;
import cn.edu.sicau.pfdistribution.Entity.SWJTU_DTO;
import lombok.var;
import org.springframework.stereotype.Service;
import scala.Array;

import java.util.ArrayList;
import java.util.List;
@Service
public class KspServiceImpl implements KspService {
    @Override
    public List<KspSearchResult> findKsp(SWJTU_DTO swjtu_dto) {
        List<String> a=new ArrayList();
        List<String> b=new ArrayList();
        a.add("北京");
        a.add("上海");
        a.add("广州");
        b.add("重庆");
        b.add("成都");
        b.add("深圳");
        List<List<String>> KspResult = new ArrayList();
        KspResult.add(a);
        KspResult.add(b);
        List<KspSearchResult> kspSearchResults = new ArrayList<KspSearchResult>();
        for (int i = 1; i <= KspResult.size(); i++) {
            KspSearchResult kspSearchResult = new KspSearchResult(Integer.toString(i), KspResult.get(i-1));
            kspSearchResults.add(kspSearchResult);
        }
        return kspSearchResults;
    }
}
