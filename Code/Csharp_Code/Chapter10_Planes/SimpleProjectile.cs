using System;

public class SimpleProjectile : ODE
{
  //  gravitational acceleration.
  public const double G = -9.81;

  public SimpleProjectile(double x0, double y0, double z0, 
                          double vx0, double vy0, double vz0,
                          double time) : base(6) {

    //  Load the initial position, velocity, and time 
    //  values into the s field and q array from the
    //  ODE class.
    this.S = time;
    SetQ(vx0,0);
    SetQ(x0, 1);
    SetQ(vy0,2);
    SetQ(y0, 3);
    SetQ(vz0,4);
    SetQ(z0, 5);
  }

  //  These methods return the location, velocity, 
  //  and time values
  public double GetVx() {
    return GetQ(0);
  }

  public double GetVy() {
    return GetQ(2);
  }

  public double GetVz() {
    return GetQ(4);
  }

  public double GetX() {
    return GetQ(1);
  }

  public double GetY() {
    return GetQ(3);
  }

  public double GetZ() {
    return GetQ(5);
  }

  public double GetTime() {
    return this.S;
  }

  //  This method updates the velocity and position
  //  of the projectile according to the gravity-only model.
  public virtual void UpdateLocationAndVelocity(double dt) {
    //  Get current location, velocity, and time values
    //  From the values stored in the ODE class.
    double time = this.S;
    double vx0 = GetQ(0);
    double x0 = GetQ(1);
    double vy0 = GetQ(2);
    double y0 = GetQ(3);
    double vz0 = GetQ(4);
    double z0 = GetQ(5);

    //  Update the xyz locations and the z-component
    //  of velocity. The x- and y-velocities don't change.
    double x = x0 + vx0*dt;
    double y = y0 + vy0*dt;
    double vz = vz0 + G*dt;
    double z = z0 + vz0*dt + 0.5*G*dt*dt;

    //  Update time;
    time = time + dt;

    //  Load new values into ODE arrays and fields.
    this.S = time;
    SetQ(x, 1);
    SetQ(y, 3);
    SetQ(vz,4);
    SetQ(z, 5);
  }

  //  Because SimpleProjectile extends the ODE class,
  //  it must implement the getRightHandSide method.
  //  In this case, the method returns a dummy array.
  public override double[] GetRightHandSide(double s, double[] q, 
               double[] deltaQ, double ds, double qScale) {
    return new double[1];
  }
}
