public class CessnaSkyhawk extends PropPlane 
{
  //  Cessna Skyhawk data
  //  wingArea = 16.2 = wing wetted area, m^2
  //  wingSpan = 10.9 = wing span, m
  //  tailArea = 2.0 = tail wetted area, m^2
  //  clSlope0 = 0.0889 = slope of Cl-alpha curve
  //  cl0 = 0.178 = Cl value when alpha = 0
  //  clSlope1 = -0.1 = slope of post-stall Cl-alpha curve
  //  cl1 = 3.2 = intercept of post-stall Cl-alpha curve
  //  alphaClMax = 16.0 = alpha at Cl(max)
  //  cdp = 0.034 = parasitic drag coefficient
  //  eff = 0.77 = induced drag efficiency coefficient
  //  mass = 1114.0 = airplane mass, kg
  //  enginePower = 119310.0 = peak engine power, W
  //  engineRps = 40.0 = engine turnover rate, rev/s
  //  propDiameter = 1.905 = propeller diameter, m
  //  a = 1.83 = propeller efficiency curve fit coefficient
  //  b = -1.32 = propeller efficiency curve fit coefficient

  //  The CessnaSkyhawk constructor calls the
  //  PropPlane constructor.
  public CessnaSkyhawk(double x, double y, double z, 
           double vx, double vy, double vz, double time) {
    super(x, y, z, vx, vy, vz, time, 16.2, 10.9, 2.0,
          0.0889, 0.178, -0.1, 3.2, 16.0, 0.034, 0.77, 
          1114.0, 119310.0, 40.0, 1.905, 1.83, -1.32);
  }
}