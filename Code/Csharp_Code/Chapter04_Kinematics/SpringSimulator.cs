using System;
using System.Windows.Forms;
using System.Drawing;

public class SpringSimulator : Form
{
  private Label massLabel;
  private Label muLabel;
  private Label kLabel;
  private Label x0Label;

  private TextBox massTextBox;
  private TextBox muTextBox;
  private TextBox kTextBox;
  private TextBox x0TextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  SpringODE spring;

  public SpringSimulator() {

    //  Create a SpringODE object with default values so
    //  the display can be updated the first time.
    spring = new SpringODE(1.0, 0.5, 20.0, 0.4);

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    massLabel = new Label();
    massLabel.Text = "End mass, kg";
    massLabel.Font = new Font(massLabel.Font, FontStyle.Bold);
    massLabel.Top = 50;
    massLabel.Left = 10;
    massLabel.Width = 100;

    muLabel = new Label();
    muLabel.Text = "Damping coefficient, kg/s";
    muLabel.Font = new Font(muLabel.Font, FontStyle.Bold);
    muLabel.Top = 80;
    muLabel.Left = 10;
    muLabel.Width = 130;

    kLabel = new Label();
    kLabel.Text = "Spring constant, N/m";
    kLabel.Font = new Font(kLabel.Font, FontStyle.Bold);
    kLabel.Top = 110;
    kLabel.Left = 10;
    kLabel.Width = 120;

    x0Label = new Label();
    x0Label.Text = "Initial location, m";
    x0Label.Font = new Font(x0Label.Font, FontStyle.Bold);
    x0Label.Top = 140;
    x0Label.Left = 10;
    x0Label.Width = 120;

    //  Create TextBox objects to display the outcome.
    massTextBox = new TextBox();
    massTextBox.Width = 50;
    massTextBox.Text = "1.0";
    massTextBox.AutoSize = true;
    massTextBox.Top = massLabel.Top;
    massTextBox.Left = 150;

    muTextBox = new TextBox();
    muTextBox.Width = 50;
    muTextBox.Text = "0.5";
    muTextBox.AutoSize = true;
    muTextBox.Top = muLabel.Top;
    muTextBox.Left = 150;

    kTextBox = new TextBox();
    kTextBox.Width = 50;
    kTextBox.Text = "20.0";
    kTextBox.AutoSize = true;
    kTextBox.Top = kLabel.Top;
    kTextBox.Left = 150;

    x0TextBox = new TextBox();
    x0TextBox.Width = 50;
    x0TextBox.Text = "0.4";
    x0TextBox.AutoSize = true;
    x0TextBox.Top = x0Label.Top;
    x0TextBox.Left = 150;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 200;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 250;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 151;
    drawingPanel.Height = 301;
    drawingPanel.Left = 230;
    drawingPanel.Top = 50;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(massLabel);
    this.Controls.Add(muLabel);
    this.Controls.Add(kLabel);
    this.Controls.Add(x0Label);
    this.Controls.Add(massTextBox);
    this.Controls.Add(muTextBox);
    this.Controls.Add(kTextBox);
    this.Controls.Add(x0TextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Height = 450;
    this.Width = 400;
    this.Text = "Spring Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Get the initial values from the textfield
    double mass = Convert.ToDouble(massTextBox.Text);
    double mu = Convert.ToDouble(muTextBox.Text);
    double k = Convert.ToDouble(kTextBox.Text);
    double x0 = Convert.ToDouble(x0TextBox.Text);

    //  Create a SpringODE object
    spring = new SpringODE(mass, mu, k, x0);

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the location of the spring based on what
    //  value is in the "Initial location" textfield.
    double x0 = Convert.ToDouble(x0TextBox.Text);
    spring.SetQ(x0, 1);

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
    g.DrawLine(blackPen, 0, 20, width, 20);

    //  Update the position of the box and
    //  ball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    int zPosition = (int)(125 - 100.0*spring.GetX());
    g.FillRectangle(brush, 65, zPosition, 20, 20);
    g.DrawLine(blackPen, 75, 20, 75, zPosition+10);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Use the ODE solver to update the location of
    //  the spring. 
    double dt = 0.05;
    spring.UpdatePositionAndVelocity(dt);

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new SpringSimulator());
  }
}
