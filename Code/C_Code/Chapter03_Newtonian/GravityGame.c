#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[]) {

  double boxLocation;   //  horizontal location of box
  double boxVelocity;   //  horizontal velocity of box
  int boxWidth;         // width of box in pixels.
  double initialAltitude;  
  double ballAltitude;  // vertical location of ball
  double ballLocation;  //  horizontal location of ball
  double g;         //  gravitational acceleration
  double time;      // elapsed time.
  double timeIncrement;

  //  Set initial values.
  boxLocation = 0.0;
  boxVelocity = 25.0;
  boxWidth = 40;
  initialAltitude = 120.0;
  ballAltitude = initialAltitude;
  ballLocation = 210.0;
  time = 0.0;
  timeIncrement = 0.05;

  g = 9.81;    // Earth
//  g = 1.624;  //  Moon
//  g = 24.8;   //  Jupiter

  while (ballAltitude > 0.0) {
    time += timeIncrement; 
    boxLocation = boxVelocity*time;
    ballAltitude = initialAltitude - 0.5*g*time*time;
    printf("time=%lf  boxLocation=%lf  ballAltitude=%lf\n",
           time, boxLocation, ballAltitude);
  }

  if ( ballLocation >= boxLocation &&
       ballLocation <= boxLocation + boxWidth - 10 ) {
    printf("You Win!\n");
  }
  else {
    printf("Try again\n");
  }

  return 0;
}

