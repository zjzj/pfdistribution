package cn.edu.sicau.pfdistribution.service.Web;

import cn.edu.sicau.pfdistribution.Entity.KspSearchResult;
import cn.edu.sicau.pfdistribution.Entity.SWJTU_DTO;

import java.util.List;

public interface KspService {
    public List<KspSearchResult> findKsp(SWJTU_DTO swjtu_dto);
}
