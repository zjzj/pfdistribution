package cn.edu.sicau.pfdistribution.dao.mysqlsave;

public interface regionsava {
    //保存路径
    public void ksprouteadd(String ksp);
    //保存路径和分配的人数
    public void kspregionadd(String route,double passenger);
    //保存区间和分配的人数
    public void odregion(String kspregion,double passenger);
}
