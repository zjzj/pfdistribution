package cn.edu.sicau.pfdistribution.service.netrouter;


import NetRouterClient.Address;
import NetRouterClient.NetRouterClient;
import NetRouterClient.RecvMessage;
import NetRouterClient.SendMessage;
import cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import scala.Tuple2;


import java.io.*;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class IntervalDistributionNetRouter {

    Logger log = LoggerFactory.getLogger(StationAndSectionNetRouter.class);

    @Autowired
    private MainDistribution distribution;

    private Gson gson = new GsonBuilder().create();
    private static void loadJNILibDynamically() {
        try {
            System.setProperty("java.library.path", System.getProperty("java.library.path")
                    + ";.\\bin\\");
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);

            System.loadLibrary("NetRouterCppClient");
        } catch (Exception e) {
            // do nothing for exception
        }
    }

    private boolean SendData(NetRouterClient netClient, List<Address> f_list, Map<String, String> data) {

        SendMessage f_msg = new SendMessage(f_list, gson.toJson(data));
        if (!netClient.sendMessage(f_msg)) {
            System.out.println("Send fail");
            return false;
        }
        System.out.println("Interval Send suc");
        return true;
    }


    @Async
    public void receiver() throws Exception {
        loadJNILibDynamically();
        Address localaddr = new Address((byte) 8, (byte) 1, (short) 2, (byte) 2, (short) 6);
        List<Address> destAddrs = new LinkedList<Address>();
        Address destaddr1 = new Address((byte) 8, (byte) 1, (short) 2, (byte) 1, (short) 6);
        destAddrs.add(destaddr1);

        NetRouterClient netRouterClient = new NetRouterClient("Test", "10.0.140.213", 9003, "10.2.55.51", 9005, localaddr, "");
        while (!netRouterClient.start()) {
            System.out.println("IntervalDistributionNetRouter Start fails.");
            Thread.sleep(10);
        }
        System.out.println("IntervalDistributionNetRouter Start succeeds.");

//        SendData(netRouterClient, destAddrs,args);

        while (true) {
            if (netRouterClient.isNet1Connected() || netRouterClient.isNet2Connected()) {
                RecvMessage recvMessage = new RecvMessage();
                if (netRouterClient.receiveBlockMessage(recvMessage)) {
                    try {
                        Map<String, String> message = gson.fromJson(recvMessage.getMessage(), new TypeToken<Map<String, String>>() {
                        }.getType());
                        log.info("从NetRouter读取数据" + message);
                        if (message != null) {
                            final List<Tuple2<String, String>> list = new java.util.ArrayList<>(message.size());
                            for (final Map.Entry<String, String> entry : message.entrySet()) {
                                list.add(Tuple2.apply(entry.getKey(), entry.getValue()));
                            }
                            final scala.collection.Seq<Tuple2<String, String>> seq = scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
                            scala.collection.immutable.Map<String, String> abc = (scala.collection.immutable.Map<String, String>) scala.collection.immutable.Map$.MODULE$.apply(seq);
                            log.info("数据接受成功");
                            SendData(netRouterClient, destAddrs, distribution.intervalTriggerTask(abc));
                        }
                    }catch (Exception e){
                        System.out.println("处理出错");
                    }
                }
            }
        }
    }
}
