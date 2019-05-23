package cn.edu.sicau.pfdistribution.Utils;

import cn.edu.sicau.pfdistribution.entity.KspSearchResult;

import java.util.List;

public class ResultMsg {
    private int status;
    private String msg;
    private List<KspSearchResult> data;

    public ResultMsg(int status, String msg, List<KspSearchResult> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<KspSearchResult> getData() {
        return data;
    }

    public void setData(List<KspSearchResult> data) {
        this.data = data;
    }
}
