using System;
using System.Windows.Forms;
using System.Drawing;

public class GolfGame : Form
{
  private Label clubLabel;
  private Label velocityLabel;
  private Label distanceLabel;
  private Label axesLabel;
  private Label densityLabel;
  private Label windVxLabel;
  private Label windVyLabel;
  private Label rxLabel;
  private Label ryLabel;
  private Label rzLabel;
  private Label spinAxisLabel;

  private TextBox velocityTextBox;
  private TextBox distanceTextBox;
  private TextBox densityTextBox;
  private TextBox windVxTextBox;
  private TextBox windVyTextBox;
  private TextBox rxTextBox;
  private TextBox ryTextBox;
  private TextBox rzTextBox;

  private ComboBox axesComboBox;
  private ComboBox clubComboBox;

  private Button fireButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  This field is for the images used in the game.
  private Image golferIcon;
  private Image flagIcon;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  //  Declare a GolfBall object
  private GolfBall golfball;

  //  The player can control the distance to the hole.
  private double distanceToHole;

  public GolfGame() {

    //  Create a GolfBall object with default values
    //  so the display can be updated the first time.
    golfball = 
        new GolfBall(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.0459, 0.001432, 0.5, 1.225, 0.0, 0.0,
                           0.0, 1.0, 0.0, 300.0, 0.02135);

    //  Set up images
    golferIcon = Image.FromFile("Golfer.jpg");
    flagIcon = Image.FromFile("Hole_Cartoon.jpg");

    //  Initialize the distanceToHole field.
    distanceToHole = 200.0;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    clubLabel = new Label();
    clubLabel.Text = "Club";
    clubLabel.Font = new Font(clubLabel.Font, FontStyle.Bold);
    clubLabel.Top = 20;
    clubLabel.Left = 10;
    clubLabel.Width = 50;

    velocityLabel = new Label();
    velocityLabel.Text = "Impact velocity, m/s";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 50;
    velocityLabel.Left = 10;
    velocityLabel.Width = 120;

    distanceLabel = new Label();
    distanceLabel.Text = "Distance to hole, m";
    distanceLabel.Font = new Font(distanceLabel.Font, FontStyle.Bold);
    distanceLabel.Top = 80;
    distanceLabel.Left = 10;
    distanceLabel.Width = 120;

    axesLabel = new Label();
    axesLabel.Text = "View axes";
    axesLabel.Font = new Font(axesLabel.Font, FontStyle.Bold);
    axesLabel.Top = 110;
    axesLabel.Left = 10;
    axesLabel.Width = 70;

    densityLabel = new Label();
    densityLabel.Text = "density, kg/m^3";
    densityLabel.Font = new Font(densityLabel.Font, FontStyle.Bold);
    densityLabel.Top = 20;
    densityLabel.Left = 220;
    densityLabel.Width = 110;

    windVxLabel = new Label();
    windVxLabel.Text = "Wind x-velocity, m/s";
    windVxLabel.Font = new Font(windVxLabel.Font, FontStyle.Bold);
    windVxLabel.Top = 50;
    windVxLabel.Left = 220;
    windVxLabel.Width = 120;

    windVyLabel = new Label();
    windVyLabel.Text = "Wind y-velocity, m/s";
    windVyLabel.Font = new Font(windVyLabel.Font, FontStyle.Bold);
    windVyLabel.Top = 80;
    windVyLabel.Left = 220;
    windVyLabel.Width = 120;

    spinAxisLabel = new Label();
    spinAxisLabel.Text = "Spin Axes";
    spinAxisLabel.Font = new Font(spinAxisLabel.Font, FontStyle.Bold);
    spinAxisLabel.Top = 110;
    spinAxisLabel.Left = 220;
    spinAxisLabel.Width = 120;

    rxLabel = new Label();
    rxLabel.Text = "rx";
    rxLabel.Font = new Font(rxLabel.Font, FontStyle.Bold);
    rxLabel.Top = 140;
    rxLabel.Left = 250;
    rxLabel.Width = 50;

    ryLabel = new Label();
    ryLabel.Text = "ry";
    ryLabel.Font = new Font(ryLabel.Font, FontStyle.Bold);
    ryLabel.Top = 170;
    ryLabel.Left = 250;
    ryLabel.Width = 50;

    rzLabel = new Label();
    rzLabel.Text = "rz";
    rzLabel.Font = new Font(rzLabel.Font, FontStyle.Bold);
    rzLabel.Top = 200;
    rzLabel.Left = 250;
    rzLabel.Width = 50;

    //  Create TextBox objects to display the inputs.
    velocityTextBox = new TextBox();
    velocityTextBox.Width = 50;
    velocityTextBox.Text = "40.0";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 140;

    distanceTextBox = new TextBox();
    distanceTextBox.Width = 50;
    distanceTextBox.Text = "200.0";
    distanceTextBox.AutoSize = true;
    distanceTextBox.Top = distanceLabel.Top;
    distanceTextBox.Left = 140;

    densityTextBox = new TextBox();
    densityTextBox.Width = 60;
    densityTextBox.Text = "1.225";
    densityTextBox.AutoSize = true;
    densityTextBox.Top = densityLabel.Top;
    densityTextBox.Left = 330;

    windVxTextBox = new TextBox();
    windVxTextBox.Width = 50;
    windVxTextBox.Text = "0.0";
    windVxTextBox.AutoSize = true;
    windVxTextBox.Top = windVxLabel.Top;
    windVxTextBox.Left = 350;

    windVyTextBox = new TextBox();
    windVyTextBox.Width = 50;
    windVyTextBox.Text = "0.0";
    windVyTextBox.AutoSize = true;
    windVyTextBox.Top = windVyLabel.Top;
    windVyTextBox.Left = 350;

    rxTextBox = new TextBox();
    rxTextBox.Width = 60;
    rxTextBox.Text = "0.0";
    rxTextBox.AutoSize = true;
    rxTextBox.Top = rxLabel.Top;
    rxTextBox.Left = 300;

    ryTextBox = new TextBox();
    ryTextBox.Width = 50;
    ryTextBox.Text = "1.0";
    ryTextBox.AutoSize = true;
    ryTextBox.Top = ryLabel.Top;
    ryTextBox.Left = 300;

    rzTextBox = new TextBox();
    rzTextBox.Width = 50;
    rzTextBox.Text = "0.0";
    rzTextBox.AutoSize = true;
    rzTextBox.Top = rzLabel.Top;
    rzTextBox.Left = 300;

    //  Create a ComboBox to select the view axes.
    axesComboBox = new ComboBox();
    axesComboBox.Items.Add("XZ");
    axesComboBox.Items.Add("XY");
    axesComboBox.SelectedIndex = 0;
    axesComboBox.Left = 80;
    axesComboBox.Top = axesLabel.Top;

    //  Create a ComboBox to select the club.
    clubComboBox = new ComboBox();
    clubComboBox.Items.Add("Driver");
    clubComboBox.Items.Add("3 wood");
    clubComboBox.Items.Add("3 iron");
    clubComboBox.Items.Add("5 iron");
    clubComboBox.Items.Add("7 iron");
    clubComboBox.Items.Add("9 iron");
    clubComboBox.SelectedIndex = 0;
    clubComboBox.Left = 80;
    clubComboBox.Top = clubLabel.Top;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    fireButton = new Button();
    fireButton.Text = "Fire";
    fireButton.Height = buttonHeight;
    fireButton.Width = buttonWidth;
    fireButton.Top = 200;
    fireButton.Left = buttonLeft;
    fireButton.Click += new EventHandler(FireButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 250;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 501;
    drawingPanel.Height = 201;
    drawingPanel.Left = 20;
    drawingPanel.Top = 300;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(velocityLabel);
    this.Controls.Add(distanceLabel);
    this.Controls.Add(axesLabel);
    this.Controls.Add(densityLabel);
    this.Controls.Add(windVxLabel);
    this.Controls.Add(windVyLabel);
    this.Controls.Add(spinAxisLabel);
    this.Controls.Add(rxLabel);
    this.Controls.Add(ryLabel);
    this.Controls.Add(rzLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(distanceTextBox);
    this.Controls.Add(densityTextBox);
    this.Controls.Add(windVxTextBox);
    this.Controls.Add(windVyTextBox);
    this.Controls.Add(rxTextBox);
    this.Controls.Add(ryTextBox);
    this.Controls.Add(rzTextBox);
    this.Controls.Add(axesComboBox);
    this.Controls.Add(clubComboBox);
    this.Controls.Add(fireButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 650;
    this.Height = 550;
    this.Text = "Golf Game";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Fire" button
  public void FireButtonClicked(object source, EventArgs e) {

    //  Define golf ball parameters
    double ballMass = 0.0459;
    double radius = 0.02135;
    double area = Math.PI*radius*radius;
    double cd = 0.22;   //  drag coefficient
    double ecor = 0.78;   //  coefficient of restitution
    
    //  Get the initial values from the textfield
    double velocity = Convert.ToDouble(velocityTextBox.Text);
    distanceToHole = Convert.ToDouble(distanceTextBox.Text);
    double windVx = Convert.ToDouble(windVxTextBox.Text);
    double windVy = Convert.ToDouble(windVyTextBox.Text);
    double density = Convert.ToDouble(densityTextBox.Text);
    double rx = Convert.ToDouble(rxTextBox.Text);
    double ry = Convert.ToDouble(ryTextBox.Text);
    double rz = Convert.ToDouble(rzTextBox.Text);

    //  Set the club mass and loft based on the combo box 
    //  selection
    double clubMass;
    double loft;
    String club = (string)clubComboBox.SelectedItem;
    if ( String.Equals(club,"Driver") ) {
      clubMass = 0.2;
      loft = 11.0;
    }
    else if ( String.Equals(club,"3 wood") ) {
      clubMass = 0.208;
      loft = 15.0;
    }
    else if ( String.Equals(club,"3 iron") ) {
      clubMass = 0.239;
      loft = 21.0;
    }
    else if ( String.Equals(club,"5 iron") ) {
      clubMass = 0.253;
      loft = 27.0;
    }
    else if ( String.Equals(club,"7 iron") ) {
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
    double cosL = Math.Cos(loft);
    double sinL = Math.Sin(loft);

    //  Calculate the pre-collision velocities normal
    //  and parallel to the line of action.
    double vcp = cosL*velocity;
    double vcn = -sinL*velocity;

    //  Compute the post-collision velocity of the ball
    //  along the line of action.
    double vbp = (1.0+ecor)*clubMass*vcp/(clubMass+ballMass);

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

    //  Create a GolfBall object representing the golf ball.
    golfball = new GolfBall(0.0, 0.0, 0.0, vx0, vy0, vz0, 
         0.0, ballMass, area, density, cd, windVx, windVy,
         rx, ry, rz, omega, radius);

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
    golfball.S = 0.0;       //  time
    golfball.SetQ(0.0,0);   //  vx0
    golfball.SetQ(0.0,1);   //  x0
    golfball.SetQ(0.0,2);   //  vy0
    golfball.SetQ(0.0,3);   //  y0
    golfball.SetQ(0.0,4);   //  vz0
    golfball.SetQ(0.0,5);   //  z0

    //  Reset the distance to hole.
    distanceToHole = Convert.ToDouble(distanceTextBox.Text);
 
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

    //  Update the position of the golfball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    Pen blackPen = new Pen(Color.Black, 1);

    //  Draw picture based on whether the XZ or 
    //  XY axes are selected.
    string axes = (string)axesComboBox.SelectedItem;
    if ( String.Equals(axes, "XZ") ) {

      //  Draw the golfer.
      int zLocation = height - 50;
      g.DrawImage(golferIcon, 0, zLocation, 34, 50);

      //  Draw the flag
      zLocation = height - 62;
      g.DrawImage(flagIcon, (int)(2.0*distanceToHole), 
                  zLocation, 55, 62);

      //  Update the position of the golfball
      //  on the screen.
      int xPosition = (int)(2.0*golfball.GetX() + 14);
      int zPosition = (int)(height - 5 - 2.0*golfball.GetZ());
      g.FillEllipse(brush, xPosition, zPosition, 5, 5);
    }
    else {
      //  Draw location of green.
      g.DrawEllipse(blackPen, (int)(2.0*distanceToHole - 20), 80, 40, 40);
      g.FillEllipse(brush, (int)(2.0*distanceToHole - 4), 96, 8, 8);

      //  Update the position of the golfball
      //  on the screen.
      int xPosition = (int)(2.0*golfball.GetX());
      int yPosition = (int)(100 - 2 - 2.0*golfball.GetY());
      g.FillEllipse(brush, xPosition, yPosition, 5, 5);
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Update the time and compute the new position
    //  of the golfball. 
    double timeIncrement = 0.07;
    golfball.UpdateLocationAndVelocity(timeIncrement);

    //  Update the display
    UpdateDisplay();

    //  Access the Graphics object of the drawing panel.
    Graphics g = drawingPanel.CreateGraphics();

    //  When the golfball hits the ground, stop the simulation
    //  and see where ball has landed.
    if ( golfball.GetZ() <= 0.0 ) {

Console.WriteLine("time="+(float)golfball.GetTime()+
"  x="+(float)golfball.GetX()+
"  y="+(float)golfball.GetY()+"  z="+(float)golfball.GetZ());

      //  Stop the simulation
      gameTimer.Stop();

        //  Determine if ball is on the green.
      SolidBrush brush = new SolidBrush(Color.Black);
      Font font = new Font("Arial", 12);
      if ( golfball.GetX() > distanceToHole - 10.0 &&
           golfball.GetX() < distanceToHole + 10.0 &&
           golfball.GetY() < 10.0) {
        g.DrawString("You're on the green", font, brush, 100, 30);

      }
      else {
        g.DrawString("You missed", font, brush, 100, 30);
      }
    }
  }

  static void Main() {
    Application.Run(new GolfGame());
  }
}
