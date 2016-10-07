using System;

public abstract class ODE
{
  //  Declare fields used by the class
  private int numEqns;  //  number of equations to solve
  private double[] q;   //  array of dependent variables
  private double s;     //  independent variable

  //  Constructor
  public ODE(int numEqns) {
    this.numEqns = numEqns;
    q = new double[numEqns];
  }

  //  These properties access the value of the numEqns
  //  and s fields.
  public int NumEqns {
    get {
      return numEqns;
    }
  }

  public double S {
    get {
      return s;
    }
    set {
      s = value;
    }
  }

  //  This method returns the entire q[] array.
  public double GetQ(int index) {
    return q[index];
  }

  public void SetQ(double value, int index) {
    q[index] = value;
    return;
  }

  public double[] GetAllQ() {
    return q;
  }

  //  This method returns the right-hand side of the 
  //  ODEs. It is declared abstract to force subclasses
  //  to implement their own version of the method.
  public abstract double[] GetRightHandSide(double s, 
      double[] q,double[] deltaQ, double ds, double qScale);
}
