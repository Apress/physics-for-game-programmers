using System;

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

  //  These properties access the fields declared in the class.
  public double XLocation {
    get {
      return xLocation;
    }
    set {
      xLocation = value;
    }
  }

  public double YLocation {
    get {
      return yLocation;
    }
    set {
      yLocation = value;
    }
  }

  public double Speed {
    get {
      return speed;
    }
    set {
      speed = value;
    }
  }
}
