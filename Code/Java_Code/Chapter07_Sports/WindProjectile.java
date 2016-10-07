public class WindProjectile extends DragProjectile
{
  private double windVx;
  private double windVy;

  public WindProjectile(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double Cd,
             double windVx, double windVy) {
    //  Call the DragProjectile class constructor
    super(x0, y0, z0, vx0, vy0, vz0, time, mass, area, density, Cd);

    //  Initialize variables declared in the WindProjectile class.
    this.windVx = windVx;
    this.windVy = windVy;
  }

  //  These methods return the value of the fields
  //  declared in this class.
  public double getWindVx() {
    return windVx;
  }

  public double getWindVy() {
    return windVy;
  }

  //  This method updates the velocity and location
  //  of the projectile using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public void updateLocationAndVelocity(double dt) {
    ODESolver.rungeKutta4(this, dt);
  }

  //  The getRightHandSide() method returns the right-hand
  //  sides of the six first-order projectile ODEs
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
    double newQ[] = new double[6];

    //  Compute the intermediate values of the 
    //  dependent variables.
    for(int i=0; i<6; ++i) {
      newQ[i] = q[i] + qScale*deltaQ[i];
    }

    //  Declare some convenience variables representing
    //  the intermediate values of velocity.
    double vx = newQ[0];
    double vy = newQ[2];
    double vz = newQ[4];

    //  Compute the apparent velocities by subtracting
    //  the wind velocity components from the projectile
    //  velocity components.
    double vax = vx - windVx;
    double vay = vy - windVy;
    double vaz = vz;

    //  Compute the apparent velocity magnitude. The 1.0e-8 term
    //  ensures there won't be a divide by zero later on
    //  if all of the velocity components are zero.
    double va = Math.sqrt(vax*vax + vay*vay + vaz*vaz) + 1.0e-8;

    //  Compute the total drag force.
    double Fd = 0.5*getDensity()*getArea()*getCd()*va*va;

    //  Compute the right-hand sides of the six ODEs
    dQ[0] = -ds*Fd*vax/(getMass()*va);
    dQ[1] = ds*vx;
    dQ[2] = -ds*Fd*vay/(getMass()*va);
    dQ[3] = ds*vy;
    dQ[4] = ds*(G - Fd*vaz/(getMass()*va));
    dQ[5] = ds*vz;

    return dQ;
  }
}
