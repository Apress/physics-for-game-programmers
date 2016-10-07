import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class FlightSimulator extends JFrame implements ActionListener
{
  //  Declare the GUI components that will be
  //  displayed in the JFrame.
  private JTextField headingTextField;
  private JTextField climbAngleTextField;
  private JTextField airspeedTextField;
  private JTextField climbRateTextField;
  private JTextField altitudeTextField;
  private JTextField statusTextField;

  private JLabel headingLabel;
  private JLabel climbAngleLabel;
  private JLabel airspeedLabel;
  private JLabel climbRateLabel;
  private JLabel altitudeLabel;
  private JLabel statusLabel;
  private JLabel throttleLabel;
  private JLabel alphaLabel;
  private JLabel bankLabel;
  private JLabel flapLabel;

  private JSlider throttleSlider;
  private JSlider alphaSlider;
  private JSlider bankSlider;

  private JComboBox flapComboBox;

  private JPanel drawingPanel;

  private JButton startButton;
  private JButton resetButton;

  private GridBagConstraints gbc;

  private CessnaSkyhawk plane; 

  private GameUpdater gameUpdater;
  private Timer gameTimer;

  //  These fields are for the images used in the game.
  private ImageIcon planeIcon;
  private int planeWidth; 
  private int planeHeight;

  //  A rectangular marker is used to simulate motion.
  //  This field stores its x-location.
  private double rectangleOneX;

  public FlightSimulator() {
    //  Create a CessnaSkyhawk object representing a
    //  Cessna 172 Skyhawk.
    double x0 = 0.0;
    double y0 = 0.0;
    double z0 = 0.0;
    double vx0 = 0.0;
    double vy0 = 0.0;
    double vz0 = 0.0;
    double time = 0.0;
    
    plane = new CessnaSkyhawk(x0, y0, z0, vx0, vy0, vz0, time);

    //  Create a Timer object that will be used
    //  to slow the action down. The Timer will update
    //  the location and velocity every 100 milliseconds.
    gameUpdater = new GameUpdater();
    gameTimer = new Timer(100, gameUpdater);

    //  Set up some images and determine their dimensions
    planeIcon = new ImageIcon("airplaneCartoon.jpg");
    planeWidth = planeIcon.getIconWidth();
    planeHeight = planeIcon.getIconHeight();

    //  Initialize the x-location of the rectangular marker
    rectangleOneX = 200.0;

    //  Create JTextField objects to input the initial
    //  conditions.
    headingTextField = new JTextField("0.0",10);
    climbAngleTextField = new JTextField("0.0",10);
    airspeedTextField = new JTextField("0.0",10);
    climbRateTextField = new JTextField("0.0",10);
    altitudeTextField = new JTextField("0.0",10);

    //  This JTextField displays status messages.
    statusTextField = new JTextField("",20);
    statusTextField.setEditable(false);
    statusTextField.setText("Throttle set to zero");

    //  Create some JLabels
    headingLabel = new JLabel("Heading angle (deg)");
    climbAngleLabel = new JLabel("Climb angle (deg)");
    airspeedLabel = new JLabel("Airspeed (km/hr)");
    climbRateLabel = new JLabel("Climb rate (m/s)");
    altitudeLabel = new JLabel("Altitude (m)");
    statusLabel = new JLabel("Status");
    throttleLabel = new JLabel("Throttle (%)");
    alphaLabel = new JLabel("Angle of attack (deg)");
    bankLabel = new JLabel("Bank angle (deg)");
    flapLabel = new JLabel("Flap deflection (deg)");

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(401, 301));

    //  Create a JButton that will start the simulation
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will reset the plane fields
    //  to default values.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        gameTimer.stop();
        plane.setS(0.0);     //  time = 0.0
        plane.setQ(0.0,0);   //  vx0 = 0.0
        plane.setQ(0.0,1);   //  x0 = 0.0
        plane.setQ(0.0,2);   //  vy0 = 0.0
        plane.setQ(0.0,3);   //  y0 = 0.0
        plane.setQ(0.0,4);   //  vz0 = 0.0
        plane.setQ(0.0,5);   //  z0 = 0.0
        plane.setThrottle(0.0);
        plane.setAlpha(0.0);
        plane.setBank(0.0);

        //  Reset GUI controls
        throttleSlider.setValue(0);
        alphaSlider.setValue(0);
        bankSlider.setValue(0);
        statusTextField.setText("Throttle set to zero");
        resetDisplay();
      }
    });

    //  This JSlider controls the engine throttle
    throttleSlider = new JSlider(JSlider.HORIZONTAL,
                           0, 100, 0);
    throttleSlider.setMajorTickSpacing(20);
    throttleSlider.setMinorTickSpacing(5);
    throttleSlider.setPaintTicks(true);
    throttleSlider.setPaintLabels(true);
    throttleSlider.putClientProperty("JSlider.isFilled", Boolean.TRUE);
    throttleSlider.setForeground(Color.black);
    throttleSlider.setBorder(BorderFactory.createEtchedBorder());

    //  This JSlider controls the angle of attack
    alphaSlider = new JSlider(JSlider.HORIZONTAL,
                           -16, 20, 0);
    alphaSlider.setMajorTickSpacing(4);
    alphaSlider.setMinorTickSpacing(1);
    alphaSlider.setPaintTicks(true);
    alphaSlider.setPaintLabels(true);
    alphaSlider.setForeground(Color.black);
    alphaSlider.setBorder(BorderFactory.createEtchedBorder());

    //  This JSlider controls the bank angle
    bankSlider = new JSlider(JSlider.HORIZONTAL,
                           -20, 20, 0);
    bankSlider.setMajorTickSpacing(10);
    bankSlider.setMinorTickSpacing(2);
    bankSlider.setPaintTicks(true);
    bankSlider.setPaintLabels(true);
    bankSlider.setForeground(Color.black);
    bankSlider.setBorder(BorderFactory.createEtchedBorder());

    //  Create a JComboBox to choose the coordinate axes that 
    //  will be displayed.
    flapComboBox = new JComboBox();
    flapComboBox.addItem("0");
    flapComboBox.addItem("20");
    flapComboBox.addItem("40");

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
    gridBagLayout1.setConstraints(throttleLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(throttleSlider, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(alphaLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(alphaSlider, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(bankLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(bankSlider, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(flapLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(flapComboBox, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(throttleLabel);
    westPanel.add(throttleSlider);
    westPanel.add(alphaLabel);
    westPanel.add(alphaSlider);
    westPanel.add(bankLabel);
    westPanel.add(bankSlider);
    westPanel.add(flapLabel);
    westPanel.add(flapComboBox);
    westPanel.add(startButton);
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
    gridBagLayout2.setConstraints(headingLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(headingTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(climbAngleLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(climbAngleTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(airspeedLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(airspeedTextField, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(climbRateLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(climbRateTextField, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(altitudeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(altitudeTextField, gbc);

    eastPanel.add(headingLabel);
    eastPanel.add(headingTextField);
    eastPanel.add(climbAngleLabel);
    eastPanel.add(climbAngleTextField);
    eastPanel.add(airspeedLabel);
    eastPanel.add(airspeedTextField);
    eastPanel.add(climbRateLabel);
    eastPanel.add(climbRateTextField);
    eastPanel.add(altitudeLabel);
    eastPanel.add(altitudeTextField);

    //  Place components on a panel using a GridBagLayout
    JPanel southPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    southPanel.setLayout(gridBagLayout3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(statusLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(statusTextField, gbc);

    southPanel.add(statusLabel);
    southPanel.add(statusTextField);

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
    setTitle("Flight Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,600,700);
    setVisible(true);
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    //  Start the plane moving using a Timer object
    //  to slow down the action.
    gameTimer.start();
  }

  //  This method updates the textfield displays
  private void resetDisplay() { 
    double vx = plane.getVx();
    double vy = plane.getVy();
    double vz = plane.getVz();
    double vh = Math.sqrt(vx*vx + vy*vy);
    double airspeed = Math.sqrt(vx*vx + vy*vy + vz*vz);
    double climbAngle;
    double headingAngle;

    if ( vh == 0.0 ) {
      climbAngle = 0.0;
    }
    else {
      climbAngle = Math.atan(vz/vh);
    }

    if ( vx >= 0.0 && vy == 0.0 ) {
      headingAngle = 0.0;
    }
    else if ( vx == 0.0 && vy > 0.0 ) {
      headingAngle = 0.5*Math.PI;
    }
    else if ( vx <= 0.0 && vy == 0.0 ) {
      headingAngle = Math.PI;
    }
    else if ( vx == 0.0 && vy < 0.0 ) {
      headingAngle = 1.5*Math.PI;
    }
    else if ( vx > 0.0 && vy > 0.0 ) {
      headingAngle = Math.atan(vy/vx);
    }
    else if ( vx < 0.0 && vy > 0.0 ) {
      headingAngle = 0.5*Math.PI + Math.atan(Math.abs(vx/vy));
    }
    else if ( vx < 0.0 && vy < 0.0 ) {
      headingAngle = Math.PI + Math.atan(vy/vx);
    }
    else {
      headingAngle = 1.5*Math.PI + Math.atan(Math.abs(vx/vy));
    }

    climbAngleTextField.setText(""+(float)(climbAngle*180.0/Math.PI));
    headingTextField.setText(""+(float)(headingAngle*180.0/Math.PI));
    climbRateTextField.setText(""+(float)vz);
    altitudeTextField.setText(""+(float)plane.getZ());

    //  Airspeed is converted from m/s to km/hr.
    airspeedTextField.setText(""+(float)(airspeed*3.6));

    //  Get the graphics object on the JPanel
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, height-10, width, height-10);

    //  Draw the plane.
    int planeZ = height - 10 - planeHeight - 
                 (int)(plane.getZ()/20.0);
    g.drawImage(planeIcon.getImage(), 170, planeZ, 
                planeWidth, planeHeight, drawingPanel);

    //  Draw the marker
    g.fillRect((int)rectangleOneX, height-30, 10, 20);
  }

  public static void main(String args[]) {
    FlightSimulator gui = new FlightSimulator();
  }

  //  This ActionListener is called by the Timer.
  //  It updates the plane velocity and position at the next
  //  time increment. 
  //  Bank angle is converted from degrees to radians
  //  Angle of attack is not converted because the
  //  Cl-alpha curve is defined in terms of degrees.
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {
 
      //  Get the initial values from the textfield.
      //  The bank angle is converted to radians. The angle of
      //  attack is not converted to radians because the Cl-alpha
      //  equations use alpha in units of degrees.
      double throttle = throttleSlider.getValue()/100.0;
      double alpha = alphaSlider.getValue();
      double bankAngle = bankSlider.getValue()*Math.PI/180.0;
      String flap = (String)flapComboBox.getSelectedItem();

      plane.setThrottle(throttle);
      plane.setAlpha(alpha);
      plane.setBank(bankAngle);
      plane.setFlap(flap);

      if ( plane.getThrottle() == 0.0 ) {
        statusTextField.setText("Throttle set to zero");
      }
      else {
        statusTextField.setText("");
      }

      //  When the plane reaches the ground, stop the
      //  simulation. If the z-velocity is greater than
      //  -2.0 m/s, you have landed safely. If it's
      //  greater than -5.0 m/s it was a rough landing.
      //  Anything beyond -5.0 m/s is a crash.
      if ( plane.getZ() < 0.0 ) { 
        gameTimer.stop();
     
        if ( plane.getVz() > -2.0 ) {
          statusTextField.setText("You've landed safely");
        }
        else if ( plane.getVz() > -5.0 ) {
          statusTextField.setText("Rough landing");
        }
        else {
          statusTextField.setText("You crashed");
        }
      }

      //  Update the location and velocity of the airplane.
      double timeIncrement = 0.2;
      plane.updateLocationAndVelocity(timeIncrement);

      //  Update the location of the rectangular marker
      rectangleOneX = rectangleOneX - 5.0*plane.getVx()*timeIncrement;

      //  If the marker has gone off the display, move it
      //  back to the right hand side
      if ( rectangleOneX < 0.0 ) {
        rectangleOneX = 401.0;
      }
      if ( rectangleOneX > 410.0 ) {
        rectangleOneX = 0.0;
      }

      resetDisplay();

    }
  }
}