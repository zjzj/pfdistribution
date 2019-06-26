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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//@Component
//@Service
public class IntervalDistributionNetRouter {
//
//    Logger log = LoggerFactory.getLogger(StationAndSectionNetRouter.class);
//
//    @Autowired
//    private MainDistribution distribution;
//    private Gson gson = new GsonBuilder().create();
//    private static void loadJNILibDynamically(String libName) throws IOException {// synchronized static
//
//        String systemType = System.getProperty("os.name");
//        String libExtension = (systemType.toLowerCase().indexOf("win")!=-1) ? ".dll" : ".so";
//
//        String libFullName = libName + libExtension;
//
//        String nativeTempDir = System.getProperty("java.io.tmpdir");
//
//        InputStream in = null;
//        BufferedInputStream reader = null;
//        FileOutputStream writer = null;
//
//        File extractedLibFile = new File(nativeTempDir+File.separator+libFullName);
//
//        if(!extractedLibFile.exists()){
//            try {
//                if (IntervalDistributionNetRouter.class.getResource("/" + libFullName) == null) {
//                    throw new IllegalStateException("Lib " + libFullName + "not found!");
//                }
//                in = IntervalDistributionNetRouter.class.getResourceAsStream("/" + libFullName);
//                reader = new BufferedInputStream(in);
//                writer = new FileOutputStream(extractedLibFile);
//
//                byte[] buffer = new byte[1024];
//
//                while (reader.read(buffer) > 0){
//                    writer.write(buffer);
//                    buffer = new byte[1024];
//                }
//            }
//            finally {
//                if(in!=null)
//                    in.close();
//                if(writer!=null)
//                    writer.close();
//            }
//        }
//        System.load(extractedLibFile.toString());
//    }
//
//    private boolean SendData(NetRouterClient netClient, List<Address> f_list, Map<String, String> data) {
//
//        SendMessage f_msg = new SendMessage(f_list, gson.toJson(data));
//        if (!netClient.sendMessage(f_msg)) {
//            log.info("Send fail");
//            return false;
//        }
//        log.info("Interval Send suc");
//        return true;
//    }
//
//
//    @Async
//    public void receiver() throws Exception {
//        loadJNILibDynamically("NetRouterCppClient");
//        Address localaddr = new Address((byte) 8, (byte) 1, (short) 2, (byte) 2, (short) 6);
//        List<Address> destAddrs = new LinkedList<Address>();
//        Address destaddr1 = new Address((byte) 8, (byte) 1, (short) 2, (byte) 1, (short) 6);
//        destAddrs.add(destaddr1);
//
//        NetRouterClient netRouterClient = new NetRouterClient("Test", "192.168.43.82", 9003, "192.168.69.108", 9005, localaddr, "");
//        while (!netRouterClient.start()) {
//            log.info("IntervalDistributionNetRouter Start fails.");
//            Thread.sleep(10);
//        }
//        log.info("IntervalDistributionNetRouter Start succeeds.");
//
////        SendData(netRouterClient, destAddrs,args);
//
//        while (true) {
//            if (netRouterClient.isNet1Connected() || netRouterClient.isNet2Connected()) {
//                RecvMessage recvMessage = new RecvMessage();
//                if (netRouterClient.receiveBlockMessage(recvMessage)) {
//                    try {
//                    Map<String, String> message = gson.fromJson(recvMessage.getMessage(), new TypeToken<Map<String, String>>() {
//                    }.getType());
//                    if (message != null) {
//                            final List<Tuple2<String, String>> list = new java.util.ArrayList<>(message.size());
//                            for (final Map.Entry<String, String> entry : message.entrySet()) {
//                                list.add(Tuple2.apply(entry.getKey(), entry.getValue()));
//                            }
//                            final scala.collection.Seq<Tuple2<String, String>> seq = scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toSeq();
//                            scala.collection.immutable.Map<String, String> abc = (scala.collection.immutable.Map<String, String>) scala.collection.immutable.Map$.MODULE$.apply(seq);
//                            log.info("IntervalDistributionNetRouter数据接受成功");
//                            distribution.intervalTriggerTask(abc);
//                            SendData(netRouterClient, destAddrs, null);
//                    }
//                    }catch (Exception e){
//                        log.info("IntervalDistributionNetRouter数据不对应");
//                    }
//                }
//            }
//        }
//    }
}
        /*try {
        }catch (Exception e){
        System.out.println("IntervalDistributionNetRouter处理出错");
        }*/
