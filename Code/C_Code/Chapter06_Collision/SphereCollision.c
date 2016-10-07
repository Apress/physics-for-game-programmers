#include <stdio.h>
#include <math.h>

int main(int argc, char *argv[]) {

  //  These fields store sphere data and the coefficient 
  //  of restitution.
  double vx1;
  double x1;
  double mass1;
  double vx2;
  double x2;
  double mass2;
  double e;            //  coefficient of restitution
  double sphereRadius; //  radius of spheres
  double timeIncrement; 
  double distance;
  double tmp;
  double newVx1;
  double newVx2;
  double width;

  //  Initialize sphere location, velocity, and
  //  coefficient of restitution.
  vx1 = 10.0;
  x1 = 5.0;
  mass1 = 10.0;
  vx2 = -5.0;
  x2 = 15.0;
  mass2 = 5.0;
  e = 0.9;
  sphereRadius = 2.0;
  timeIncrement = 0.07;
  width = 300.0;

  while (1) {
    //  Determine if a collision occurs and if it
    //  does, change the velocties of the spheres.
    //  A collision occurs if the distance between the
    //  centers of the spheres is less than twice their
    //  radius
    distance = fabs(x1 - x2);
    tmp = 1.0/(mass1 + mass2);

    if ( distance <= 2.0*sphereRadius ) {
      printf("\nCollision occurred\n\n");
      newVx1 = (mass1 - e*mass2)*vx1*tmp + 
               (1.0 + e)*mass2*vx2*tmp;
      newVx2 = (1.0 + e)*mass1*vx1*tmp +
               (mass2 - e*mass1)*vx2*tmp;
      vx1 = newVx1;
      vx2 = newVx2;
    }

    //  Compute the new location of the spheres. 
    x1 = x1 + timeIncrement*vx1;
    x2 = x2 + timeIncrement*vx2;

    printf("x1=%lf  vx1=%lf  x2=%lf  vx2=%lf\n", x1, vx1, x2, vx2);

    //  If either of the spheres hits the outer edge
    //  of the display, stop the simulation.
    if ( 10.0*(x1 + sphereRadius) > width || 
         10.0*(x1 - sphereRadius) < 0.0 ||
         10.0*(x2 + sphereRadius) > width || 
         10.0*(x2 - sphereRadius) < 0.0 ) {
      break;
    }
  }

  return 0;
}

