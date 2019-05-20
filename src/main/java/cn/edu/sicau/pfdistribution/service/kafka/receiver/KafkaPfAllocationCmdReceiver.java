package cn.edu.sicau.pfdistribution.service.kafka.receiver;

import cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

//接收命令触发客流分配过程计算
@Component
public class KafkaPfAllocationCmdReceiver {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MainDistribution distribution;

    private Gson gson = new GsonBuilder().create();

    @KafkaListener(topics = "PF-Allocation-CMD")
    public void processMessage(String msg) {
        Map<String, String> message = gson.fromJson(msg, Map.class);
        log.info("从kafka读取数据" + message);
        distribution.triggerTask(message);
    }

}