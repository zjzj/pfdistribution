package cn.edu.sicau.pfdistribution.service.kafka.controller;

import cn.edu.sicau.pfdistribution.service.kafka.receiver.KafkaReceiver;
import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/")
@RestController
public class HelloKafka {

    @Autowired
    KafkaSender sender;

    @Autowired
    KafkaReceiver receiver;

    @RequestMapping("")
    public String hello(String message){

        sender.send(message);

        System.out.println("存数据完成");

        return "成功了";
    }
}
