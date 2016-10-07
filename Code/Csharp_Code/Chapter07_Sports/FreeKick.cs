using System;
using System.Windows.Forms;
using System.Drawing;

public class FreeKick : Form
{
  private Label vxLabel;
  private Label vyLabel;
  private Label vzLabel;
  private Label rxLabel;
  private Label ryLabel;
  private Label rzLabel;
  private Label spinAxisLabel;
  private Label spinRateLabel;

  private TextBox vxTextBox;
  private TextBox vyTextBox;
  private TextBox vzTextBox;
  private TextBox spinRateTextBox;
  private TextBox rxTextBox;
  private TextBox ryTextBox;
  private TextBox rzTextBox;

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

  //  Declare a SoccerBall object
  private SoccerBall soccerBall;

  public FreeKick() {

    //  Create a SoccerBall object representing the soccer ball.
    soccerBall = 
        new SoccerBall(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.43, 0.038, 1.2, 0.5, 0.0, 0.0,
                           0.0, 0.0, -1.0, 10.0, 0.11, 294.0);

    //  Set up images
    playerIcon = Image.FromFile("SoccerPlayerCartoon.gif");
    playerWidth = playerIcon.Width;
    playerHeight = playerIcon.Height;
    ballIcon = Image.FromFile("SoccerBallCartoon.jpg");

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 20;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    vxLabel = new Label();
    vxLabel.Text = "Initial x-velocity (m/s)";
    vxLabel.Font = new Font(vxLabel.Font, FontStyle.Bold);
    vxLabel.Top = 20;
    vxLabel.Left = 10;
    vxLabel.Width = 120;

    vyLabel = new Label();
    vyLabel.Text = "Initial y-velocity (m/s)";
    vyLabel.Font = new Font(vyLabel.Font, FontStyle.Bold);
    vyLabel.Top = 50;
    vyLabel.Left = 10;
    vyLabel.Width = 120;

    vzLabel = new Label();
    vzLabel.Text = "Initial z-velocity (m/s)";
    vzLabel.Font = new Font(vzLabel.Font, FontStyle.Bold);
    vzLabel.Top = 80;
    vzLabel.Left = 10;
    vzLabel.Width = 120;

    spinRateLabel = new Label();
    spinRateLabel.Text = "Spin rate (rev/s)";
    spinRateLabel.Font = new Font(spinRateLabel.Font, FontStyle.Bold);
    spinRateLabel.Top = 110;
    spinRateLabel.Left = 10;
    spinRateLabel.Width = 100;

    spinAxisLabel = new Label();
    spinAxisLabel.Text = "Spin Axes";
    spinAxisLabel.Font = new Font(spinAxisLabel.Font, FontStyle.Bold);
    spinAxisLabel.Top = 140;
    spinAxisLabel.Left = 10;
    spinAxisLabel.Width = 70;

    rxLabel = new Label();
    rxLabel.Text = "rx";
    rxLabel.Font = new Font(rxLabel.Font, FontStyle.Bold);
    rxLabel.Top = 170;
    rxLabel.Left = 50;
    rxLabel.Width = 50;

    ryLabel = new Label();
    ryLabel.Text = "ry";
    ryLabel.Font = new Font(ryLabel.Font, FontStyle.Bold);
    ryLabel.Top = 200;
    ryLabel.Left = 50;
    ryLabel.Width = 50;

    rzLabel = new Label();
    rzLabel.Text = "rz";
    rzLabel.Font = new Font(rzLabel.Font, FontStyle.Bold);
    rzLabel.Top = 230;
    rzLabel.Left = 50;
    rzLabel.Width = 50;

    //  Create TextBox objects to display the inputs.
    vxTextBox = new TextBox();
    vxTextBox.Width = 50;
    vxTextBox.Text = "-28.0";
    vxTextBox.AutoSize = true;
    vxTextBox.Top = vxLabel.Top;
    vxTextBox.Left = 140;

    vyTextBox = new TextBox();
    vyTextBox.Width = 50;
    vyTextBox.Text = "10.0";
    vyTextBox.AutoSize = true;
    vyTextBox.Top = vyLabel.Top;
    vyTextBox.Left = 140;

    vzTextBox = new TextBox();
    vzTextBox.Width = 50;
    vzTextBox.Text = "4.0";
    vzTextBox.AutoSize = true;
    vzTextBox.Top = vzLabel.Top;
    vzTextBox.Left = 140;

    spinRateTextBox = new TextBox();
    spinRateTextBox.Width = 50;
    spinRateTextBox.Text = "10.0";
    spinRateTextBox.AutoSize = true;
    spinRateTextBox.Top = spinRateLabel.Top;
    spinRateTextBox.Left = 140;

    rxTextBox = new TextBox();
    rxTextBox.Width = 60;
    rxTextBox.Text = "0.0";
    rxTextBox.AutoSize = true;
    rxTextBox.Top = rxLabel.Top;
    rxTextBox.Left = 110;

    ryTextBox = new TextBox();
    ryTextBox.Width = 50;
    ryTextBox.Text = "0.0";
    ryTextBox.AutoSize = true;
    ryTextBox.Top = ryLabel.Top;
    ryTextBox.Left = 110;

    rzTextBox = new TextBox();
    rzTextBox.Width = 50;
    rzTextBox.Text = "-1.0";
    rzTextBox.AutoSize = true;
    rzTextBox.Top = rzLabel.Top;
    rzTextBox.Left = 110;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    fireButton = new Button();
    fireButton.Text = "Fire";
    fireButton.Height = buttonHeight;
    fireButton.Width = buttonWidth;
    fireButton.Top = 260;
    fireButton.Left = buttonLeft;
    fireButton.Click += new EventHandler(FireButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 310;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 301;
    drawingPanel.Left = 300;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(vxLabel);
    this.Controls.Add(vyLabel);
    this.Controls.Add(vzLabel);
    this.Controls.Add(spinRateLabel);
    this.Controls.Add(spinAxisLabel);
    this.Controls.Add(rxLabel);
    this.Controls.Add(ryLabel);
    this.Controls.Add(rzLabel);
    this.Controls.Add(vxTextBox);
    this.Controls.Add(vyTextBox);
    this.Controls.Add(vzTextBox);
    this.Controls.Add(spinRateTextBox);
    this.Controls.Add(rxTextBox);
    this.Controls.Add(ryTextBox);
    this.Controls.Add(rzTextBox);
    this.Controls.Add(fireButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 650;
    this.Height = 400;
    this.Text = "Free Kick";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Fire" button
  public void FireButtonClicked(object source, EventArgs e) {

    //  Extract input values from textfields
    double vx0 = Convert.ToDouble(vxTextBox.Text);
    double vy0 = Convert.ToDouble(vyTextBox.Text);
    double vz0 = Convert.ToDouble(vzTextBox.Text);
    double spinRate = Convert.ToDouble(spinRateTextBox.Text);
    double rx = Convert.ToDouble(rxTextBox.Text);
    double ry = Convert.ToDouble(ryTextBox.Text);
    double rz = Convert.ToDouble(rzTextBox.Text);

    //  Calculate the angular velocity from the spin rate.
    double omega = spinRate*2.0*Math.PI;

    //  The ball starts at a spot 18 meters from and directly
    //  in front of the goal.
    double x0 = 23.2;
    double y0 = 15.0;
    double z0 = 0.0;

    //  Set the density to be sea level, the wind 
    //  velocity to zero, and temperature to be 294 K.
    double density = 1.2;
    double temperature = 294.0;
    double windVx = 0.0;
    double windVy = 0.0;

    //  Define some soccer ball variables. The cd value will be
    //  overridden in the getFunction method of the SoccerBall class.
    double ballMass = 0.43;
    double radius = 0.11;
    double area = Math.PI*radius*radius;
    double cd = 0.25;

    //  Create a SoccerBall object representing the soccer ball.
    soccerBall = new SoccerBall(x0, y0, z0, vx0, vy0, vz0, 
         0.0, ballMass, area, density, cd, windVx, windVy,
         rx, ry, rz, omega, radius, temperature);

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
    soccerBall.S = 0.0;     //  time
    soccerBall.SetQ(0.0,0);   //  vx0
    soccerBall.SetQ(23.2,1);  //  x0
    soccerBall.SetQ(0.0,2);   //  vy0
    soccerBall.SetQ(15.0,3);  //  y0
    soccerBall.SetQ(0.0,4);   //  vz0
    soccerBall.SetQ(0.0,5);   //  z0

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

    //  Draw goal box and endline
    g.DrawLine(blackPen, 30, 60, 85, 60);
    g.DrawLine(blackPen, 85, 60, 85, 243);
    g.DrawLine(blackPen, 85, 243, 30, 243);
    g.DrawLine(blackPen, 30, 0, 30, height);

    //  Draw goal 
    g.DrawLine(blackPen, 30, 115, 10, 115);
    g.DrawLine(blackPen, 10, 115, 10, 188);
    g.DrawLine(blackPen, 10, 188, 30, 188);

    //  Draw defenders
    g.FillEllipse(brush, 75, 126, 10, 10);
    g.FillEllipse(brush, 75, 136, 10, 10);
    g.FillEllipse(brush, 75, 146, 10, 10);
    g.FillEllipse(brush, 75, 156, 10, 10);
    g.FillEllipse(brush, 75, 166, 10, 10);

    //  Draw the soccer player.
    g.DrawImage(playerIcon, 230, 120, 40, 40);

    //  Update the position of the soccerBall
    //  on the screen.
    int xPosition = (int)(10.0*soccerBall.GetX());
    int yPosition = (int)(height - 10.0*soccerBall.GetY());
    g.DrawImage(ballIcon, xPosition-5, yPosition-5, 10, 10);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Update the time and compute the new position
    //  of the soccerBall. 
    double timeIncrement = 0.025;
    soccerBall.UpdateLocationAndVelocity(timeIncrement);

    //  Update the display
    UpdateDisplay();

    //  Access the Graphics object of the drawing panel.
    Graphics g = drawingPanel.CreateGraphics();
    SolidBrush brush = new SolidBrush(Color.Black);
    Font font = new Font("Arial", 12);

    //  See if the soccer ball hits the defenders
    if ( soccerBall.GetX() <= 7.5 && soccerBall.GetX() >= 6.5 ) {
      if ( soccerBall.GetY() < 17.6 && soccerBall.GetY() > 12.1 ) {
          g.DrawString("Hit defenders", font, brush, 80, 40);

        //  Stop the simulation
        gameTimer.Stop();
      }
    }

    //  When the soccerBall passes the end line, stop the 
    //  simulation and see if a goal was scored.
    if ( soccerBall.GetX() <= 3.0 ) {
      if ( soccerBall.GetY() < 11.3) {
        g.DrawString("Wide Left", font, brush, 80, 40);
      }
      else if ( soccerBall.GetY() > 18.5) {
        g.DrawString("Wide Right", font, brush, 80, 40);
      }   
      else if ( soccerBall.GetZ() > 2.44) {
        g.DrawString("Over the Crossbar", font, brush, 80, 40);
      }   
      else {
        g.DrawString("GOAL!  GOAL!", font, brush, 80, 40);
      } 

      //  Stop the simulation
      gameTimer.Stop();
    }
  }

  static void Main() {
    Application.Run(new FreeKick());
  }
}
