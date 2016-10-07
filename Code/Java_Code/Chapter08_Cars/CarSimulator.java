import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class CarSimulator extends JFrame implements ActionListener
{
  private JTextField velocityTextField;
  private JTextField rpmTextField;
  private JTextField gearTextField;
  private JTextField distanceTextField;
  private JTextField timeTextField;
  private JTextField messageTextField;

  private JLabel velocityLabel;
  private JLabel rpmLabel;
  private JLabel gearLabel;
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
  private JButton shiftUpButton;
  private JButton shiftDownButton;
  private GridBagConstraints gbc;

  private BoxsterS car; 

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  //  These fields are for the images used in the game.
  private ImageIcon carIcon;
  private int carWidth; 
  private int carHeight;

  //  Two rectangular markers are used to simulate motion.
  //  These fields store their x-locations.
  private double rectangleOneX;
  private double rectangleTwoX;

  public CarSimulator() {
    //  Create a BoxsterS object.
    double x0 = 0.0;
    double y0 = 0.0;
    double z0 = 0.0;
    double vx0 = 0.0;
    double vy0 = 0.0;
    double vz0 = 0.0;
    double time = 0.0;
    double density = 1.2;
    car = new BoxsterS(x0, y0, z0, vx0, vy0, vz0, time, density);

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable is
    //  the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up some images and determine their dimensions
    carIcon = new ImageIcon("porscheCartoon.jpg");
    carWidth = carIcon.getIconWidth();
    carHeight = carIcon.getIconHeight();

    //  Initialize the x-locations of the rectangular markers
    rectangleOneX = 0.0;
    rectangleTwoX = 200.0;

    //  Create JTextField objects to input the initial
    //  conditions.
    velocityTextField = new JTextField("0.0",10);
    rpmTextField = new JTextField("1000.0",10);
    gearTextField = new JTextField("1",5);
    distanceTextField = new JTextField("0.0",10);
    timeTextField = new JTextField("0.0",10);

    messageTextField = new JTextField("",20);
    messageTextField.setEditable(false);

    //  Create some JLabels
    velocityLabel = new JLabel("Velocity (km/hr)");
    rpmLabel = new JLabel("Engine rpm");
    gearLabel = new JLabel("Gear");
    distanceLabel = new JLabel("Distance traveled (m)");
    timeLabel = new JLabel("Time (s)");

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(401, 151));

    //  Create a JRadioButton to accelerate the car.
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

    //  Create a JRadioButton to brake.
    brakeButton = new JRadioButton("Brake");
    brakeButton.setName("Brake");
    brakeButton.setPreferredSize(new Dimension(100,35));
    brakeButton.setHorizontalTextPosition(SwingConstants.LEADING);

    //  Use a ButtonGroup to make the radio buttons
    //  mutually exclusive
    buttonGroup = new ButtonGroup();
    buttonGroup.add(accelButton);
    buttonGroup.add(cruiseButton);
    buttonGroup.add(brakeButton);

    //  Create a JButton that will start the car moving
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will stop the car, shift into 
    //  first gear, but the distance and time values are
    //  unchanged.
    stopButton = new JButton("Stop");
    stopButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    stopButton.setPreferredSize(new Dimension(60,35));
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        gameTimer.stop();
        car.setQ(0.0,0);   //  vx0 set to zero
        car.setOmegaE(1000.0);
        car.setGearNumber(1);
        accelButton.setSelected(true);
        resetDisplay();
      }
    });

    //  Create a JButton that will reset the car fields
    //  to default values.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        gameTimer.stop();
        car.setS(0.0);     //  time set to zero
        car.setQ(0.0,0);   //  vx0 set to zero
        car.setQ(0.0,1);   //  x0 set to zero
        car.setOmegaE(1000.0);
        car.setGearNumber(1);
        accelButton.setSelected(true);
        rectangleOneX = 0.0;
        rectangleTwoX = 200.0;

        resetDisplay();
      }
    });

    //  Create a JButton that will shift to a higher gear.
    shiftUpButton = new JButton("Shift Up");
    shiftUpButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    shiftUpButton.setPreferredSize(new Dimension(60,35));
    shiftUpButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        car.shiftGear(1);
      }
    });

    //  Create a JButton that will shift to a lower gear.
    shiftDownButton = new JButton("Shift Down");
    shiftDownButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    shiftDownButton.setPreferredSize(new Dimension(60,35));
    shiftDownButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        car.shiftGear(-1);
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
    gridBagLayout1.setConstraints(shiftUpButton, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(shiftDownButton, gbc);

    row = 6;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(stopButton, gbc);

    row = 7;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(accelButton);
    westPanel.add(cruiseButton);
    westPanel.add(brakeButton);
    westPanel.add(startButton);
    westPanel.add(shiftUpButton);
    westPanel.add(shiftDownButton);
    westPanel.add(stopButton);
    westPanel.add(resetButton);

    //  Place components on a panel using a GridBagLayout
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout2);

    col = 0;
    row = 0;
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
    gridBagLayout2.setConstraints(rpmLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(rpmTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(gearLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(gearTextField, gbc);

    row = 3;
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

    row = 4;
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
    eastPanel.add(rpmLabel);
    eastPanel.add(rpmTextField);
    eastPanel.add(gearLabel);
    eastPanel.add(gearTextField);
    eastPanel.add(distanceLabel);
    eastPanel.add(distanceTextField);
    eastPanel.add(timeLabel);
    eastPanel.add(timeTextField);

    //  Place components on a panel using a GridBagLayout
    JPanel southPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    southPanel.setLayout(gridBagLayout3);

    col = 0;
    row = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(messageTextField, gbc);

    southPanel.add(messageTextField);

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
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    getContentPane().add(northPanel, BorderLayout.NORTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Car Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,550);
    setVisible(true);
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    //  Start the car moving using a Timer object
    //  to slow down the action.
    gameTimer.start();
  }

  //  This method updates the textfield and JPanel displays
  private void resetDisplay() { 

    timeTextField.setText(""+(float)car.getTime());

    //  Convert the velocity from m/s to km/hr and
    //  only show integer values
    velocityTextField.setText(""+(int)(car.getVx()*3.6));

    //  Only show integer values for rpm, gear number, 
    //  and distance.
    rpmTextField.setText(""+(int)car.getOmegaE());
    gearTextField.setText(""+(int)car.getGearNumber());
    distanceTextField.setText(""+(int)car.getX());

    //  Get the graphics object on the JPanel
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, 70, width, 70);

    //  Draw the car.
    g.drawImage(carIcon.getImage(), 100, 95, carWidth, carHeight, drawingPanel);

    //  Draw the markers
    g.fillRect((int)rectangleOneX, 40, 10, 30);
    g.fillRect((int)rectangleTwoX, 40, 10, 30);
  }

  public static void main(String args[]) {
    CarSimulator gui = new CarSimulator();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      //  Figure out if the car is accelerating,
      //  cruising, or braking, and set the mode of
      //  the car accordingly
      if ( accelButton.isSelected() == true ) {
        car.setMode("accelerating");
      }
      else if ( cruiseButton.isSelected() == true ) {
        car.setMode("cruising");
      }
      else {
        car.setMode("braking");
      }

      //  Update the car velocity and position at the next
      //  time increment. 
      double timeIncrement = 0.06;
      car.updateLocationAndVelocity(timeIncrement);

      //  Compute the new engine rpm value
      double rpm = car.getVx()*60.0*car.getGearRatio()*
          car.getFinalDriveRatio()/(2.0*Math.PI*car.getWheelRadius());
      car.setOmegaE(rpm);

      //  If the rpm exceeds the redline value, put a
      //  warning message on the screen. First, clear the
      //  message textfield of any existing messages. 
      messageTextField.setText("");
      if ( car.getOmegaE() > car.getRedline() ) {
        messageTextField.setText("Warning: Exceeding redline rpm");
      }
      if ( car.getOmegaE() > 8000.0 ) {
        messageTextField.setText("You have blown the engine!");
        gameTimer.stop();
      }

      //  Update the location of the rectangular markers
      rectangleOneX = rectangleOneX + 10.0*car.getVx()*timeIncrement;
      rectangleTwoX = rectangleTwoX + 10.0*car.getVx()*timeIncrement;

      //  If the markers have gone off the display, move them
      //  back to zero
      if ( rectangleOneX > 401.0 ) {
        rectangleOneX = 0.0;
      }
      if ( rectangleTwoX > 401.0 ) {
        rectangleTwoX = 0.0;
      }

      //  Update the display
      resetDisplay();
    }
  }
}