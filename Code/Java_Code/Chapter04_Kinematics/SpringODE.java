public class SpringODE extends ODE
{
  private double mass;  //  mass at end of spring
  private double mu;    //  damping coefficient
  private double k;     //  spring constant
  private double x0;    //  initial spring deflection
  private double time;  //  independent variable

  //  SpringODE constructor.
  public SpringODE(double mass, double mu, double k,
                   double x0) {
    //  Call the ODE constructor indicating that there
    //  will be two coupled first-order ODEs.
    super(2);

    //  Initialize fields declared in the class.
    this.mass = mass;
    this.mu = mu;
    this.k = k;
    this.x0 = x0;
    time = 0.0;

    //  Set the initial conditions of the dependent
    //  variables.
    //  q[0] = vx
    //  q[1] = x;
    setQ(0.0, 0);
    setQ(x0, 1);
  } 

  //  These methods return field values
  public double getMass() {
    return mass;
  }

  public double getMu() {
    return mu;
  }

  public double getK() {
    return k;
  }

  public double getX0() {
    return x0;
  }


  //  These methods change field values
  public void setMass(double value) {
    mass = value;
    return;
  }

  public void setMu(double value) {
    mu = value;
    return;
  }

  public void setK(double value) {
    k = value;
    return;
  }

  public void setX0(double value) {
    x0 = value;
    return;
  }

  //  These methods return the spring location
  //  and velocity as computed by the ODE solver.
  public double getVx() {
    return getQ(0);
  }

  public double getX() {
    return getQ(1);
  }

  public double getTime() {
    return getS();
  }

  //  This method updates the velocity and position
  //  of the spring using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public void updatePositionAndVelocity(double dt) {
    ODESolver.rungeKutta4(this, dt);
  }

  //  The getRightHandSide() method returns the right-hand
  //  sides of the two first-order damped spring ODE's
  //  q[0] = vx
  //  q[1] = x
  //  dq[0] = d(vx) = dt*(-mu*dxdt - k*x)/mass
  //  dq[1] = d(x) = dt*(v)
  public double[] getRightHandSide(double s, double q[], 
             double deltaQ[], double ds, double qScale) {

    double dq[] = new double[4];   // right-hand side values
    double newQ[] = new double[4]; // intermediate dependent
                                   // variable values.

    //  Compute the intermediate values of the 
    //  dependent variables.
    for(int i=0; i<2; ++i) {
      newQ[i] = q[i] + qScale*deltaQ[i];
    }

    //  Compute right-hand side values.
    double G = -9.81;
    dq[0] = ds*G - ds*(mu*newQ[0] + k*newQ[1])/mass;
    dq[1] = ds*(newQ[0]);

    return dq;
  }
}
