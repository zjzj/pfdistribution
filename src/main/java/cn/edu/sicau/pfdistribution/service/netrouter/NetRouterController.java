package cn.edu.sicau.pfdistribution.service.netrouter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class NetRouterController {
    @Autowired
    private IntervalDistributionNetRouter intervalDistributionNetRouter;
    @Autowired
    private RiskLevelNetRouter riskLevelNetRouter;
    @Autowired
    private StationAndSectionNetRouter stationAndSectionNetRouter;
    @PostConstruct
    public void StartAllNetRouter() throws Exception {
        intervalDistributionNetRouter.receiver();
        riskLevelNetRouter.receiver();
        stationAndSectionNetRouter.receiver();
    }
}
