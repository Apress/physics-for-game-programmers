using System;
using System.Windows.Forms;
using System.Drawing;

public class LaserSimulator : Form
{
  private Label radiusLabel;
  private Label powerLabel;
  private Label absorptionLabel;

  private TextBox radiusTextBox;
  private TextBox powerTextBox;
  private TextBox absorptionTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  private double elapsedTime;
  private double explosionTime;
  private double airplaneX;
  private int laserX;
  private int laserZ;

  //  These fields are for the images used in the game.
  private Image airplaneIcon;
  private int airplaneIconWidth;
  private int airplaneIconHeight;
  private Image explosionIcon;
  private int explosionIconWidth;
  private int explosionIconHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  public LaserSimulator() {

    elapsedTime = 0.0;
    airplaneX = 0.0;
    laserX = 150;
    laserZ = 280;

    //  Set up some images and determine their dimensions
    airplaneIcon = Image.FromFile("airplaneCartoon.jpg");
    airplaneIconWidth = airplaneIcon.Width;
    airplaneIconHeight = airplaneIcon.Height;
    explosionIcon = Image.FromFile("explosionCartoon.jpg");
    explosionIconWidth = explosionIcon.Width;
    explosionIconHeight = explosionIcon.Height;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 100;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    powerLabel = new Label();
    powerLabel.Text = "Beam power, J/cm^2";
    powerLabel.Font = new Font(powerLabel.Font, FontStyle.Bold);
    powerLabel.Top = 350;
    powerLabel.Left = 20;
    powerLabel.Width = 120;

    radiusLabel = new Label();
    radiusLabel.Text = "Beam radius, cm";
    radiusLabel.Font = new Font(radiusLabel.Font, FontStyle.Bold);
    radiusLabel.Top = 380;
    radiusLabel.Left = 20;
    radiusLabel.Width = 120;

    absorptionLabel = new Label();
    absorptionLabel.Text = "Absorption coefficient";
    absorptionLabel.Font = new Font(absorptionLabel.Font, FontStyle.Bold);
    absorptionLabel.Top = 410;
    absorptionLabel.Left = 20;
    absorptionLabel.Width = 120;

    //  Create TextBox objects to display the inputs.
    radiusTextBox = new TextBox();
    radiusTextBox.Width = 60;
    radiusTextBox.Text = "35.0";
    radiusTextBox.AutoSize = true;
    radiusTextBox.Top = radiusLabel.Top;
    radiusTextBox.Left = 150;

    powerTextBox = new TextBox();
    powerTextBox.Width = 60;
    powerTextBox.Text = "7.5e+6";
    powerTextBox.AutoSize = true;
    powerTextBox.Top = powerLabel.Top;
    powerTextBox.Left = 150;

    absorptionTextBox = new TextBox();
    absorptionTextBox.Width = 60;
    absorptionTextBox.Text = "0.75";
    absorptionTextBox.AutoSize = true;
    absorptionTextBox.Top = absorptionLabel.Top;
    absorptionTextBox.Left = 150;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 350;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 370;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 410;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 351;
    drawingPanel.Height = 301;
    drawingPanel.Left = 50;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;
    drawingPanel.MouseMove += new MouseEventHandler(MouseMoved);

    //  Add the GUI components to the Form
    this.Controls.Add(radiusLabel);
    this.Controls.Add(powerLabel);
    this.Controls.Add(absorptionLabel);
    this.Controls.Add(radiusTextBox);
    this.Controls.Add(powerTextBox);
    this.Controls.Add(absorptionTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 450;
    this.Height = 500;
    this.Text = "Laser Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method called when the mouse moves
  //  over the panel.
  public void MouseMoved(object source, MouseEventArgs evt) {

    laserX = evt.X;
    laserZ = evt.Y;
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset values.
    elapsedTime = 0.0;
    airplaneX = 0.0;
    laserX = 150;
    laserZ = 280;

    //  Update the display.
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Get some initial quantities from the textfields.
    double power = Convert.ToDouble(powerTextBox.Text);
    double radius = Convert.ToDouble(radiusTextBox.Text);
    double absorption = Convert.ToDouble(absorptionTextBox.Text);

    //  Determine the beam area.
    double area = Math.PI*radius*radius;

    //  Compute beam contact time required to destroy airplane.
    explosionTime = 10000.0*area/(absorption*power);

    //  Update the display
    UpdateDisplay();

    //  Start the airplane using a Timer object
    gameTimer.Start();
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

    //  Draw the airplane.
    g.DrawImage(airplaneIcon, (int)airplaneX, 20, 
                airplaneIconWidth, airplaneIconHeight);

    //  Draw the laser building.
    g.FillRectangle(brush, 140, height-30, 20, 20);

    //  Draw laser beam
    Pen redPen = new Pen(Color.Red, 2);
    g.DrawLine(redPen, 150, height-20, laserX, laserZ);

    //  If the time that the laser has been on the airplane is
    //  greater than the necessary time to destroy the airplane, 
    //  draw the explosion icon.
    if ( elapsedTime > explosionTime ) {
      g.DrawImage(explosionIcon, (int)airplaneX - 10, 10, 
                  explosionIconWidth, explosionIconHeight);      
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.1 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  set the time increment. 
    double timeIncrement = 0.1;

    //  If the laser is hitting the airplane increment
    //  the elapsedTime variable. If not, set the elapsedTime
    //  variable to zero.
    if ( laserX > airplaneX && laserX < airplaneX + airplaneIconWidth &&
         laserZ > 10 && laserZ < 10 + airplaneIconHeight ) {
      elapsedTime = elapsedTime + timeIncrement;
    }
    else {
      elapsedTime = 0.0;
    }

    //  The airplane cartoon moves across the screen at a 
    //  constant velocity of 20 pixels/second.
    airplaneX = airplaneX + 20.0*timeIncrement;

    //  Update the display
    UpdateDisplay();

    //  If the time that the laser has been on the airplane is
    //  greater than the necessary time to destroy the airplane, 
    //  stop the simulation.
    if ( elapsedTime > explosionTime ) {
      gameTimer.Stop();      
    }
  }

  static void Main() {
    Application.Run(new LaserSimulator());
  }
}
