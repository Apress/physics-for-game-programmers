import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class RocketSimulator extends JFrame 
         implements ActionListener, ItemListener
{
  private JTextField velocityTextField;
  private JTextField altitudeTextField;
  private JTextField crossRangeTextField;
  private JTextField pitchAngleTextField;
  private JTextField payloadTextField;
  private JTextField massTextField;
  private JTextField burnTimeTextField;
  private JTextField diameterTextField;
  private JTextField seaLevelThrustTextField;
  private JTextField vacuumThrustTextField;
  private JTextField initialMassTextField;

  private JLabel velocityLabel;
  private JLabel altitudeLabel;
  private JLabel crossRangeLabel;
  private JLabel pitchAngleLabel;
  private JLabel payloadLabel;
  private JLabel engineTypeLabel;
  private JLabel numEngineLabel;
  private JLabel massLabel;
  private JLabel rocketSpecLabel;
  private JLabel rocketResultsLabel;
  private JLabel burnTimeLabel;
  private JLabel diameterLabel;
  private JLabel seaLevelThrustLabel;
  private JLabel vacuumThrustLabel;
  private JLabel initialMassLabel;

  private JComboBox engineTypeComboBox;
  private JComboBox numEngineComboBox;

  private JButton launchButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  Declare a SimpleRocket object
  private SimpleRocket rocket;

  //  These fields are for the images used in the game.
  private ImageIcon rocket90Icon;
  private int rocket90IconWidth;
  private int rocket90IconHeight;
  private ImageIcon rocket60Icon;
  private int rocket60IconWidth;
  private int rocket60IconHeight;
  private ImageIcon rocket30Icon;
  private int rocket30IconWidth;
  private int rocket30IconHeight;
  private ImageIcon rocket0Icon;
  private int rocket0IconWidth;
  private int rocket0IconHeight;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public RocketSimulator() {

    //  Create a SimpleRocket object
    rocket = new SimpleRocket(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                              0.0, 0.0, 1, 0.0, 0.0, 0.0, 0.0, 
                              0.5*Math.PI, 0.0, 0.0);

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. 
    gameUpdater = new GameUpdater();
    gameTimer = new Timer(200, gameUpdater);

    //  Set up some images and determine their dimensions
    rocket90Icon = new ImageIcon("rocket90Cartoon.jpg");
    rocket90IconWidth = rocket90Icon.getIconWidth();
    rocket90IconHeight = rocket90Icon.getIconHeight();
    rocket60Icon = new ImageIcon("rocket60Cartoon.jpg");
    rocket60IconWidth = rocket60Icon.getIconWidth();
    rocket60IconHeight = rocket60Icon.getIconHeight();
    rocket30Icon = new ImageIcon("rocket30Cartoon.jpg");
    rocket30IconWidth = rocket30Icon.getIconWidth();
    rocket30IconHeight = rocket30Icon.getIconHeight();
    rocket0Icon = new ImageIcon("rocket0Cartoon.jpg");
    rocket0IconWidth = rocket0Icon.getIconWidth();
    rocket0IconHeight = rocket0Icon.getIconHeight();

    //  Create JTextField objects to input the initial
    //  conditions.
    velocityTextField = new JTextField("0.0",8);
    velocityTextField.setEditable(false);

    altitudeTextField = new JTextField("0.0",8);
    altitudeTextField.setEditable(false);

    crossRangeTextField = new JTextField("0.0",8);
    crossRangeTextField.setEditable(false);

    pitchAngleTextField = new JTextField("90.0",8);
    pitchAngleTextField.setEditable(false);

    payloadTextField = new JTextField("0.0",8);

    massTextField = new JTextField("0.0",8);
    massTextField.setEditable(false);

    burnTimeTextField = new JTextField("0.0",8);
    burnTimeTextField.setEditable(false);

    diameterTextField = new JTextField("10.0",8);

    seaLevelThrustTextField = new JTextField("6670000.0",8);
    seaLevelThrustTextField.setEditable(false);

    vacuumThrustTextField = new JTextField("7860000.0",8);
    vacuumThrustTextField.setEditable(false);

    initialMassTextField = new JTextField("424371.0",8);
    initialMassTextField.setEditable(false);

    //  Create some JLabels
    velocityLabel = new JLabel("Rocket velocity, m/s");
    altitudeLabel = new JLabel("Altitude, m");
    crossRangeLabel = new JLabel("Cross range, m");
    pitchAngleLabel = new JLabel("Pitch angle, deg");
    payloadLabel = new JLabel("Payload mass, kg");
    engineTypeLabel = new JLabel("Engine type");
    numEngineLabel = new JLabel("Number of engines");
    massLabel = new JLabel("Rocket mass, kg");
    rocketSpecLabel = new JLabel("Rocket Specifications");
    rocketResultsLabel = new JLabel("Trajectory Data");
    burnTimeLabel = new JLabel("Rocket burn time, s");
    diameterLabel = new JLabel("Rocket diameter, m");
    seaLevelThrustLabel = new JLabel("Sea level thrust, N");
    vacuumThrustLabel = new JLabel("Vacuum thrust, N");
    initialMassLabel = new JLabel("Initial mass, kg");

    //  Create a JComboBox to choose the rocket engine type.
    engineTypeComboBox = new JComboBox();
    engineTypeComboBox.addItem("F1");
    engineTypeComboBox.addItem("RD-180");
    engineTypeComboBox.addItemListener(this);

    //  Create a JComboBox to choose the club.
    numEngineComboBox = new JComboBox();
    numEngineComboBox.addItem("1");
    numEngineComboBox.addItem("2");
    numEngineComboBox.addItem("3");
    numEngineComboBox.addItem("4");
    numEngineComboBox.addItem("5");
    numEngineComboBox.addItem("6");
    numEngineComboBox.addItemListener(this);

    //  Create a JButton that will start the ball moving
    launchButton = new JButton("Launch");
    launchButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    launchButton.setPreferredSize(new Dimension(60,35));
    launchButton.addActionListener(this);

    //  Create a JButton that will update the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        //  stop the timer.
        gameTimer.stop();

        //  Reset the time, location, velocity, and mass of rocket
        rocket.setS(0.0);     //  time = 0.0
        rocket.setQ(0.0,0);   //  vx0 = 0.0
        rocket.setQ(0.0,1);   //  x0  = 0.0
        rocket.setQ(0.0,2);   //  vy0 = 0.0
        rocket.setQ(0.0,3);   //  y0  = 0.0
        rocket.setQ(0.0,4);   //  vz0 = 0.0
        rocket.setQ(0.0,5);   //  z0  = 0.0
        rocket.setQ(rocket.getInitialMass(), 7);
        rocket.setQ(Math.PI/2.0, 9);  //  pitch angle in radians

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(551, 251));

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
    gridBagLayout1.setConstraints(rocketSpecLabel, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(engineTypeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(engineTypeComboBox, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(numEngineLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(numEngineComboBox, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(payloadLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(payloadTextField, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(diameterLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(diameterTextField, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(seaLevelThrustLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(seaLevelThrustTextField, gbc);

    row = 6;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vacuumThrustLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vacuumThrustTextField, gbc);

    row = 7;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(initialMassLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(initialMassTextField, gbc);

    row = 8;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(launchButton, gbc);

    row = 9;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(rocketSpecLabel);
    westPanel.add(engineTypeLabel);
    westPanel.add(engineTypeComboBox);
    westPanel.add(numEngineLabel);
    westPanel.add(numEngineComboBox);
    westPanel.add(payloadLabel);
    westPanel.add(payloadTextField);
    westPanel.add(diameterLabel);
    westPanel.add(diameterTextField);
    westPanel.add(seaLevelThrustLabel);
    westPanel.add(seaLevelThrustTextField);
    westPanel.add(vacuumThrustLabel);
    westPanel.add(vacuumThrustTextField);
    westPanel.add(initialMassLabel);
    westPanel.add(initialMassTextField);
    westPanel.add(launchButton);
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
    gridBagLayout2.setConstraints(rocketResultsLabel, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(burnTimeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(burnTimeTextField, gbc);

    row = 2;
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

    row = 3;
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

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(crossRangeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(crossRangeTextField, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(pitchAngleLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(pitchAngleTextField, gbc);

    row = 6;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(massLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(massTextField, gbc);

    eastPanel.add(rocketResultsLabel);
    eastPanel.add(burnTimeLabel);
    eastPanel.add(burnTimeTextField);
    eastPanel.add(altitudeLabel);
    eastPanel.add(altitudeTextField);
    eastPanel.add(velocityLabel);
    eastPanel.add(velocityTextField);
    eastPanel.add(crossRangeLabel);
    eastPanel.add(crossRangeTextField);
    eastPanel.add(pitchAngleLabel);
    eastPanel.add(pitchAngleTextField);
    eastPanel.add(massLabel);
    eastPanel.add(massTextField);

    //  The drawing panel.
    JPanel northPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    northPanel.setLayout(gridBagLayout3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout3.setConstraints(drawingPanel, gbc);

    northPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(eastPanel, BorderLayout.EAST);
    getContentPane().add(northPanel, BorderLayout.NORTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Rocket Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,650,675);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The itemStateChanged() method is called when either
  //  of the JComboBox selections change. 
  public void itemStateChanged(ItemEvent event) {

    //  Get some initial quantities from the textfields.
    double payloadMass = Double.parseDouble(payloadTextField.getText());

    //  Determine number of engines and engine type
    String numEngineString = (String)numEngineComboBox.getSelectedItem();
    int numEngines = Integer.parseInt(numEngineString);

    double seaLevelThrustPerEngine;
    double vacuumThrustPerEngine;
    double massFlowRate;
    double engineMass;
    double burnTime;
    String engineSelection = (String)engineTypeComboBox.getSelectedItem();
    if ( engineSelection.equals("F1") ) {
      seaLevelThrustPerEngine = 6.67e+6;
      vacuumThrustPerEngine = 7.86e+6;
      massFlowRate = 2616.0;
      engineMass = 8371.0;
      burnTime = 150.0;
    }
    else { 
      //  RD-180 data
      seaLevelThrustPerEngine = 3.83e+6;
      vacuumThrustPerEngine = 4.15e+6;
      engineMass = 5480.0;
      massFlowRate = 1254.0;
      burnTime = 227.0;
    }

    //  Calculate propellant mass per engine
    double propellantMass = massFlowRate*burnTime;

    //  Estimate rocket structural mass;
    double structureMass = 20000.0 + numEngines*4000.0;

    //  Compute initial mass of rocket
    double initialMass = numEngines*(engineMass + propellantMass) +
                         payloadMass + structureMass;

    //  Update the textfields 
    massTextField.setText(""+initialMass);
    initialMassTextField.setText(""+initialMass);
    seaLevelThrustTextField.setText(""+numEngines*seaLevelThrustPerEngine);
    vacuumThrustTextField.setText(""+numEngines*vacuumThrustPerEngine);
  }

  //  The actionPerformed() method is called when 
  //  the "Launch" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Get some initial quantities from the textfields.
    double payloadMass = Double.parseDouble(payloadTextField.getText());
    double rocketDiameter = 
            Double.parseDouble(diameterTextField.getText());

    //  Determine number of engines and engine type
    String numEngineString = (String)numEngineComboBox.getSelectedItem();
    int numEngines = Integer.parseInt(numEngineString);

    double seaLevelThrustPerEngine;
    double vacuumThrustPerEngine;
    double massFlowRate;
    double engineMass;
    double burnTime;
    String engineSelection = (String)engineTypeComboBox.getSelectedItem();
    if ( engineSelection.equals("F1") ) {
      seaLevelThrustPerEngine = 6.67e+6;
      vacuumThrustPerEngine = 7.86e+6;
      massFlowRate = 2616.0;
      engineMass = 8371.0;
      burnTime = 150.0;
    }
    else { 
      //  RD-180 data
      seaLevelThrustPerEngine = 3.83e+6;
      vacuumThrustPerEngine = 4.15e+6;
      engineMass = 5480.0;
      massFlowRate = 1254.0;
      burnTime = 227.0;
    }

    //  Calculate propellant mass per engine
    double propellantMass = massFlowRate*burnTime;

    //  Estimate rocket structural mass;
    double structureMass = 20000.0 + numEngines*4000.0;

    //  Compute initial mass of rocket
    double initialMass = numEngines*(engineMass + propellantMass) +
                         payloadMass + structureMass;

    //  Set values for drag coefficient and pitch angle.
    //  The pitch angle is in radians.
    double cd = 0.5;
    double theta = 0.5*Math.PI;

    //  Set the change in pitch angle in rad/s so that at the end
    //  of the burn time the rocket will at a pitch angle of 10 deg.
    double omega = -80*Math.PI/(180.0*burnTime);

    //  Create a SimpleRocket object
    rocket = new SimpleRocket(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                   initialMass, massFlowRate, numEngines, 
                   seaLevelThrustPerEngine, vacuumThrustPerEngine, 
                   rocketDiameter, cd, theta, omega, burnTime);

    //  Update the display
    updateDisplay();

    //  Launch the rocket using a Timer object
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
    g.drawLine(0, height-10, width, height-10);

    //  Draw the rocket.
    int rocketX = (int)(rocket.getX()/500.0) + 10;
    int rocketZ;
    double theta = rocket.getTheta()*180.0/Math.PI;
    if ( theta < 12.0 ) {
      rocketZ = height - 10 - rocket0IconHeight - 
                  (int)(rocket.getZ()/600.0);
      g.drawImage(rocket0Icon.getImage(), rocketX, rocketZ, 
                  rocket0IconWidth, rocket0IconHeight, drawingPanel);
    }
    else if ( theta < 40.0 ) {
      rocketZ = height - 10 - rocket30IconHeight - 
                  (int)(rocket.getZ()/600.0);
      g.drawImage(rocket30Icon.getImage(), rocketX, rocketZ, 
                  rocket30IconWidth, rocket30IconHeight, drawingPanel);
    }
    else if ( theta < 70.0 ) {
      rocketZ = height - 10 - rocket60IconHeight - 
                  (int)(rocket.getZ()/600.0);
      g.drawImage(rocket60Icon.getImage(), rocketX, rocketZ, 
                  rocket60IconWidth, rocket60IconHeight, drawingPanel);
    }
    else {
      rocketZ = height - 10 - rocket90IconHeight - 
                  (int)(rocket.getZ()/600.0);
      g.drawImage(rocket90Icon.getImage(), rocketX, rocketZ, 
                  rocket90IconWidth, rocket90IconHeight, drawingPanel);
    }
  }

  public static void main(String args[]) {
    RocketSimulator gui = new RocketSimulator();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Compute the new location and velocity of the rocket. 
      double timeIncrement = 1.0;
      rocket.updateLocationAndVelocity(timeIncrement);

      //  Update the display
      updateDisplay();

      //  Update the "Trajectory Data" textfields
      burnTimeTextField.setText(""+(int)rocket.getTime());
      altitudeTextField.setText(""+(int)rocket.getZ());
      double vx = rocket.getVx();
      double vz = rocket.getVz();
      double v = Math.sqrt(vx*vx + vz*vz);
      velocityTextField.setText(""+(int)v);
      crossRangeTextField.setText(""+(int)rocket.getX());
      double pitchAngle = rocket.getTheta()*180.0/Math.PI;
      pitchAngleTextField.setText(""+(int)pitchAngle);
      massTextField.setText(""+(int)rocket.getMass());

      //  If the time reaches the rocket burn time, stop
      //  the simulation.
      if ( rocket.getTime() > rocket.getBurnTime() ) {
        gameTimer.stop();
      }

    }
  }
}