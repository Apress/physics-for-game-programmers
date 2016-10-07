import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class GolfGame extends JFrame implements ActionListener
{
  private JTextField velocityTextField;
  private JTextField distanceTextField;
  private JTextField densityTextField;
  private JTextField windVxTextField;
  private JTextField windVyTextField;
  private JTextField rxTextField;
  private JTextField ryTextField;
  private JTextField rzTextField;

  private JLabel velocityLabel;
  private JLabel clubLabel;
  private JLabel distanceLabel;
  private JLabel axesLabel;
  private JLabel densityLabel;
  private JLabel windVxLabel;
  private JLabel windVyLabel;
  private JLabel rxLabel;
  private JLabel ryLabel;
  private JLabel rzLabel;
  private JLabel spinAxisLabel;

  private JComboBox clubComboBox;
  private JComboBox axesComboBox;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  Declare a GolfBall object
  private GolfBall golfball;

  //  The player can control the distance to the hole.
  private double distanceToHole;

  //  These fields are for the images used in the game.
  private ImageIcon golferIcon;
  private ImageIcon flagIcon;
  private int golferWidth;
  private int golferHeight;
  private int flagWidth; 
  private int flagHeight;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public GolfGame() {

    //  Create a GolfBall object representing the golf ball.
    golfball = 
        new GolfBall(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.0459, 0.001432, 0.5, 1.225, 0.0, 0.0,
                           0.0, 1.0, 0.0, 300.0, 0.02135);

    //  Initialize the distanceToHole field.
    distanceToHole = 200.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up some images and determine their dimensions
    golferIcon = new ImageIcon("Golfer.jpg");
    golferWidth = golferIcon.getIconWidth();
    golferHeight = golferIcon.getIconHeight();

    flagIcon = new ImageIcon("Hole_Cartoon.jpg");
    flagWidth = flagIcon.getIconWidth();
    flagHeight = flagIcon.getIconHeight();

    //  Create JTextField objects to input the initial
    //  golfball velocities and distance to hole.
    velocityTextField = new JTextField("40.0",6);
    distanceTextField = new JTextField("200.0",6);
    densityTextField = new JTextField("1.225",6);
    windVxTextField = new JTextField("0.0",6);
    windVyTextField = new JTextField("0.0",6);
    rxTextField = new JTextField("0.0",6);
    ryTextField = new JTextField("1.0",6);
    rzTextField = new JTextField("0.0",6);

    //  Create some JLabels
    velocityLabel = new JLabel("Impact velocity, m/s");
    clubLabel = new JLabel("Club");
    distanceLabel = new JLabel("Distance to hole, m");
    axesLabel = new JLabel("View axes");
    densityLabel = new JLabel("density (kg/m^3)");
    windVxLabel = new JLabel("Wind vx, m/s");
    windVyLabel = new JLabel("Wind vy, m/s");
    rxLabel = new JLabel("rx");
    ryLabel = new JLabel("ry");
    rzLabel = new JLabel("rz");
    spinAxisLabel = new JLabel("Spin axes");

    //  Create a JComboBox to choose the coordinate axes that 
    //  will be displayed.
    axesComboBox = new JComboBox();
    axesComboBox.addItem("XZ");
    axesComboBox.addItem("XY");

    //  Create a JComboBox to choose the club.
    clubComboBox = new JComboBox();
    clubComboBox.addItem("Driver");
    clubComboBox.addItem("3 wood");
    clubComboBox.addItem("3 iron");
    clubComboBox.addItem("5 iron");
    clubComboBox.addItem("7 iron");
    clubComboBox.addItem("9 iron");

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
        golfball.setS(0.0);     //  time
        golfball.setQ(0.0,0);   //  vx0
        golfball.setQ(0.0,1);   //  x0
        golfball.setQ(0.0,2);   //  vy0
        golfball.setQ(0.0,3);   //  y0
        golfball.setQ(0.0,4);   //  vz0
        golfball.setQ(0.0,5);   //  z0

        //  Reset the distance to hole.
        distanceToHole = Double.parseDouble(distanceTextField.getText());
 
        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(501, 201));

    //  Place components on a panel using a GridBagLayout
    JPanel westPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    westPanel.setLayout(gridBagLayout1);

    int col;
    int row;
    int numCol = 1;
    int numRow = 1;
    Insets insets = new Insets(5, 3, 5, 3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(clubLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(clubComboBox, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(distanceLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(distanceTextField, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(axesLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(axesComboBox, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    row = 0;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(densityLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(densityTextField, gbc);

    row = 1;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(windVxLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(windVxTextField, gbc);

    row = 2;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(windVyLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(windVyTextField, gbc);

    row = 3;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(spinAxisLabel, gbc);

    row = 4;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rxLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rxTextField, gbc);

    row = 5;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(ryLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(ryTextField, gbc);

    row = 6;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rzLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rzTextField, gbc);

    westPanel.add(clubLabel);
    westPanel.add(clubComboBox);
    westPanel.add(velocityLabel);
    westPanel.add(velocityTextField);
    westPanel.add(distanceLabel);
    westPanel.add(distanceTextField);
    westPanel.add(axesLabel);
    westPanel.add(axesComboBox);
    westPanel.add(densityLabel);
    westPanel.add(densityTextField);
    westPanel.add(windVxLabel);
    westPanel.add(windVxTextField);
    westPanel.add(windVyLabel);
    westPanel.add(windVyTextField);
    westPanel.add(spinAxisLabel);
    westPanel.add(rxLabel);
    westPanel.add(rxTextField);
    westPanel.add(ryLabel);
    westPanel.add(ryTextField);
    westPanel.add(rzLabel);
    westPanel.add(rzTextField);
    westPanel.add(fireButton);
    westPanel.add(resetButton);

    //  The drawing panel.
    JPanel southPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    southPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout2.setConstraints(drawingPanel, gbc);

    southPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(southPanel, BorderLayout.SOUTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Golf Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,650,550);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Fire" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Define golf ball parameters
    double ballMass = 0.0459;
    double radius = 0.02135;
    double area = Math.PI*radius*radius;
    double cd = 0.22;   //  drag coefficient
    double e = 0.78;   //  coefficient of restitution
    
    //  Get some initial quantities from the textfields.
    double velocity = Double.parseDouble(velocityTextField.getText());
    double density = Double.parseDouble(densityTextField.getText());
    distanceToHole = Double.parseDouble(distanceTextField.getText());
    double windVx = Double.parseDouble(windVxTextField.getText());
    double windVy = Double.parseDouble(windVyTextField.getText());
    double rx = Double.parseDouble(rxTextField.getText());
    double ry = Double.parseDouble(ryTextField.getText());
    double rz = Double.parseDouble(rzTextField.getText());

    //  Set the club mass and loft based on the combo box 
    //  selection
    double clubMass;
    double loft;
    String club = (String)clubComboBox.getSelectedItem();
    if ( club.equals("Driver") ) {
      clubMass = 0.2;
      loft = 11.0;
    }
    else if ( club.equals("3 wood") ) {
      clubMass = 0.208;
      loft = 15.0;
    }
    else if ( club.equals("3 iron") ) {
      clubMass = 0.239;
      loft = 21.0;
    }
    else if ( club.equals("5 iron") ) {
      clubMass = 0.253;
      loft = 27.0;
    }
    else if ( club.equals("7 iron") ) {
      clubMass = 0.267;
      loft = 35.0;
    }
    else {
      clubMass = 0.281;
      loft = 43.0;
    }

    //  Convert the loft angle from degrees to radians and
    //  assign values to some convenience variables.
    loft = loft*Math.PI/180.0;
    double cosL = Math.cos(loft);
    double sinL = Math.sin(loft);

    //  Calculate the pre-collision velocities normal
    //  and parallel to the line of action.
    double vcp = cosL*velocity;
    double vcn = -sinL*velocity;

    //  Compute the post-collision velocity of the ball
    //  along the line of action.
    double vbp = (1.0+e)*clubMass*vcp/(clubMass+ballMass);

    //  Compute the post-collision velocity of the ball
    //  perpendicular to the line of action.
    double vbn = (2.0/7.0)*clubMass*vcn/(clubMass+ballMass);

    //  Compute the initial spin rate assuming ball is
    //  rolling without sliding.
    double omega = (5.0/7.0)*vcn/radius;

    //  Rotate post-collision ball velocities back into 
    //  standard Cartesian frame of reference. Because the
    //  line-of-action was in the xy plane, the z-velocity
    //  is zero.
    double vx0 = cosL*vbp - sinL*vbn;
    double vy0 = 0.0;
    double vz0 = sinL*vbp + cosL*vbn;
System.out.println("vx0="+vx0+"  vy0="+vy0+"  vz0="+vz0+
"  omega="+omega);

    //  Create a GolfBall object representing the golf ball.
    golfball = new GolfBall(0.0, 0.0, 0.0, vx0, vy0, vz0, 
         0.0, ballMass, area, density, cd, windVx, windVy,
         rx, ry, rz, omega, radius);

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

    //  Draw picture based on whether the XZ or 
    //  XY axes are selected.
    String axes = (String)axesComboBox.getSelectedItem();
    if ( axes.equals("XZ") ) {

      //  Draw the golfer.
      int zLocation = height - 50;
      g.drawImage(golferIcon.getImage(), 0, zLocation, 34, 50, drawingPanel);

      //  Draw the flag
      zLocation = height - 62;
      g.drawImage(flagIcon.getImage(), (int)(2.0*distanceToHole), 
                  zLocation, 55, 62, drawingPanel);

      //  Update the position of the golfball
      //  on the screen.
      g.setColor(Color.BLACK);
      int xPosition = (int)(2.0*golfball.getX() + 14);
      int zPosition = (int)(height - 5 - 2.0*golfball.getZ());
      g.fillOval(xPosition, zPosition, 5, 5);
    }
    else {
      //  Draw location of green.
      g.setColor(Color.BLACK);
      g.drawOval((int)(2.0*distanceToHole - 20), 80, 40, 40);
      g.fillOval((int)(2.0*distanceToHole - 4), 96, 8, 8);

      //  Update the position of the golfball
      //  on the screen.
      int xPosition = (int)(2.0*golfball.getX());
      int yPosition = (int)(100 - 2 - 2.0*golfball.getY());
      g.fillOval(xPosition, yPosition, 5, 5);
    }

  }

  public static void main(String args[]) {
    GolfGame gui = new GolfGame();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the golfball. 
      double timeIncrement = 0.07;
      golfball.updateLocationAndVelocity(timeIncrement);

      //  Update the display
      updateDisplay();

      //  Access the Graphics object of the drawing panel.
      Graphics g = drawingPanel.getGraphics();

      //  When the golfball hits the ground, stop the simulation
      //  and see where ball has landed.
      if ( golfball.getZ() <= 0.0 ) {

System.out.println("time="+(float)golfball.getTime()+
"  x="+(float)golfball.getX()+
"  y="+(float)golfball.getY()+"  z="+(float)golfball.getZ());
System.out.println("time="+(float)golfball.getTime()+
"  vx="+(float)golfball.getVx()+
"  vy="+(float)golfball.getVy()+"  vz="+(float)golfball.getVz());

        //  Stop the simulation
        gameTimer.stop();

        //  Determine if ball is on the green.
        if ( golfball.getX() > distanceToHole - 8.0 &&
             golfball.getX() < distanceToHole + 8.0 &&
             golfball.getY() < 8.0) {
          g.drawString("You're on the green", 100, 30);

        }
        else {
//          g.drawString("Shot distance = "+golfball.getX(), 100, 30);
        }
      }
    }
  }
}