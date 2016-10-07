public class InverseNormal
{
  public static void main(String args[]) {
    double grp1;
    double grp2;
    double x = 0.0;
    double f;
    double t;
    double fshift;

    for(int i=0; i<201; ++i) {
      t = Math.sqrt( Math.log(1.0/(x*x)) );
      grp1 = 2.515517 + 0.802853*t + 0.010328*t*t;
      grp2 = 1.0 + 1.432788*t + 0.189269*t*t + 
             0.001308*t*t*t;
      f = -t + grp1/grp2;

      fshift = f + 10.0;
      System.out.println(""+x+"  "+t+"  "+f+"  "+fshift);
      x += 0.005;
    }
  }
}
