using System;
using System.Windows.Forms;
using System.Drawing;

public class BoatSimulator : Form
{
  private Label velocityLabel;
  private Label distanceLabel;
  private Label timeLabel;

  private TextBox velocityTextBox;
  private TextBox distanceTextBox;
  private TextBox timeTextBox;

  private Button startButton;
  private Button stopButton;
  private Button resetButton;

  private RadioButton accelButton;
  private RadioButton cruiseButton;
  private RadioButton brakeButton;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image noPlaneIcon;
  private int noPlaneWidth; 
  private int noPlaneHeight;
  private Image halfPlaneIcon;
  private int halfPlaneWidth; 
  private int halfPlaneHeight;
  private Image fullPlaneIcon;
  private int fullPlaneWidth; 
  private int fullPlaneHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private FountainLightning boat; 

  //  A rectangular marker is used to simulate motion.
  //  This field stores its x-location.
  private double rectangleOneX;

  public BoatSimulator() {

    //  Create a FountainLighning object with default values to
    //  initialize the display.
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

    //  Set up some images and determine their dimensions
    noPlaneIcon = Image.FromFile("Boat_NoPlane.jpg");
    noPlaneWidth = noPlaneIcon.Width;
    noPlaneHeight = noPlaneIcon.Height;

    halfPlaneIcon = Image.FromFile("Boat_HalfPlane.jpg");
    halfPlaneWidth = halfPlaneIcon.Width;
    halfPlaneHeight = halfPlaneIcon.Height;

    fullPlaneIcon = Image.FromFile("Boat_FullPlane.jpg");
    fullPlaneWidth = fullPlaneIcon.Width;
    fullPlaneHeight = fullPlaneIcon.Height;

    //  Initialize the x-location of the rectangular marker
    rectangleOneX = 200.0;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 100;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    velocityLabel = new Label();
    velocityLabel.Text = "Velocity (km/hr)";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 300;
    velocityLabel.Left = 280;
    velocityLabel.Width = 120;

    distanceLabel = new Label();
    distanceLabel.Text = "Distance traveled (m)";
    distanceLabel.Font = new Font(distanceLabel.Font, FontStyle.Bold);
    distanceLabel.Top = 330;
    distanceLabel.Left = 280;
    distanceLabel.Width = 120;

    timeLabel = new Label();
    timeLabel.Text = "Time (s)";
    timeLabel.Font = new Font(timeLabel.Font, FontStyle.Bold);
    timeLabel.Top = 360;
    timeLabel.Left = 280;
    timeLabel.Width = 50;

    //  Create TextBox objects to display the inputs.
    velocityTextBox = new TextBox();
    velocityTextBox.Width = 50;
    velocityTextBox.Text = "0.0";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 420;

    distanceTextBox = new TextBox();
    distanceTextBox.Width = 60;
    distanceTextBox.Text = "0.0";
    distanceTextBox.AutoSize = true;
    distanceTextBox.Top = distanceLabel.Top;
    distanceTextBox.Left = 420;

    timeTextBox = new TextBox();
    timeTextBox.Width = 60;
    timeTextBox.Text = "0.0";
    timeTextBox.AutoSize = true;
    timeTextBox.Top = timeLabel.Top;
    timeTextBox.Left = 420;

    //  Create RadioButton objects 
    int buttonHeight = 20;
    int buttonWidth = 70;
    int buttonLeft = 30;

    accelButton = new RadioButton();
    accelButton.Text = "Accelerate";
    accelButton.Height = buttonHeight;
    accelButton.Width = buttonWidth;
    accelButton.Top = 250;
    accelButton.Left = buttonLeft;
    accelButton.Checked = true;

    cruiseButton = new RadioButton();
    cruiseButton.Text = "Cruise";
    cruiseButton.Height = buttonHeight;
    cruiseButton.Width = buttonWidth;
    cruiseButton.Top = 280;
    cruiseButton.Left = buttonLeft;

    brakeButton = new RadioButton();
    brakeButton.Text = "Declerate";
    brakeButton.Height = buttonHeight;
    brakeButton.Width = buttonWidth;
    brakeButton.Top = 310;
    brakeButton.Left = buttonLeft;

    //  Create Button objects 
    buttonHeight = 20;
    buttonWidth = 45;
    buttonLeft = 20;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 340;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    stopButton = new Button();
    stopButton.Text = "Stop";
    stopButton.Height = buttonHeight;
    stopButton.Width = buttonWidth;
    stopButton.Top = 370;
    stopButton.Left = buttonLeft;
    stopButton.Click += new EventHandler(StopButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 400;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 401;
    drawingPanel.Height = 151;
    drawingPanel.Left = 50;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(velocityLabel);
    this.Controls.Add(distanceLabel);
    this.Controls.Add(timeLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(distanceTextBox);
    this.Controls.Add(timeTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(stopButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(accelButton);
    this.Controls.Add(cruiseButton);
    this.Controls.Add(brakeButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 450;
    this.Text = "Boat Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Start the boat moving using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Stop" button
  public void StopButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset boat parameters.
    boat.SetQ(0.0,0);   //  vx0 = 0.0
    accelButton.Checked = true;

    //  Update the display.
    UpdateDisplay();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the time, location, and velocity of boat;
    boat.S = 0.0;       //  time = 0.0
    boat.SetQ(0.0,0);   //  vx0 = 0.0
    boat.SetQ(0.0,1);   //  x0 = 0.0
    boat.SetQ(0.0,2);   //  vy0 = 0.0
    boat.SetQ(0.0,3);   //  y0 = 0.0
    boat.SetQ(0.0,4);   //  vz0 = 0.0
    boat.SetQ(0.0,5);   //  z0 = 0.0

    rectangleOneX = 200.0;
    accelButton.Checked = true;

    //  Update the display.
    UpdateDisplay();
  }

  //  This method redraws the GUI display.
  private void UpdateDisplay() {
    Graphics g = drawingPanel.CreateGraphics();
    int width = drawingPanel.Width - 1;
    int height = drawingPanel.Height - 1;

    //  Clear the current display.
    g.Clear(Color.White);

    //  Update the TextBox components.
    //  Convert the velocity from m/s to km/hr and
    //  only show integer values
    distanceTextBox.Text = ""+(int)boat.GetX();
    timeTextBox.Text = ""+(float)boat.GetTime();
    velocityTextBox.Text = ""+(int)(boat.GetVx()*3.6);

    //  Update the position of the soccerball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    Pen blackPen = new Pen(Color.Black, 1);

    //  Draw the boat and the waterline.
    //  Draw the boat depending on whether the boat
    //  is planing or not.
    g.DrawLine(blackPen, 0, 70, width, 70);

    if ( boat.GetVx() < boat.PlaningSpeed ) {
      g.DrawImage(noPlaneIcon, 100, 95, noPlaneWidth, noPlaneHeight);
    }
    else if ( boat.GetVx() < boat.PlaningSpeed + 2.0 ) {
      g.DrawImage(halfPlaneIcon, 100, 95, halfPlaneWidth, halfPlaneHeight);
    }
    else {
      g.DrawImage(fullPlaneIcon, 100, 95, fullPlaneWidth, fullPlaneHeight);
    }

    //  Draw the markers
    g.FillRectangle(brush, (int)rectangleOneX, 40, 10, 30);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Figure out if the boat is accelerating,
    //  cruising, or braking, and set the mode of
    //  the boat accordingly
    if ( accelButton.Checked == true ) {
      boat.Mode = "accelerating";
    }
    else if ( cruiseButton.Checked == true ) {
      boat.Mode = "cruising";
    }
    else {
      boat.Mode = "decelerating";
    }

    //  Update the boat velocity and position at the next
    //  time increment. 
    double timeIncrement = 0.08;
    boat.UpdateLocationAndVelocity(timeIncrement);

    //  Update the location of the rectangular marker
    rectangleOneX = rectangleOneX - 10.0*boat.GetVx()*timeIncrement;

    //  If the marker has gone off the display, move it
    //  back to the right hand side
    if ( rectangleOneX < 0.0 ) {
      rectangleOneX = 401.0;
    }

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new BoatSimulator());
  }
}
