public class Gaussian
{
  public static void main(String args[]) {
    double sigma = 1.0;
    double mu = 10.0;

    double grp1 = 1.0/Math.sqrt(2.0*Math.PI*sigma*sigma);
    double grp2 = 2.0*sigma*sigma;
    double grp3;
    double x;
    double f;

    for(int i=0; i<81; ++i) {
      x = 5.0 + i*0.125;
      grp3 = -(x-mu)*(x-mu)/grp2;
      f = grp1*Math.exp(grp3);
      System.out.println(""+x+"  "+f);
    }
  }
}
