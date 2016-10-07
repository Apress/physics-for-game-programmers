using System;
using System.Windows.Forms;
using System.Drawing;

public class SoldierGame : Form
{
  private Label meanLabel;
  private Label sigmaLabel;

  private TextBox meanTextBox;
  private TextBox sigmaTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  Declare an array of Soldier objects
  private Soldier[] soldier;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  public SoldierGame() {

    //  Create an array of 30 soldiers and set their
    //  x- and y-locations.
    soldier = new Soldier[30];

    for(int j=0; j<15; ++j) {
      soldier[j] = new Soldier();
      soldier[j].XLocation = 15.0 + j*10.0;
      soldier[j].YLocation = 10.0;
      soldier[j].Speed = 10.0;

      soldier[j+15] = new Soldier();
      soldier[j+15].XLocation = 20.0 + j*10.0;
      soldier[j+15].YLocation = 20.0;
      soldier[j+15].Speed = 10.0;
    }

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 1000;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    meanLabel = new Label();
    meanLabel.Text = "Mean value";
    meanLabel.Font = new Font(meanLabel.Font, FontStyle.Bold);
    meanLabel.Top = 50;
    meanLabel.Left = 20;
    meanLabel.Width = 100;

    sigmaLabel = new Label();
    sigmaLabel.Text = "Standard deviation";
    sigmaLabel.Font = new Font(sigmaLabel.Font, FontStyle.Bold);
    sigmaLabel.Top = 80;
    sigmaLabel.Left = 20;
    sigmaLabel.Width = 120;

    //  Create TextBox objects to display the inputs.
    meanTextBox = new TextBox();
    meanTextBox.Width = 60;
    meanTextBox.Text = "5.0";
    meanTextBox.AutoSize = true;
    meanTextBox.Top = meanLabel.Top;
    meanTextBox.Left = 140;

    sigmaTextBox = new TextBox();
    sigmaTextBox.Width = 60;
    sigmaTextBox.Text = "1.0";
    sigmaTextBox.AutoSize = true;
    sigmaTextBox.Top = sigmaLabel.Top;
    sigmaTextBox.Left = 150;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 40;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 120;
    startButton.Left = buttonLeft;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 160;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 201;
    drawingPanel.Height = 201;
    drawingPanel.Left = 250;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(meanLabel);
    this.Controls.Add(sigmaLabel);
    this.Controls.Add(meanTextBox);
    this.Controls.Add(sigmaTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 300;
    this.Text = "Soldier Game";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the display
    UpdateDisplay();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {

    //  stop the timer.
    gameTimer.Stop();

    //  Reset the position and speed of the soldiers
    for(int j=0; j<15; ++j) {
      soldier[j].XLocation = 15.0 + j*10.0;
      soldier[j].YLocation = 10.0;
      soldier[j].Speed = 10.0;

      soldier[j+15].XLocation = 20.0 + j*10.0;
      soldier[j+15].YLocation = 20.0;
      soldier[j+15].Speed = 10.0;
    }

    //  Update the display.
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Start the soldiers moving using a Timer object
    //  to slow down the action.
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
    Font font = new Font("Arial", 12);

    //  Draw the current location of the soldiers
    int x;
    int y;
    for(int j=0; j<30; ++j) { 
      x = (int)(soldier[j].XLocation);
      y = (int)(soldier[j].YLocation);
      g.DrawString("+", font, brush, x, y);
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 1.0 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Extract sample size number from textfield
    double mean = Convert.ToDouble(meanTextBox.Text);
    double sigma = Convert.ToDouble(sigmaTextBox.Text);

    //  Create a Random object to generate random
    //  numbers. The object is seeded with the time
    //  in milliseconds from Jan 1, 1970.
    Random random = new Random();

    //  Update the speed of each soldier based on a
    //  Gaussian distribution and then update the 
    //  position of each soldier based on the new speed.
    double x;
    double grp1;
    double grp2;
    double speed;
    double t;
    double newY;
    double dt = 1.0;
    for(int j=0; j<30; ++j) {
      //  Generate a random number between 0 and 1.
      x = random.NextDouble();
  
      //  Find the speed corresponding to the random
      //  number using the Gaussian distribution with a
      //  mean value of 0 and a standard deviation of 1
      t = Math.Sqrt( Math.Log(1.0/(x*x)) );
      grp1 = 2.515517 + 0.802853*t + 0.010328*t*t;
      grp2 = 1.0 + 1.432788*t + 0.189269*t*t + 
               0.001308*t*t*t;
      speed = -t + grp1/grp2;

      //  Shift the converted speed to the proper
      //  mean and standard deviation value.
      speed = mean + speed*sigma;

      //  Update the value of the speed field for
      //  each Soldier object.
      soldier[j].Speed = speed;

      //  Update the y-location of each soldier.
      //  If they reach the bottom of the panel, they stop.
      newY = soldier[j].YLocation + dt*soldier[j].Speed;
      if ( newY > drawingPanel.Height - 1 ) {
        newY = drawingPanel.Height - 1;
      }

      soldier[j].YLocation = newY;
    }

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new SoldierGame());
  }
}
