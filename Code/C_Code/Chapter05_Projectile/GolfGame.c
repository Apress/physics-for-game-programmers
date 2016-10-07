#include <stdio.h>
#include <math.h>
#include <malloc.h>

//******************************************************
//  Main method. It initializes a simple projectile
//  and solves for its trajectory
//******************************************************
int main(int argc, char *argv[]) {

  double G = -9.81;
  double x;
  double y;
  double z;
  double vx;
  double vy;
  double vz;
  double x0;
  double y0;
  double z0;
  double vx0;
  double vy0;
  double vz0;
  double time;
  double dt;
  int i;

  //  set initial values
  x0 = 0.0;
  y0 = 0.0;
  z0 = 0.0;
  z = z0;
  vx0 = 31.0;
  vy0 = 0.0;
  vz0 = 35.0;
  time = 0.0;
  dt = 0.1;

  //  Fly the golf ball
  while ( z >= 0.0 ) {
    time = time + dt;
    x = x0 + vx0*time;
    y = y0 + vy0*time;
    vz = vz0 + G*time;
    z = z0 + vz0*time + 0.5*G*time*time;
    printf("time = %lf  x = %lf  z = %lf  vz = %lf\n",
            time, x, z, vz);
  }

  return 0;
}

