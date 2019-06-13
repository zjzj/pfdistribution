package cn.edu.sicau.pfdistribution.service.netrouter;

import NetRouterClient.Address;
import NetRouterClient.NetRouterClient;
import NetRouterClient.RecvMessage;
import NetRouterClient.SendMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
@Component
@Service
public class RiskLevelNetRouter {
    Logger log = LoggerFactory.getLogger(StationAndSectionNetRouter.class);

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

    private boolean SendData(NetRouterClient netClient, List<Address> f_list, String data) {
        SendMessage f_msg = new SendMessage(f_list,data);
        if (!netClient.sendMessage(f_msg)) {
            System.out.println("Send fail");
            return false;
        }
        System.out.println("Risk Send suc");
        return true;
    }

    @Async
    public void receiver() throws Exception {
        loadJNILibDynamically();
        Address localaddr = new Address((byte) 8, (byte) 1, (short) 2, (byte) 2, (short) 6);
        List<Address> destAddrs = new LinkedList<Address>();
        Address destaddr1 = new Address((byte) 8, (byte) 1, (short) 1, (byte) 1, (short) 6);
        destAddrs.add(destaddr1);
        String reginfo =
                "<in_condition>\n"+
                        "<rec>\n"+
                        "<protocol418_condition>\n"+
                        "<type_func>0x01,0x01</type_func>\n"+
                        "<type_func>0x01,0x01</type_func>\n"+
                        "</protocol418_condition>\n"+
                        "</rec>\n"+
                        "</in_condition>\n";



        NetRouterClient netRouterClient = new NetRouterClient("Test", "10.0.140.213", 9003, "10.2.55.51", 9005, localaddr, reginfo);
        while (!netRouterClient.start()) {
            System.out.println("RiskLevelNetRouter Start fails.");
            Thread.sleep(10);
        }
        System.out.println("RiskLevelNetRouter Start succeeds.");

//        SendData(netRouterClient, destAddrs,args);

        while (true) {
            if (netRouterClient.isNet1Connected() || netRouterClient.isNet2Connected()) {
                RecvMessage recvMessage = new RecvMessage();
                if (netRouterClient.receiveBlockMessage(recvMessage)) {
                   String message = recvMessage.getMessage();
                   byte[] a = message.getBytes();
                   byte[] str = Arrays.copyOfRange(a,0,2);
                   System.out.println(recvMessage.getMessage());
                   log.info("从riskNetRouter数据接" + message);
                   String functionCode = new String(str);
                   JsonTransfer jsonTransfer = new JsonTransfer();
                    if (functionCode=="11") {
                        byte[] str2 = Arrays.copyOfRange(a,2,a.length);
                        String data = new String(str2);
                        jsonTransfer.riskDataAnalysis(data);
                        log.info("从riskNetRouter数据接收成功" + message);
                        SendData(netRouterClient, destAddrs,"succeeds");
                    }
                }
            }
        }
    }
}
