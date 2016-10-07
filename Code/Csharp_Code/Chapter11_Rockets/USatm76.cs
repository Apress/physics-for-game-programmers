using System;

public class USatm76
{
  private double R = 287.1;    //  Gas constant for air
  private double G = 9.80665;  //  Gravity acceleration
  private double RE = 6356766.0; // Earth radius in meters

  private double pressure;
  private double density;
  private double temperature;

  public USatm76(double altitude) {
    //  Set the field values according to an
    //  initial altitude
    UpdateConditions(altitude);
  }

  //  These properties access the values of the fields
  //  declared in the class
  public double Pressure {
    get {
      return pressure;
    }
  }

  public double Density {
    get {
      return density;
    }
  }

  public double Temperature {
    get {
      return temperature;
    }
  }

  //  This method computes atmospheric density,
  //  pressure, and temperature based on the US
  //  1976 Standard Atmosphere.
  public void UpdateConditions(double altitude) {
    double slope;  //  slope of the temperature line
    double T0;     //  reference temperature value
    double p0;     //  reference pressure value
    double h0;     //  reference altitude
    double geoAltitude;  //  geopotential altitude
    double grp;    //  temporary variable
    double grp2;   //  temporary variable

    //  The 1976 US Standard Atmosphere model equations 
    //  are functions of geopotential altitude, so we 
    //  need to compute it. Geopotential altitude is an 
    //  equivalent altitude assuming gravity is constant
    //  with altitude.
    geoAltitude = altitude*RE/(altitude + RE);

    //  Assign values to the reference temperature,
    //  pressure, and altitude based on the current
    //  altitude
    if (geoAltitude <= 11000.0) {
      slope = -0.0065;
      T0 = 288.15;
      p0 = 101325.0;
      h0 = 0.0;
    }
    else if (geoAltitude < 20000.0) {
      slope = 0.0;
      T0 = 216.65;
      p0 = 22631.9;
      h0 = 11000.0;
    }
    else if (geoAltitude < 32000.0) {
      slope = 0.001;
      T0 = 216.65;
      p0 = 5474.8;
      h0 = 20000.0;
    }
    else if (geoAltitude < 47000.0) {
      slope = 0.0028;
      T0 = 228.65;
      p0 = 868.0;
      h0 = 32000.0;
    }
    else if (geoAltitude < 51000.0) {
      slope = 0.0;
      T0 = 270.65;
      p0 = 110.9;
      h0 = 47000.0;
    }
    else if (geoAltitude < 71000.0) {
      slope = -0.0028;
      T0 = 270.65;
      p0 = 66.9;
      h0 = 51000.0;
    }
    else if (geoAltitude < 84000.0) {
      slope = -0.002;
      T0 = 214.65;
      p0 = 3.96;
      h0 = 71000.0;
    }
    else {
      slope = 0.0;
      T0 = 186.9;
      p0 = 0.373;
      h0 = 84000.0;
    }

    //  Compute temperature and pressure. The equations
    //  used depend on whether the temperature is constant
    //  in the current altitude range.
    if ( slope == 0.0 ) {
      temperature = T0;
      grp = -G*(geoAltitude - h0)/(R*temperature);
      pressure = p0*Math.Exp(grp);
    }
    else {
      temperature = T0 + slope*(geoAltitude - h0);
      grp = T0/temperature;
      grp2 = G/(slope*R);
      pressure = p0*Math.Pow(grp,grp2);
    }

    density = pressure/(R*temperature);

    return;
  }
}