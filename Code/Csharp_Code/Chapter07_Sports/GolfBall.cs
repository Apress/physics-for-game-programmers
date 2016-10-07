using System;

public class GolfBall : SpinProjectile
{
  //  The GolfBall constructor simply calls the 
  //  SpinProjectile constructor.
  public GolfBall(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double Cd,
             double windVx, double windVy, double rx, double ry, 
             double rz, double omega, double radius):
              base( x0, y0, z0, vx0, vy0, vz0, time, mass, area, 
                    density, Cd, windVx, windVy, rx, ry, rz, 
                    omega, radius) {
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

    //  Compute the apparent velocities by subtracting
    //  the wind velocity components from the projectile
    //  velocity components.
    double vax = vx - this.WindVx;
    double vay = vy - this.WindVy;
    double vaz = vz;

    //  Compute the apparent velocity magnitude. The 1.0e-8 term
    //  ensures there won't be a divide by zero later on
    //  if all of the velocity components are zero.
    double va = Math.Sqrt(vax*vax + vay*vay + vaz*vaz) + 1.0e-8;

    //  Compute the total drag force and the dirctional
    //  drag components.
    double density = this.Density;
    double area = this.Area;
    double cd = this.Cd;
    double mass = this.Mass;
    double Fd = 0.5*density*area*cd*va*va;
    double Fdx = -Fd*vax/va;
    double Fdy = -Fd*vay/va;
    double Fdz = -Fd*vaz/va;

    //  Compute the velocity magnitude
    double v = Math.Sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Evaluate the Magnus force terms.
    double radius = this.Radius;
    double omega = this.Omega;
    double rx = this.Rx;
    double ry = this.Ry;
    double rz = this.Rz;
    double Cl = -0.05 + Math.Sqrt(0.0025 + 
                0.36*Math.Abs(radius*omega/v));
    double Fm = 0.5*density*area*Cl*v*v;
    double Fmx =  (vy*rz - ry*vz)*Fm/v;
    double Fmy = -(vx*rz - rx*vz)*Fm/v;
    double Fmz =  (vx*ry - rx*vy)*Fm/v;

    //  Compute the right-hand sides of the six ODEs
    dQ[0] = ds*(Fdx + Fmx)/mass;
    dQ[1] = ds*vx;
    dQ[2] = ds*(Fdy + Fmy)/mass;
    dQ[3] = ds*vy;
    dQ[4] = ds*(G + (Fdz + Fmz)/mass);
    dQ[5] = ds*vz;

    return dQ;
  }
}
