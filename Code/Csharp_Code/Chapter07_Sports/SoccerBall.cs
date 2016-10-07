using System;

public class SoccerBall : SpinProjectile
{
  private double temperature;

  public SoccerBall(double x0, double y0, double z0, 
             double vx0, double vy0, double vz0, double time,
             double mass, double area, double density, double Cd,
             double windVx, double windVy, double rx, double ry, 
             double rz, double omega, double radius, 
             double temperature) :
               base( x0, y0, z0, vx0, vy0, vz0, time, mass, area, 
                     density, Cd, windVx, windVy, rx, ry, rz, 
                     omega, radius) {

    this.temperature = temperature;
  }

  //  This properties are used to access the temperature field.
  public double Temperature {
    get {
      return temperature;
    }
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

    //  Compute the velocity magnitude
    double v = Math.Sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Compute the drag coefficient, which depends on
    //  the Reynolds number.
    double radius = this.Radius;
    double density = this.Density;
    double viscosity = 1.458e-6*Math.Pow(temperature,1.5)/
                       (temperature + 110.4);
    double Re = density*v*2.0*radius/viscosity;
    double cd;
    if ( Re < 1.0e+5 ) { 
      cd = 0.47;
    }
    else if ( Re > 1.35e+5 ) {
      cd = 0.22;
    }
    else {
      cd = 0.47 - 0.25*(Re - 1.0e+5)/35000.0;
    }

    //  Compute the total drag force and the dirctional
    //  drag components.
    double area = this.Area;
    double mass = this.Mass;
    double Fd = 0.5*density*area*cd*va*va;
    double Fdx = -Fd*vax/va;
    double Fdy = -Fd*vay/va;
    double Fdz = -Fd*vaz/va;

    //  Evaluate the Magnus force terms.
    double omega = this.Omega;
    double rx = this.Rx;
    double ry = this.Ry;
    double rz = this.Rz;
    double rotSpinRatio = Math.Abs(radius*omega/v);
    double Cl = 0.385*Math.Pow(rotSpinRatio, 0.25);

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
