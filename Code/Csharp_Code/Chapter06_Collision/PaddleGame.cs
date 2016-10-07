using System;
using System.Windows.Forms;
using System.Drawing;

public class PaddleGame : Form
{
  private Label vxLabel;
  private Label vzLabel;

  private TextBox vxTextBox;
  private TextBox vzTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  These fields store ball data and the coefficient 
  //  of restitution.
  private double ballVx;
  private double ballVz;
  private double ballX;
  private double ballZ;
  private double paddleZ;
  private int paddleHeight;
  private double ballRadius; //  radius of ball

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  public PaddleGame() {

    //  Initialize ball location, velocity, and
    //  paddle location.
    ballVx = 100.0;
    ballVz = -71.0;
    ballX = 100.0;
    ballZ = 100.0;
    paddleZ = 100;
    paddleHeight = 40;
    ballRadius = 5.0;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    vxLabel = new Label();
    vxLabel.Text = "x-velocity, m/s";
    vxLabel.Font = new Font(vxLabel.Font, FontStyle.Bold);
    vxLabel.Top = 30;
    vxLabel.Left = 10;
    vxLabel.Width = 100;

    vzLabel = new Label();
    vzLabel.Text = "z-velocity, m/s";
    vzLabel.Font = new Font(vzLabel.Font, FontStyle.Bold);
    vzLabel.Top = 60;
    vzLabel.Left = 10;
    vzLabel.Width = 100;

    //  Create TextBox objects to display the outcome.
    vxTextBox = new TextBox();
    vxTextBox.Width = 60;
    vxTextBox.Text = "100.0";
    vxTextBox.AutoSize = true;
    vxTextBox.Top = vxLabel.Top;
    vxTextBox.Left = 110;

    vzTextBox = new TextBox();
    vzTextBox.Width = 60;
    vzTextBox.Text = "-80.0";
    vzTextBox.AutoSize = true;
    vzTextBox.Top = vzLabel.Top;
    vzTextBox.Left = 110;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;

    startButton = new Button();
    startButton.Text = "Start";
    startButton.Height = buttonHeight;
    startButton.Width = buttonWidth;
    startButton.Top = 30;
    startButton.Left = 200;
    startButton.Click += new EventHandler(StartButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 30;
    resetButton.Left = 260;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 201;
    drawingPanel.Left = 130;
    drawingPanel.Top = 110;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;
    drawingPanel.MouseDown += new MouseEventHandler(MovePaddle);

    //  Add the GUI components to the Form
    this.Controls.Add(vxLabel);
    this.Controls.Add(vzLabel);
    this.Controls.Add(vxTextBox);
    this.Controls.Add(vzTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 350;
    this.Text = "PaddleGame";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs evt) {

    //  Extract initial data from the textfields.
    ballVx = Convert.ToDouble(vxTextBox.Text);
    ballVz = Convert.ToDouble(vzTextBox.Text);

    //  Update the display
    UpdateDisplay();

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs evt) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the ball location, velocity, and
    //  paddle location.
    ballVx = 100.0;
    ballVz = -80.0;
    ballX = 100.0;
    ballZ = 100.0;
    paddleZ = 100;
  
    //  Update the display.
    UpdateDisplay();
  }

  //  This event handling method is called when the
  //  mouse is dragged over the panel. 
  public void MovePaddle(object source, MouseEventArgs evt) {

    //  Move the paddle to where the mouse was clicked.
    int height = drawingPanel.Height - 1;
    paddleZ = height - evt.Y;

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

    //  Update the position of the spheres on the screen.
    SolidBrush brush = new SolidBrush(Color.Black);

    //  Update the position of the ball on the screen.
    int xPosition = (int)(ballX - ballRadius);
    int zPosition = (int)(height - ballRadius - ballZ);
    g.FillEllipse(brush, xPosition, zPosition, 2*(int)(ballRadius), 
                  2*(int)(ballRadius));

    //  Update the position of the paddle on the screen.
    zPosition = (int)(height - paddleZ);
    g.FillRectangle(brush, 10, zPosition - paddleHeight/2, 
                    10, paddleHeight);

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs evt) {
    //  Get dimensions of drawing area.
    int width = drawingPanel.Width - 1;
    int height = drawingPanel.Height - 1;

    //  Determine if ball collides with right wall.
    //  If it does, change the x-velocity of the ball.
    if ( ballVx > 0.0 && ballX + ballRadius >= width ) {
      ballVx = -ballVx;
    }

    //  Determine if ball collides with the top wall.
    //  If it does, change the z-velocity of the ball.
    if ( ballVz > 0.0 && ballZ + ballRadius >= height ) {
      ballVz = -ballVz;
    }

    //  Determine if ball collides with the bottom wall.
    //  If it does, change the z-velocity of the ball.
    if ( ballVz < 0.0 && ballZ - ballRadius <= 0.0 ) {
      ballVz = -ballVz;
    }

    //  Determine if ball collides with paddle.
    //  If it does, change the x-velocity of the ball.
    if ( ballVx < 0.0 && ballX - ballRadius <= 20.0 ) {
      if ( ballZ - ballRadius >= paddleZ - paddleHeight/2 &&
           ballZ + ballRadius <= paddleZ + paddleHeight/2 ) {
        ballVx = -ballVx;
      }
    }

    //  If ball travels off the left edge of the game
    //  area, stop the simulation.
    if ( ballX <= 0.0 ) {
Console.WriteLine("simulation stopped");
      gameTimer.Stop();
    }

    //  Compute the new location of the ball. 
    double timeIncrement = 0.07;
    ballX = ballX + timeIncrement*ballVx;
    ballZ = ballZ + timeIncrement*ballVz;

    //  Update the display
    UpdateDisplay();
  }

  static void Main() {
    Application.Run(new PaddleGame());
  }
}
