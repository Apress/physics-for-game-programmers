#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a drag projectile
//********************************************
struct DragProjectile {
   int numEqns;
   double s;
   double q[6];
   double mass;
   double area;
   double density;
   double Cd;
};


//***********************
//  Function prototypes
//***********************
void projectileRightHandSide(struct DragProjectile *projectile, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq);
void projectileRungeKutta4(struct DragProjectile *projectile, double ds);


//******************************************************
//  Main method. It initializes a drag projectile and solves
//  for the basketball motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct DragProjectile basketball;
  double time;
  double angle;
  double x;
  double z;
  double vx;
  double vz;
  double dt = 0.025;
  double pi = acos(-1.0);
  double velocity;
  double e = 0.75;  // coefficient of restitution
  double dx;
  double dz;
  double distance;

  velocity = 7.5;
  angle = 40.0;

  //  Convert the shot angle to radians.
  angle = angle*pi/180.0;

  //  Initialize basketball parameters
  basketball.mass = 0.62;
  basketball.area = 0.045239;
  basketball.density = 1.2;
  basketball.Cd = 0.5;
  basketball.numEqns = 6;
  basketball.s = 0.0;      //  time 
  basketball.q[0] = velocity*cos(angle);  //  vx 
  basketball.q[1] = 1.0;   //  x  
  basketball.q[2] = 0.0;   //  vy 
  basketball.q[3] = 0.0;   //  y  
  basketball.q[4] = velocity*sin(angle);  //  vz 
  basketball.q[5] = 2.25;  //  z  

    x = basketball.q[1];
    vx = basketball.q[0];
    vz = basketball.q[4];
    z = basketball.q[5];
  printf("time = %lf  x = %lf  z = %lf  vx = %lf  vz = %lf\n",
            time, x, z, vx, vz);

  //  Fly the basketball until z<0
  while ( basketball.q[5] >= 0.0 ) {
    projectileRungeKutta4(&basketball, dt);

    time = basketball.s;
    x = basketball.q[1];
    vz = basketball.q[4];
    z = basketball.q[5];
    printf("time = %lf  x = %lf  z = %lf  vz = %lf\n",
            time, x, z, vz);

    //  Determine if the ball impacts the backboard. If it does,
    //  change the x-velocity assuming a frictionless collision.
    //  A collision occurs if the x-velocity is positive, and the
    //  ball location is inside the backboard area.
    if ( x > 0.0 && x >= 5.5 && z > 2.93 && z < 4.0 ) {
      basketball.q[0] = -e*basketball.q[0];   //  vx = -e*vx
    }

    //  Determine if the shot is good.
    //  The center of the basket is 4.2 m from the free
    //  throw line (which is at x = 1.0 m). A shot is considered
    //  to be made if the center of the ball is within 0.22
    //  of the center of the basket.
    dx = x - 5.2;
    dz = z - 3.048;
    distance = sqrt( dx*dx + dz*dz );
    if ( distance <= 0.14 ) {
      printf("Shot was good\n");
      break;
    }

    //  If the basketball hits the ground, stop the simulation
    if ( z <= 0.25 ) {
      printf("Shot missed\n");
      break;
    }
  }

  return 0;
}

//************************************************************
//  This method solves for the projectile motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void projectileRungeKutta4(struct DragProjectile *projectile, double ds) {

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
//  This method loads the right-hand sides for the projectile ODEs
//*************************************************************
void projectileRightHandSide(struct DragProjectile *projectile, 
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
  double G = -9.81;

  int i;

  mass = projectile->mass;
  area = projectile->area;
  density = projectile->density;
  Cd = projectile->Cd;

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

  //  Compute the velocity magnitude. The 1.0e-8 term
  //  ensures there won't be a divide by zero later on
  //  if all of the velocity components are zero.
  v = sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

  //  Compute the total drag force.
  Fd = 0.5*density*area*Cd*v*v;

  //  Compute right-hand side values.
  dq[0] = -ds*Fd*vx/(mass*v);
  dq[1] = ds*vx;
  dq[2] = -ds*Fd*vy/(mass*v);
  dq[3] = ds*vy;
  dq[4] = ds*(G - Fd*vz/(mass*v));
  dq[5] = ds*vz;

  return;
}

