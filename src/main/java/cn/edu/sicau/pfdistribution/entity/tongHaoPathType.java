package cn.edu.sicau.pfdistribution.entity;

import org.springframework.stereotype.Service;
import java.lang.String;

/**
 * @author 谭华波
 */
@Service
public class tongHaoPathType {
    private String path;
    private String passengers;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }
}
