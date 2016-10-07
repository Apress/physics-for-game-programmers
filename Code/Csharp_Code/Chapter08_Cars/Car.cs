using System;

public class Car : DragProjectile
{
  private double muR;
  private double omegaE;
  private double redline;
  private double finalDriveRatio;
  private double wheelRadius;
  private int gearNumber;     //  gear the car is in
  private int numberOfGears;  //  total number of gears
  private string mode;
  private double[] gearRatio;  //  gear ratios

  //  The Car constructor calls the 
  //  DragProjectile constructor and then initializes
  //  the car-specific variables.
  public Car(double x, double y, double z, double vx, 
             double vy, double vz, double time, double mass, 
             double area, double density, double Cd, double redline,
             double finalDriveRatio, double wheelRadius,
             int numberOfGears) : base(x, y, z, 
             vx, vy, vz, time, mass, area, density, Cd){

    //  Initialize some fields based on values passed
    //  to the constructor.
    this.redline = redline;           //  redline rpm
    this.finalDriveRatio = finalDriveRatio;  //  final drive ratio
    this.wheelRadius = wheelRadius;   //  wheel radius
    this.numberOfGears = numberOfGears;   //  number of gears

    //  Initialize the array that stores the gear ratios.
    //  The array is shifted so the first index in the 
    //  array correpsonds to first gear and so on.
    //  Give all gear ratios the dummy value of 1.0
    gearRatio = new double[numberOfGears + 1];
    gearRatio[0] = 0.0; 
    for(int i=1; i<numberOfGears+1; ++i) {
      gearRatio[i] = 1.0;
    }
         
    //  Set some fields the same for all cars
    muR = 0.015;             //  coefficient of rolling friction
    omegaE = 1000.0;         //  engine rpm
    gearNumber = 1;          //  gear the car is in
    mode = "accelerating";   //  accelerating, cruising, or
  }

  //  These properties are used to access the fields declared
  //  in the class.
  public double MuR {
    get {
      return muR;
    }
  }

  public double OmegaE {
    get {
      return omegaE;
    }
    set {
      omegaE = value;
    }
  }

  public double Redline {
    get {
      return redline;
    }
  }

  public double FinalDriveRatio {
    get {
      return finalDriveRatio;
    }
  }

  public double WheelRadius {
    get {
      return wheelRadius;
    }
  }

  public int GearNumber {
    get {
      return gearNumber;
    }
    set {
      gearNumber = value;
    }
  }

  public int NumberOfGears {
    get {
      return numberOfGears;
    }
  }

  public string Mode {
    get {
      return mode;
    }
    set {
      mode = value;
    }
  }

  //  These methods allow access to the gearRatio array.
  public double GetGearRatio() {
    return gearRatio[gearNumber];
  }

  public void SetGearRatio(int index, double value) {
    gearRatio[index] = value;
  }

  //  This method simulates a gear shift
  public void ShiftGear(int shift) {
    //  If the car will shift beyond highest gear, return.
    if ( shift + this.GearNumber > this.NumberOfGears ) {
      return;
    }
    //  If the car will shift below 1st gear, return.
    else if ( shift + this.GearNumber  < 1 ) {
      return;
    }
    //  Otherwise, change the gear and recompute
    //  the engine rpm value.
    else {
      double oldGearRatio = GetGearRatio();
      this.GearNumber = this.GearNumber + shift;
      double newGearRatio = GetGearRatio();
      this.OmegaE = this.OmegaE*newGearRatio/oldGearRatio;
    }

    return;
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

    //  Compute the constants that define the
    //  torque curve line.
    double b, d;
    if ( this.OmegaE <= 1000.0 ) {
      b = 0.0;
      d = 220.0;
    }
    else if ( this.OmegaE < 4600.0 ) {
      b = 0.025;
      d = 195.0; 
    }
    else {
      b = -0.032;
      d = 457.2;
    }

    //  Declare some convenience variables representing
    //  the intermediate values of velocity.
    double vx = newQ[0];
    double vy = newQ[2];
    double vz = newQ[4];
    double v = Math.Sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Compute the total drag force.
    double density = this.Density;
    double area = this.Area;
    double cd = this.Cd;
    double Fd = 0.5*density*area*cd*v*v;

    //  Compute the force of rolling friction. Because
    //  the G constant defined in the SimpleProjectile
    //  class has a negative sign, the value computed here
    //  will be negative.
    double mass = this.Mass;
    double Fr = muR*mass*G;

    //  Compute the right-hand sides of the six ODEs
    //  newQ[0] is the intermediate value of velocity.
    //  The acceleration of the car is determined by 
    //  whether the car is accelerating, cruising, or
    //  braking. The braking acceleration is assumed to
    //  be a constant -5.0 m/s^2.
    if ( String.Equals(mode,"accelerating") ) {
      double c1 = -Fd/mass;
      double tmp = GetGearRatio()*finalDriveRatio/wheelRadius;
      double c2 = 60.0*tmp*tmp*b*v/(2.0*Math.PI*mass);
      double c3 = (tmp*d + Fr)/mass;
      dQ[0] = ds*(c1 + c2 + c3);
    }
    else if ( String.Equals(mode,"braking") ) {
      //  Only brake if the velocity is positive.
      if ( newQ[0] > 0.1 ) {
        dQ[0] = ds*(-5.0);
      }
      else {
        dQ[0] = 0.0;
      }
    }
    else {
      dQ[0] = 0.0;
    }

    dQ[1] = ds*newQ[0];
    dQ[2] = 0.0;
    dQ[3] = 0.0;
    dQ[4] = 0.0;
    dQ[5] = 0.0;

    return dQ;
  }
}
