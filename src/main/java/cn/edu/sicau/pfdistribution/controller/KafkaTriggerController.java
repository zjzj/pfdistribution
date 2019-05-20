package cn.edu.sicau.pfdistribution.controller;

import cn.edu.sicau.pfdistribution.service.kafka.receiver.KafkaPfAllocationResultReceiver;
import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@RestController
public class KafkaTriggerController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    KafkaPfAllocationMessageSender sender;

    @Autowired
    KafkaPfAllocationResultReceiver receiver;

    @RequestMapping("kafkaCmdTrigger")
    public String trigger(String command, String startTime, String endTime, String predictionInterval){
        Map message = new HashMap();
        message.put("command",command);
        message.put("startTime",startTime);
        message.put("endTime",startTime);
        message.put("predictionInterval",predictionInterval);
;
        sender.send("PF-Allocation-CMD", message);

        log.info("Message send: " + "PF-Allocation-CMD" + ": " + message );

        return "\"Message send: \" + \"PF-Allocation-CMD\" + \": \" + message ";
    }
}
