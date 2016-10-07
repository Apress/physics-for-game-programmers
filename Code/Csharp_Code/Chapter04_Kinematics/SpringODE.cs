using System;

public class SpringODE : ODE
{
  private double mass;  //  mass at end of spring
  private double mu;    //  damping coefficient
  private double k;     //  spring constant
  private double x0;    //  initial spring deflection

  //  The SpringODE constructor calls the ODE constructor
  public SpringODE(double mass, double mu, double k,
                   double x0) : base(2) {
    //  Initialize fields declared in the class.
    this.mass = mass;
    this.mu = mu;
    this.k = k;
    this.x0 = x0;

    //  Set the initial conditions of the dependent
    //  variables.
    //  q[0] = vx
    //  q[1] = x;
    SetQ(0.0, 0);
    SetQ(x0, 1);
  } 

  //  These properties access the value of the fields
  //  declared in the class.
  public double Mu {
    get {
      return mu;
    }
    set {
      mu = value;
    }
  }

  public double Mass {
    get {
      return mass;
    }
    set {
      mass = value;
    }
  }

  public double K {
    get {
      return k;
    }
    set {
      k = value;
    }
  }

  public double X0 {
    get {
      return x0;
    }
    set {
      x0 = value;
    }
  }

  //  These methods return the spring location
  //  and velocity as computed by the ODE solver.
  public double GetVx() {
    return GetQ(0);
  }

  public double GetX() {
    return GetQ(1);
  }

  public double GetTime() {
    return this.S;
  }

  //  This method updates the velocity and position
  //  of the spring using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public void UpdatePositionAndVelocity(double dt) {
    ODESolver.RungeKutta4(this, dt);
  }

  //  The GetRightHandSide() method returns the right-hand
  //  sides of the two first-order damped spring ODE's
  //  q[0] = vx
  //  q[1] = x
  //  dq[0] = d(vx) = dt*(-mu*dxdt - k*x)/mass
  //  dq[1] = d(x) = dt*(v)
  public override double[] GetRightHandSide(double s, double[] q, 
             double[] deltaQ, double ds, double qScale) {

    double[] dq = new double[4];   // right-hand side values
    double[] newQ = new double[4]; // intermediate dependent
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
