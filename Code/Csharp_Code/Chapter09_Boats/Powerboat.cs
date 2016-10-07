using System;

public class Powerboat : SimpleProjectile
{
  private string mode;
  private double planingSpeed;

  //  The Powerboat constructor calls the
  //  SimpleProjectile constructor and initializes
  //  the value of the mode and planingSpeed variables.
  public Powerboat(double x, double y, double z, double vx, 
                   double vy, double vz, double time, 
                   double planingSpeed) :
                     base(x, y, z, vx, vy, vz, time) {

    mode = "accelerating";   //  accelerating, cruising, or
                             //  decelerating
    this.planingSpeed = planingSpeed;
  }

  //  These properties are used to access the fields declared
  //  in the class.
  public string Mode {
    get {
      return mode;
    }
    set {
      mode = value;
    }
  }

  public double PlaningSpeed {
    get {
      return planingSpeed;
    }
  }

  //  This method updates the velocity and location
  //  of the projectile using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public override void UpdateLocationAndVelocity(double dt) {
    ODESolver.RungeKutta4(this, dt);
  }

  //  The GetRightHandSide() method returns the right-hand
  //  sides of the six first-order ODEs. The Powerboat
  //  implementation of this method does nothing. It is
  //  meant to be overridden by subclasses of Powerboat.
  //  q[0] = vx = dxdt
  //  q[1] = x
  //  q[2] = vy = dydt
  //  q[3] = y
  //  q[4] = vz = dzdt
  //  q[5] = z
  public override double[] GetRightHandSide(double s, double[] q, 
                              double[] deltaQ, double ds,
                              double qScale) {
    double[] dQ = new double[6];

    dQ[0] = 0.0;
    dQ[1] = 0.0;
    dQ[2] = 0.0;
    dQ[3] = 0.0;
    dQ[4] = 0.0;
    dQ[5] = 0.0;

    return dQ;
  }
}
