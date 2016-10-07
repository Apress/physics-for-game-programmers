import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class BoatSimulator extends JFrame implements ActionListener
{
  private JTextField velocityTextField;
  private JTextField distanceTextField;
  private JTextField timeTextField;

  private JLabel velocityLabel;
  private JLabel distanceLabel;
  private JLabel timeLabel;

  private JRadioButton accelButton;
  private JRadioButton cruiseButton;
  private JRadioButton brakeButton;
  private ButtonGroup buttonGroup;

  private JPanel drawingPanel;

  private JButton startButton;
  private JButton stopButton;
  private JButton resetButton;
  private GridBagConstraints gbc;

  private FountainLightning boat; 

  private GameUpdater gameUpdater;
  private Timer gameTimer;

  //  These fields are for the images used in the game.
  private ImageIcon noPlaneIcon;
  private int noPlaneWidth; 
  private int noPlaneHeight;
  private ImageIcon halfPlaneIcon;
  private int halfPlaneWidth; 
  private int halfPlaneHeight;
  private ImageIcon fullPlaneIcon;
  private int fullPlaneWidth; 
  private int fullPlaneHeight;

  //  A rectangular marker is used to simulate motion.
  //  This field stores its x-location.
  private double rectangleOneX;

  public BoatSimulator() {
    //  Create a FountainLighning object.
    double x0 = 0.0;
    double y0 = 0.0;
    double z0 = 0.0;
    double vx0 = 0.0;
    double vy0 = 0.0;
    double vz0 = 0.0;
    double time = 0.0;
    double planingSpeed = 8.44;
    boat = new FountainLightning(x0, y0, z0, vx0, vy0, vz0, 
                                 time, planingSpeed);

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. 
    gameUpdater = new GameUpdater();
    gameTimer = new Timer(100, gameUpdater);

    //  Set up some images and determine their dimensions
    noPlaneIcon = new ImageIcon("Boat_NoPlane.jpg");
    noPlaneWidth = noPlaneIcon.getIconWidth();
    noPlaneHeight = noPlaneIcon.getIconHeight();

    halfPlaneIcon = new ImageIcon("Boat_HalfPlane.jpg");
    halfPlaneWidth = halfPlaneIcon.getIconWidth();
    halfPlaneHeight = halfPlaneIcon.getIconHeight();

    fullPlaneIcon = new ImageIcon("Boat_FullPlane.jpg");
    fullPlaneWidth = fullPlaneIcon.getIconWidth();
    fullPlaneHeight = fullPlaneIcon.getIconHeight();

    //  Initialize the x-location of the rectangular marker
    rectangleOneX = 200.0;

    //  Create JTextField objects to input the initial
    //  conditions.
    velocityTextField = new JTextField("0.0",10);
    distanceTextField = new JTextField("0.0",10);
    timeTextField = new JTextField("0.0",10);

    //  Create some JLabels
    velocityLabel = new JLabel("Velocity (km/hr)");
    distanceLabel = new JLabel("Distance traveled (m)");
    timeLabel = new JLabel("Time (s)");

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(401, 151));

    //  Create a JRadioButton to accelerate the boat.
    accelButton = new JRadioButton("Accelerate");
    accelButton.setName("Accelerate");
    accelButton.setPreferredSize(new Dimension(100,35));
    accelButton.setHorizontalTextPosition(SwingConstants.LEADING);
    accelButton.setSelected(true);

    //  Create a JRadioButton to cruise at constant velocity.
    cruiseButton = new JRadioButton("Cruise");
    cruiseButton.setName("Cruise");
    cruiseButton.setPreferredSize(new Dimension(100,35));
    cruiseButton.setHorizontalTextPosition(SwingConstants.LEADING);

    //  Create a JRadioButton to decelerate the boat.
    brakeButton = new JRadioButton("Decelerate");
    brakeButton.setName("Decelerate");
    brakeButton.setPreferredSize(new Dimension(100,35));
    brakeButton.setHorizontalTextPosition(SwingConstants.LEADING);

    //  Use a ButtonGroup to make the radio buttons
    //  mutually exclusive
    buttonGroup = new ButtonGroup();
    buttonGroup.add(accelButton);
    buttonGroup.add(cruiseButton);
    buttonGroup.add(brakeButton);

    //  Create a JButton that will start the boat moving
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will stop the boat,  
    //  but the distance and time values are unchanged.
    stopButton = new JButton("Stop");
    stopButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    stopButton.setPreferredSize(new Dimension(60,35));
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        gameTimer.stop();
        boat.setQ(0.0,0);   //  vx0 = 0.0
        accelButton.setSelected(true);
        resetDisplay();
      }
    });

    //  Create a JButton that will reset the boat fields
    //  to default values.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        gameTimer.stop();

        //  Reset the time, location, and velocity of boat;
        boat.setS(0.0);     //  time = 0.0
        boat.setQ(0.0,0);   //  vx0 = 0.0
        boat.setQ(0.0,1);   //  x0 = 0.0
        boat.setQ(0.0,2);   //  vy0 = 0.0
        boat.setQ(0.0,3);   //  y0 = 0.0
        boat.setQ(0.0,4);   //  vz0 = 0.0
        boat.setQ(0.0,5);   //  z0 = 0.0

        rectangleOneX = 200.0;
        accelButton.setSelected(true);
        resetDisplay();
      }
    });

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
    gridBagLayout1.setConstraints(accelButton, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(cruiseButton, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(brakeButton, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(stopButton, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(accelButton);
    westPanel.add(cruiseButton);
    westPanel.add(brakeButton);
    westPanel.add(startButton);
    westPanel.add(stopButton);
    westPanel.add(resetButton);

    //  Place components on a panel using a GridBagLayout
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(velocityLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(velocityTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(distanceLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(distanceTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(timeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(timeTextField, gbc);

    eastPanel.add(velocityLabel);
    eastPanel.add(velocityTextField);
    eastPanel.add(distanceLabel);
    eastPanel.add(distanceTextField);
    eastPanel.add(timeLabel);
    eastPanel.add(timeTextField);

    //  The drawing panel.
    JPanel northPanel = new JPanel();
    GridBagLayout gridBagLayout4 = new GridBagLayout();
    northPanel.setLayout(gridBagLayout4);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout4.setConstraints(drawingPanel, gbc);
    northPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(eastPanel, BorderLayout.EAST);
    getContentPane().add(northPanel, BorderLayout.NORTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Boat Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,450);
    setVisible(true);
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    //  Start the boat moving by calling the start
    //  method on the Timer object..
    gameTimer.start();
  }

  //  This method updates the textfield displays
  private void resetDisplay() { 
    distanceTextField.setText(""+(int)boat.getX());
    timeTextField.setText(""+(float)boat.getTime());

    //  Convert the velocity from m/s to km/hr and
    //  only show integer values
    velocityTextField.setText(""+(int)(boat.getVx()*3.6));

    //  Get the graphics object on the JPanel
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, 70, width, 70);

    //  Draw the boat depending on whether the boat
    //  is planing or not.
    if ( boat.getVx() < boat.getPlaningSpeed() ) {
      g.drawImage(noPlaneIcon.getImage(), 100, 95, 
                noPlaneWidth, noPlaneHeight, drawingPanel);
    }
    else if ( boat.getVx() < boat.getPlaningSpeed() + 2.0 ) {
      g.drawImage(halfPlaneIcon.getImage(), 100, 95, 
                halfPlaneWidth, halfPlaneHeight, drawingPanel);
    }
    else {
      g.drawImage(fullPlaneIcon.getImage(), 100, 95, 
                fullPlaneWidth, fullPlaneHeight, drawingPanel);
    }

    //  Draw the marker
    g.fillRect((int)rectangleOneX, 40, 10, 30);
  }

  public static void main(String args[]) {
    BoatSimulator gui = new BoatSimulator();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      //  Figure out if the boat is accelerating,
      //  cruising, or braking, and set the mode of
      //  the boat accordingly
      if ( accelButton.isSelected() == true ) {
        boat.setMode("accelerating");
      }
      else if ( cruiseButton.isSelected() == true ) {
        boat.setMode("cruising");
      }
      else {
        boat.setMode("decelerating");
      }

      //  Update the boat velocity and position at the next
      //  time increment. 
      double timeIncrement = 0.08;
      boat.updateLocationAndVelocity(timeIncrement);

      //  Update the location of the rectangular marker
      rectangleOneX = rectangleOneX - 10.0*boat.getVx()*timeIncrement;

      //  If the marker has gone off the display, move it
      //  back to the right hand side
      if ( rectangleOneX < 0.0 ) {
        rectangleOneX = 401.0;
      }

      //  Update the display
      resetDisplay();
    }
  }
}