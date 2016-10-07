using System;

public class BoxsterS : Car
{
  //  The BoxsterS constructor calls the Car constructor
  //  and then sets the gear ratios for the BoxsterS.
  //  Here are some specs for the BoxsterS
  //  mass = 1393.0 kg (with 70 kg driver)
  //  area = 1.94 m^2 
  //  Cd = 0.31
  //  redline = 7200 rpm
  //  finalDriveRatio = 3.44
  //  wheelRadius = 0.3186
  //  numberOfGears = 6;

  public BoxsterS(double x, double y, double z, double vx, 
             double vy, double vz, double time, double density) :
             base(x, y, z, vx, vy, vz, time, 1393.0, 1.94, 
                  density, 0.31, 7200.0, 3.44, 0.3186, 6) {

    //  Set the gear ratios.
    SetGearRatio(1, 3.82);
    SetGearRatio(2, 2.20);
    SetGearRatio(3, 1.52);
    SetGearRatio(4, 1.22);
    SetGearRatio(5, 1.02);
    SetGearRatio(6, 0.84);
  }
}
