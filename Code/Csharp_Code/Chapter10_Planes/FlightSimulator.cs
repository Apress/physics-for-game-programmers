using System;
using System.Windows.Forms;
using System.Drawing;

public class FlightSimulator : Form
{
  private Label headingLabel;
  private Label climbAngleLabel;
  private Label airspeedLabel;
  private Label climbRateLabel;
  private Label altitudeLabel;
  private Label statusLabel;
  private Label throttleLabel;
  private Label alphaLabel;
  private Label bankLabel;
  private Label flapLabel;
  private Label zero1Label;
  private Label zero2Label;
  private Label zero3Label;
  private Label hundredLabel;
  private Label twentyALabel;
  private Label twentyBLabel;
  private Label ntwentyALabel;
  private Label ntwentyBLabel;

  private TextBox headingTextBox;
  private TextBox climbAngleTextBox;
  private TextBox airspeedTextBox;
  private TextBox climbRateTextBox;
  private TextBox altitudeTextBox;
  private TextBox statusTextBox;

  private Button startButton;
  private Button resetButton;

  private TrackBar throttleTrackBar;
  private TrackBar alphaTrackBar;
  private TrackBar bankTrackBar;

  private ComboBox flapComboBox;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image planeIcon;
  private int planeWidth; 
  private int planeHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private CessnaSkyhawk plane; 

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

    //  Set up images
    planeIcon = Image.FromFile("airplaneCartoon.jpg");
    planeWidth = planeIcon.Width;
    planeHeight = planeIcon.Height;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 100;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Initialize the x-location of the rectangular marker
    rectangleOneX = 200.0;

    //  Create some Labels
    headingLabel = new Label();
    headingLabel.Text = "Heading angle (deg)";
    headingLabel.Font = new Font(headingLabel.Font, FontStyle.Bold);
    headingLabel.Top = 400;
    headingLabel.Left = 330;
    headingLabel.Width = 120;

    climbAngleLabel = new Label();
    climbAngleLabel.Text = "Climb angle (deg)";
    climbAngleLabel.Font = new Font(climbAngleLabel.Font, FontStyle.Bold);
    climbAngleLabel.Top = 430;
    climbAngleLabel.Left = 330;
    climbAngleLabel.Width = 120;

    airspeedLabel = new Label();
    airspeedLabel.Text = "Airspeed (km/hr)";
    airspeedLabel.Font = new Font(airspeedLabel.Font, FontStyle.Bold);
    airspeedLabel.Top = 460;
    airspeedLabel.Left = 330;
    airspeedLabel.Width = 120;

    climbRateLabel = new Label();
    climbRateLabel.Text = "Climb rate (m/s)";
    climbRateLabel.Font = new Font(climbRateLabel.Font, FontStyle.Bold);
    climbRateLabel.Top = 490;
    climbRateLabel.Left = 330;
    climbRateLabel.Width = 120;

    altitudeLabel = new Label();
    altitudeLabel.Text = "Altitude (m)";
    altitudeLabel.Font = new Font(altitudeLabel.Font, FontStyle.Bold);
    altitudeLabel.Top = 520;
    altitudeLabel.Left = 330;
    altitudeLabel.Width = 100;

    throttleLabel = new Label();
    throttleLabel.Text = "Throttle (%)";
    throttleLabel.Font = new Font(throttleLabel.Font, FontStyle.Bold);
    throttleLabel.Top = 350;
    throttleLabel.Left = 20;
    throttleLabel.Width = 80;

    alphaLabel = new Label();
    alphaLabel.Text = "Angle of attack (deg)";
    alphaLabel.Font = new Font(alphaLabel.Font, FontStyle.Bold);
    alphaLabel.Top = 420;
    alphaLabel.Left = 20;
    alphaLabel.Width = 130;

    bankLabel = new Label();
    bankLabel.Text = "Bank angle (deg)";
    bankLabel.Font = new Font(bankLabel.Font, FontStyle.Bold);
    bankLabel.Top = 480;
    bankLabel.Left = 20;
    bankLabel.Width = 120;

    flapLabel = new Label();
    flapLabel.Text = "Flap deflection (deg)";
    flapLabel.Font = new Font(flapLabel.Font, FontStyle.Bold);
    flapLabel.Top = 540;
    flapLabel.Left = 20;
    flapLabel.Width = 120;

    statusLabel = new Label();
    statusLabel.Text = "Status";
    statusLabel.Font = new Font(statusLabel.Font, FontStyle.Bold);
    statusLabel.Top = 600;
    statusLabel.Left = 150;
    statusLabel.Width = 50;

    zero1Label = new Label();
    zero1Label.Text = "0";
    zero1Label.Font = new Font(zero1Label.Font, FontStyle.Bold);
    zero1Label.Top = 338;
    zero1Label.Left = 120;
    zero1Label.Height = 12;

    hundredLabel = new Label();
    hundredLabel.Text = "100";
    hundredLabel.Font = new Font(hundredLabel.Font, FontStyle.Bold);
    hundredLabel.Top = 338;
    hundredLabel.Left = 260;
    hundredLabel.Height = 12;

    zero2Label = new Label();
    zero2Label.Text = "0";
    zero2Label.Font = new Font(zero2Label.Font, FontStyle.Bold);
    zero2Label.Top = 408;
    zero2Label.Left = 200;
    zero2Label.Height = 12;

    twentyALabel = new Label();
    twentyALabel.Text = "20";
    twentyALabel.Font = new Font(twentyALabel.Font, FontStyle.Bold);
    twentyALabel.Top = 398;
    twentyALabel.Left = 260;
    twentyALabel.Height = 12;

    ntwentyALabel = new Label();
    ntwentyALabel.Text = "-20";
    ntwentyALabel.Font = new Font(ntwentyALabel.Font, FontStyle.Bold);
    ntwentyALabel.Top = 398;
    ntwentyALabel.Left = 135;
    ntwentyALabel.Height = 12;

    zero3Label = new Label();
    zero3Label.Text = "0";
    zero3Label.Font = new Font(zero3Label.Font, FontStyle.Bold);
    zero3Label.Top = 468;
    zero3Label.Left = 190;
    zero3Label.Height = 12;

    twentyBLabel = new Label();
    twentyBLabel.Text = "20";
    twentyBLabel.Font = new Font(twentyBLabel.Font, FontStyle.Bold);
    twentyBLabel.Top = 468;
    twentyBLabel.Left = 250;
    twentyBLabel.Height = 12;

    ntwentyBLabel = new Label();
    ntwentyBLabel.Text = "-20";
    ntwentyBLabel.Font = new Font(ntwentyBLabel.Font, FontStyle.Bold);
    ntwentyBLabel.Top = 468;
    ntwentyBLabel.Left = 130;
    ntwentyBLabel.Height = 12;

    //  Create TextBox objects to display the inputs.
    headingTextBox = new TextBox();
    headingTextBox.Width = 60;
    headingTextBox.Text = "0.0";
    headingTextBox.AutoSize = true;
    headingTextBox.Top = headingLabel.Top;
    headingTextBox.Left = 470;

    climbAngleTextBox = new TextBox();
    climbAngleTextBox.Width = 60;
    climbAngleTextBox.Text = "0.0";
    climbAngleTextBox.AutoSize = true;
    climbAngleTextBox.Top = climbAngleLabel.Top;
    climbAngleTextBox.Left = 470;

    airspeedTextBox = new TextBox();
    airspeedTextBox.Width = 60;
    airspeedTextBox.Text = "0.0";
    airspeedTextBox.AutoSize = true;
    airspeedTextBox.Top = airspeedLabel.Top;
    airspeedTextBox.Left = 470;

    climbRateTextBox = new TextBox();
    climbRateTextBox.Width = 60;
    climbRateTextBox.Text = "0.0";
    climbRateTextBox.AutoSize = true;
    climbRateTextBox.Top = climbRateLabel.Top;
    climbRateTextBox.Left = 470;

    altitudeTextBox = new TextBox();
    altitudeTextBox.Width = 60;
    altitudeTextBox.Text = "0.0";
    altitudeTextBox.AutoSize = true;
    altitudeTextBox.Top = altitudeLabel.Top;
    altitudeTextBox.Left = 470;

    statusTextBox = new TextBox();
    statusTextBox.Width = 200;
    statusTextBox.Text = "";
    statusTextBox.AutoSize = true;
    statusTextBox.Top = statusLabel.Top;
    statusTextBox.Left = 210;
    statusTextBox.ReadOnly = true;

    //  Create TrackBar components to change the
    //  throttle, angle of attack, and bank angle.
    throttleTrackBar = new TrackBar();
    throttleTrackBar.Minimum = 0;
    throttleTrackBar.Maximum = 100;
    throttleTrackBar.Orientation = Orientation.Horizontal;
    throttleTrackBar.TickStyle = TickStyle.TopLeft;
    throttleTrackBar.TickFrequency = 10;
    throttleTrackBar.Value = 0;
    throttleTrackBar.Width = 150;
    throttleTrackBar.Height = 40;
    throttleTrackBar.Left = 120;
    throttleTrackBar.Top = throttleLabel.Top;

    alphaTrackBar = new TrackBar();
    alphaTrackBar.Minimum = -20;
    alphaTrackBar.Maximum = 20;
    alphaTrackBar.Orientation = Orientation.Horizontal;
    alphaTrackBar.TickStyle = TickStyle.TopLeft;
    alphaTrackBar.TickFrequency = 4;
    alphaTrackBar.Value = 0;
    alphaTrackBar.Width = 150;
    alphaTrackBar.Height = 40;
    alphaTrackBar.Left = 130;
    alphaTrackBar.Top = alphaLabel.Top;

    bankTrackBar = new TrackBar();
    bankTrackBar.Minimum = -20;
    bankTrackBar.Maximum = 20;
    bankTrackBar.Orientation = Orientation.Horizontal;
    bankTrackBar.TickStyle = TickStyle.TopLeft;
    bankTrackBar.TickFrequency = 4;
    bankTrackBar.Value = 0;
    bankTrackBar.Width = 150;
    bankTrackBar.Height = 40;
    bankTrackBar.Left = 120;
    bankTrackBar.Top = bankLabel.Top;

    //  Create a ComboBox to select the flap angle.
    flapComboBox = new ComboBox();
    flapComboBox.Items.Add("0");
    flapComboBox.Items.Add("20");
    flapComboBox.Items.Add("40");
    flapComboBox.SelectedIndex = 0;
    flapComboBox.Left = 150;
    flapComboBox.Width = 50;
    flapComboBox.Top = flapLabel.Top;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 570;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 610;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 401;
    drawingPanel.Height = 301;
    drawingPanel.Left = 50;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(headingLabel);
    this.Controls.Add(climbAngleLabel);
    this.Controls.Add(airspeedLabel);
    this.Controls.Add(climbRateLabel);
    this.Controls.Add(altitudeLabel);
    this.Controls.Add(statusLabel);
    this.Controls.Add(throttleLabel);
    this.Controls.Add(alphaLabel);
    this.Controls.Add(bankLabel);
    this.Controls.Add(flapLabel);
    this.Controls.Add(headingTextBox);
    this.Controls.Add(climbAngleTextBox);
    this.Controls.Add(airspeedTextBox);
    this.Controls.Add(climbRateTextBox);
    this.Controls.Add(altitudeTextBox);
    this.Controls.Add(statusTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(flapComboBox);
    this.Controls.Add(throttleTrackBar);
    this.Controls.Add(alphaTrackBar);
    this.Controls.Add(bankTrackBar);
    this.Controls.Add(zero1Label);
    this.Controls.Add(zero2Label);
    this.Controls.Add(zero3Label);
    this.Controls.Add(hundredLabel);
    this.Controls.Add(twentyALabel);
    this.Controls.Add(ntwentyALabel);
    this.Controls.Add(twentyBLabel);
    this.Controls.Add(ntwentyBLabel);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 600;
    this.Height = 700;
    this.Text = "Flight Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Start the plane moving using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    plane.S = 0.0;       //  time = 0.0
    plane.SetQ(0.0,0);   //  vx0 = 0.0
    plane.SetQ(0.0,1);   //  x0 = 0.0
    plane.SetQ(0.0,2);   //  vy0 = 0.0
    plane.SetQ(0.0,3);   //  y0 = 0.0
    plane.SetQ(0.0,4);   //  vz0 = 0.0
    plane.SetQ(0.0,5);   //  z0 = 0.0

    plane.Throttle = 0.0;
    plane.Alpha = 0.0;
    plane.Bank = 0.0;

    //  Reset GUI controls
    throttleTrackBar.Value = 0;
    alphaTrackBar.Value = 0;
    bankTrackBar.Value = 0;
    statusTextBox.Text = "Throttle set to zero";

    rectangleOneX = 200.0;

    //  Update the display.
    UpdateDisplay();
  }

  //  This method redraws the GUI display.
  private void UpdateDisplay() {
    double vx = plane.GetVx();
    double vy = plane.GetVy();
    double vz = plane.GetVz();
    double vh = Math.Sqrt(vx*vx + vy*vy);
    double airspeed = Math.Sqrt(vx*vx + vy*vy + vz*vz);
    double climbAngle;
    double headingAngle;

    if ( vh == 0.0 ) {
      climbAngle = 0.0;
    }
    else {
      climbAngle = Math.Atan(vz/vh);
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
      headingAngle = Math.Atan(vy/vx);
    }
    else if ( vx < 0.0 && vy > 0.0 ) {
      headingAngle = 0.5*Math.PI + Math.Atan(Math.Abs(vx/vy));
    }
    else if ( vx < 0.0 && vy < 0.0 ) {
      headingAngle = Math.PI + Math.Atan(vy/vx);
    }
    else {
      headingAngle = 1.5*Math.PI + Math.Atan(Math.Abs(vx/vy));
    }

    climbAngleTextBox.Text = ""+(float)(climbAngle*180.0/Math.PI);
    headingTextBox.Text = ""+(float)(headingAngle*180.0/Math.PI);
    climbRateTextBox.Text = ""+(float)vz;
    altitudeTextBox.Text = ""+(float)plane.GetZ();

    //  Airspeed is converted from m/s to km/hr.
    airspeedTextBox.Text = ""+(float)(airspeed*3.6);

    Graphics g = drawingPanel.CreateGraphics();
    int width = drawingPanel.Width - 1;
    int height = drawingPanel.Height - 1;

    //  Clear the current display.
    g.Clear(Color.White);

    //  Update the position of the soccerball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    Pen blackPen = new Pen(Color.Black, 1);

    //  Draw the plane and the ground.
    g.DrawLine(blackPen, 0, height-10, width, height-10);
    int planeZ = height - 10 - planeHeight - 
                 (int)(plane.GetZ()/20.0);
    g.DrawImage(planeIcon, 170, planeZ, planeWidth, planeHeight);

    //  Draw the markers
    g.FillRectangle(brush, (int)rectangleOneX, height-30, 10, 20);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.1 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Get the initial values from the TextBox.
    //  The bank angle is converted to radians. The angle of
    //  attack is not converted to radians because the Cl-alpha
    //  equations use alpha in units of degrees.
    double throttle = (double)throttleTrackBar.Value/100.0;
    double alpha = (double)alphaTrackBar.Value;
    double bank = (double)bankTrackBar.Value*Math.PI/180.0;

    string flap = (string)flapComboBox.SelectedItem;

    plane.Throttle = throttle;
    plane.Alpha = alpha;
    plane.Bank = bank;
    plane.Flap = flap;

    if ( plane.Throttle == 0.0 ) {
      statusTextBox.Text = "Throttle set to zero";
    }
    else {
      statusTextBox.Text = "";
    }

    //  When the plane reaches the ground, stop the
    //  simulation. If the z-velocity is greater than
    //  -2.0 m/s, you have landed safely. If it's
    //  greater than -5.0 m/s it was a rough landing.
    //  Anything beyond -5.0 m/s is a crash.
    if ( plane.GetZ() < 0.0 ) { 
      gameTimer.Stop();
     
      if ( plane.GetVz() > -2.0 ) {
        statusTextBox.Text = "You've landed safely";
      }
      else if ( plane.GetVz() > -5.0 ) {
        statusTextBox.Text = "Rough landing";
      }
      else {
        statusTextBox.Text = "You crashed";
      }
    }

    //  Update the location and velocity of the airplane.
    double timeIncrement = 0.2;
    plane.UpdateLocationAndVelocity(timeIncrement);

    //  Update the location of the rectangular marker
    rectangleOneX = rectangleOneX - 5.0*plane.GetVx()*timeIncrement;

    //  If the marker has gone off the display, move it
    //  back to the right hand side
    if ( rectangleOneX < 0.0 ) {
      rectangleOneX = 401.0;
    }
    if ( rectangleOneX > 410.0 ) {
      rectangleOneX = 0.0;
    }

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new FlightSimulator());
  }
}
