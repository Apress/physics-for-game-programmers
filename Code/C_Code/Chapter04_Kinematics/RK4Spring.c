#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a spring
//********************************************
struct SpringODE {
   int numEqns;
   double s;
   double q[2];
   double mass;
   double k;
   double mu;
};


//***********************
//  Function prototypes
//***********************
void springRightHandSide( struct SpringODE *spring, double *q, 
             double *deltaQ, double ds, double qScale, double *dq);
void springRungeKutta4(struct SpringODE *spring, double ds);


//******************************************************
//  Main method. It initializes a spring and solves
//  for the spring motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct SpringODE spring;
  double dt = 0.1;
  int i;

  //  Initialize spring
  spring.mass = 1.0;
  spring.mu = 1.5;
  spring.k = 20.0;
  spring.numEqns = 2;
  spring.s = 0.0;      //  time = 0.0
  spring.q[0] = 0.0;   //  vx = 0.0
  spring.q[1] = -0.2;  //  x = -0.2

  //  Solve for the spring motion over a range
  //  of 7 seconds using a 0.1 second time increment.
  for(i=0; i<70; ++i) {
    springRungeKutta4(&spring, dt);
    printf("time = %lf  x = %lf  vx = %lf\n",
            spring.s, spring.q[1], spring.q[0]);
  }

  return 0;
}

//************************************************************
//  This method solves for the spring motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void springRungeKutta4(struct SpringODE *spring, double ds) {

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
  numEqns = spring->numEqns;

  //  Allocate memory for the arrays.
  q = (double *)malloc(numEqns*sizeof(double));
  dq1 = (double *)malloc(numEqns*sizeof(double));
  dq2 = (double *)malloc(numEqns*sizeof(double));
  dq3 = (double *)malloc(numEqns*sizeof(double));
  dq4 = (double *)malloc(numEqns*sizeof(double));

  //  Retrieve the current values of the dependent
  //  and independent variables.
  s = spring->s;
  for(j=0; j<numEqns; ++j) {
    q[j] = spring->q[j];
  }     

  // Compute the four Runge-Kutta steps, The return 
  // value of springRightHandSide method is an array of 
  // delta-q values for each of the four steps.
  springRightHandSide(spring, q, q,   ds, 0.0, dq1);
  springRightHandSide(spring, q, dq1, ds, 0.5, dq2);
  springRightHandSide(spring, q, dq2, ds, 0.5, dq3);
  springRightHandSide(spring, q, dq3, ds, 1.0, dq4);

  //  Update the dependent and independent variable values
  //  at the new dependent variable location and store the
  //  values in the ODE object arrays.
  spring->s = spring->s + ds;

  for(j=0; j<numEqns; ++j) {
//printf("j=%d  dq1=%lf  dq2=%lf  dq3=%lf  dq4=%lf\n",
//j,dq1[j],dq2[j],dq3[j],dq4[j]);
    q[j] = q[j] + (dq1[j] + 2.0*dq2[j] + 2.0*dq3[j] + dq4[j])/6.0;
    spring->q[j] = q[j];
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
//  This method loads the right-hand sides for the spring ODEs
//*************************************************************
void springRightHandSide( struct SpringODE *spring, double *q, 
             double *deltaQ, double ds, double qScale, double *dq) {
  //  q[0] = vx
  //  q[1] = x
  //  dq[0] = d(vx) = dt*(-mu*dxdt - k*x)/mass
  //  dq[1] = d(x) = dt*(v)
  double newQ[2]; // intermediate dependent variable values.
  double G = -9.81;
  double mass;
  double mu;
  double k;
  int i;

  mass = spring->mass;
  mu = spring->mu;
  k = spring->k;

  //  Compute the intermediate values of the 
  //  dependent variables.
  for(i=0; i<2; ++i) {
    newQ[i] = q[i] + qScale*deltaQ[i];
  }

  //  Compute right-hand side values.
  dq[0] = ds*G - ds*(mu*newQ[0] + k*newQ[1])/mass;
  dq[1] = ds*(newQ[0]);

  return;
}

