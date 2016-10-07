#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a car.
//********************************************
struct Car {
   int numEqns;
   double s;
   double q[6];
   double mass;
   double area;
   double density;
   double Cd;

   double muR;
   double omegaE;
   double redline;
   double finalDriveRatio;
   double wheelRadius;
   int gearNumber;     //  gear the car is in
   int numberOfGears;  //  total number of gears
   char* mode;
   double gearRatio[7];  //  gear ratios
};


//***********************
//  Function prototypes
//***********************
void carRightHandSide(struct Car *car, double *q, double *deltaQ, 
                      double ds, double qScale, double *dq);
void carRungeKutta4(struct Car *car, double ds);


//******************************************************
//  Main method. It initializes a car and solves
//  for the car motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct Car car;
  double time;
  double x;
  double z;
  double vx;
  double gearRatio;
  int gearNumber;
  double oldGearRatio;
  double newGearRatio;
  double dt = 0.5;
  double pi = acos(-1.0);

  //  Initialize car parameters
  car.mass = 1393.0;
  car.area = 1.94;
  car.density = 1.2;
  car.Cd = 0.31;
  car.redline = 7200;
  car.finalDriveRatio = 3.44;
  car.wheelRadius = 0.3186;
  car.numberOfGears = 6;
  car.muR = 0.015;             //  coefficient of rolling friction
  car.omegaE = 1000.0;         //  engine rpm
  car.gearNumber = 1;          //  gear the car is in
  car.mode = "accelerating";   //  accelerating, cruising, or braking
  car.gearRatio[1] = 3.82;
  car.gearRatio[2] = 2.20;
  car.gearRatio[3] = 1.52;
  car.gearRatio[4] = 1.22;
  car.gearRatio[5] = 1.02;
  car.gearRatio[6] = 0.84;

  car.numEqns = 6;
  car.s = 0.0;      //  time 
  car.q[0] = 0.0;   //  vx 
  car.q[1] = 0.0;   //  x  
  car.q[2] = 0.0;   //  vy 
  car.q[3] = 0.0;   //  y  
  car.q[4] = 0.0;   //  vz 
  car.q[5] = 0.0;   //  z  

  //  accelerate the car for 40 seconds
  while ( car.s < 40.0 ) {
    carRungeKutta4(&car, dt);

    time = car.s;
    x = car.q[1];
    vx = car.q[0];

    //  Compute the new engine rpm value
    gearNumber = car.gearNumber;
    gearRatio = car.gearRatio[gearNumber];
    car.omegaE = vx*60.0*gearRatio*
                 car.finalDriveRatio/(2.0*pi*car.wheelRadius);

    printf("time=%lf  x=%lf  vx=%lf  gear=%d  rpm=%lf\n",
            time, x, vx, gearNumber, car.omegaE);

    //  If the engine is at the redline rpm value,
    //  shift gears upward.
    if ( car.omegaE > car.redline ) {
      oldGearRatio = gearRatio;
      ++car.gearNumber;
      newGearRatio = car.gearRatio[car.gearNumber];
      car.omegaE = car.omegaE*newGearRatio/oldGearRatio;
    }
  }

  return 0;
}

//************************************************************
//  This method solves for the car motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void carRungeKutta4(struct Car *car, double ds) {

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
  numEqns = car->numEqns;

  //  Allocate memory for the arrays.
  q = (double *)malloc(numEqns*sizeof(double));
  dq1 = (double *)malloc(numEqns*sizeof(double));
  dq2 = (double *)malloc(numEqns*sizeof(double));
  dq3 = (double *)malloc(numEqns*sizeof(double));
  dq4 = (double *)malloc(numEqns*sizeof(double));

  //  Retrieve the current values of the dependent
  //  and independent variables.
  s = car->s;
  for(j=0; j<numEqns; ++j) {
    q[j] = car->q[j];
  }     

  // Compute the four Runge-Kutta steps, The return 
  // value of carRightHandSide method is an array
  // of delta-q values for each of the four steps.
  carRightHandSide(car, q, q,   ds, 0.0, dq1);
  carRightHandSide(car, q, dq1, ds, 0.5, dq2);
  carRightHandSide(car, q, dq2, ds, 0.5, dq3);
  carRightHandSide(car, q, dq3, ds, 1.0, dq4);

  //  Update the dependent and independent variable values
  //  at the new dependent variable location and store the
  //  values in the ODE object arrays.
  car->s = car->s + ds;

  for(j=0; j<numEqns; ++j) {
    q[j] = q[j] + (dq1[j] + 2.0*dq2[j] + 2.0*dq3[j] + dq4[j])/6.0;
    car->q[j] = q[j];
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
//  This method loads the right-hand sides for the car ODEs
//*************************************************************
void carRightHandSide(struct Car *car, 
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
  double b;
  double d;
  double omegaE;
  double Fr;
  double c1;
  double c2;
  double c3;
  double tmp;
  double muR;
  double redline;
  double finalDriveRatio;
  double gearRatio;
  int gearNumber;
  double wheelRadius;
  double pi = acos(-1.0);

  int i;

  mass = car->mass;
  area = car->area;
  density = car->density;
  Cd = car->Cd;
  omegaE = car->omegaE;
  muR = car->muR;
  redline = car->redline;
  finalDriveRatio = car->finalDriveRatio;
  wheelRadius = car->wheelRadius;
  gearNumber = car->gearNumber;
  gearRatio = car->gearRatio[gearNumber];

  //  Compute the intermediate values of the 
  //  dependent variables.
  for(i=0; i<6; ++i) {
    newQ[i] = q[i] + qScale*deltaQ[i];
  }

  //  Compute the constants that define the
  //  torque curve line.
  if ( omegaE <= 1000.0 ) {
    b = 0.0;
    d = 220.0;
  }
  else if ( omegaE < 4600.0 ) {
    b = 0.025;
    d = 195.0; 
  }
  else {
    b = -0.032;
    d = 457.2;
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

  //  Compute the force of rolling friction. Because
  //  the G constant has a negative sign, the value 
  //  computed here will be negative
  Fr = muR*mass*G;

  //  Compute the right-hand sides of the six ODEs
  //  newQ[0] is the intermediate value of velocity.
  //  The acceleration of the car is determined by 
  //  whether the car is accelerating, cruising, or
  //  braking. The braking acceleration is assumed to
  //  be a constant -5.0 m/s^2.
  if ( !strcmp(car->mode,"accelerating") ) {
    c1 = -Fd/mass;
    tmp = gearRatio*finalDriveRatio/wheelRadius;
    c2 = 60.0*tmp*tmp*b*v/(2.0*pi*mass);
    c3 = (tmp*d + Fr)/mass;
    dq[0] = ds*(c1 + c2 + c3);
  }
  else if ( !strcmp(car->mode,"braking") ) {
    //  Only brake if the velocity is positive.
    if ( newQ[0] > 0.1 ) {
      dq[0] = ds*(-5.0);
    }
    else {
      dq[0] = 0.0;
    }
  }
  else {
    dq[0] = 0.0;
  }

  //  Compute right-hand side values.
  dq[1] = ds*newQ[0];
  dq[2] = 0.0;
  dq[3] = 0.0;
  dq[4] = 0.0;
  dq[5] = 0.0;

  return;
}

