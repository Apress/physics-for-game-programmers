using System;
using System.Windows.Forms;
using System.Drawing;

public class CarSimulator : Form
{
  private Label velocityLabel;
  private Label rpmLabel;
  private Label gearLabel;
  private Label distanceLabel;
  private Label timeLabel;

  private TextBox velocityTextBox;
  private TextBox rpmTextBox;
  private TextBox gearTextBox;
  private TextBox distanceTextBox;
  private TextBox timeTextBox;
  private TextBox messageTextBox;

  private Button startButton;
  private Button stopButton;
  private Button resetButton;
  private Button shiftUpButton;
  private Button shiftDownButton;

  private RadioButton accelButton;
  private RadioButton cruiseButton;
  private RadioButton brakeButton;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image carIcon;
  private int carWidth; 
  private int carHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private BoxsterS car; 

  //  Two rectangular markers are used to simulate motion.
  //  These fields store their x-locations.
  private double rectangleOneX;
  private double rectangleTwoX;

  public CarSimulator() {

    //  Create a BoxsterS object with default values to
    //  initialize the display.
    double x0 = 0.0;
    double y0 = 0.0;
    double z0 = 0.0;
    double vx0 = 0.0;
    double vy0 = 0.0;
    double vz0 = 0.0;
    double time = 0.0;
    double density = 1.2;
    car = new BoxsterS(x0, y0, z0, vx0, vy0, vz0, time, density);

    //  Set up images
    carIcon = Image.FromFile("porscheCartoon.jpg");
    carWidth = carIcon.Width;
    carHeight = carIcon.Height;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Initialize the x-locations of the rectangular markers
    rectangleOneX = 0.0;
    rectangleTwoX = 200.0;

    //  Create some Labels
    velocityLabel = new Label();
    velocityLabel.Text = "Velocity (km/hr)";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 300;
    velocityLabel.Left = 280;
    velocityLabel.Width = 120;

    rpmLabel = new Label();
    rpmLabel.Text = "Engine rpm";
    rpmLabel.Font = new Font(rpmLabel.Font, FontStyle.Bold);
    rpmLabel.Top = 330;
    rpmLabel.Left = 280;
    rpmLabel.Width = 80;

    gearLabel = new Label();
    gearLabel.Text = "Gear";
    gearLabel.Font = new Font(gearLabel.Font, FontStyle.Bold);
    gearLabel.Top = 360;
    gearLabel.Left = 280;
    gearLabel.Width = 50;

    distanceLabel = new Label();
    distanceLabel.Text = "Distance traveled (m)";
    distanceLabel.Font = new Font(distanceLabel.Font, FontStyle.Bold);
    distanceLabel.Top = 390;
    distanceLabel.Left = 280;
    distanceLabel.Width = 120;

    timeLabel = new Label();
    timeLabel.Text = "Time (s)";
    timeLabel.Font = new Font(timeLabel.Font, FontStyle.Bold);
    timeLabel.Top = 420;
    timeLabel.Left = 280;
    timeLabel.Width = 50;

    //  Create TextBox objects to display the inputs.
    velocityTextBox = new TextBox();
    velocityTextBox.Width = 50;
    velocityTextBox.Text = "0.0";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 420;

    rpmTextBox = new TextBox();
    rpmTextBox.Width = 50;
    rpmTextBox.Text = "1000.0";
    rpmTextBox.AutoSize = true;
    rpmTextBox.Top = rpmLabel.Top;
    rpmTextBox.Left = 420;

    gearTextBox = new TextBox();
    gearTextBox.Width = 50;
    gearTextBox.Text = "1";
    gearTextBox.AutoSize = true;
    gearTextBox.Top = gearLabel.Top;
    gearTextBox.Left = 420;

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

    messageTextBox = new TextBox();
    messageTextBox.Width = 200;
    messageTextBox.Text = "";
    messageTextBox.AutoSize = true;
    messageTextBox.Top = 500;
    messageTextBox.Left = 110;
    messageTextBox.ReadOnly = true;

    //  Create RadioButton objects 
    int buttonHeight = 20;
    int buttonWidth = 50;
    int buttonLeft = 30;

    accelButton = new RadioButton();
    accelButton.Text = "Accelerate";
    accelButton.Height = buttonHeight;
    accelButton.Width = buttonWidth+20;
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
    brakeButton.Text = "Brake";
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

    shiftUpButton = new Button();
    shiftUpButton.Text = "Shift Up";
    shiftUpButton.Height = buttonHeight;
    shiftUpButton.Width = buttonWidth+10;
    shiftUpButton.Top = 370;
    shiftUpButton.Left = buttonLeft;
    shiftUpButton.Click += new EventHandler(ShiftUpButtonClicked);

    shiftDownButton = new Button();
    shiftDownButton.Text = "Shift Down";
    shiftDownButton.Height = buttonHeight;
    shiftDownButton.Width = buttonWidth+20;
    shiftDownButton.Top = 400;
    shiftDownButton.Left = buttonLeft;
    shiftDownButton.Click += new EventHandler(ShiftDownButtonClicked);

    stopButton = new Button();
    stopButton.Text = "Stop";
    stopButton.Height = buttonHeight;
    stopButton.Width = buttonWidth;
    stopButton.Top = 430;
    stopButton.Left = buttonLeft;
    stopButton.Click += new EventHandler(StopButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 460;
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
    this.Controls.Add(rpmLabel);
    this.Controls.Add(gearLabel);
    this.Controls.Add(distanceLabel);
    this.Controls.Add(timeLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(rpmTextBox);
    this.Controls.Add(gearTextBox);
    this.Controls.Add(distanceTextBox);
    this.Controls.Add(timeTextBox);
    this.Controls.Add(messageTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(stopButton);
    this.Controls.Add(shiftUpButton);
    this.Controls.Add(shiftDownButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(accelButton);
    this.Controls.Add(cruiseButton);
    this.Controls.Add(brakeButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 550;
    this.Text = "Car Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Start the car moving using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Shift Up" button
  public void ShiftUpButtonClicked(object source, EventArgs e) {
    car.ShiftGear(1);
  }

  //  Event handling method for the "Shift Down" button
  public void ShiftDownButtonClicked(object source, EventArgs e) {
    car.ShiftGear(-1);
  }

  //  Event handling method for the "Stop" button
  public void StopButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset car parameters.
    car.SetQ(0.0,0);   //  vx0 set to zero
    car.OmegaE = 1000.0;
    car.GearNumber = 1;
    accelButton.Checked = true;

    //  Update the display.
    UpdateDisplay();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset car parameters.
    car.S = 0.0;       //  time set to zero
    car.SetQ(0.0,0);   //  vx0 set to zero
    car.SetQ(0.0,1);   //  x0 set to zero
    car.OmegaE = 1000.0;
    car.GearNumber = 1;
    accelButton.Checked = true;
    rectangleOneX = 0.0;
    rectangleTwoX = 200.0;

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

    //  Update the position of the soccerball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    Pen blackPen = new Pen(Color.Black, 1);

    //  Display the current time
    timeTextBox.Text = ""+(float)car.GetTime();

    //  Convert the velocity from m/s to km/hr and
    //  only show integer values
    velocityTextBox.Text = ""+(int)(car.GetVx()*3.6);

    //  Only show integer values for rpm, gear number, 
    //  and distance.
    rpmTextBox.Text = ""+(int)car.OmegaE;
    gearTextBox.Text = ""+(int)car.GearNumber;
    distanceTextBox.Text = ""+(int)car.GetX();

    //  Draw the car and the ground.
    g.DrawLine(blackPen, 0, 70, width, 70);
    g.DrawImage(carIcon, 100, 95, carWidth, carHeight);

    //  Draw the markers
    g.FillRectangle(brush, (int)rectangleOneX, 40, 10, 30);
    g.FillRectangle(brush, (int)rectangleTwoX, 40, 10, 30);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Figure out if the car is accelerating,
    //  cruising, or braking, and set the mode of
    //  the car accordingly
    if ( accelButton.Checked == true ) {
      car.Mode = "accelerating";
    }
    else if ( cruiseButton.Checked == true ) {
      car.Mode = "cruising";
    }
    else {
      car.Mode = "braking";
    }

    //  Update the car velocity and position at the next
    //  time increment. 
    double timeIncrement = 0.06;
    car.UpdateLocationAndVelocity(timeIncrement);

    //  Compute the new engine rpm value
    double rpm = car.GetVx()*60.0*car.GetGearRatio()*
          car.FinalDriveRatio/(2.0*Math.PI*car.WheelRadius);
    car.OmegaE = rpm;

    //  If the rpm exceeds the redline value, put a
    //  warning message on the screen. First, clear the
    //  message textfield of any existing messages. 
    messageTextBox.Text = "";
    if ( car.OmegaE > car.Redline ) {
      messageTextBox.Text = "Warning: Exceeding redline rpm";
    }
    if ( car.OmegaE > 8000.0 ) {
      messageTextBox.Text = "You have blown the engine!";
      gameTimer.Stop();
    }

    //  Update the location of the rectangular markers
    rectangleOneX = rectangleOneX + 10.0*car.GetVx()*timeIncrement;
    rectangleTwoX = rectangleTwoX + 10.0*car.GetVx()*timeIncrement;

    //  If the markers have gone off the display, move them
    //  back to zero
    if ( rectangleOneX > 401.0 ) {
      rectangleOneX = 0.0;
    }
    if ( rectangleTwoX > 401.0 ) {
      rectangleTwoX = 0.0;
    }

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new CarSimulator());
  }
}
