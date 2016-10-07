using System;

public class PropPlane : SimpleProjectile
{
  //  Declare fields
  private double bank;
  private double alpha;  //  angle of attack
  private double throttle;
  private double wingArea;
  private double wingSpan;
  private double tailArea;
  private double clSlope0;    // slope of Cl-alpha curve
  private double cl0;         // intercept of Cl-alpha curve
  private double clSlope1;    // post-stall slope of Cl-alpha curve
  private double cl1;         // post-stall intercept of Cl-alpha curve
  private double alphaClMax;  // alpha when Cl=Clmax
  private double cdp;         // parasite drag coefficient
  private double eff;         // induced drag efficiency coefficient
  private double mass;
  private double enginePower;
  private double engineRps;   // revolutions per second
  private double propDiameter;
  private double a;           //  propeller efficiency coefficient
  private double b;           //  propeller efficiency coefficient
  private string flap;        //  flap deflection amount

  //  The PropPlane constructor calls the
  //  SimpleProjectile constructor and initializes
  //  the value of the PropPlane class fields.
  public PropPlane(double x, double y, double z, 
           double vx, double vy, double vz, double time,
           double wingArea, double wingSpan, double tailArea,
           double clSlope0, double cl0, double clSlope1,
           double cl1, double alphaClMax,
           double cdp, double eff, double mass, 
           double enginePower, double engineRps,
           double propDiameter, double a, double b) :
             base(x, y, z, vx, vy, vz, time) {

    this.wingArea = wingArea;
    this.wingSpan = wingSpan;
    this.tailArea = tailArea;
    this.clSlope0 = clSlope0;
    this.cl0 = cl0;
    this.clSlope1 = clSlope1;
    this.cl1 = cl1;
    this.alphaClMax = alphaClMax;
    this.cdp = cdp;
    this.eff = eff;
    this.mass = mass;
    this.enginePower = enginePower;
    this.engineRps = engineRps;
    this.propDiameter = propDiameter;
    this.a = a;
    this.b = b;

    //  Initially, set bank, angle of attack, 
    //  and throttle to zero
    bank = 0.0;
    alpha = 0.0;
    throttle = 0.0;
    flap = "0";
  }

  //  These properties are used to access the fields declared
  //  in the class.
  public double Bank {
    get {
      return bank;
    }

    set {
      bank = value;
    }
  }

  public double Alpha {
    get {
      return alpha;
    }

    set {
      alpha = value;
    }
  }

  public double Throttle {
    get {
      return throttle;
    }

    set {
      throttle = value;
    }
  }

  public double WingArea {
    get {
      return wingArea;
    }
  }

  public double WingSpan {
    get {
      return wingSpan;
    }
  }

  public double TailArea {
    get {
      return tailArea;
    }
  }

  public double ClSlope0 {
    get {
      return clSlope0;
    }
  }

  public double Cl0 {
    get {
      return cl0;
    }
  }

  public double ClSlope1 {
    get {
      return clSlope1;
    }
  }

  public double Cl1 {
    get {
      return cl1;
    }
  }

  public double AlphaClMax {
    get {
      return alphaClMax;
    }
  }

  public double Cdp {
    get {
      return cdp;
    }
  }

  public double Eff {
    get {
      return eff;
    }
  }

  public double Mass {
    get {
      return mass;
    }
  }

  public double EnginePower {
    get {
      return enginePower;
    }
  }

  public double EngineRps {
    get {
      return engineRps;
    }
  }

  public double PropDiameter {
    get {
      return propDiameter;
    }
  }

  public double A {
    get {
      return a;
    }
  }

  public double B {
    get {
      return b;
    }
  }

  public string Flap {
    get {
      return flap;
    }

    set {
      flap = value;
    }
  }

  //  This method updates the velocity and location
  //  of the projectile using a 4th order Runge-Kutta
  //  solver to integrate the equations of motion.
  public override void UpdateLocationAndVelocity(double dt) {
    ODESolver.RungeKutta4(this, dt);
  }

  //  The GetRightHandSide() method returns the right-hand
  //  sides of the equations of motion for the prop plane.
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

    //  Assign convenenience variables to the intermediate 
    //  values of the locations and velocities.
    double vx = newQ[0];
    double vy = newQ[2];
    double vz = newQ[4];
    double x = newQ[1];
    double y = newQ[3];
    double z = newQ[5];
    double vh = Math.Sqrt(vx*vx + vy*vy);
    double vtotal = Math.Sqrt(vx*vx + vy*vy + vz*vz);

    //  Compute the air density
    double temperature = 288.15 - 0.0065*z;
    double grp = (1.0 - 0.0065*z/288.15);
    double pressure = 101325.0*Math.Pow(grp, 5.25);
    double density = 0.00348*pressure/temperature;

    //  Compute power drop-off factor
    double omega = density/1.225;
    double factor = (omega - 0.12)/0.88;

    //  Compute thrust
    double advanceRatio = vtotal/(engineRps*propDiameter);
    double thrust = throttle*factor*enginePower*
           (a + b*advanceRatio*advanceRatio)/(engineRps*propDiameter); 

    //  Compute lift coefficient. The Cl curve is 
    //  modeled using two straight lines.
    double cl;
    if ( alpha < alphaClMax ) {
      cl = clSlope0*alpha + cl0;
    }
    else {
      cl = clSlope1*alpha + cl1;
    }

    //  Include effects of flaps and ground effects.
    //  Ground effects are present if the plane is
    //  within 5 meters of the ground.
    if ( String.Equals(flap,"20") ) {
      cl += 0.25;
    }
    if ( String.Equals(flap,"40") ) {
      cl += 0.5;
    }
    if ( z < 5.0 ) {
      cl += 0.25;
    }

    //  Compute lift
    double lift = 0.5*cl*density*vtotal*vtotal*wingArea;

    //  Compute drag coefficient
    double aspectRatio = wingSpan*wingSpan/wingArea;
    double cd = cdp + cl*cl/(Math.PI*aspectRatio*eff);

    //  Compute drag force
    double drag = 0.5*cd*density*vtotal*vtotal*wingArea;

    //  Define some shorthand convenience variables
    //  for use with the rotation matrix.
    //  Compute the sine and cosines of the climb angle,
    //  bank angle, and heading angle;

    double cosW = Math.Cos(bank); //  bank angle
    double sinW = Math.Sin(bank); //  bank angle
    double cosP;      //  climb angle
    double sinP;      //  climb angle
    double cosT;      //  heading angle
    double sinT;      //  heading angle

    if ( vtotal == 0.0 ) {
      cosP = 1.0;
      sinP = 0.0;
    }
    else {
      cosP = vh/vtotal;  
      sinP = vz/vtotal;  
    }

    if ( vh == 0.0 ) {
      cosT = 1.0;
      sinT = 0.0;
    }
    else {
      cosT = vx/vh;
      sinT = vy/vh;
    }

    //  Convert the thrust, drag, and lift forces into
    //  x-, y-, and z-components using the rotation matrix.
    double Fx = cosT*cosP*(thrust - drag) + 
               (sinT*sinW - cosT*sinP*cosW)*lift;
    double Fy = sinT*cosP*(thrust - drag) + 
               (-cosT*sinW - sinT*sinP*cosW)*lift;
    double Fz = sinP*(thrust - drag) + cosP*cosW*lift;

    //  Add the gravity force to the z-direction force.
    Fz = Fz + mass*G;

    //  Since the plane can't sink into the ground, if the
    //  altitude is less than or equal to zero and the z-component
    //  of force is less than zero, set the z-force
    //  to be zero.
    if ( z <= 0.0 && Fz <= 0.0 ) {
      Fz = 0.0;
    }

    //  Load the right-hand sides of the ODE's
    dQ[0] = ds*(Fx/mass);
    dQ[1] = ds*vx;
    dQ[2] = ds*(Fy/mass);
    dQ[3] = ds*vy;
    dQ[4] = ds*(Fz/mass);
    dQ[5] = ds*vz;

    return dQ;
  }
}
