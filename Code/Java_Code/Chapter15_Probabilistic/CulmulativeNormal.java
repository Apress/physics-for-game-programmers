public class CulmulativeNormal
{
  public static void main(String args[]) {
    double sigma = 1.0;
    double mu = 0.0;

    double grp1 = 1.0/Math.sqrt(2.0*Math.PI*sigma*sigma);
    double grp2 = 2.0*sigma*sigma;
    double grp3;
    double x = -4.0;
    double f;
    double t;
    double p;
    double xshift;
    double xtmp;

    for(int i=0; i<81; ++i) {
      grp3 = -(x-mu)*(x-mu)/grp2;
      f = grp1*Math.exp(grp3);

      if ( x < 0.0 ) {
        xtmp = -x;
      }
      else {
        xtmp = x;
      }

      t = 1.0/(1.0 + 0.33267*xtmp);
      p = 1.0 - f*(0.4361836*t - 0.1201676*t*t +
          0.9372980*t*t*t);
      if ( x < 0.0 ) {
        p = 1.0 - p;
      }

      xshift = x + 10.0;

      System.out.println(""+xshift+"  "+f+"  "+p);
      x += 0.1;
    }
  }
}
