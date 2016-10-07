public abstract class ODE
{
  //  Declare fields used by the class
  private int numEqns;  //  number of equations to solve
  private double q[];   //  array of dependent variables
  private double s;     //  independent variable

  //  Constructor
  public ODE(int numEqns) {
    this.numEqns = numEqns;
    q = new double[numEqns];
  }

  //  These methods return the number of equations or
  //  the value of the dependent or independent variables.
  public int getNumEqns() {
    return numEqns;
  }

  public double getS() {
    return s;
  }

  public double getQ(int index) {
    return q[index];
  }

  public double[] getAllQ() {
    return q;
  }

  //  These methods change the value of the dependent
  //  or independent variables.
  public void setS(double value) {
    s = value;
    return;
  }

  public void setQ(double value, int index) {
    q[index] = value;
    return;
  }

  //  This method returns the right-hand side of the 
  //  ODEs. It is declared abstract to force subclasses
  //  to implement their own version of the method.
  public abstract double[] getRightHandSide(double s, 
      double q[],double deltaQ[], double ds, double qScale);
}
