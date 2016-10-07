using System;
using System.Windows.Forms;
using System.Drawing;

public class FreeThrow : Form
{
  private Label velocityLabel;
  private Label angleLabel;

  private TextBox velocityTextBox;
  private TextBox angleTextBox;

  private Button fireButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image playerIcon;
  private int playerWidth;
  private int playerHeight;
  private Image ballIcon;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  //  Declare a DragProjectile object that will model
  //  the basketball
  private DragProjectile basketball;

  //  A flag to tell if the shot was good.
  bool shotMade;

  public FreeThrow() {

    //  Create a DragProjectile object to represent the soccer ball.
    basketball = 
        new DragProjectile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.62, 0.0452, 1.2, 0.5);
    basketball.SetQ(1.0,1);   //  x0
    basketball.SetQ(2.25,5);  //  z0

    //  The shot is missed until it is made
    shotMade = false;

    //  Set up images
    playerIcon = Image.FromFile("basketball_player.gif");
    playerWidth = playerIcon.Width;
    playerHeight = playerIcon.Height;
    ballIcon = Image.FromFile("Basketball.jpg");

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 20;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    velocityLabel = new Label();
    velocityLabel.Text = "Initial velocity (m/s)";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 20;
    velocityLabel.Left = 10;
    velocityLabel.Width = 120;

    angleLabel = new Label();
    angleLabel.Text = "Shot angle (deg)";
    angleLabel.Font = new Font(angleLabel.Font, FontStyle.Bold);
    angleLabel.Top = 50;
    angleLabel.Left = 10;
    angleLabel.Width = 120;

    //  Create TextBox objects to display the inputs.
    velocityTextBox = new TextBox();
    velocityTextBox.Width = 50;
    velocityTextBox.Text = "7.5";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 140;

    angleTextBox = new TextBox();
    angleTextBox.Width = 50;
    angleTextBox.Text = "40.0";
    angleTextBox.AutoSize = true;
    angleTextBox.Top = angleLabel.Top;
    angleTextBox.Left = 140;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    fireButton = new Button();
    fireButton.Text = "Fire";
    fireButton.Height = buttonHeight;
    fireButton.Width = buttonWidth;
    fireButton.Top = 80;
    fireButton.Left = buttonLeft;
    fireButton.Click += new EventHandler(FireButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 130;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 251;
    drawingPanel.Left = 300;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(velocityLabel);
    this.Controls.Add(angleLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(angleTextBox);
    this.Controls.Add(fireButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 650;
    this.Height = 400;
    this.Text = "Free Throw";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Fire" button
  public void FireButtonClicked(object source, EventArgs e) {

    //  Set the shot made flag to false.
    shotMade = false;

    //  Extract input values from textfields
    double velocity = Convert.ToDouble(velocityTextBox.Text);
    double angle = Convert.ToDouble(angleTextBox.Text);

    //  Convert the angle to radians.
    angle = angle*Math.PI/180.0;

    //  Compute initial velocities
    double vx0 = velocity*Math.Cos(angle);
    double vy0 = 0.0;
    double vz0 = velocity*Math.Sin(angle);

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
    UpdateDisplay();

    //  Fire the golf ball using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the time, location, and velocity of ball;
    basketball.S = 0.0;       //  time
    basketball.SetQ(0.0,0);   //  vx0
    basketball.SetQ(1.0,1);   //  x0
    basketball.SetQ(0.0,2);   //  vy0
    basketball.SetQ(0.0,3);   //  y0
    basketball.SetQ(0.0,4);   //  vz0
    basketball.SetQ(2.25,5);  //  z0
    shotMade = false;

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

    //  Backboard
    g.FillRectangle(brush, 218, 87, 2, 47);

    //  Basket
    g.FillRectangle(brush, 198, 129, 74, 2);

    g.DrawLine(blackPen, 198, 129, 203, 144);
    g.DrawLine(blackPen, 203, 144, 212, 144);
    g.DrawLine(blackPen, 212, 144, 217, 129);

    //  Floor support
    g.FillRectangle(brush, 272, 129, 2, 122);

     //  Draw basketball player
    g.DrawImage(playerIcon, 30, 150, playerWidth/2, playerHeight/2);

    //  Update the position of the basketball
    //  on the screen.
    int xPosition = (int)(40.0*basketball.GetX());
    int zPosition = (int)(height - 40.0*basketball.GetZ());
    g.DrawImage(ballIcon, xPosition-5, zPosition-5, 10, 10);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Update the time and compute the new position
    //  of the basketball. 
    double timeIncrement = 0.025;
    basketball.UpdateLocationAndVelocity(timeIncrement);

    //  Update the display
    UpdateDisplay();

    //  Access the Graphics object of the drawing panel.
    Graphics g = drawingPanel.CreateGraphics();
    SolidBrush brush = new SolidBrush(Color.Black);
    Font font = new Font("Arial", 12);

    //  Determine if the ball impacts the backboard. If it does,
    //  change the x-velocity assuming a frictionless collision.
    //  A collision occurs if the x-velocity is positive, and the
    //  ball location is inside the backboard area.
    if ( basketball.GetVx() > 0.0 && 
         basketball.GetX() >= 5.5 && 
         basketball.GetZ() > 2.93 && 
         basketball.GetZ() < 4.0 ) {
      double ecor = 0.75;  // coefficient of restitution
      basketball.SetQ(-ecor*basketball.GetVx(),0);   //  vx
    }

    //  Determine if the shot is good.
    //  The center of the basket is 4.2 m from the free
    //  throw line (which is at x = 1.0 m). A shot is considered
    //  to be made if the center of the ball is within 0.22
    //  of the center of the basket.
    double dx = basketball.GetX() - 5.2;
    double dz = basketball.GetZ() - 3.048;
    double distance = Math.Sqrt( dx*dx + dz*dz );
    if ( distance <= 0.14 ) {
      shotMade = true;
      basketball.SetQ(0.0,0);   //  vx
    }

    //  If the basketball hits the ground, stop the simulation
    if ( basketball.GetZ() <= 0.25 ) {
      if ( shotMade == true ) {
        g.DrawString("Shot is good.", font, brush, 80, 40); 
      }
      else {
        g.DrawString("Shot missed.", font, brush, 80, 40); 
      }

      //  Stop the simulation
      gameTimer.Stop();
    }
  }

  static void Main() {
    Application.Run(new FreeThrow());
  }
}
