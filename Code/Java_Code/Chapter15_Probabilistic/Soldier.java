public class Soldier
{
  private double xLocation;
  private double yLocation;
  private double speed;

  public Soldier() {
    xLocation = 0.0;
    yLocation = 0.0;
    speed = 0.0;
  }

  //  These methods return the value of
  //  the fields declared in the class.
  public double getXLocation() {
    return xLocation;
  }

  public double getYLocation() {
    return yLocation;
  }

  public double getSpeed() {
    return speed;
  }

  //  These methods change the value of
  //  the fields declared in the class.
  public void setXLocation(double value) {
    xLocation = value;
  }

  public void setYLocation(double value) {
    yLocation = value;
  }

  public void setSpeed(double value) {
    speed = value;
  }

}
