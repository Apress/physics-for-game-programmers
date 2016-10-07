using System;
using System.Windows.Forms;
using System.Drawing;

public class GravityGame : Form
{
  private Label planetLabel;
  private Label velocityLabel;
  private Label resultsLabel;

  private TextBox resultsTextBox;
  private TextBox velocityTextBox;

  private Button startButton;
  private Button dropButton;
  private Button resetButton;

  private ComboBox planetComboBox;

  private Panel drawingPanel;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private double boxLocation;   //  horizontal location of box
  private double boxVelocity;
  private int boxWidth;         // width of box in pixels.
  private double ballAltitude;  // vertical location of ball
  private double ballLocation;  //  horizontal location of ball
  private double initialAltitude;  // initial ball altitude
  private double g;         //  gravitational acceleration
  private double time;      // time since box begins to move.
  private double dropTime;  // time since ball was dropped
  private bool dropped;  // true if the ball has been dropped.

  public GravityGame() {

    //  Set box, ball, and time parameters.
    boxLocation = 0.0;
    boxWidth = 40;
    initialAltitude = 120.0;
    ballAltitude = initialAltitude;
    ballLocation = 210.0;
    time = 0.0;
    dropTime = 0.0;
    dropped = false;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    planetLabel = new Label();
    planetLabel.Text = "Planet";
    planetLabel.Font = new Font(planetLabel.Font, FontStyle.Bold);
    planetLabel.Top = 30;
    planetLabel.Left = 10;
    planetLabel.Width = 50;

    velocityLabel = new Label();
    velocityLabel.Text = "Box velocity, m/s";
    velocityLabel.Font = new Font(velocityLabel.Font, FontStyle.Bold);
    velocityLabel.Top = 60;
    velocityLabel.Left = 10;

    resultsLabel = new Label();
    resultsLabel.Text = "Results";
    resultsLabel.Font = new Font(resultsLabel.Font, FontStyle.Bold);
    resultsLabel.Top = 230;
    resultsLabel.Left = 10;
    resultsLabel.Width = 50;

    //  Create TextBox objects to display the outcome.
    velocityTextBox = new TextBox();
    velocityTextBox.Width = 100;
    velocityTextBox.Text = "25.0";
    velocityTextBox.AutoSize = true;
    velocityTextBox.Top = velocityLabel.Top;
    velocityTextBox.Left = 110;

    resultsTextBox = new TextBox();
    resultsTextBox.Width = 100;
    resultsTextBox.Text = "";
    resultsTextBox.AutoSize = true;
    resultsTextBox.Top = resultsLabel.Top;
    resultsTextBox.Left = 80;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 100;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    dropButton = new Button();
    dropButton.Text = "Drop";
    dropButton.Height = buttonHeight;
    dropButton.Width = buttonWidth;
    dropButton.Top = 140;
    dropButton.Left = buttonLeft;
    dropButton.Click += new EventHandler(DropButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 180;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a ComboBox to select a planet on which
    //  the ball will be dropped.
    planetComboBox = new ComboBox();
    planetComboBox.Items.Add("Earth");
    planetComboBox.Items.Add("Moon");
    planetComboBox.Items.Add("Jupiter");
    planetComboBox.SelectedIndex = 0;
    planetComboBox.Left = 80;
    planetComboBox.Top = planetLabel.Top;

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 251;
    drawingPanel.Height = 151;
    drawingPanel.Left = 230;
    drawingPanel.Top = 50;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(planetLabel);
    this.Controls.Add(velocityLabel);
    this.Controls.Add(resultsLabel);
    this.Controls.Add(velocityTextBox);
    this.Controls.Add(resultsTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(dropButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(planetComboBox);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Height = 300;
    this.Width = 500;
    this.Text = "Gravity Game";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Get the box velocity from the textfield
    boxVelocity = Convert.ToDouble(velocityTextBox.Text);

    //  Determine which planet is selected and set
    //  the gravitational acceleration accordingly.
    string selectedItem = (string)planetComboBox.SelectedItem;
    if ( String.Equals(selectedItem, "Earth") ) {
      g = 9.81;
    }
    else if ( String.Equals(selectedItem, "Moon") ) {
      g = 1.624;
    }
    else {
      g = 24.8;  //  Jupiter
    }

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Drop" button
  public void DropButtonClicked(object source, EventArgs e) {
    dropped = true;
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the box and ball location and time;
    boxLocation = 0.0;
    ballAltitude = initialAltitude;
    time = 0.0;
    dropTime = 0.0;
    dropped = false;

    //  Blank out the results textfield.
    resultsTextBox.Text = "";

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
    g.DrawLine(blackPen, 0, 130, width, 130);

    //  Update the position of the box and
    //  ball on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);
    g.FillRectangle(brush, (int)boxLocation, 120, boxWidth, 10);

    int zPosition = (int)(initialAltitude - ballAltitude);
    g.FillEllipse(brush, (int)ballLocation, zPosition, 10, 10);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs e) {
    //  Update the time and compute the new position
    //  of the box and ball. 
    double timeIncrement = 0.05;
    time += timeIncrement; 
    boxLocation = boxVelocity*time;

    if ( dropped ) {
      dropTime += timeIncrement;
      ballAltitude = 
           initialAltitude - 0.5*g*dropTime*dropTime;
    }

    //  Update the display
    UpdateDisplay();

    //  If the ball hits the ground, stop the simulation
    //  and determine if it landed in the box.
    if ( ballAltitude <= 0.0 ) {
      gameTimer.Stop();

      if ( ballLocation >= boxLocation &&
           ballLocation <= boxLocation + boxWidth - 10 ) {
        resultsTextBox.Text = "You Win!";
      }
      else {
        resultsTextBox.Text = "Try again";
      }
    }
  }

  static void Main() {
    Application.Run(new GravityGame());
  }
}
