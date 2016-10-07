#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a spin projectile
//********************************************
struct SpinProjectile {
   int numEqns;
   double s;
   double q[6];
   double mass;
   double area;
   double density;
   double Cd;
   double windVx;
   double windVy;
   double rx;     //  spin axis vector component
   double ry;     //  spin axis vector component
   double rz;     //  spin axis vector component
   double omega;  //  angular velocity, m/s
   double radius; //  sphere radius, m
};


//***********************
//  Function prototypes
//***********************
void projectileRightHandSide(struct SpinProjectile *projectile, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq);
void projectileRungeKutta4(struct SpinProjectile *projectile, double ds);


//******************************************************
//  Main method. It initializes a spin projectile and solves
//  for the golf ball motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct SpinProjectile golfball;
  double time;
  double x;
  double z;
  double vz;
  double dt = 0.1;
  double velocity;  //  club velocity
  double clubMass;
  double loft;
  double pi = acos(-1.0);
  char* clubType;
  double cosL;
  double sinL;
  double vcp;
  double vcn;
  double vbp;
  double vbn;
  double omega;
  double ballMass;
  double radius;
  double e;  //  coefficient of restitution
  double vx0;
  double vy0;
  double vz0;

  //  Initialize golfball parameters
  golfball.mass = 0.0459;
  golfball.area = 0.001432;
  golfball.density = 1.225;
  golfball.Cd = 0.22;
  golfball.windVx = 0.0;
  golfball.windVy = 0.0;
  golfball.rx = 0.0;
  golfball.ry = 1.0;
  golfball.rz = 0.0;
  golfball.omega = 300.0;
  golfball.radius = 0.02135;
  golfball.numEqns = 6;
  golfball.s = 0.0;      //  time = 0.0
  golfball.q[0] = 0.0;   //  vx = 0.0
  golfball.q[1] = 0.0;   //  x  = 0.0
  golfball.q[2] = 0.0;   //  vy = 0.0
  golfball.q[3] = 0.0;   //  y  = 0.0
  golfball.q[4] = 0.0;   //  vz = 0.0
  golfball.q[5] = 0.0;   //  z  = 0.0

  velocity = 50.0;
  ballMass = golfball.mass;
  e = 0.78;
  clubType = "Driver";
  radius = golfball.radius;

  if ( !strcmp(clubType,"Driver") ) {
    clubMass = 0.2;
    loft = 11.0;
  }
  else if ( !strcmp(clubType,"3 wood") ) {
    clubMass = 0.208;
    loft = 15.0;
  }
  else if ( !strcmp(clubType,"3 iron") ) {
    clubMass = 0.239;
    loft = 21.0;
  }
  else if ( !strcmp(clubType,"5 iron") ) {
    clubMass = 0.253;
    loft = 27.0;
  }
  else if ( !strcmp(clubType,"7 iron") ) {
    clubMass = 0.267;
    loft = 35.0;
  }
  else {
    clubMass = 0.281;
    loft = 43.0;
  }

printf("clubMass=%lf  loft=%lf  pi=%lf\n",clubMass, loft, pi);

  //  Convert the loft angle from degrees to radians and
  //  assign values to some convenience variables.
  loft = loft*pi/180.0;
  cosL = cos(loft);
  sinL = sin(loft);

  //  Calculate the pre-collision velocities normal
  //  and parallel to the line of action.
  vcp = cosL*velocity;
  vcn = -sinL*velocity;

  //  Compute the post-collision velocity of the ball
  //  along the line of action.
  vbp = (1.0+e)*clubMass*vcp/(clubMass+ballMass);

  //  Compute the post-collision velocity of the ball
  //  perpendicular to the line of action.
  vbn = (2.0/7.0)*clubMass*vcn/(clubMass+ballMass);

  //  Compute the initial spin rate assuming ball is
  //  rolling without sliding.
  omega = (5.0/7.0)*vcn/radius;

  //  Rotate post-collision ball velocities back into 
  //  standard Cartesian frame of reference. Because the
  //  line-of-action was in the xy plane, the z-velocity
  //  is zero.
  vx0 = cosL*vbp - sinL*vbn;
  vy0 = 0.0;
  vz0 = sinL*vbp + cosL*vbn;
  printf("vx0=%lf  vy0=%lf  vz0=%lf  omega=%lf\n",vx0,vy0,vz0,omega);

  //  Load the initial ball velocities into the 
  //  SpinProjectile struct.
  golfball.omega = omega;
  golfball.q[0] = vx0;   //  vx 
  golfball.q[2] = vy0;   //  vy 
  golfball.q[4] = vz0;   //  vz 

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
void projectileRungeKutta4(struct SpinProjectile *projectile, double ds) {

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
void projectileRightHandSide(struct SpinProjectile *projectile, 
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
  double Fdx;
  double Fdy;
  double Fdz;
  double vax;
  double vay;
  double vaz;
  double va;
  double windVx;
  double windVy;
  double rx;     
  double ry;     
  double rz;     
  double omega;  
  double radius; 
  double Cl;
  double Fm;
  double Fmx;
  double Fmy;
  double Fmz;

  double G = -9.81;

  int i;

  mass = projectile->mass;
  area = projectile->area;
  density = projectile->density;
  Cd = projectile->Cd;
  windVx = projectile->windVx;
  windVy = projectile->windVy;
  rx = projectile->rx;
  ry = projectile->ry;
  rz = projectile->rz;
  omega = projectile->omega;
  radius = projectile->radius;

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
  Fdx = -Fd*vax/va;
  Fdy = -Fd*vay/va;
  Fdz = -Fd*vaz/va;

  //  Compute the velocity magnitude
  v = sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

  //  Evaluate the Magnus force terms.
  Cl = -0.05 + sqrt(0.0025 + 0.36*fabs(radius*omega/v));;
  Fm = 0.5*density*area*Cl*v*v;
  Fmx =  (vy*rz - ry*vz)*Fm/v;
  Fmy = -(vx*rz - rx*vz)*Fm/v;
  Fmz =  (vx*ry - rx*vy)*Fm/v;

  //  Compute right-hand side values.
  dq[0] = ds*(Fdx + Fmx)/mass;
  dq[1] = ds*vx;
  dq[2] = ds*(Fdy + Fmy)/mass;
  dq[3] = ds*vy;
  dq[4] = ds*(G + (Fdz + Fmz)/mass);
  dq[5] = ds*vz;

  return;
}

