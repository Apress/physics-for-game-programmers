import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;

public class PiEstimator extends JFrame implements ActionListener
{
  private JTextField sampleSizeTextField;
  private JTextField piValueTextField;

  private JLabel sampleSizeLabel;
  private JLabel piValueLabel;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  public PiEstimator() {
    //  Create JTextField objects to input the initial
    //  conditions.
    sampleSizeTextField = new JTextField("10",10);

    piValueTextField = new JTextField("0.0",10);
    piValueTextField.setEditable(false);

    //  Create some JLabels
    sampleSizeLabel = new JLabel("Sample size");
    piValueLabel = new JLabel("Pi value");

    //  Create a JButton that will start the boat moving
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will clear the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        Graphics g = drawingPanel.getGraphics();
        int width = drawingPanel.getWidth() - 1;
        int height = drawingPanel.getHeight() - 1;

        g.clearRect(0, 0, width, height);
        g.drawRect(0, 0, width, height); 
        g.drawOval(0, 0, width, height); 
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(201, 201));

    //  Place components on a panel using a GridBagLayout
    JPanel westPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    westPanel.setLayout(gridBagLayout1);

    int col;
    int row;
    int numCol = 1;
    int numRow = 1;
    Insets insets = new Insets(5, 3, 5, 3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(sampleSizeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(sampleSizeTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(piValueLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(piValueTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(sampleSizeLabel);
    westPanel.add(sampleSizeTextField);
    westPanel.add(piValueLabel);
    westPanel.add(piValueTextField);
    westPanel.add(startButton);
    westPanel.add(resetButton);

    //  The drawing panel.
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout2.setConstraints(drawingPanel, gbc);

    eastPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(eastPanel, BorderLayout.EAST);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Pi Estimator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,300);
    setVisible(true);

    //  Draw a circle and square on the GUI
    drawingPanel.getGraphics().drawRect(0, 0, 200, 200); 
    drawingPanel.getGraphics().drawOval(0, 0, 200, 200); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    //  Extract sample size number from textfield
    int sampleSize = Integer.parseInt(sampleSizeTextField.getText());

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
      x = random.nextDouble();
      y = random.nextDouble();

      //  Display the sample point on the screen
      int xLoc = (int)(200.0*x);
      int yLoc = (int)(200.0*y);
      drawingPanel.getGraphics().drawString("+", xLoc, yLoc); 

      //  Determine if the point is inside the circle
      //  by computing the distance from the point to
      //  the center of the circle. If the distance is
      //  less than 1, the point is inside.
      distance = Math.sqrt((x-0.5)*(x-0.5) + (y-0.5)*(y-0.5));
      if ( distance <= 0.5 ) {
        ++numInCircle;
      }

      //  Update the value of pi
      piValue = 4.0*numInCircle/(j+1.0);
      piValueTextField.setText(""+(float)piValue);
    }
  }

  public static void main(String args[]) {
    PiEstimator gui = new PiEstimator();
  }

}