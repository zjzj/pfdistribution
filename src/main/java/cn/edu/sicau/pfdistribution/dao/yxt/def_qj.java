package cn.edu.sicau.pfdistribution.dao.yxt;

public class def_qj
{
  public int qjbh;
  public int czh1;
  public String czm1;
  public int czh2;
  public String czm2;
  public short sxx;//上下行标志
  public short qjbs;//区间闭塞＝2半自动＝1自动  2011.12.8加地铁模式下 1:固定闭塞 2:准移动闭塞 3:移动闭塞
  public short qjxz;//区间性质＝2单线＝1双线
  public String ljm;
  public double qjcd;

  public def_qj()
  {
      Initial();
  }

  public void Initial()
  {
     qjbh = 0;
     czh1 = 0;
     czm1 = "";
     czh2 = 0;
     czm2 = "";
     sxx = 0;
     qjbs = 0;
     qjxz = 0;
     ljm = "";
     qjcd = 0.0;
  }
}
