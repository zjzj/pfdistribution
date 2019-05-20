package cn.edu.sicau.pfdistribution.service.kafka.receiver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

//接收分配结果
@Component
public class KafkaPfAllocationResultReceiver {

    Logger log = LoggerFactory.getLogger(getClass());

    private Gson gson = new GsonBuilder().create();

    @KafkaListener(topics = "PF-Allocation-Result")
    public void processMessage(String msg) {
        Map<String, String> result = gson.fromJson(msg, Map.class);
        log.info("从kafka读取处理结果数据:" + result);
    }

}