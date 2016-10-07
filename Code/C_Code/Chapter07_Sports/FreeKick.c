#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a soccer ball
//********************************************
struct SoccerBall {
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
   double temperature;  //  air temperature
};


//***********************
//  Function prototypes
//***********************
void projectileRightHandSide(struct SoccerBall *projectile, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq);
void projectileRungeKutta4(struct SoccerBall *projectile, double ds);


//******************************************************
//  Main method. It initializes a spin projectile and solves
//  for the soccer ball motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct SoccerBall soccerball;
  double time;
  double x;
  double y;
  double z;
  double dt = 0.025;

  //  Initialize soccerball parameters
  soccerball.mass = 0.43;
  soccerball.area = 0.038013;
  soccerball.density = 1.2;
  soccerball.Cd = 0.25;
  soccerball.windVx = 0.0;
  soccerball.windVy = 0.0;
  soccerball.rx = 0.0;
  soccerball.ry = 0.0;
  soccerball.rz = -1.0;
  soccerball.omega = 10.0;
  soccerball.radius = 0.11;
  soccerball.temperature = 294.0;
  soccerball.numEqns = 6;
  soccerball.s = 0.0;      //  time 
  soccerball.q[0] = -28.0; //  vx 
  soccerball.q[1] = 23.2;  //  x  
  soccerball.q[2] = 10.0;  //  vy 
  soccerball.q[3] = 15.0;  //  y  
  soccerball.q[4] = 4.0;   //  vz 
  soccerball.q[5] = 0.0;   //  z  

  //  Fly the soccer ball
  while ( soccerball.q[1] >= 3.0 ) {
    projectileRungeKutta4(&soccerball, dt);

    time = soccerball.s;
    x = soccerball.q[1];
    y = soccerball.q[3];
    z = soccerball.q[5];
    printf("time = %lf  x = %lf  y = %lf  z = %lf\n",
            time, x, y, z);

    //  See if the soccer ball hits the defenders
    if ( x <= 7.5 && x >= 6.5 ) {
      if ( y < 17.6 && y > 12.1 ) {
        printf("Hit defenders\n"); 
        break;
      }
    }

    //  When the soccerBall passes the end line, stop the 
    //  simulation and see if a goal was scored.
    if ( x <= 3.0 ) {
      if ( y < 11.3) {
        printf("Wide Left\n"); 
      }
      else if ( y > 18.5) {
        printf("Wide Right\n"); 
      }   
      else if ( z > 2.44) {
        printf("Over the Crossbar\n"); 
      }   
      else {
        printf("GOAL!  GOAL!\n"); 
      } 

      break;
    }
  }

  return 0;
}

//************************************************************
//  This method solves for the projectile motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void projectileRungeKutta4(struct SoccerBall *projectile, double ds) {

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
void projectileRightHandSide(struct SoccerBall *projectile, 
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

  double temperature;
  double viscosity;
  double Re;
  double rotSpinRatio;

  double G = -9.81;
  double pi = acos(-1.0);

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
  temperature = projectile->temperature;

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

  //  Compute the velocity magnitude
  v = sqrt(vx*vx + vy*vy + vz*vz) + 1.0e-8;

  //  Compute the drag coefficient, which depends on
  //  the Reynolds number.
  viscosity = 1.458e-6*pow(temperature,1.5)/
                     (temperature + 110.4);
  Re = density*v*2.0*radius/viscosity;

  if ( Re < 1.0e+5 ) { 
    Cd = 0.47;
  }
  else if ( Re > 1.35e+5 ) {
    Cd = 0.22;
  }
  else {
    Cd = 0.47 - 0.25*(Re - 1.0e+5)/35000.0;
  }

  //  Compute the total drag force.
  Fd = 0.5*density*area*Cd*va*va;
  Fdx = -Fd*vax/va;
  Fdy = -Fd*vay/va;
  Fdz = -Fd*vaz/va;

  //  Calculate the angular velocity from the spin rate.
  omega = omega*2.0*pi;

  //  Evaluate the Magnus force terms.
  rotSpinRatio = fabs(radius*omega/v);
  Cl = 0.385*pow(rotSpinRatio, 0.25);
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

