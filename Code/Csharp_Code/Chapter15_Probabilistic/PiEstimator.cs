using System;
using System.Windows.Forms;
using System.Drawing;

public class PiEstimator : Form
{
  private Label sampleSizeLabel;
  private Label piValueLabel;

  private TextBox sampleSizeTextBox;
  private TextBox piValueTextBox;

  private Button startButton;
  private Button resetButton;

  private Panel drawingPanel;

  public PiEstimator() {

    //  Create some Labels
    sampleSizeLabel = new Label();
    sampleSizeLabel.Text = "Sample size";
    sampleSizeLabel.Font = new Font(sampleSizeLabel.Font, FontStyle.Bold);
    sampleSizeLabel.Top = 50;
    sampleSizeLabel.Left = 20;
    sampleSizeLabel.Width = 100;

    piValueLabel = new Label();
    piValueLabel.Text = "Pi value";
    piValueLabel.Font = new Font(piValueLabel.Font, FontStyle.Bold);
    piValueLabel.Top = 80;
    piValueLabel.Left = 20;
    piValueLabel.Width = 100;

    //  Create TextBox objects to display the inputs.
    sampleSizeTextBox = new TextBox();
    sampleSizeTextBox.Width = 60;
    sampleSizeTextBox.Text = "100";
    sampleSizeTextBox.AutoSize = true;
    sampleSizeTextBox.Top = sampleSizeLabel.Top;
    sampleSizeTextBox.Left = 140;

    piValueTextBox = new TextBox();
    piValueTextBox.Width = 60;
    piValueTextBox.Text = "";
    piValueTextBox.AutoSize = true;
    piValueTextBox.ReadOnly = true;
    piValueTextBox.Top = piValueLabel.Top;
    piValueTextBox.Left = 140;

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
    this.Controls.Add(sampleSizeLabel);
    this.Controls.Add(piValueLabel);
    this.Controls.Add(sampleSizeTextBox);
    this.Controls.Add(piValueTextBox);
    this.Controls.Add(startButton);
    this.Controls.Add(resetButton);
    this.Controls.Add(drawingPanel);

    // Set the size and title of the form
    this.Width = 500;
    this.Height = 300;
    this.Text = "Pi Estimator";

    //  Center the form on the screen and make
    //  it visible.
    this.StartPosition = FormStartPosition.CenterScreen;
    this.Visible = true;

    //  Draw a circle inside the panel.
    Graphics g = drawingPanel.CreateGraphics();
    g.Clear(Color.White);
    Pen blackPen = new Pen(Color.Black, 1);
    g.DrawEllipse(blackPen, 0, 0, 200, 200); 
    g.Dispose();

  }

  //  Event handling method for the "Reset" button
  public void ResetButtonClicked(object source, EventArgs e) {
    //  Clear the panel.
    Graphics g = drawingPanel.CreateGraphics();
    g.Clear(Color.White);
    Pen blackPen = new Pen(Color.Black, 1);
    g.DrawEllipse(blackPen, 0, 0, 200, 200); 
    g.Dispose();
  }

  //  Event handling method for the "Start" button
  public void StartButtonClicked(object source, EventArgs e) {

    //  Access the Graphics object of the drawing panel.
    Graphics g = drawingPanel.CreateGraphics();
    g.Clear(Color.White);
    SolidBrush brush = new SolidBrush(Color.Black);
    Font font = new Font("Arial", 12);
    Pen blackPen = new Pen(Color.Black, 1);
    g.DrawEllipse(blackPen, 0, 0, 200, 200); 

    //  Get some initial quantities from the TextBox.
    int sampleSize = Convert.ToInt32(sampleSizeTextBox.Text);

    //  Create a Random object to generate random
    //  numbers. The object is seeded with the time
    //  in milliseconds from Jan 1, 1970.
    Random random = new Random();

    //  Create the sample points. Generate
    //  two random numbers representing a data point.
    //  See if the data point is inside the circle area
    //  or not.
    double x;
    double y;
    double distance;
    int numInCircle = 0;
    double piValue = 0.0;
    for(int j=0; j<sampleSize; ++j) {
      //  Generate an x-y point.
      x = random.NextDouble();
      y = random.NextDouble();

      //  Display the sample point on the screen
      int xLoc = (int)(200.0*x);
      int yLoc = (int)(200.0*y);
      g.DrawString("+", font, brush, xLoc, yLoc); 

      //  Determine if the point is inside the circle
      //  by computing the distance from the point to
      //  the center of the circle. If the distance is
      //  less than 1, the point is inside.
      distance = Math.Sqrt((x-0.5)*(x-0.5) + (y-0.5)*(y-0.5));
      if ( distance <= 0.5 ) {
        ++numInCircle;
      }

      //  Update the value of pi
      piValue = 4.0*numInCircle/(j+1.0);
      piValueTextBox.Text = ""+(float)piValue;
    }

    //  Clean up the Graphics object.
    g.Dispose();
  }

  static void Main() {
    Application.Run(new PiEstimator());
  }
}
