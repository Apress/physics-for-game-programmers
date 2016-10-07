public class SpinProjectile extends WindProjectile
{
  private double rx;     //  spin axis vector component
  private double ry;     //  spin axis vector component
  private double rz;     //  spin axis vector component
  private double omega;  //  angular velocity, m/s
  private double radius; //  sphere radius, m

  public SpinProjectile(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double Cd,
             double windVx, double windVy, double rx, double ry, 
             double rz, double omega, double radius) {
    //  Call the WindProjectile class constructor
    super(x0, y0, z0, vx0, vy0, vz0, time, mass, area, 
          density, Cd, windVx, windVy);

    //  Initialize variables declared in the SpinProjectile class.
    this.rx = rx;
    this.ry = ry;
    this.rz = rz;
    this.omega = omega;
    this.radius = radius;
  }

  //  These methods return the value of the fields
  //  declared in this class.
  public double getRx() {
    return rx;
  }

  public double getRy() {
    return ry;
  }

  public double getRz() {
    return rz;
  }

  public double getOmega() {
    return omega;
  }

  public double getRadius() {
    return radius;
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
    double vax = vx - getWindVx();
    double vay = vy - getWindVy();
    double vaz = vz;

    //  Compute the apparent velocity magnitude. The 1.0e-8 term
    //  ensures there won't be a divide by zero later on
    //  if all of the velocity components are zero.
    double va = Math.sqrt(vax*vax + vay*vay + vaz*vaz) + 1.0e-8;

    //  Compute the total drag force and the dirctional
    //  drag components.
    double Fd = 0.5*getDensity()*getArea()*getCd()*va*va;
    double Fdx = -Fd*vax/va;
    double Fdy = -Fd*vay/va;
    double Fdz = -Fd*vaz/va;

    //  Compute the velocity magnitude
    double v = Math.sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Evaluate the Magnus force terms.
    double Cl = radius*omega/v;
    double Fm = 0.5*getDensity()*getArea()*Cl*v*v;
    double Fmx =  (vy*rz - ry*vz)*Fm/v;
    double Fmy = -(vx*rz - rx*vz)*Fm/v;
    double Fmz =  (vx*ry - rx*vy)*Fm/v;

    //  Compute the right-hand sides of the six ODEs
    dQ[0] = ds*(Fdx + Fmx)/getMass();
    dQ[1] = ds*vx;
    dQ[2] = ds*(Fdy + Fmy)/getMass();
    dQ[3] = ds*vy;
    dQ[4] = ds*(G + (Fdz + Fmz)/getMass());
    dQ[5] = ds*vz;

    return dQ;
  }
}
