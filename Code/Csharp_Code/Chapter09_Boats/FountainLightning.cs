using System;

public class FountainLightning : Powerboat
{
  //  The FountainLightning constructor calls the
  //  SimpleProjectile constructor and initializes
  //  the value of the mode variable.
  public FountainLightning(double x, double y, double z, 
                          double vx, double vy, double vz,
                          double time, double planingSpeed) :
                  base(x, y, z, vx, vy, vz, time, planingSpeed) {
  }

  //  The GetRightHandSide() method returns the right-hand
  //  sides of the six first-order ODEs. In this 
  //  simulation, the boat travels in the x-direction only.
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

    //  Compute the right-hand sides of the six ODEs
    //  newQ[0] is the intermediate value of x-velocity.
    double v = newQ[0];
    double ax;   //  x-direction acceleration

    if ( String.Equals(this.Mode, "accelerating") ) {
      //  If the velocity is at or above the maximum
      //  value, set the acceleration to zero.
      if ( v >= 46.1 ) {
        ax = 0.0;
      }
      //  If the velocity is less than 11.2 m/s set the
      //  acceleration equal to the value at 11.2 m/s
      else if ( v < 11.2 ) {
        ax = 2.1;
      }
      //  Otherwise, evaluate the acceleration according
      //  to the curve fit equation.
      else {
        ax = -4.44e-7*Math.Pow(v,4.0) + 2.56e-4*Math.Pow(v,3.0) -
              0.0216*v*v + 0.527*v - 1.51;
      }
    }
    else if ( String.Equals(this.Mode, "decelerating") ) {
      //  Only decelerate if the velocity is positive.
      if ( newQ[0] > 0.1 ) {
        ax = -2.0;
      }
      else {
        ax = 0.0;
      }
    }
    //  If the mode is "crusing", set the acceleration
    //  to zero.
    else {
      ax = 0.0;
    }

    //  Fill the right-hand sides of the equation of 
    //  motion ODEs.
    dQ[0] = ds*ax;
    dQ[1] = ds*newQ[0];
    dQ[2] = 0.0;
    dQ[3] = 0.0;
    dQ[4] = 0.0;
    dQ[5] = 0.0;

    return dQ;
  }
}
