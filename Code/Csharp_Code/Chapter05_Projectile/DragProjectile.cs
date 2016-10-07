using System;

public class DragProjectile : SimpleProjectile
{
  private double mass;
  private double area;
  private double density;
  private double cd;

  public DragProjectile(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double cd) :
             base(x0, y0, z0, vx0, vy0, vz0, time) {

    //  Initialize variables declared in the DragProjectile class.
    this.mass = mass;
    this.area = area;
    this.density = density;
    this.cd = cd;
  }

  //  These properties are used to access the fields declared
  //  in the class.
  public double Mass {
    get {
      return mass;
    }
  }

  public double Area {
    get {
      return area;
    }
  }

  public double Density {
    get {
      return density;
    }
  }

  public double Cd {
    get {
      return cd;
    }
  }

  //  This method updates the velocity and location
  //  of the projectile using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public override void UpdateLocationAndVelocity(double dt) {
    ODESolver.RungeKutta4(this, dt);
  }

  //  The GetRightHandSide() method returns the right-hand
  //  sides of the six first-order projectile ODEs
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
    double[] newQ = new double[6];

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
    double v = Math.Sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Compute the total drag force.
    double Fd = 0.5*density*area*cd*v*v;

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
