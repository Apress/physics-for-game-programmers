#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>

//********************************************
//  This structure defines the data required
//  to model a soldier.
//********************************************
struct Soldier {
  double xLocation;
  double yLocation;
  double speed;
};


//******************************************************
//  Main method. It initializes a soldier and solves
//  for the soldier motion
//******************************************************
int main(int argc, char *argv[]) {

  struct Soldier soldier[3];
  double elapsedTime = 0.0;
  double dt = 1.0;
  int i;

  double mean;
  double sigma;
  double x;
  double grp1;
  double grp2;
  double speed;
  double t;
  double newY;

  //  Initialize soldier parameters
  for(i=0; i<3; ++i) {
    soldier[i].xLocation = i*10.0;
    soldier[i].yLocation = 0.0;
    soldier[i].speed = 0.0;   
  }   

  mean = 5.0;
  sigma = 1.0;

  //  Initialize random number generator
  srand((unsigned)time(NULL));  

  //  Compute the soldier's motion for 20 seconds
  while ( elapsedTime < 20.0 ) {

    elapsedTime = elapsedTime + dt;

    for(i=0; i<3; ++i) {
      //  Generate a random number between 0 and 1.
      x = (double)rand()/RAND_MAX;
  
      //  Find the speed corresponding to the random
      //  number using the Gaussian distribution with a
      //  mean value of 0 and a standard deviation of 1
      t = sqrt( log(1.0/(x*x)) );
      grp1 = 2.515517 + 0.802853*t + 0.010328*t*t;
      grp2 = 1.0 + 1.432788*t + 0.189269*t*t + 
             0.001308*t*t*t;
      speed = -t + grp1/grp2;

      //  Shift the converted speed to the proper
      //  mean and standard deviation value.
      speed = mean + speed*sigma;

      //  Update the value of the speed field for
      //  each soldier.
      soldier[i].speed = speed;

      //  Update the y-location of each soldier.
      soldier[i].yLocation = soldier[i].yLocation + dt*speed;
    }

    //  Print the y-location of each soldier as they 
    //  randomly march along.
    printf("time=%lf  soldier1=%lf  soldier2=%lf  soldier3=%lf\n",
            elapsedTime, soldier[0].yLocation, soldier[1].yLocation, 
            soldier[2].yLocation);
  }

  return 0;
}

