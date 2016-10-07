using System;
using System.Windows.Forms;
using System.Drawing;

public class Shuffleboard : Form
{
  private Label muLabel;
  private Label velocityLabel;
  private Label massLabel;

  private TextBox muTextBox;
  private TextBox massTextBox;
  private TextBox velocityTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private double mu;
  private double mass;
  private double initialVelocity;
  private double xLocation;
  private double time;

  private static double G = 9.81;

  public Shuffleboard() {

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    muLabel = new Label();
    muLabel.Text = "Friction coefficient";
    muLabel.Font = new Font(muLabel.Font, FontStyle.Bold);
    muLabel.Top = 30;
    muLabel.Left = 10;
    muLabel.Width = 120;

    velocityLabel = new Label();
    velocityLabel.Text = "Initial velocity, m/s";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 60;
    velocityLabel.Left = 10;
    velocityLabel.Width = 120;

    massLabel = new Label();
    massLabel.Text = "Disk mass, kg";
    massLabel.Font = new Font(massLabel.Font, FontStyle.Bold);
    massLabel.Top = 90;
    massLabel.Left = 10;
    massLabel.Width = 120;

    //  Create TextBox objects to display the outcome.
    muTextBox = new TextBox();
    muTextBox.Width = 100;
    muTextBox.Text = "0.5";
    muTextBox.AutoSize = true;
    muTextBox.Top = muLabel.Top;
    muTextBox.Left = 150;

    velocityTextBox = new TextBox();
    velocityTextBox.Width = 100;
    velocityTextBox.Text = "4.5";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 150;

    massTextBox = new TextBox();
    massTextBox.Width = 100;
    massTextBox.Text = "1.0";
    massTextBox.AutoSize = true;
    massTextBox.Top = massLabel.Top;
    massTextBox.Left = 150;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 400;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 50;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 100;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 151;
    drawingPanel.Left = 50;
    drawingPanel.Top = 150;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(muLabel);
    this.Controls.Add(velocityLabel);
    this.Controls.Add(massLabel);
    this.Controls.Add(muTextBox);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(massTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Height = 350;
    this.Width = 500;
    this.Text = "Shuffleboard Game";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Extract initial data from the textfields.
    mu = Convert.ToDouble(muTextBox.Text);
    initialVelocity = Convert.ToDouble(velocityTextBox.Text);
    mass = Convert.ToDouble(massTextBox.Text);

    //  Set the time and the initial x-location of the disk
    time = 0.0;
    xLocation = 0.0;

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the disk location
    xLocation = 0.0;

    //  Update the display.
    UpdateDisplay();
  }

  //  This method redraws the GUI display.
  private void UpdateDisplay() {
    Graphics g = drawingPanel.CreateGraphics();
    int width = drawingPanel.Width - 1;
    int height = drawingPanel.Height - 1;

    //  Clear the current display.
    g.Clear(drawingPanel.BackColor);

    Pen blackPen = new Pen(Color.Black, 2);
    g.DrawLine(blackPen, 0, 125, width, 125);
    g.DrawLine(blackPen, 200, 125, 200, height);
    g.DrawLine(blackPen, 225, 125, 225, height);
    g.DrawLine(blackPen, 250, 125, 250, height);
    g.DrawLine(blackPen, 275, 125, 275, height);

    SolidBrush brush = new SolidBrush(Color.Black);
    Font font = new Font("Arial", 12);
    g.DrawString("10", font, brush, 202, 127);
    g.DrawString("20", font, brush, 227, 127);
    g.DrawString("50", font, brush, 252, 127);
    g.DrawString("0", font, brush, 277, 127);

    //  Update the position of the box and
    //  ball on the screen.
    int x = (int)(xLocation*100);
    g.FillRectangle(brush, x, 115, 10, 10);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {
    //  Update the time and compute the new position
    //  of the disk. 
    time += 0.05; 

    //  Compute the current velocity of the disk.
    double velocity = initialVelocity - mu*G*time;

    //  Update the position of the disk.
    xLocation = initialVelocity*time - 0.5*mu*G*time*time;

    //  Update the display
    UpdateDisplay();

    //  If the disk stops moving or if it reaches
    //  the end of the board, stop the simulation
    if ( velocity <= 0.0 || xLocation > 2.9) {
      gameTimer.Stop();
    }

  }

  static void Main() {
    Application.Run(new Shuffleboard());
  }
}
