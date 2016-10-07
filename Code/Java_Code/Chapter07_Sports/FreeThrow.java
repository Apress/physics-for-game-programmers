import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class FreeThrow extends JFrame implements ActionListener
{
  private JTextField velocityTextField;
  private JTextField angleTextField;

  private JLabel velocityLabel;
  private JLabel angleLabel;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  Declare a DragProjectile object that will model
  //  the basketball
  private DragProjectile basketball;

  //  A flag to tell if the shot was good.
  boolean shotMade;

  //  These fields are for the images used in the game.
  private ImageIcon playerIcon;
  private int playerWidth;
  private int playerHeight;
  private ImageIcon ballIcon;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public FreeThrow() {

    //  Create a DragProjectile object to represent the soccer ball.
    basketball = 
        new DragProjectile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.62, 0.0452, 1.2, 0.5);
    basketball.setQ(1.0,1);   //  x0
    basketball.setQ(2.25,5);  //  z0

    //  The shot is missed until it is made
    shotMade = false;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 20;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up image2 and determine their dimensions
    playerIcon = new ImageIcon("basketball_player.gif");
    playerWidth = playerIcon.getIconWidth();
    playerHeight = playerIcon.getIconHeight();
    ballIcon = new ImageIcon("Basketball.jpg");

    //  Create JTextField objects to input the initial
    //  conditions.
    velocityTextField = new JTextField("7.5",5);
    angleTextField = new JTextField("40.0",5);

    //  Create some JLabels
    velocityLabel = new JLabel("Initial velocity (m/s)");
    angleLabel = new JLabel("Shot angle (deg)");

    //  Create a JButton that will start the ball moving
    fireButton = new JButton("Fire");
    fireButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    fireButton.setPreferredSize(new Dimension(60,35));
    fireButton.addActionListener(this);

    //  Create a JButton that will update the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        //  stop the timer.
        gameTimer.stop();

        //  Reset the time, location, and velocity of ball;
        basketball.setS(0.0);     //  time
        basketball.setQ(0.0,0);   //  vx0
        basketball.setQ(1.0,1);   //  x0
        basketball.setQ(0.0,2);   //  vy0
        basketball.setQ(0.0,3);   //  y0
        basketball.setQ(0.0,4);   //  vz0
        basketball.setQ(2.25,5);  //  z0
        shotMade = false;

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(301, 251));

    //  Place components on a panel using a GridBagLayout
    JPanel westPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    westPanel.setLayout(gridBagLayout1);

    int col = 0;
    int row = 0;
    int numCol = 1;
    int numRow = 1;
    Insets insets = new Insets(5, 3, 5, 3);
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(angleLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(angleTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(velocityLabel);
    westPanel.add(velocityTextField);
    westPanel.add(angleLabel);
    westPanel.add(angleTextField);
    westPanel.add(fireButton);
    westPanel.add(resetButton);

    //  The drawing panel.
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout2.setConstraints(drawingPanel, gbc);

    eastPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(eastPanel, BorderLayout.EAST);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Free Throw");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,650,400);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Fire" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Set the shot made flag to be false.
    shotMade = false;

    //  Extract input values from textfields
    double velocity = Double.parseDouble(velocityTextField.getText());
    double angle = Double.parseDouble(angleTextField.getText());

    //  Convert the angle to radians.
    angle = angle*Math.PI/180.0;

    //  Compute initial velocities
    double vx0 = velocity*Math.cos(angle);
    double vy0 = 0.0;
    double vz0 = velocity*Math.sin(angle);

    //  The ball starts at a spot 18 meters from and directly
    //  in front of the goal.
    double x0 = 1.0;
    double y0 = 0.0;
    double z0 = 2.25;

    //  Define some basketball variables. The Cd value will be
    //  assumed to be constant.
    double ballMass = 0.62;
    double radius = 0.12;
    double density = 1.2;
    double area = Math.PI*radius*radius;
    double cd = 0.5;

    //  Create a DragProjectile object representing the basketball.
    basketball = new DragProjectile(x0, y0, z0, vx0, vy0, vz0, 
         0.0, ballMass, area, density, cd);

    //  Update the display
    updateDisplay();

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.start();
  }

  //  This method redraws the GUI display.
  private void updateDisplay() {
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, 0, width, 0);
    g.drawLine(width, 0, width, height);
    g.drawLine(width, height, 0, height);
    g.drawLine(0, height, 0, 0);

    //  Backboard
    g.fillRect(218, 87, 2, 47);

    //  Basket
    g.fillRect(198, 129, 74, 2);

    g.drawLine(198, 129, 203, 144);
    g.drawLine(203, 144, 212, 144);
    g.drawLine(212, 144, 217, 129);

    //  Floor support
    g.fillRect(272, 129, 2, 122);

     //  Draw basketball player
    g.drawImage(playerIcon.getImage(), 30, 
                150, playerWidth/2, playerHeight/2, drawingPanel);

    //  Update the position of the basketball
    //  on the screen.
    int xPosition = (int)(40.0*basketball.getX());
    int zPosition = (int)(height - 40.0*basketball.getZ());
    g.drawImage(ballIcon.getImage(), xPosition-5, 
                zPosition-5, 10, 10, drawingPanel);

  }

  public static void main(String args[]) {
    FreeThrow gui = new FreeThrow();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the basketball. 
      double timeIncrement = 0.025;
      basketball.updateLocationAndVelocity(timeIncrement);

      //  Update the display
      updateDisplay();

      //  Access the Graphics object of the drawing panel.
      Graphics g = drawingPanel.getGraphics();

       //  Determine if the ball impacts the backboard. If it does,
      //  change the x-velocity assuming a frictionless collision.
      //  A collision occurs if the x-velocity is positive, and the
      //  ball location is inside the backboard area.
      if ( basketball.getVx() > 0.0 && 
           basketball.getX() >= 5.5 && 
           basketball.getZ() > 2.93 && 
           basketball.getZ() < 4.0 ) {
        double e = 0.75;  // coefficient of restitution
        basketball.setQ(-e*basketball.getVx(),0);   //  vx
      }

     //  Determine if the shot is good.
      //  The center of the basket is 4.2 m from the free
      //  throw line (which is at x = 1.0 m). A shot is considered
      //  to be made if the center of the ball is within 0.22
      //  of the center of the basket.
      double dx = basketball.getX() - 5.2;
      double dz = basketball.getZ() - 3.048;
      double distance = Math.sqrt( dx*dx + dz*dz );
      if ( distance <= 0.14 ) {
        shotMade = true;
        basketball.setQ(0.0,0);   //  vx
      }

      //  If the basketball hits the ground, stop the simulation
      if ( basketball.getZ() <= 0.25 ) {
        if ( shotMade == true ) {
          g.drawString("Shot is good.", 80, 40); 
        }
        else {
          g.drawString("Shot missed.", 80, 40); 
        }

        //  Stop the simulation
        gameTimer.stop();
      }
    }
  }
}