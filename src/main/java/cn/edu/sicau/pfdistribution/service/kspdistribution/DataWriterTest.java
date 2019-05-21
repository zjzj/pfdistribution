package cn.edu.sicau.pfdistribution.service.kspdistribution;


import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Service
public class DataWriterTest {
    public void write(Map<String, String> args) {
        try {

            File file = new File("G:/工作室/铁路客流预测/distributionTest.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (String key : args.keySet()) {
                bw.write(key + ">>>>>>>" + args.get(key));
            }
            bw.close();

//            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

