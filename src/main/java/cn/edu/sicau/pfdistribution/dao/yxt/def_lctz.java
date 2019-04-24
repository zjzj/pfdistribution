package cn.edu.sicau.pfdistribution.dao.yxt;

public class def_lctz
{
  public int lcxh;
  public String cc;
  public String qzfd;
  public String dzdd;
  public int qzh;
  public String sfczm;
  public int btqzid;
  public String btqz;
  public int zzh;
  public String zdczm;
  public int btzzid;
  public String btzz;
  public String kxgl;

  public int[] ddsk;//到达时刻
  public int[] cfsk;//出发时刻
  public short[] yyts;//营业停时
  public short[] jsts;//技术停时
  public int[]   qjh;//区间号
  public short[] yxzlh;//运行种类号

  public def_lctz()
  {
    Initial();
  }

  public void Initial()
  {
    lcxh = 0;
    cc = "";
    qzfd = "";
    dzdd = "";
    qzh = 0;
    sfczm = "";
    btqzid = 0;
    btqz = "";
    zzh = 0;
    zdczm = "";
    btzzid = 0;
    btzz = "";
    kxgl = "";

    ddsk = new int[YxtMain.ljcd+1];//到达时刻
    cfsk = new int[YxtMain.ljcd+1];//出发时刻
    yyts = new short[YxtMain.ljcd+1];//营业停时
    jsts = new short[YxtMain.ljcd+1];//技术停时
    qjh = new int[YxtMain.ljcd+1];//区间号
    yxzlh = new short[YxtMain.ljcd+1];//运行种类号
  }
}
