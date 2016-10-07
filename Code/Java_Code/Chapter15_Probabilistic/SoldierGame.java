import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class SoldierGame extends JFrame implements ActionListener
{
  private JTextField meanTextField;
  private JTextField sigmaTextField;

  private JLabel meanLabel;
  private JLabel sigmaLabel;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private Soldier soldier[];
  private SoldierUpdater soldierUpdater;
  private Timer soldierTimer;

  public SoldierGame() {
    //  Create an array of 30 soldiers and set their
    //  x- and y-locations.
    soldier = new Soldier[30];

    for(int j=0; j<15; ++j) {
      soldier[j] = new Soldier();
      soldier[j].setXLocation(15.0 + j*10.0);
      soldier[j].setYLocation(10.0);
      soldier[j].setSpeed(10.0);

      soldier[j+15] = new Soldier();
      soldier[j+15].setXLocation(20.0 + j*10.0);
      soldier[j+15].setYLocation(20.0);
      soldier[j+15].setSpeed(10.0);
    }

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The dt variable is
    //  the time delay in milliseconds.
    soldierUpdater = new SoldierUpdater();
    soldierTimer = new Timer(1000, soldierUpdater);

    //  Create JTextField objects to input the initial
    //  conditions.
    meanTextField = new JTextField("5",10);
    sigmaTextField = new JTextField("1.0",10);

    //  Create some JLabels
    meanLabel = new JLabel("Mean value");
    sigmaLabel = new JLabel("Standard deviation");

    //  Create a JButton that will start the boat moving
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will update the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        //  stop the timer.
        soldierTimer.stop();

        //  Reset the position and speed of the soldiers
        for(int j=0; j<15; ++j) {
          soldier[j].setXLocation(5.0 + j*10.0);
          soldier[j].setYLocation(10.0);
          soldier[j].setSpeed(10.0);

          soldier[j+15].setXLocation(10.0 + j*10.0);
          soldier[j+15].setYLocation(20.0);
          soldier[j+15].setSpeed(10.0);
        }

        //  Update the display.
        updateDisplay();
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
    gridBagLayout1.setConstraints(meanLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(meanTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(sigmaLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(sigmaTextField, gbc);

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

    westPanel.add(meanLabel);
    westPanel.add(meanTextField);
    westPanel.add(sigmaLabel);
    westPanel.add(sigmaTextField);
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
    setTitle("Soldier Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,300);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    //  Start the soldiers moving using a Timer object
    //  to slow down the action.
    soldierTimer.start();
  }

  //  This method redraws the GUI display.
  private void updateDisplay() {
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.drawRect(0, 0, width, height); 

    //  Draw the current location of the soldiers
    int x;
    int y;
    for(int j=0; j<30; ++j) { 
      x = (int)(soldier[j].getXLocation());
      y = (int)(soldier[j].getYLocation());
      g.drawString("+", x, y);
    }
  }

  public static void main(String args[]) {
    SoldierGame gui = new SoldierGame();
  }

  //  This ActionListener is called by the Timer
  class SoldierUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      //  Extract sample size number from textfield
      double mean = Double.parseDouble(meanTextField.getText());
      double sigma = Double.parseDouble(sigmaTextField.getText());

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
        x = random.nextDouble();
  
        //  Find the speed corresponding to the random
        //  number using the Gaussian distribution with a
        //  mean value of 0 and a standard deviation of 1
        t = Math.sqrt( Math.log(1.0/(x*x)) );
        grp1 = 2.515517 + 0.802853*t + 0.010328*t*t;
        grp2 = 1.0 + 1.432788*t + 0.189269*t*t + 
               0.001308*t*t*t;
        speed = -t + grp1/grp2;

        //  Shift the converted speed to the proper
        //  mean and standard deviation value.
        speed = mean + speed*sigma;

        //  Update the value of the speed field for
        //  each Soldier object.
        soldier[j].setSpeed(speed);

        //  Update the y-location of each soldier.
        //  If they reach the bottom of the panel, they stop.
        newY = soldier[j].getYLocation() + dt*soldier[j].getSpeed();
        if ( newY > drawingPanel.getHeight() - 1 ) {
          newY = drawingPanel.getHeight() - 1;
        }

        soldier[j].setYLocation(newY);
      }

      //  Update the display
      updateDisplay();
    }
  }
}