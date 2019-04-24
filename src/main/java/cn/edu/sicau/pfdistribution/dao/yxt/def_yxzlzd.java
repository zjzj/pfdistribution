package cn.edu.sicau.pfdistribution.dao.yxt;

public class def_yxzlzd
{
    public short    yxzlh;
    public int      xlh;
    public String   xlm;

    int             qjh;
    short           yxsj;//运行时间
    short           qcfj;//起车附加
    short           tcfj;//停车附加

    public def_yxzlzd()
    {
        Initial();
    }
    public void Initial()
    {
        yxzlh = 0;
        xlh = 0;
        xlm = "";
        qjh = 0;
        yxsj = 0;
        qcfj = 0;
        tcfj = 0;
    }
}
