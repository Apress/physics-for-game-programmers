public class SampleTrajectory 
{
  public static void main(String args[]) {
    double t = 0.0;
    double g = -9.81;
    double z;
    double v;
    double z0 = 0.0;
    double mass = 2.0;
    double force = mass*g;

    for(int i=0; i<51; ++i) {
      v = g*t;
      z = z0 + 0.5*g*t*t;
      System.out.println(""+t+"  "+force+"  "+
        g+"  "+v+"  "+z);
      t += 0.05;
    }
  }
}