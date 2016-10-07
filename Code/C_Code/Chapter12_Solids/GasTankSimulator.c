#include <stdio.h>
#include <math.h>

//********************************************
//  This structure defines the data required
//  to model a gastank.
//********************************************
struct GasTank {
  double thickness;   
  double diffusivity;  
  double initialT;  
  double boundaryT;  
};


//***********************
//  Function prototypes
//***********************
double getTemperature(struct GasTank *gastank, double x, double time);
double getErrorFunction(double s);


//******************************************************
//  Main method. It initializes a gastank and solves
//  for the gastank inner temperature
//******************************************************
int main(int argc, char *argv[]) {

  struct GasTank gastank;
  double innerWallTemp;
  double time = 0.0;
  double dt = 0.5;

  //  Initialize gastank parameters
  gastank.thickness = 0.02;
  gastank.diffusivity = 9.975e-5;  // aluminum
  gastank.initialT = 300.0;   
  gastank.boundaryT = 1000.0;   

  //  Compute the inner wall temperature for 40 seconds
  while ( time < 40.0 ) {
    time = time + dt;
    innerWallTemp = getTemperature(&gastank,gastank.thickness,time);

    printf("time=%lf  inner wall temp=%lf\n",time, innerWallTemp);
  }

  return 0;
}

//*************************************************************
//  This method computes the value of the temperature
//  for any given x and time value.
//*************************************************************
double getTemperature(struct GasTank *gastank, double x, double time) {
  double temperature;
  double grp;
  double erf;
  double diffusivity = gastank->diffusivity;
  double boundaryT = gastank->boundaryT;
  double initialT = gastank->initialT;

  grp = 0.5*x/sqrt(diffusivity*time);
  erf = getErrorFunction(grp);
  temperature = boundaryT + (initialT-boundaryT)*erf;

  return temperature;
}

//*************************************************************
//  This method computes and returns the value of
//  the error function using a table lookup method.
//*************************************************************
double getErrorFunction(double s) {
  double erf[] = {0.0, 0.1125, 0.2227, 0.3286, 0.4284,
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

