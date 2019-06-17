package cn.edu.sicau.pfdistribution.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.String;

/**
 * @author 谭华波
 */
@Service
public class TongHaoPathType implements Serializable{
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
