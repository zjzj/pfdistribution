package cn.edu.sicau.pfdistribution.Utils;

public class ResultMsg {
    private int errcode;
    private String errmsg;
    private Object Data;

    public ResultMsg(int ErrCode, String ErrMsg, Object Data)
    {
        this.errcode = ErrCode;
        this.errmsg = ErrMsg;
        this.Data = Data;
    }
    public int getErrcode() {
        return errcode;
    }
    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    public Object getData() {
        return Data;
    }
    public void setData(Object Data) {
        this.Data = Data;
    }
}
