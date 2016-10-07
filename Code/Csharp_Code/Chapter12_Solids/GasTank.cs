using System;

public class GasTank 
{
  private double thickness;   
  private double diffusivity;  
  private double initialT;  
  private double boundaryT;  

  public GasTank(double thickness, double diffusivity,
                  double initialT, double boundaryT) {
    this.thickness = thickness;
    this.diffusivity = diffusivity;
    this.initialT = initialT;
    this.boundaryT = boundaryT;
  }

  //  These properties access the fields declared in the class.
  public double Thickness {
    get {
      return thickness;
    }
  }

  public double Diffusivity {
    get {
      return diffusivity;
    }
  }

  public double InitialT {
    get {
      return initialT;
    }
  }

  public double BoundaryT {
    get {
      return boundaryT;
    }
    set {
      boundaryT = value;
    }
  }

  //  This method computes the value of the temperature
  //  for any given x and time value.
  public double GetTemperature(double x, double time) {
    double temperature;
    double grp;
    double erf;

    grp = 0.5*x/Math.Sqrt(diffusivity*time);
    erf = GetErrorFunction(grp);
    temperature = boundaryT + (initialT-boundaryT)*erf;

    return temperature;
  }

  //  This method computes and returns the value of
  //  the error function using a table lookup method.
  private double GetErrorFunction(double s) {
    double[] erf = {0.0, 0.1125, 0.2227, 0.3286, 0.4284,
                  0.5205, 0.6039, 0.6778, 0.7421, 0.7969,
                  0.8427, 0.8802, 0.9103, 0.9340, 0.9523,
                  0.9661, 0.9764, 0.9838, 0.9891, 0.9928,
                  0.9953};
    int j;
    double value;

    //  If the argument is greater than 2.0, set the
    //  error function value to 1. Otherwise, find the
    //  value using the data in the erf[] array.
    if ( s >= 2.0 ) {
      value = 1.0;
    }
    else {
      j = (int)(s*10.0);
      value = erf[j] + (s*10.0 - j)*(erf[j+1] - erf[j]);
    }

    return value;
  }
}
