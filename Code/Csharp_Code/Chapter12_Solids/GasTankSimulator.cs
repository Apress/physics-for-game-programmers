using System;
using System.Windows.Forms;
using System.Drawing;

public class GasTankSimulator : Form
{
  private Label thicknessLabel;
  private Label flameTempLabel;
  private Label timeLabel;
  private Label outerTempLabel;
  private Label innerTempLabel;
  private Label materialLabel;

  private TextBox thicknessTextBox;
  private TextBox flameTempTextBox;
  private TextBox timeTextBox;
  private TextBox outerTempTextBox;
  private TextBox innerTempTextBox;

  private Button fireButton;
  private Button resetButton;

  private ComboBox materialComboBox;

  private Panel drawingPanel;

  //  These fields are for the images used in the game.
  private Image soldierIcon;
  private int soldierIconWidth;
  private int soldierIconHeight;
  private Image flameIcon;
  private int flameIconWidth;
  private int flameIconHeight;
  private Image explosionIcon;
  private int explosionIconWidth;
  private int explosionIconHeight;

  //  The Timer is used to control the execution speed
  //  of the game.
  private Timer gameTimer;

  private double time;
  private double innerWallTemp;

  //  Declare a GasTank object
  private GasTank tank;

  public GasTankSimulator() {

    time = 0.0;
    innerWallTemp = 300.0;

    //  Set up some images and determine their dimensions
    soldierIcon = Image.FromFile("soldierCartoon.jpg");
    soldierIconWidth = soldierIcon.Width;
    soldierIconHeight = soldierIcon.Height;
    flameIcon = Image.FromFile("flameCartoon.jpg");
    flameIconWidth = flameIcon.Width;
    flameIconHeight = flameIcon.Height;
    explosionIcon = Image.FromFile("explosionCartoon.jpg");
    explosionIconWidth = explosionIcon.Width;
    explosionIconHeight = explosionIcon.Height;

    //  Create a Timer object that will be used
    //  to slow the action down.
    gameTimer = new Timer();
    gameTimer.Interval = 100;  //  delay in milliseconds.
    gameTimer.Tick += new EventHandler(ActionPerformed);

    //  Create some Labels
    flameTempLabel = new Label();
    flameTempLabel.Text = "Flame temperature, K";
    flameTempLabel.Font = new Font(flameTempLabel.Font, FontStyle.Bold);
    flameTempLabel.Top = 270;
    flameTempLabel.Left = 20;
    flameTempLabel.Width = 140;

    thicknessLabel = new Label();
    thicknessLabel.Text = "Tank thickness, m";
    thicknessLabel.Font = new Font(thicknessLabel.Font, FontStyle.Bold);
    thicknessLabel.Top = 300;
    thicknessLabel.Left = 20;
    thicknessLabel.Width = 120;

    materialLabel = new Label();
    materialLabel.Text = "Tank material";
    materialLabel.Font = new Font(materialLabel.Font, FontStyle.Bold);
    materialLabel.Top = 330;
    materialLabel.Left = 20;
    materialLabel.Width = 120;

    timeLabel = new Label();
    timeLabel.Text = "Elapsed time, s";
    timeLabel.Font = new Font(timeLabel.Font, FontStyle.Bold);
    timeLabel.Top = 290;
    timeLabel.Left = 270;
    timeLabel.Width = 120;

    outerTempLabel = new Label();
    outerTempLabel.Text = "Outer wall temp, K";
    outerTempLabel.Font = new Font(outerTempLabel.Font, FontStyle.Bold);
    outerTempLabel.Top = 320;
    outerTempLabel.Left = 270;
    outerTempLabel.Width = 120;

    innerTempLabel = new Label();
    innerTempLabel.Text = "Inner wall temp, K";
    innerTempLabel.Font = new Font(innerTempLabel.Font, FontStyle.Bold);
    innerTempLabel.Top = 350;
    innerTempLabel.Left = 270;
    innerTempLabel.Width = 120;

    //  Create TextBox objects to display the inputs.
    thicknessTextBox = new TextBox();
    thicknessTextBox.Width = 60;
    thicknessTextBox.Text = "0.02";
    thicknessTextBox.AutoSize = true;
    thicknessTextBox.Top = thicknessLabel.Top;
    thicknessTextBox.Left = 150;

    flameTempTextBox = new TextBox();
    flameTempTextBox.Width = 60;
    flameTempTextBox.Text = "1000.0";
    flameTempTextBox.AutoSize = true;
    flameTempTextBox.Top = flameTempLabel.Top;
    flameTempTextBox.Left = 180;

    timeTextBox = new TextBox();
    timeTextBox.Width = 60;
    timeTextBox.Text = "0.0";
    timeTextBox.AutoSize = true;
    timeTextBox.ReadOnly = true;
    timeTextBox.Top = timeLabel.Top;
    timeTextBox.Left = 400;

    outerTempTextBox = new TextBox();
    outerTempTextBox.Width = 60;
    outerTempTextBox.Text = "0.0";
    outerTempTextBox.AutoSize = true;
    outerTempTextBox.ReadOnly = true;
    outerTempTextBox.Top = outerTempLabel.Top;
    outerTempTextBox.Left = 400;

    innerTempTextBox = new TextBox();
    innerTempTextBox.Width = 60;
    innerTempTextBox.Text = "0.0";
    innerTempTextBox.AutoSize = true;
    innerTempTextBox.ReadOnly = true;
    innerTempTextBox.Top = innerTempLabel.Top;
    innerTempTextBox.Left = 400;

    //  Create a ComboBox to select the material.
    materialComboBox = new ComboBox();
    materialComboBox.Items.Add("Aluminum");
    materialComboBox.Items.Add("Concrete");
    materialComboBox.SelectedIndex = 0;
    materialComboBox.Left = 150;
    materialComboBox.Width = 80;
    materialComboBox.Top = materialLabel.Top;

    //  Create Button objects 
    int buttonHeight = 30;
    int buttonWidth = 50;
    int buttonLeft = 20;

    fireButton = new Button();
    fireButton.Text = "Fire";
    fireButton.Height = buttonHeight;
    fireButton.Width = buttonWidth;
    fireButton.Top = 370;
    fireButton.Left = buttonLeft;
    fireButton.Click += new EventHandler(FireButtonClicked);

    resetButton = new Button();
    resetButton.Text = "Reset";
    resetButton.Height = buttonHeight;
    resetButton.Width = buttonWidth;
    resetButton.Top = 410;
    resetButton.Left = buttonLeft;
    resetButton.Click += new EventHandler(ResetButtonClicked);

    //  Create a drawing panel.
    drawingPanel = new Panel();
    drawingPanel.Width = 301;
    drawingPanel.Height = 151;
    drawingPanel.Left = 50;
    drawingPanel.Top = 20;
    drawingPanel.BorderStyle = BorderStyle.FixedSingle;

    //  Add the GUI components to the Form
    this.Controls.Add(thicknessLabel);
    this.Controls.Add(flameTempLabel);
    this.Controls.Add(timeLabel);
    this.Controls.Add(outerTempLabel);
    this.Controls.Add(innerTempLabel);
    this.Controls.Add(materialLabel);
    this.Controls.Add(thicknessTextBox);
    this.Controls.Add(flameTempTextBox);
    this.Controls.Add(timeTextBox);
    this.Controls.Add(outerTempTextBox);
    this.Controls.Add(innerTempTextBox); 
    this.Controls.Add(fireButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(materialComboBox);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 550;
    this.Height = 500;
    this.Text = "Gas Tank Simulator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Update the GUI display
    UpdateDisplay();
  }

  //  Event handling method for the "Fire" button
  public void FireButtonClicked(object source, EventArgs e) {

    //  Get some initial quantities from the textfields.
    double flameTemp = Convert.ToDouble(flameTempTextBox.Text);
    double thickness = Convert.ToDouble(thicknessTextBox.Text);

    //  Set the initial temperature.
    double initialT = 300.0;

    //  Set the diffusivity according to the material selected.
    double diffusivity;
    String material = (string)materialComboBox.SelectedItem;

    if ( String.Equals(material,"Aluminum") ) {
      diffusivity = 9.975e-5;
    }
    else { 
      diffusivity = 6.6e-7;   //  concrete
    }

    //  Create a GasTank object.
    tank = new GasTank(thickness, diffusivity, initialT, flameTemp);

    //  Set the display for the outer wall temperature textfield
    outerTempTextBox.Text = ""+flameTemp;

    //  Update the display
    UpdateDisplay();

    //  Fire the flamethrower using a Timer object
    gameTimer.Start();
  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  stop the timer.
    gameTimer.Stop();

    //  Reset the time and inner wall temperature values.
    time = 0.0;
    innerWallTemp = 300.0;

    //  Reset the textfields
    timeTextBox.Text = "0.0";
    outerTempTextBox.Text = "300.0";
    innerTempTextBox.Text = "300.0";

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

    //  Draw the ground
    g.DrawLine(blackPen, 0, height-10, width, height-10);

    //  Draw the soldier.
    int soldierZ = height - 10 - soldierIconHeight;
    g.DrawImage(soldierIcon, 20, soldierZ, 
                soldierIconWidth, soldierIconHeight);

    //  If the flamethrower is on, draw the flame
    if ( time > 0.0 ) {
      g.DrawImage(flameIcon, 80, height - 77, 
                flameIconWidth, flameIconHeight);
    }

    //  Draw the gas tank.
    g.DrawEllipse(blackPen, 150, height-60, 50, 50);

    //  If the inner wall of the tank has reached the ignition
    //  temperature of gasoline, stop the simulation and draw
    //  the explosion icon.
    if ( innerWallTemp > 550.0 ) {
      g.DrawImage(explosionIcon, 100, height - 140, 
                explosionIconWidth, explosionIconHeight);      
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  //  This method is called by the Timer every 0.1 seconds.
  public void ActionPerformed(object source, EventArgs e) {

    //  Compute the new inner wall temperature. 
    double timeIncrement = 0.1;
    time = time + timeIncrement;
    innerWallTemp = tank.GetTemperature(tank.Thickness,time);

    //  Update the display
    UpdateDisplay();

    //  Update the output data textfields
    timeTextBox.Text = ""+(float)time;
    innerTempTextBox.Text = ""+(int)innerWallTemp;

    //  If the inner wall temperature exceeds the ignition 
    //  temperature of gasoline, stop the simulation.
    if ( innerWallTemp > 550.0 ) {
      gameTimer.Stop();      
    }
  }

  static void Main() {
    Application.Run(new GasTankSimulator());
  }
}
