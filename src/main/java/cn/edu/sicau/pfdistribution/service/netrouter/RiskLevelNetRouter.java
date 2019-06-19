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

    private static void loadJNILibDynamically(String libName) throws IOException {// synchronized static

        String systemType = System.getProperty("os.name");
        String libExtension = (systemType.toLowerCase().indexOf("win")!=-1) ? ".dll" : ".so";

        String libFullName = libName + libExtension;

        String nativeTempDir = System.getProperty("java.io.tmpdir");

        InputStream in = null;
        BufferedInputStream reader = null;
        FileOutputStream writer = null;

        File extractedLibFile = new File(nativeTempDir+File.separator+libFullName);

        if(!extractedLibFile.exists()){
            try {
                if (RiskLevelNetRouter.class.getResource("/" + libFullName) == null) {
                    throw new IllegalStateException("Lib " + libFullName + "not found!");
                }
                in = RiskLevelNetRouter.class.getResourceAsStream("/" + libFullName);
                reader = new BufferedInputStream(in);
                writer = new FileOutputStream(extractedLibFile);

                byte[] buffer = new byte[1024];

                while (reader.read(buffer) > 0){
                    writer.write(buffer);
                    buffer = new byte[1024];
                }
            }
            finally {
                if(in!=null)
                    in.close();
                if(writer!=null)
                    writer.close();
            }
        }
        System.load(extractedLibFile.toString());
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
        loadJNILibDynamically("NetRouterCppClient");
        Address localaddr = new Address((byte) 8, (byte) 1, (short) 2, (byte) 2, (short) 6);
        List<Address> destAddrs = new LinkedList<Address>();
        Address destaddr1 = new Address((byte) 8, (byte) 1, (short) 1, (byte) 1, (short) 6);
        destAddrs.add(destaddr1);

        NetRouterClient netRouterClient = new NetRouterClient("Test", "10.0.140.213", 9003, "10.4.208.74", 9005, localaddr, "");
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
                   log.info("从riskNetRouter数据接" + message);
                   byte[] a = message.getBytes();
                   byte[] str = Arrays.copyOfRange(a,0,2);
                   /*byte[] str3 = Arrays.copyOfRange(a,2,a.length);
                   String data1 = new String(str3);*/
                   byte[] functionCode = {34,92};
                   JsonTransfer jsonTransfer = new JsonTransfer();
                    if (str==functionCode) {
                        byte[] str2 = Arrays.copyOfRange(a,2,a.length);
                        String data = new String(str2);
                        jsonTransfer.riskDataAnalysis(data);
                        log.info("riskNetRouter数据接收成功");
                        /*SendData(netRouterClient, destAddrs,"succeeds");*/
                    }
                }
            }
        }
    }
}
