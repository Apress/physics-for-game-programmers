#include <stdio.h>
#include <math.h>

//******************************************************
//  Main method. It determines the energy absorbed from
//  a laser.
//******************************************************
int main(int argc, char *argv[]) {

  double power;
  double radius;
  double absorption;
  double area;
  double energyAbsorbed;
  double time = 0.0;
  double dt = 1.0;
  double pi = acos(-1.0);

  //  Initialize laser parameters
  power = 7.5e+6;    //  laser power
  radius = 35.0;     //  beam radius
  absorption = 0.75; //  absorption coefficient
  area = pi*radius*radius; 

  //  Compute the energy absorbed over 40 seconds.
  energyAbsorbed = 0.0;
  while ( time < 20.0 ) {
    time = time + dt;
    energyAbsorbed = absorption*power*time/area;

    printf("time=%lf  energy=%lf\n",time, energyAbsorbed);
  }

  return 0;
}

