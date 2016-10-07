#include <stdio.h>
#include <math.h>
#include <malloc.h>

//********************************************
//  This structure defines the data required
//  to model a rocket.
//********************************************
struct Rocket {
   int numEqns;
   double s;
   double q[10];

   int numberOfEngines;
   double seaLevelThrustPerEngine;
   double vacuumThrustPerEngine;
   double rocketDiameter;
   double cd;
   double initialMass;
   double burnTime;
};

struct USatm76 {
  double pressure;
  double density;
  double temperature;
};


//***********************
//  Function prototypes
//***********************
void rocketRightHandSide(struct Rocket *rocket, double *q, double *deltaQ, 
                      double ds, double qScale, double *dq);
void rocketRungeKutta4(struct Rocket *rocket, double ds);
void updateConditions(struct USatm76 *air, double altitude);


//******************************************************
//  Main method. It initializes a rocket and solves
//  for the rocket motion using the Runge-Kutta solver
//******************************************************
int main(int argc, char *argv[]) {

  struct Rocket rocket;

  double x;
  double z;
  double v;
  double time;
  double mass;
  double dt = 1.0;
  double pi = acos(-1.0);

  //  Set rocket data
  rocket.numberOfEngines = 1;
  rocket.seaLevelThrustPerEngine = 6670000.0;
  rocket.vacuumThrustPerEngine = 7860000.0;
  rocket.rocketDiameter = 10.0;
  rocket.cd = 0.5;
  rocket.initialMass = 424771.0;
  rocket.burnTime = 150.0;

  rocket.numEqns = 10;
  rocket.s = 0.0;      //  time 
  rocket.q[0] = 0.0;   //  vx 
  rocket.q[1] = 0.0;   //  x  
  rocket.q[2] = 0.0;   //  vy 
  rocket.q[3] = 0.0;   //  y  
  rocket.q[4] = 0.0;   //  vz 
  rocket.q[5] = 0.0;   //  z  
  rocket.q[6] = 2616.0;   //  mass flow rate  
  rocket.q[7] = rocket.initialMass;   //  rocket mass  
  rocket.q[8] = -80*pi/(180.0*rocket.burnTime);   //  d(theta)/dt  
  rocket.q[9] = 0.5*pi;   //  theta = pitch angle  

  //  launch the rocket.
  while ( rocket.s < rocket.burnTime ) {
    rocketRungeKutta4(&rocket, dt);

    time = rocket.s;
    x = rocket.q[1];
    z = rocket.q[5];
    mass = rocket.q[7];
    v = sqrt( rocket.q[0]*rocket.q[0] + rocket.q[2]*rocket.q[2] +
              rocket.q[4]*rocket.q[4] );

    printf("time=%lf  x=%lf  altitude=%lf  mass=%lf  airspeed=%lf\n",
           time, x, z, mass, v);
  }

  return 0;
}

//************************************************************
//  This method solves for the rocket motion using a
//  4th-order Runge-Kutta solver
//************************************************************
void rocketRungeKutta4(struct Rocket *rocket, double ds) {

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
  numEqns = rocket->numEqns;

  //  Allocate memory for the arrays.
  q = (double *)malloc(numEqns*sizeof(double));
  dq1 = (double *)malloc(numEqns*sizeof(double));
  dq2 = (double *)malloc(numEqns*sizeof(double));
  dq3 = (double *)malloc(numEqns*sizeof(double));
  dq4 = (double *)malloc(numEqns*sizeof(double));

  //  Retrieve the current values of the dependent
  //  and independent variables.
  s = rocket->s;
  for(j=0; j<numEqns; ++j) {
    q[j] = rocket->q[j];
  }     

  // Compute the four Runge-Kutta steps, The return 
  // value of rocketRightHandSide method is an array
  // of delta-q values for each of the four steps.
  rocketRightHandSide(rocket, q, q,   ds, 0.0, dq1);
  rocketRightHandSide(rocket, q, dq1, ds, 0.5, dq2);
  rocketRightHandSide(rocket, q, dq2, ds, 0.5, dq3);
  rocketRightHandSide(rocket, q, dq3, ds, 1.0, dq4);

  //  Update the dependent and independent variable values
  //  at the new dependent variable location and store the
  //  values in the ODE object arrays.
  rocket->s = rocket->s + ds;

  for(j=0; j<numEqns; ++j) {
    q[j] = q[j] + (dq1[j] + 2.0*dq2[j] + 2.0*dq3[j] + dq4[j])/6.0;
    rocket->q[j] = q[j];
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
//  This method loads the right-hand sides for the rocket ODEs
//*************************************************************
void rocketRightHandSide(struct Rocket *rocket, 
                             double *q, double *deltaQ, double ds, 
                             double qScale, double *dq) {
  //  q[0] = vx = dxdt
  //  q[1] = x
  //  q[2] = vy = dydt
  //  q[3] = y
  //  q[4] = vz = dzdt
  //  q[5] = z
  //  q[6] = mass flow rate = dm/dt
  //  q[7] = mass
  //  q[8] = omega = d(theta)/dt
  //  q[9] = theta
  double newQ[10]; // intermediate dependent variable values.

  int numberOfEngines;
  double seaLevelThrustPerEngine;
  double vacuumThrustPerEngine;
  double rocketDiameter;
  double cd;
  double initialMass;
  double burnTime;

  double vx;
  double vy;
  double vz;
  double vtotal;
  double x;
  double y;
  double z;
  double massFlowRate;
  double mass;
  double omega;
  double theta;
  double pressure;
  double density;
  double pressureRatio;
  double thrustPerEngine;
  double thrust;
  double area;
  double drag;
  double re;  
  double g;
  double lift;
  double Fx;
  double Fz;

  int i;
  double pi = acos(-1.0);
  struct USatm76 air;

  numberOfEngines = rocket->numberOfEngines;
  seaLevelThrustPerEngine = rocket->seaLevelThrustPerEngine;
  vacuumThrustPerEngine = rocket->vacuumThrustPerEngine;
  rocketDiameter = rocket->rocketDiameter;
  cd = rocket->cd;
  initialMass = rocket->initialMass;
  burnTime = rocket->burnTime;

  //  Compute the intermediate values of the 
  //  dependent variables.
  for(i=0; i<10; ++i) {
    newQ[i] = q[i] + qScale*deltaQ[i];
  }

  //  Assign convenenience variables to the intermediate 
  //  values of the locations and velocities.
  vx = newQ[0];
  vy = newQ[2];
  vz = newQ[4];
  vtotal = sqrt(vx*vx + vy*vy + vz*vz);
  x = newQ[1];
  y = newQ[3];
  z = newQ[5];
  massFlowRate = newQ[6];
  mass = newQ[7];
  omega = newQ[8];
  theta = newQ[9];

  //  Update the values of pressure, density, and
  //  temperature based on the current altitude.
  updateConditions(&air, z);
  pressure = air.pressure;
  density = air.density;

  //  Compute the thrust per engine and total thrust
  pressureRatio = pressure/101325.0;
  thrustPerEngine = vacuumThrustPerEngine - 
       (vacuumThrustPerEngine - seaLevelThrustPerEngine)*pressureRatio;
  thrust = numberOfEngines*thrustPerEngine;

  //  Compute the drag force based on the frontal area
  //  of the rocket.
  area = 0.25*pi*rocketDiameter*rocketDiameter;
  drag = 0.5*cd*density*vtotal*vtotal*area;

  //  Compute the gravitational acceleration
  //  as a function of altitude
  re = 6356766.0;  // radius of the Earth in meters.
  g = 9.80665*re*re/pow(re+z,2.0);

  //  For this simulation, lift will be assumed to be zero.
  lift = 0.0;

  //  Compute the force components in the x- and z-directions.
  //  The rocket will be assumed to be traveling in the x-z plane.
  Fx = (thrust - drag)*cos(theta) - lift*sin(theta);
  Fz = (thrust - drag)*sin(theta) + lift*cos(theta) - mass*g;

  //  Load the right-hand sides of the ODE's
  dq[0] = ds*(Fx/mass);
  dq[1] = ds*vx;
  dq[2] = 0.0;   //  y-component of accleration = 0
  dq[3] = 0.0;
  dq[4] = ds*(Fz/mass);
  dq[5] = ds*vz;
  dq[6] = 0.0;   //  mass flow rate is constant
  dq[7] = -ds*(massFlowRate*numberOfEngines);
  dq[8] = 0.0;   //  d(theta)/dt is constant
  dq[9] = ds*omega;

  return;
}

//*************************************************************
//  This method computes atmospheric pressure, density,
//  and temperature using the USatm76 model
//*************************************************************
void updateConditions(struct USatm76 *air, double altitude) {
    double slope;  //  slope of the temperature line
    double T0;     //  reference temperature value
    double p0;     //  reference pressure value
    double h0;     //  reference altitude
    double geoAltitude;  //  geopotential altitude
    double grp;    //  temporary variable
    double grp2;   //  temporary variable
    double R = 287.1;    //  Gas constant for air
    double G = 9.80665;  //  Gravity acceleration
    double RE = 6356766.0; // Earth radius in meters
    double pressure;
    double temperature;
    double density;

    //  The 1976 US Standard Atmosphere model equations 
    //  are functions of geopotential altitude, so we 
    //  need to compute it. Geopotential altitude is an 
    //  equivalent altitude assuming gravity is constant
    //  with altitude.
    geoAltitude = altitude*RE/(altitude + RE);

    //  Assign values to the reference temperature,
    //  pressure, and altitude based on the current
    //  altitude
    if (geoAltitude <= 11000.0) {
      slope = -0.0065;
      T0 = 288.15;
      p0 = 101325.0;
      h0 = 0.0;
    }
    else if (geoAltitude < 20000.0) {
      slope = 0.0;
      T0 = 216.65;
      p0 = 22631.9;
      h0 = 11000.0;
    }
    else if (geoAltitude < 32000.0) {
      slope = 0.001;
      T0 = 216.65;
      p0 = 5474.8;
      h0 = 20000.0;
    }
    else if (geoAltitude < 47000.0) {
      slope = 0.0028;
      T0 = 228.65;
      p0 = 868.0;
      h0 = 32000.0;
    }
    else if (geoAltitude < 51000.0) {
      slope = 0.0;
      T0 = 270.65;
      p0 = 110.9;
      h0 = 47000.0;
    }
    else if (geoAltitude < 71000.0) {
      slope = -0.0028;
      T0 = 270.65;
      p0 = 66.9;
      h0 = 51000.0;
    }
    else if (geoAltitude < 84000.0) {
      slope = -0.002;
      T0 = 214.65;
      p0 = 3.96;
      h0 = 71000.0;
    }
    else {
      slope = 0.0;
      T0 = 186.9;
      p0 = 0.373;
      h0 = 84000.0;
    }

    //  Compute temperature and pressure. The equations
    //  used depend on whether the temperature is constant
    //  in the current altitude range.
    if ( slope == 0.0 ) {
      temperature = T0;
      grp = -G*(geoAltitude - h0)/(R*temperature);
      pressure = p0*exp(grp);
    }
    else {
      temperature = T0 + slope*(geoAltitude - h0);
      grp = T0/temperature;
      grp2 = G/(slope*R);
      pressure = p0*pow(grp,grp2);
    }

    density = pressure/(R*temperature);

    air->pressure = pressure;
    air->temperature = temperature;
    air->density = density;

    return;
}

