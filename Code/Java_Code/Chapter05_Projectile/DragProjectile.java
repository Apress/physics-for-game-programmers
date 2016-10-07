public class DragProjectile extends SimpleProjectile
{
  private double mass;
  private double area;
  private double density;
  private double Cd;

  public DragProjectile(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double Cd) {
    //  Call the SimpleProjectile class constructor
    super(x0, y0, z0, vx0, vy0, vz0, time);

    //  Initialize variables declared in the DragProjectile class.
    this.mass = mass;
    this.area = area;
    this.density = density;
    this.Cd = Cd;
  }

  //  These methods return the value of the fields
  //  declared in this class.
  public double getMass() {
    return mass;
  }

  public double getArea() {
    return area;
  }

  public double getDensity() {
    return density;
  }

  public double getCd() {
    return Cd;
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

    //  Compute the velocity magnitude. The 1.0e-8 term
    //  ensures there won't be a divide by zero later on
    //  if all of the velocity components are zero.
    double v = Math.sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Compute the total drag force.
    double Fd = 0.5*density*area*Cd*v*v;

    //  Compute the right-hand sides of the six ODEs
    dQ[0] = -ds*Fd*vx/(mass*v);
    dQ[1] = ds*vx;
    dQ[2] = -ds*Fd*vy/(mass*v);
    dQ[3] = ds*vy;
    dQ[4] = ds*(G - Fd*vz/(mass*v));
    dQ[5] = ds*vz;

    return dQ;
  }
}
