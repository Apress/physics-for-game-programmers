public class TestSimpleProjectile {
  public static void main(String args[]) {
    double x0 = 0.0;
    double y0 = 0.0;
    double z0 = 0.0;
    double vx0 = 15.0;
    double vy0 = 0.0;
    double vz0 = 20.0;
    double x; 
    double y; 
    double z;
    double time = 0.0;
    double dt = 0.05;
    SimpleProjectile projectile = 
      new SimpleProjectile(x0, y0, z0, vx0, vy0, vz0, time);

    for(int i=0; i<83; ++i) {
      projectile.updateLocationAndVelocity(dt);

      time = projectile.getTime();
      x = projectile.getX();
      y = projectile.getY();
      z = projectile.getZ();
      System.out.println(""+time+"  "+x+"  "+y+"  "+z);
    }

    return;
  }
}
