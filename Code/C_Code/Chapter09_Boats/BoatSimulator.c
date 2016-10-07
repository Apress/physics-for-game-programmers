#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a boat.
//********************************************
struct Boat {
   int numEqns;
   double s;
   double q[6];

   char* mode;
   double planingSpeed;
};


//***********************
//  Function prototypes
//***********************
void boatRightHandSide(struct Boat *boat, double *q, double *deltaQ, 
                      double ds, double qScale, double *dq);
void boatRungeKutta4(struct Boat *boat, double ds);


//******************************************************
//  Main method. It initializes a boat and solves
//  for the boat motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct Boat boat;
  double planingSpeed;
  double x;
  double vx;
  double time;
  double dt = 0.5;

  //  Initialize boat parameters
  boat.planingSpeed = 1393.0;
  boat.mode = "accelerating";   //  accelerating, cruising, or braking

  boat.numEqns = 6;
  boat.s = 0.0;      //  time 
  boat.q[0] = 0.0;   //  vx 
  boat.q[1] = 0.0;   //  x  
  boat.q[2] = 0.0;   //  vy 
  boat.q[3] = 0.0;   //  y  
  boat.q[4] = 0.0;   //  vz 
  boat.q[5] = 0.0;   //  z  

  //  accelerate the boat for 40 seconds
  while ( boat.s < 40.0 ) {
    boatRungeKutta4(&boat, dt);

    time = boat.s;
    x = boat.q[1];
    vx = boat.q[0];

    printf("time=%lf  x=%lf  vx=%lf\n",time, x, vx);
  }

  return 0;
}

//************************************************************
//  This method solves for the boat motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void boatRungeKutta4(struct Boat *boat, double ds) {

  int j;
  int numEqns;
  double s;
  double *q;
  double *dq1;
  double *dq2;
  double *dq3;
  double *dq4;

  //  Define a convenience variable to make the
  //  code more readable
  numEqns = boat->numEqns;

  //  Allocate memory for the arrays.
  q = (double *)malloc(numEqns*sizeof(double));
  dq1 = (double *)malloc(numEqns*sizeof(double));
  dq2 = (double *)malloc(numEqns*sizeof(double));
  dq3 = (double *)malloc(numEqns*sizeof(double));
  dq4 = (double *)malloc(numEqns*sizeof(double));

  //  Retrieve the current values of the dependent
  //  and independent variables.
  s = boat->s;
  for(j=0; j<numEqns; ++j) {
    q[j] = boat->q[j];
  }     

  // Compute the four Runge-Kutta steps, The return 
  // value of boatRightHandSide method is an array
  // of delta-q values for each of the four steps.
  boatRightHandSide(boat, q, q,   ds, 0.0, dq1);
  boatRightHandSide(boat, q, dq1, ds, 0.5, dq2);
  boatRightHandSide(boat, q, dq2, ds, 0.5, dq3);
  boatRightHandSide(boat, q, dq3, ds, 1.0, dq4);

  //  Update the dependent and independent variable values
  //  at the new dependent variable location and store the
  //  values in the ODE object arrays.
  boat->s = boat->s + ds;

  for(j=0; j<numEqns; ++j) {
    q[j] = q[j] + (dq1[j] + 2.0*dq2[j] + 2.0*dq3[j] + dq4[j])/6.0;
    boat->q[j] = q[j];
  }     

  //  Free up memory
  free(q);
  free(dq1);
  free(dq2);
  free(dq3);
  free(dq4);

  return;
}
 
//*************************************************************
//  This method loads the right-hand sides for the boat ODEs
//*************************************************************
void boatRightHandSide(struct Boat *boat, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq) {
  //  q[0] = vx = dxdt
  //  q[1] = x
  //  q[2] = vy = dydt
  //  q[3] = y
  //  q[4] = vz = dzdt
  //  q[5] = z
  double newQ[6]; // intermediate dependent variable values.
  double vx;    //  x-direction velocity
  double ax;    //  x-direction acceleration

  int i;

  //  Compute the intermediate values of the 
  //  dependent variables.
  for(i=0; i<6; ++i) {
    newQ[i] = q[i] + qScale*deltaQ[i];
  }

  //  Compute the right-hand sides of the six ODEs
  //  newQ[0] is the intermediate value of x-velocity.
  vx = newQ[0];

  if ( !strcmp(boat->mode,"accelerating") ) {
    //  If the velocity is at or above the maximum
    //  value, set the acceleration to zero.
    if ( vx >= 46.1 ) {
      ax = 0.0;
    }
    //  If the velocity is less than 11.2 m/s set the
    //  acceleration equal to the value at 11.2 m/s
    else if ( vx < 11.2 ) {
      ax = 2.1;
    }
    //  Otherwise, evaluate the acceleration according
    //  to the curve fit equation.
    else {
      ax = -4.44e-7*pow(vx,4.0) + 2.56e-4*pow(vx,3.0) -
            0.0216*vx*vx + 0.527*vx - 1.51;
    }
    }
  else if ( !strcmp(boat->mode,"decelerating") ) {
    //  Only decelerate if the velocity is positive.
    if ( newQ[0] > 0.1 ) {
      ax = -2.0;
    }
    else {
      ax = 0.0;
    }
  }
  //  If the mode is "crusing", set the acceleration
  //  to zero.
  else {
    ax = 0.0;
  }

  //  Fill the right-hand sides of the equation of 
  //  motion ODEs.
  dq[0] = ds*ax;
  dq[1] = ds*newQ[0];
  dq[2] = 0.0;
  dq[3] = 0.0;
  dq[4] = 0.0;
  dq[5] = 0.0;

  return;
}

