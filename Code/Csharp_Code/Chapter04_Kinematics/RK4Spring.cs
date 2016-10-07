using System;

public class RK4Spring
{
  static void Main() {

    //  Create a SpringODE object that represents
    //  a 1.0 kg spring with a spring constant of
    //  20 N/m and a damping coefficient of 1.5 N-s/m
    double mass = 1.0;
    double mu = 1.5;
    double k = 20.0;
    double x0 = -0.2;

    SpringODE ode = new SpringODE(mass, mu, k, x0);

    //  Solve the ODE over a range of 7 seconds
    //  using a 0.1 second time increment.
    double dt = 0.1;

    Console.WriteLine("t   x   v");
    Console.WriteLine(""+ode.GetTime()+"  "+(float)ode.GetX()+
                      "  "+(float)ode.GetVx());

    while ( ode.GetTime() <= 7.0 ) {
      ode.UpdatePositionAndVelocity(dt);
      Console.WriteLine(""+ode.GetTime()+"  "+(float)ode.GetX()+
                         "  "+(float)ode.GetVx());
    }

    return;
  }
}
