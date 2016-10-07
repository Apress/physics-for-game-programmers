#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a wind projectile
//********************************************
struct WindProjectile {
   int numEqns;
   double s;
   double q[6];
   double mass;
   double area;
   double density;
   double Cd;
   double windVx;
   double windVy;
};


//***********************
//  Function prototypes
//***********************
void projectileRightHandSide(struct WindProjectile *projectile, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq);
void projectileRungeKutta4(struct WindProjectile *projectile, double ds);


//******************************************************
//  Main method. It initializes a wind projectile and solves
//  for the golf ball motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct WindProjectile golfball;
  double time;
  double x;
  double z;
  double vz;
  double dt = 0.1;

  //  Initialize golfball parameters
  golfball.mass = 0.0459;
  golfball.area = 0.001432;
  golfball.density = 1.225;
  golfball.Cd = 0.25;
  golfball.windVx = 10.0;
  golfball.windVy = 0.0;
  golfball.numEqns = 6;
  golfball.s = 0.0;      //  time = 0.0
  golfball.q[0] = 31.0;  //  vx = 31.0
  golfball.q[1] = 0.0;   //  x  = 0.0
  golfball.q[2] = 0.0;   //  vy = 0.0
  golfball.q[3] = 0.0;   //  y  = 0.0
  golfball.q[4] = 35.0;  //  vz = 35.0
  golfball.q[5] = 0.0;   //  z  = 0.0

  //  Fly the golf ball until z<0
  while ( golfball.q[5] >= 0.0 ) {
    projectileRungeKutta4(&golfball, dt);

    time = golfball.s;
    x = golfball.q[1];
    vz = golfball.q[4];
    z = golfball.q[5];
    printf("time = %lf  x = %lf  z = %lf  vz = %lf\n",
            time, x, z, vz);
  }

  return 0;
}

//************************************************************
//  This method solves for the projectile motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void projectileRungeKutta4(struct WindProjectile *projectile, double ds) {

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
  numEqns = projectile->numEqns;

  //  Allocate memory for the arrays.
  q = (double *)malloc(numEqns*sizeof(double));
  dq1 = (double *)malloc(numEqns*sizeof(double));
  dq2 = (double *)malloc(numEqns*sizeof(double));
  dq3 = (double *)malloc(numEqns*sizeof(double));
  dq4 = (double *)malloc(numEqns*sizeof(double));

  //  Retrieve the current values of the dependent
  //  and independent variables.
  s = projectile->s;
  for(j=0; j<numEqns; ++j) {
    q[j] = projectile->q[j];
  }     

  // Compute the four Runge-Kutta steps, The return 
  // value of projectileRightHandSide method is an array
  // of delta-q values for each of the four steps.
  projectileRightHandSide(projectile, q, q,   ds, 0.0, dq1);
  projectileRightHandSide(projectile, q, dq1, ds, 0.5, dq2);
  projectileRightHandSide(projectile, q, dq2, ds, 0.5, dq3);
  projectileRightHandSide(projectile, q, dq3, ds, 1.0, dq4);

  //  Update the dependent and independent variable values
  //  at the new dependent variable location and store the
  //  values in the ODE object arrays.
  projectile->s = projectile->s + ds;

  for(j=0; j<numEqns; ++j) {
    q[j] = q[j] + (dq1[j] + 2.0*dq2[j] + 2.0*dq3[j] + dq4[j])/6.0;
    projectile->q[j] = q[j];
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
void projectileRightHandSide(struct WindProjectile *projectile, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq) {
  //  q[0] = vx = dxdt
  //  q[1] = x
  //  q[2] = vy = dydt
  //  q[3] = y
  //  q[4] = vz = dzdt
  //  q[5] = z
  double newQ[6]; // intermediate dependent variable values.
  double mass;
  double area;
  double density;
  double Cd;
  double vx;
  double vy;
  double vz;
  double v;
  double Fd;
  double vax;
  double vay;
  double vaz;
  double va;
  double windVx;
  double windVy;
  double G = -9.81;

  int i;

  mass = projectile->mass;
  area = projectile->area;
  density = projectile->density;
  Cd = projectile->Cd;
  windVx = projectile->windVx;
  windVy = projectile->windVy;

  //  Compute the intermediate values of the 
  //  dependent variables.
  for(i=0; i<6; ++i) {
    newQ[i] = q[i] + qScale*deltaQ[i];
  }

  //  Declare some convenience variables representing
  //  the intermediate values of velocity.
  vx = newQ[0];
  vy = newQ[2];
  vz = newQ[4];

  //  Compute the apparent velocities by subtracting
  //  the wind velocity components from the projectile
  //  velocity components.
  vax = vx - windVx;
  vay = vy - windVy;
  vaz = vz;

  //  Compute the apparent velocity magnitude. The 1.0e-8 term
  //  ensures there won't be a divide by zero later on
  //  if all of the velocity components are zero.
  va = sqrt(vax*vax + vay*vay + vaz*vaz) + 1.0e-8;

  //  Compute the total drag force.
  Fd = 0.5*density*area*Cd*va*va;

  //  Compute right-hand side values.
  dq[0] = -ds*Fd*vax/(mass*va);
  dq[1] = ds*vx;
  dq[2] = -ds*Fd*vay/(mass*va);
  dq[3] = ds*vy;
  dq[4] = ds*(G - Fd*vaz/(mass*va));
  dq[5] = ds*vz;

  return;
}

