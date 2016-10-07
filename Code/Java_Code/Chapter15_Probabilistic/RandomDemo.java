import java.util.Random;

public class RandomDemo
{
  public static void main(String args[]) {
    //  Create a Random object to generate random
    //  numbers. The object is seeded with the time
    //  in milliseconds since Jan 1, 1970.
    Random random = new Random(1234);

    double x;

    //  Retrieve 8 floating point random numbers
    //  between 0 and 1.
    for(int j=0; j<8; ++j) {
      x = random.nextDouble();
      System.out.println("x = "+x);
    }

    System.out.println();
    for(int j=0; j<8; ++j) {
      x = Math.random();
      System.out.println("x = "+x);
    }
  }
}
