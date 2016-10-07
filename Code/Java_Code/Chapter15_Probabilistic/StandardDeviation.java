public class StandardDeviation
{
  public static void main(String args[]) {
    double mu = 10.0;
    double x;

    double sigma = 0.5;
    double grp1a = 1.0/Math.sqrt(2.0*Math.PI*sigma*sigma);
    double grp2a = 2.0*sigma*sigma;
    double grp3a;
    double fa;

    sigma = 1.0;
    double grp1b = 1.0/Math.sqrt(2.0*Math.PI*sigma*sigma);
    double grp2b = 2.0*sigma*sigma;
    double grp3b;
    double fb;

    sigma = 2.0;
    double grp1c = 1.0/Math.sqrt(2.0*Math.PI*sigma*sigma);
    double grp2c = 2.0*sigma*sigma;
    double grp3c;
    double fc;

    for(int i=0; i<81; ++i) {
      x = 5.0 + i*0.125;
      grp3a = -(x-mu)*(x-mu)/grp2a;
      fa = grp1a*Math.exp(grp3a);

      grp3b = -(x-mu)*(x-mu)/grp2b;
      fb = grp1b*Math.exp(grp3b);

      grp3c = -(x-mu)*(x-mu)/grp2c;
      fc = grp1c*Math.exp(grp3c);

      System.out.println(""+x+"  "+fa+"  "+fb+"  "+fc);
    }
  }
}
