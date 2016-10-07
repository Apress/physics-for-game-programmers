public class TestGasTank
{
  public static void main(String args[]) {
    //  Create an aluminum gas tank
    double thickness = 0.025;
    double diffusivity = 9.975e-5;
    double initialT = 300.0;
    double boundaryT = 1000.0;

    GasTank tank = 
       new GasTank(thickness, diffusivity, initialT, boundaryT);

    //  Compute the temperature at the inner tank wall 
    //  at several time values.
    double time;
    double temperature;

    for(int i=0; i<81; ++i) {
      time = i*0.1;
      temperature = tank.getTemperature(thickness, time);

      System.out.println(""+time+"  "+(float)temperature);
    }
  }
}
