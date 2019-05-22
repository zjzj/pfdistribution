package cn.edu.sicau.pfdistribution.controller;

import cn.edu.sicau.pfdistribution.entity.KspData;
import cn.edu.sicau.pfdistribution.entity.KspSearchResult;
import cn.edu.sicau.pfdistribution.entity.SWJTU_DTO;
import cn.edu.sicau.pfdistribution.Utils.ResultMsg;
import cn.edu.sicau.pfdistribution.Utils.ResultStatusCode;
import cn.edu.sicau.pfdistribution.service.Web.KspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modularSWJTU")
public class KspController {

    @Autowired
    private KspService kspService;

    @PostMapping("/tripPlan.do")
    public Object findKsp(SWJTU_DTO swjtu_dto){
        List<KspSearchResult> KspResult=kspService.findKsp(swjtu_dto);
        KspData kspData=new KspData(KspResult);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(),kspData);
        return resultMsg;
    }
}
