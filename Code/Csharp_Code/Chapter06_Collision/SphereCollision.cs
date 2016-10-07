using System;
using System.Windows.Forms;
using System.Drawing;

public class SphereCollision : Form
{
  private Label vx1Label;
  private Label mass1Label;
  private Label vx2Label;
  private Label mass2Label;
  private Label eLabel;

  private TextBox vx1TextBox;
  private TextBox mass1TextBox;
  private TextBox vx2TextBox;
  private TextBox mass2TextBox;
  private TextBox eTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  //  These fields store sphere data and the coefficient 
  //  of restitution.
  private double vx1;
  private double x1;
  private double mass1;
  private double vx2;
  private double x2;
  private double mass2;
  private double e;            //  coefficient of restitution
  private double sphereRadius; //  radius of spheres

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  public SphereCollision() {

    //  Initialize sphere location, velocity, and
    //  coefficient of restitution.
    vx1 = 10.0;
    x1 = 5.0;
    mass1 = 10.0;
    vx2 = -5.0;
    x2 = 15.0;
    mass2 = 5.0;
    e = 0.9;
    sphereRadius = 2.0;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 50;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    vx1Label = new Label();
    vx1Label.Text = "Sphere 1 velocity, m/s";
    vx1Label.Font = new Font(vx1Label.Font, FontStyle.Bold);
    vx1Label.Top = 30;
    vx1Label.Left = 10;
    vx1Label.Width = 130;

    mass1Label = new Label();
    mass1Label.Text = "Sphere 1 mass, kg";
    mass1Label.Font = new Font(mass1Label.Font, FontStyle.Bold);
    mass1Label.Top = 60;
    mass1Label.Left = 10;
    mass1Label.Width = 130;

    vx2Label = new Label();
    vx2Label.Text = "Sphere 2 velocity, m/s";
    vx2Label.Font = new Font(vx2Label.Font, FontStyle.Bold);
    vx2Label.Top = 90;
    vx2Label.Left = 10;
    vx2Label.Width = 130;

    mass2Label = new Label();
    mass2Label.Text = "Sphere 2 mass, kg";
    mass2Label.Font = new Font(mass2Label.Font, FontStyle.Bold);
    mass2Label.Top = 120;
    mass2Label.Left = 10;
    mass2Label.Width = 130;

    eLabel = new Label();
    eLabel.Text = "Coefficient of restitution";
    eLabel.Font = new Font(eLabel.Font, FontStyle.Bold);
    eLabel.Top = 150;
    eLabel.Left = 10;
    eLabel.Width = 130;

    //  Create TextBox objects to display the outcome.
    vx1TextBox = new TextBox();
    vx1TextBox.Width = 50;
    vx1TextBox.Text = "10.0";
    vx1TextBox.AutoSize = true;
    vx1TextBox.Top = vx1Label.Top;
    vx1TextBox.Left = 150;

    mass1TextBox = new TextBox();
    mass1TextBox.Width = 50;
    mass1TextBox.Text = "10.0";
    mass1TextBox.AutoSize = true;
    mass1TextBox.Top = mass1Label.Top;
    mass1TextBox.Left = 150;

    vx2TextBox = new TextBox();
    vx2TextBox.Width = 50;
    vx2TextBox.Text = "-5.0";
    vx2TextBox.AutoSize = true;
    vx2TextBox.Top = vx2Label.Top;
    vx2TextBox.Left = 150;

    mass2TextBox = new TextBox();
    mass2TextBox.Width = 50;
    mass2TextBox.Text = "5.0";
    mass2TextBox.AutoSize = true;
    mass2TextBox.Top = mass2Label.Top;
    mass2TextBox.Left = 150;

    eTextBox = new TextBox();
    eTextBox.Width = 50;
    eTextBox.Text = "0.9";
    eTextBox.AutoSize = true;
    eTextBox.Top = eLabel.Top;
    eTextBox.Left = 150;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 240;

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
    resetButton.Top = 110;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 201;
    drawingPanel.Left = 70;
    drawingPanel.Top = 220;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(vx1Label);
    this.Controls.Add(vx2Label);
    this.Controls.Add(mass1Label);
    this.Controls.Add(mass2Label);
    this.Controls.Add(eLabel);
    this.Controls.Add(vx1TextBox);
    this.Controls.Add(mass1TextBox);
    this.Controls.Add(vx2TextBox);
    this.Controls.Add(mass2TextBox);
    this.Controls.Add(eTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 500;
    this.Text = "Sphere Collision";

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
    vx1 = Convert.ToDouble(vx1TextBox.Text);
    mass1 = Convert.ToDouble(mass1TextBox.Text);
    vx2 = Convert.ToDouble(vx2TextBox.Text);
    mass2 = Convert.ToDouble(mass2TextBox.Text);
    e = Convert.ToDouble(eTextBox.Text);

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs evt) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the sphere location, velocity, and
    //  coefficient of restitution.
    vx1 = 10.0;
    x1 = 5.0;
    mass1 = 10.0;
    vx2 = -5.0;
    x2 = 15.0;
    mass2 = 5.0;
    e = 0.9;

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

    //  Update the position of the spheres on the screen.
    Pen blackPen = new Pen(Color.Black, 2);
    int xPosition = (int)(10.0*(x1 - sphereRadius));
    g.DrawEllipse(blackPen, xPosition, 80, 2*(int)(10.0*sphereRadius), 
               2*(int)(10.0*sphereRadius));

    xPosition = (int)(10.0*(x2 - sphereRadius));
    g.DrawEllipse(blackPen, xPosition, 80, 2*(int)(10.0*sphereRadius), 
               2*(int)(10.0*sphereRadius));

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.05 seconds.
  public void ActionPerformed(object source, EventArgs evt) {
    //  Determine if a collision occurs and if it
    //  does, change the velocties of the spheres.
    //  A collision occurs if the distance between the
    //  centers of the spheres is less than twice their
    //  radius
    double distance = Math.Abs(x1 - x2);
    double tmp = 1.0/(mass1 + mass2);

    if ( distance <= 2.0*sphereRadius ) {
      double newVx1 = (mass1 - e*mass2)*vx1*tmp + 
                      (1.0 + e)*mass2*vx2*tmp;
      double newVx2 = (1.0 + e)*mass1*vx1*tmp +
                      (mass2 - e*mass1)*vx2*tmp;
      vx1 = newVx1;
      vx2 = newVx2;
    }

    //  Compute the new location of the spheres. 
    double timeIncrement = 0.07;
    x1 = x1 + timeIncrement*vx1;
    x2 = x2 + timeIncrement*vx2;

    //  Update the display
    UpdateDisplay();

    //  If either of the spheres hits the outer edge
    //  of the display, stop the simulation.
    int width = drawingPanel.Width - 1;

    if ( 10.0*(x1 + sphereRadius) > width || 
         10.0*(x1 - sphereRadius) < 0.0 ||
         10.0*(x2 + sphereRadius) > width || 
         10.0*(x2 - sphereRadius) < 0.0 ) {
      gameTimer.Stop();
    }
  }

  static void Main() {
    Application.Run(new SphereCollision());
  }
}
