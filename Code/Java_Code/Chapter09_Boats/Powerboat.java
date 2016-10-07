public class Powerboat extends SimpleProjectile
{
  private String mode;
  private double planingSpeed;

  //  The Powerboat constructor calls the
  //  SimpleProjectile constructor and initializes
  //  the value of the mode and planingSpeed variables.
  public Powerboat(double x, double y, double z, double vx, 
                   double vy, double vz, double time, 
                   double planingSpeed) {
    super(x, y, z, vx, vy, vz, time);

    mode = "accelerating";   //  accelerating, cruising, or
                             //  decelerating
    this.planingSpeed = planingSpeed;
  }

  //  These methods access or change the value of the
  //  mode and planingSpeed fields.

  public String getMode() {
    return mode;
  }

  public void setMode(String value) {
    mode = value;
  }

  public double getPlaningSpeed() {
    return planingSpeed;
  }

  public void setPlaningSpeed(double value) {
    planingSpeed = value;
  }

  //  This method updates the velocity and location
  //  of the boat using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public void updateLocationAndVelocity(double dt) {
    ODESolver.rungeKutta4(this, dt);
  }

  //  The getRightHandSide() method returns the right-hand
  //  sides of the two first-order ODEs. The Powerboat
  //  implementation of this method does nothing. It is
  //  meant to be overridden by subclasses of Powerboat.
  //  q[0] = vx = dxdt
  //  q[1] = x
  //  q[2] = vy = dydt
  //  q[3] = y
  //  q[4] = vz = dzdt
  //  q[5] = z
  public double[] getRightHandSide(double s, double q[], 
                              double deltaQ[], double ds,
                              double qScale) {
    double dQ[] = new double[6];

    dQ[0] = 0.0;
    dQ[1] = 0.0;
    dQ[2] = 0.0;
    dQ[3] = 0.0;
    dQ[4] = 0.0;
    dQ[5] = 0.0;

    return dQ;
  }
}