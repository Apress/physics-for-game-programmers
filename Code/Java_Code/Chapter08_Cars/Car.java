public class Car extends DragProjectile
{
  private double muR;
  private double omegaE;
  private double redline;
  private double finalDriveRatio;
  private double wheelRadius;
  private int gearNumber;     //  gear the car is in
  private int numberOfGears;  //  total number of gears
  private String mode;
  private double[] gearRatio;  //  gear ratios

  //  The Car constructor calls the 
  //  DragProjectile constructor and then initializes
  //  the car-specific variables.
  public Car(double x, double y, double z, 
             double vx, double vy, double vz,
             double time, double mass, double area, 
             double density, double Cd, double redline,
             double finalDriveRatio, double wheelRadius,
             int numberOfGears) {

    super(x, y, z, vx, vy, vz, time, mass, area, 
          density, Cd);

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
                             //  braking
  }

  //  These methods return the value of the fields
  //  declared in this class.
  public double getMuR() {
    return muR;
  }

  public double getFinalDriveRatio() {
    return finalDriveRatio;
  }

  public int getGearNumber() {
    return gearNumber;
  }

  public int getNumberOfGears() {
    return numberOfGears;
  }

  public double getGearRatio() {
    return gearRatio[gearNumber];
  }

  public double getOmegaE() {
    return omegaE;
  }

  public double getRedline() {
    return redline;
  }

  public double getWheelRadius() {
    return wheelRadius;
  }

  public String getMode() {
    return mode;
  }

  //  These methods set the value of the fields
  //  declared in the fields.

  public void setOmegaE(double value) {
    omegaE = value;
  }

  public void setGearNumber(int value) {
    gearNumber = value;
  }

  public void setGearRatio(int index, double value) {
    gearRatio[index] = value;
  }

  public void setMode(String value) {
    mode = value;
  }

  //  This method simulates a gear shift
  public void shiftGear(int shift) {
    //  If the car will shift beyond highest gear, return.
    if ( shift + getGearNumber() > getNumberOfGears() ) {
      return;
    }
    //  If the car will shift below 1st gear, return.
    else if ( shift + getGearNumber() < 1 ) {
      return;
    }
    //  Otherwise, change the gear and recompute
    //  the engine rpm value.
    else {
      double oldGearRatio = getGearRatio();
      setGearNumber(getGearNumber() + shift);
      double newGearRatio = getGearRatio();
      setOmegaE(getOmegaE()*newGearRatio/oldGearRatio);
    }

    return;
  }

  //  The getRightHandSide() method returns the right-hand
  //  sides of the two first-order ODEs. In this 
  //  simulation, the car travels in the x-direction only.
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

    //  Compute the constants that define the
    //  torque curve line.
    double b, d;
    if ( getOmegaE() <= 1000.0 ) {
      b = 0.0;
      d = 220.0;
    }
    else if ( getOmegaE() < 4600.0 ) {
      b = 0.025;
      d = 195.0; 
    }
    else {
      b = -0.032;
      d = 457.2;
    }

    //  Compute velocity magnitude.
    double vx = newQ[0];
    double vy = newQ[2];
    double vz = newQ[4];
    double v = Math.sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

    //  Compute the total drag force.
    double Fd = 0.5*getDensity()*getArea()*getCd()*v*v;

    //  Compute the force of rolling friction. Because
    //  the G constant defined in the SimpleProjectile
    //  class has a negative sign, the value computed here
    //  will be negative
    double Fr = getMuR()*getMass()*G;

    //  Compute the right-hand sides of the six ODEs
    //  newQ[0] is the intermediate value of velocity.
    //  The acceleration of the car is determined by 
    //  whether the car is accelerating, cruising, or
    //  braking. The braking acceleration is assumed to
    //  be a constant -5.0 m/s^2.
    if ( mode.equals("accelerating") ) {
      double c1 = -Fd/getMass();
      double tmp = getGearRatio()*getFinalDriveRatio()/
                   getWheelRadius();
      double c2 = 60.0*tmp*tmp*b*v/(2.0*Math.PI*getMass());
      double c3 = (tmp*d + Fr)/getMass();
      dQ[0] = ds*(c1 + c2 + c3);
    }
    else if ( mode.equals("braking") ) {
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
