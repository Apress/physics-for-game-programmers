#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[]) {

  double mu;
  double mass;
  double initialVelocity;
  double velocity;
  double xLocation;
  double time;
  double G = 9.81;

  //  Set initial values.
  mu = 0.5;
  mass = 1.0;
  initialVelocity = 4.5;
  velocity = initialVelocity;
  xLocation = 0.0;
  time = 0.0;

  while (1) {
    //  Update the time and compute the new position
    //  of the disk. 
    time += 0.05; 

    //  Compute the current velocity of the disk.
    velocity = initialVelocity - mu*G*time;

    //  Update the position of the disk.
    xLocation = initialVelocity*time - 0.5*mu*G*time*time;

    printf("time=%lf  xLocation=%lf\n", time, xLocation);

    //  If the disk stops moving or if it reaches
    //  the end of the board, stop the simulation
    if ( velocity <= 0.0 || xLocation > 2.9) {
      break;
    }
  }

  if ( xLocation > 2.0 && xLocation <= 2.2 ) {
    printf("10 points!\n");
  }
  else if ( xLocation > 2.4 && xLocation <= 2.6 ) {
    printf("20 points\n");
  }
  else if ( xLocation > 2.6 && xLocation <= 2.8 ) {
    printf("50 points\n");
  }
  else {
    printf("0 points\n");
  }

  return 0;
}

