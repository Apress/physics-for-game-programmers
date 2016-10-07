#include <stdio.h>
#include <stdlib.h>
#include <time.h>

//******************************************************
//  Main method. It computes the value of pi from a 
//  random number generator
//******************************************************
int main(int argc, char *argv[]) {

  int sampleSize;
  int numInCircle;
  double piValue;
  double x;
  double y;
  double distance;
  int j;

  sampleSize = 1000;
  numInCircle = 0;
  piValue = 0.0;

  //  Initialize random number generator
  srand((unsigned)time(NULL));  

  //  Create the sample points. Generate
  //  two random numbers representing a data point.
  //  See if the data point is inside the circle area
  //  or not.
  for(j=0; j<sampleSize; ++j) {
    //  Generate an x-y point.
    x = (double)rand()/RAND_MAX;
    y = (double)rand()/RAND_MAX;

    //  Determine if the point is inside the circle
    //  by computing the distance from the point to
    //  the center of the circle. If the distance is
    //  less than 1, the point is inside.
    distance = sqrt((x-0.5)*(x-0.5) + (y-0.5)*(y-0.5));
    if ( distance <= 0.5 ) {
      ++numInCircle;
    }
  }

  //  Update the value of pi
  piValue = 4.0*numInCircle/(j+1.0);

  printf("pi value = %lf\n",piValue);

  return 0;
}

