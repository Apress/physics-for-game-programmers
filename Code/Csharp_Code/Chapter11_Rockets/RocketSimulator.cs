using System;
using System.Windows.Forms;
using System.Drawing;

public class RocketSimulator : Form
{
  private Label velocityLabel;
  private Label altitudeLabel;
  private Label crossRangeLabel;
  private Label pitchAngleLabel;
  private Label payloadLabel;
  private Label engineTypeLabel;
  private Label numEngineLabel;
  private Label massLabel;
  private Label rocketSpecLabel;
  private Label rocketResultsLabel;
  private Label burnTimeLabel;
  private Label diameterLabel;
  private Label seaLevelThrustLabel;
  private Label vacuumThrustLabel;
  private Label initialMassLabel;

  private TextBox velocityTextBox;
  private TextBox altitudeTextBox;
  private TextBox crossRangeTextBox;
  private TextBox pitchAngleTextBox;
  private TextBox payloadTextBox;
  private TextBox massTextBox;
  private TextBox burnTimeTextBox;
  private TextBox diameterTextBox;
  private TextBox seaLevelThrustTextBox;
  private TextBox vacuumThrustTextBox;
  private TextBox initialMassTextBox;

  private Button launchButton;
  private Button resetButton;

  private ComboBox engineTypeComboBox;
  private ComboBox numEngineComboBox;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image rocket90Icon;
  private int rocket90IconWidth;
  private int rocket90IconHeight;
  private Image rocket60Icon;
  private int rocket60IconWidth;
  private int rocket60IconHeight;
  private Image rocket30Icon;
  private int rocket30IconWidth;
  private int rocket30IconHeight;
  private Image rocket0Icon;
  private int rocket0IconWidth;
  private int rocket0IconHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  //  Declare a SimpleRocket object
  private SimpleRocket rocket;

  public RocketSimulator() {

    //  Create a SimpleRocket object with default values
    //  to initialize the display.
    rocket = new SimpleRocket(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                              0.0, 0.0, 1, 0.0, 0.0, 0.0, 0.0, 
                              0.5*Math.PI, 0.0, 0.0);

    //  Set up images
    rocket90Icon = Image.FromFile("rocket90Cartoon.jpg");
    rocket90IconWidth = rocket90Icon.Width;
    rocket90IconHeight = rocket90Icon.Height;
    rocket60Icon = Image.FromFile("rocket60Cartoon.jpg");
    rocket60IconWidth = rocket60Icon.Width;
    rocket60IconHeight = rocket60Icon.Height;
    rocket30Icon = Image.FromFile("rocket30Cartoon.jpg");
    rocket30IconWidth = rocket30Icon.Width;
    rocket30IconHeight = rocket30Icon.Height;
    rocket0Icon = Image.FromFile("rocket0Cartoon.jpg");
    rocket0IconWidth = rocket0Icon.Width;
    rocket0IconHeight = rocket0Icon.Height;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 200;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    rocketResultsLabel = new Label();
    rocketResultsLabel.Text = "Trajectory Data";
    rocketResultsLabel.Font = new Font(rocketResultsLabel.Font, FontStyle.Bold);
    rocketResultsLabel.Top = 350;
    rocketResultsLabel.Left = 330;
    rocketResultsLabel.Width = 120;

    burnTimeLabel = new Label();
    burnTimeLabel.Text = "Rocket burn time, s";
    burnTimeLabel.Font = new Font(burnTimeLabel.Font, FontStyle.Bold);
    burnTimeLabel.Top = 380;
    burnTimeLabel.Left = 330;
    burnTimeLabel.Width = 120;

    altitudeLabel = new Label();
    altitudeLabel.Text = "Altitude, m";
    altitudeLabel.Font = new Font(altitudeLabel.Font, FontStyle.Bold);
    altitudeLabel.Top = 410;
    altitudeLabel.Left = 330;
    altitudeLabel.Width = 100;

    velocityLabel = new Label();
    velocityLabel.Text = "Rocket velocity, m/s";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 440;
    velocityLabel.Left = 330;
    velocityLabel.Width = 120;

    crossRangeLabel = new Label();
    crossRangeLabel.Text = "Cross range, m";
    crossRangeLabel.Font = new Font(crossRangeLabel.Font, FontStyle.Bold);
    crossRangeLabel.Top = 470;
    crossRangeLabel.Left = 330;
    crossRangeLabel.Width = 120;

    pitchAngleLabel = new Label();
    pitchAngleLabel.Text = "Pitch angle, deg";
    pitchAngleLabel.Font = new Font(pitchAngleLabel.Font, FontStyle.Bold);
    pitchAngleLabel.Top = 500;
    pitchAngleLabel.Left = 330;
    pitchAngleLabel.Width = 120;

    massLabel = new Label();
    massLabel.Text = "Rocket mass, kg";
    massLabel.Font = new Font(massLabel.Font, FontStyle.Bold);
    massLabel.Top = 530;
    massLabel.Left = 330;
    massLabel.Width = 120;

    rocketSpecLabel = new Label();
    rocketSpecLabel.Text = "Rocket Specifications";
    rocketSpecLabel.Font = new Font(rocketSpecLabel.Font, FontStyle.Bold);
    rocketSpecLabel.Top = 310;
    rocketSpecLabel.Left = 20;
    rocketSpecLabel.Width = 130;

    engineTypeLabel = new Label();
    engineTypeLabel.Text = "Engine type";
    engineTypeLabel.Font = new Font(engineTypeLabel.Font, FontStyle.Bold);
    engineTypeLabel.Top = 340;
    engineTypeLabel.Left = 20;
    engineTypeLabel.Width = 100;

    numEngineLabel = new Label();
    numEngineLabel.Text = "Number of engines";
    numEngineLabel.Font = new Font(numEngineLabel.Font, FontStyle.Bold);
    numEngineLabel.Top = 370;
    numEngineLabel.Left = 20;
    numEngineLabel.Width = 120;

    payloadLabel = new Label();
    payloadLabel.Text = "Payload mass, kg";
    payloadLabel.Font = new Font(payloadLabel.Font, FontStyle.Bold);
    payloadLabel.Top = 400;
    payloadLabel.Left = 20;
    payloadLabel.Width = 120;

    diameterLabel = new Label();
    diameterLabel.Text = "Rocket diameter, m";
    diameterLabel.Font = new Font(diameterLabel.Font, FontStyle.Bold);
    diameterLabel.Top = 430;
    diameterLabel.Left = 20;
    diameterLabel.Height = 12;

    seaLevelThrustLabel = new Label();
    seaLevelThrustLabel.Text = "Sea level thrust, N";
    seaLevelThrustLabel.Font = 
        new Font(seaLevelThrustLabel.Font, FontStyle.Bold);
    seaLevelThrustLabel.Top = 460;
    seaLevelThrustLabel.Left = 20;
    seaLevelThrustLabel.Height = 12;

    vacuumThrustLabel = new Label();
    vacuumThrustLabel.Text = "Vacuum thrust, N";
    vacuumThrustLabel.Font = new Font(vacuumThrustLabel.Font, FontStyle.Bold);
    vacuumThrustLabel.Top = 490;
    vacuumThrustLabel.Left = 20;
    vacuumThrustLabel.Height = 12;

    initialMassLabel = new Label();
    initialMassLabel.Text = "Initial mass, kg";
    initialMassLabel.Font = new Font(initialMassLabel.Font, FontStyle.Bold);
    initialMassLabel.Top = 520;
    initialMassLabel.Left = 20;
    initialMassLabel.Height = 12;

    //  Create TextBox objects to display the inputs.
    burnTimeTextBox = new TextBox();
    burnTimeTextBox.Width = 60;
    burnTimeTextBox.Text = "0.0";
    burnTimeTextBox.AutoSize = true;
    burnTimeTextBox.ReadOnly = true;
    burnTimeTextBox.Top = burnTimeLabel.Top;
    burnTimeTextBox.Left = 490;

    altitudeTextBox = new TextBox();
    altitudeTextBox.Width = 60;
    altitudeTextBox.Text = "0.0";
    altitudeTextBox.AutoSize = true;
    altitudeTextBox.ReadOnly = true;
    altitudeTextBox.Top = altitudeLabel.Top;
    altitudeTextBox.Left = 490;

    velocityTextBox = new TextBox();
    velocityTextBox.Width = 60;
    velocityTextBox.Text = "0.0";
    velocityTextBox.AutoSize = true;
    velocityTextBox.ReadOnly = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 490;

    crossRangeTextBox = new TextBox();
    crossRangeTextBox.Width = 60;
    crossRangeTextBox.Text = "0.0";
    crossRangeTextBox.AutoSize = true;
    crossRangeTextBox.ReadOnly = true;
    crossRangeTextBox.Top = crossRangeLabel.Top;
    crossRangeTextBox.Left = 490;

    pitchAngleTextBox = new TextBox();
    pitchAngleTextBox.Width = 60;
    pitchAngleTextBox.Text = "90.0";
    pitchAngleTextBox.AutoSize = true;
    pitchAngleTextBox.ReadOnly = true;
    pitchAngleTextBox.Top = pitchAngleLabel.Top;
    pitchAngleTextBox.Left = 490;

    massTextBox = new TextBox();
    massTextBox.Width = 60;
    massTextBox.Text = "0.0";
    massTextBox.AutoSize = true;
    massTextBox.ReadOnly = true;
    massTextBox.Top = massLabel.Top;
    massTextBox.Left = 490;

    payloadTextBox = new TextBox();
    payloadTextBox.Width = 60;
    payloadTextBox.Text = "0.0";
    payloadTextBox.AutoSize = true;
    payloadTextBox.Top = payloadLabel.Top;
    payloadTextBox.Left = 150;

    diameterTextBox = new TextBox();
    diameterTextBox.Width = 60;
    diameterTextBox.Text = "10.0";
    diameterTextBox.AutoSize = true;
    diameterTextBox.Top = diameterLabel.Top;
    diameterTextBox.Left = 150;

    seaLevelThrustTextBox = new TextBox();
    seaLevelThrustTextBox.Width = 80;
    seaLevelThrustTextBox.Text = "6670000.0";
    seaLevelThrustTextBox.AutoSize = true;
    seaLevelThrustTextBox.ReadOnly = true;
    seaLevelThrustTextBox.Top = seaLevelThrustLabel.Top;
    seaLevelThrustTextBox.Left = 150;

    vacuumThrustTextBox = new TextBox();
    vacuumThrustTextBox.Width = 80;
    vacuumThrustTextBox.Text = "7860000.0";
    vacuumThrustTextBox.AutoSize = true;
    vacuumThrustTextBox.ReadOnly = true;
    vacuumThrustTextBox.Top = vacuumThrustLabel.Top;
    vacuumThrustTextBox.Left = 150;

    initialMassTextBox = new TextBox();
    initialMassTextBox.Width = 60;
    initialMassTextBox.Text = "424371.0";
    initialMassTextBox.AutoSize = true;
    initialMassTextBox.ReadOnly = true;
    initialMassTextBox.Top = initialMassLabel.Top;
    initialMassTextBox.Left = 150;

    //  Create a ComboBox to select the engine.
    engineTypeComboBox = new ComboBox();
    engineTypeComboBox.Items.Add("F1");
    engineTypeComboBox.Items.Add("RD-180");
    engineTypeComboBox.SelectedIndex = 0;
    engineTypeComboBox.Left = 150;
    engineTypeComboBox.Width = 80;
    engineTypeComboBox.Top = engineTypeLabel.Top;
    engineTypeComboBox.SelectedIndexChanged += 
                new EventHandler(EngineStateChanged);

    numEngineComboBox = new ComboBox();
    numEngineComboBox.Items.Add("1");
    numEngineComboBox.Items.Add("2");
    numEngineComboBox.Items.Add("3");
    numEngineComboBox.Items.Add("4");
    numEngineComboBox.Items.Add("5");
    numEngineComboBox.Items.Add("6");
    numEngineComboBox.SelectedIndex = 0;
    numEngineComboBox.Left = 150;
    numEngineComboBox.Width = 50;
    numEngineComboBox.Top = numEngineLabel.Top;
    numEngineComboBox.SelectedIndexChanged += 
                new EventHandler(EngineStateChanged);

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    launchButton = new Button();
    launchButton.Text = "Launch";
    launchButton.Height = buttonHeight;
    launchButton.Width = buttonWidth;
    launchButton.Top = 560;
    launchButton.Left = buttonLeft;
    launchButton.Click += new EventHandler(LaunchButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 600;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 551;
    drawingPanel.Height = 251;
    drawingPanel.Left = 50;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(velocityLabel);
    this.Controls.Add(altitudeLabel);
    this.Controls.Add(crossRangeLabel);
    this.Controls.Add(pitchAngleLabel);
    this.Controls.Add(payloadLabel);
    this.Controls.Add(engineTypeLabel);
    this.Controls.Add(numEngineLabel);
    this.Controls.Add(massLabel);
    this.Controls.Add(rocketSpecLabel);
    this.Controls.Add(rocketResultsLabel);
    this.Controls.Add(burnTimeLabel);
    this.Controls.Add(diameterLabel);
    this.Controls.Add(seaLevelThrustLabel);
    this.Controls.Add(vacuumThrustLabel);
    this.Controls.Add(initialMassLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(initialMassTextBox);
    this.Controls.Add(altitudeTextBox);
    this.Controls.Add(crossRangeTextBox);
    this.Controls.Add(pitchAngleTextBox);
    this.Controls.Add(payloadTextBox);
    this.Controls.Add(massTextBox);
    this.Controls.Add(burnTimeTextBox);
    this.Controls.Add(diameterTextBox);
    this.Controls.Add(seaLevelThrustTextBox);
    this.Controls.Add(vacuumThrustTextBox);
    this.Controls.Add(initialMassTextBox);
    this.Controls.Add(launchButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(engineTypeComboBox);
    this.Controls.Add(numEngineComboBox);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 650;
    this.Height = 675;
    this.Text = "Rocket Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Launch" button
  public void LaunchButtonClicked(object source, EventArgs e) {

    //  Get some initial quantities from the textfields.
    double payloadMass = Convert.ToDouble(payloadTextBox.Text);
    double rocketDiameter = Convert.ToDouble(diameterTextBox.Text);

    //  Determine number of engines and engine type
    String numEngineString = (string)numEngineComboBox.SelectedItem;
    int numEngines = Convert.ToInt32(numEngineString);

    double seaLevelThrustPerEngine;
    double vacuumThrustPerEngine;
    double massFlowRate;
    double engineMass;
    double burnTime;
    String engineSelection = (string)engineTypeComboBox.SelectedItem;
    if ( String.Equals(engineSelection,"F1") ) {
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
    UpdateDisplay();

    //  Launch the rocket using a Timer object
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the time, location, velocity, and mass of rocket
    rocket.S = 0.0;     //  time = 0.0
    rocket.SetQ(0.0,0);   //  vx0 = 0.0
    rocket.SetQ(0.0,1);   //  x0  = 0.0
    rocket.SetQ(0.0,2);   //  vy0 = 0.0
    rocket.SetQ(0.0,3);   //  y0  = 0.0
    rocket.SetQ(0.0,4);   //  vz0 = 0.0
    rocket.SetQ(0.0,5);   //  z0  = 0.0
    rocket.SetQ(rocket.InitialMass, 7);
    rocket.SetQ(Math.PI/2.0, 9);  //  pitch angle in radians

    //  Update the display.
    UpdateDisplay();
  }

  //  Event handling method when either of the ComboBox
  //  selections is changed.
  public void EngineStateChanged(object source, EventArgs e) {
    //  Get some initial quantities from the textfields.
    double payloadMass = Convert.ToDouble(payloadTextBox.Text);
    double rocketDiameter = Convert.ToDouble(diameterTextBox.Text);

    //  Determine number of engines and engine type
    String numEngineString = (string)numEngineComboBox.SelectedItem;
    int numEngines = Convert.ToInt32(numEngineString);

    double seaLevelThrustPerEngine;
    double vacuumThrustPerEngine;
    double massFlowRate;
    double engineMass;
    double burnTime;
    String engineSelection = (string)engineTypeComboBox.SelectedItem;
    if ( String.Equals(engineSelection,"F1") ) {
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
    massTextBox.Text = ""+initialMass;
    initialMassTextBox.Text = ""+initialMass;
    seaLevelThrustTextBox.Text = ""+numEngines*seaLevelThrustPerEngine;
    vacuumThrustTextBox.Text = ""+numEngines*vacuumThrustPerEngine;
  }

  //  This method redraws the GUI display.
  private void UpdateDisplay() {
    Graphics g = drawingPanel.CreateGraphics();
    int width = drawingPanel.Width - 1;
    int height = drawingPanel.Height - 1;

    //  Clear the current display.
    g.Clear(Color.White);

    //  Update the position of the soccerball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    Pen blackPen = new Pen(Color.Black, 1);

    //  Draw the ground
    g.DrawLine(blackPen, 0, height-10, width, height-10);

    //  Draw the rocket.
    int rocketX = (int)(rocket.GetX()/500.0) + 10;
    int rocketZ;
    double theta = rocket.GetTheta()*180.0/Math.PI;
    if ( theta < 12.0 ) {
      rocketZ = height - 10 - rocket0IconHeight - 
                  (int)(rocket.GetZ()/600.0);
      g.DrawImage(rocket0Icon, rocketX, rocketZ, 
                  rocket0IconWidth, rocket0IconHeight);
    }
    else if ( theta < 40.0 ) {
      rocketZ = height - 10 - rocket30IconHeight - 
                  (int)(rocket.GetZ()/600.0);
      g.DrawImage(rocket30Icon, rocketX, rocketZ, 
                  rocket30IconWidth, rocket30IconHeight);
    }
    else if ( theta < 70.0 ) {
      rocketZ = height - 10 - rocket60IconHeight - 
                  (int)(rocket.GetZ()/600.0);
      g.DrawImage(rocket60Icon, rocketX, rocketZ, 
                  rocket60IconWidth, rocket60IconHeight);
    }
    else {
      rocketZ = height - 10 - rocket90IconHeight - 
                  (int)(rocket.GetZ()/600.0);
      g.DrawImage(rocket90Icon, rocketX, rocketZ, 
                  rocket90IconWidth, rocket90IconHeight);
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.1 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Compute the new location and velocity of the rocket. 
    double timeIncrement = 1.0;
    rocket.UpdateLocationAndVelocity(timeIncrement);

    //  Update the display
    UpdateDisplay();

    //  Update the "Trajectory Data" textfields
    burnTimeTextBox.Text = ""+(int)rocket.GetTime();
    altitudeTextBox.Text = ""+(int)rocket.GetZ();
    double vx = rocket.GetVx();
    double vz = rocket.GetVz();
    double v = Math.Sqrt(vx*vx + vz*vz);
    velocityTextBox.Text = ""+(int)v;
    crossRangeTextBox.Text = ""+(int)rocket.GetX();
    double pitchAngle = rocket.GetTheta()*180.0/Math.PI;
    pitchAngleTextBox.Text = ""+(int)pitchAngle;
    massTextBox.Text = ""+(int)rocket.GetMass();

    //  If the time reaches the rocket burn time, stop
    //  the simulation.
    if ( rocket.GetTime() > rocket.BurnTime ) {
      gameTimer.Stop();
    }
  }

  static void Main() {
    Application.Run(new RocketSimulator());
  }
}
