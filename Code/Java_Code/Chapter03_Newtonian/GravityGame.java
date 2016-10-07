import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class GravityGame extends JFrame implements ActionListener
{
  private JTextField resultsTextField;
  private JTextField velocityTextField;

  private JComboBox planetComboBox;

  private JLabel planetLabel;
  private JLabel velocityLabel;
  private JLabel resultsLabel;

  private JButton startButton;
  private JButton dropButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private double boxLocation;   //  horizontal location of box
  private double boxVelocity;
  private int boxWidth;         // width of box in pixels.
  private double ballAltitude;  // vertical location of ball
  private double ballLocation;  //  horizontal location of ball
  private double initialAltitude;  // initial ball altitude
  private double g;         //  gravitational acceleration
  private double time;      // time since box begins to move.
  private double dropTime;  // time since ball was dropped
  private boolean dropped;  // true if the ball has been dropped.

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

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
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Create JTextField objects to display the outcome.
    velocityTextField = new JTextField("25.0",8);

    resultsTextField = new JTextField("",10);
    resultsTextField.setEditable(false);

    //  Create some JLabels
    planetLabel = new JLabel("Planet");
    velocityLabel = new JLabel("Box velocity, m/s");
    resultsLabel = new JLabel("Results");

    //  Create a JComboBox to select a planet on which
    //  the ball will be dropped.
    planetComboBox = new JComboBox();
    planetComboBox.addItem("Earth");
    planetComboBox.addItem("Moon");
    planetComboBox.addItem("Jupiter");

    //  Create a JButton that will start the box moving
    startButton = new JButton("Start");
    startButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    startButton.setPreferredSize(new Dimension(60,35));
    startButton.addActionListener(this);

    //  Create a JButton that will drop the ball.
    dropButton = new JButton("Drop");
    dropButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    dropButton.setPreferredSize(new Dimension(60,35));
    dropButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        dropped = true;
      }  
    });

    //  Create a JButton that will update the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        //  stop the timer.
        gameTimer.stop();

        //  Reset the box and ball location and time;
        boxLocation = 0.0;
        ballAltitude = initialAltitude;
        time = 0.0;
        dropTime = 0.0;
        dropped = false;

        //  Blank out the results textfield.
        resultsTextField.setText("");

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(251, 151));

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
    gridBagLayout1.setConstraints(planetLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(planetComboBox, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(velocityTextField, gbc);

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
    gridBagLayout1.setConstraints(dropButton, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resultsLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resultsTextField, gbc);

    westPanel.add(planetLabel);
    westPanel.add(velocityLabel);
    westPanel.add(velocityTextField);
    westPanel.add(planetComboBox);
    westPanel.add(startButton);
    westPanel.add(dropButton);
    westPanel.add(resetButton);
    westPanel.add(resultsLabel);
    westPanel.add(resultsTextField);

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
    setTitle("Gravity Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,550,350);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    
    //  Get the box velocity from the textfield
    boxVelocity = Double.parseDouble(velocityTextField.getText());

    //  Determine which planet is selected and set
    //  the gravitational acceleration accordingly.
    String planet = (String)planetComboBox.getSelectedItem();

    if ( planet.equals("Earth") ) {
      g = 9.81;
    }
    else if ( planet.equals("Moon") ) {
      g = 1.624;
    }
    else { 
      g = 24.8;  //  Jupiter
    }

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.start();
  }

  //  This method redraws the GUI display.
  private void updateDisplay() {
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.drawRect(0, 0, width, height); 
    g.drawLine(0, 130, width, 130);

    //  Update the position of the box and
    //  ball on the screen.
    g.fillRect((int)boxLocation, 120, boxWidth, 10);

    int zPosition = (int)(initialAltitude - ballAltitude);
    g.fillOval((int)ballLocation, zPosition, 10, 10);

  }

  public static void main(String args[]) {
    GravityGame gui = new GravityGame();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

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
      updateDisplay();

      //  If the ball hits the ground, stop the simulation
      //  and determine if it landed in the box.
      if ( ballAltitude <= 0.0 ) {
        gameTimer.stop();

        if ( ballLocation >= boxLocation &&
             ballLocation <= boxLocation + boxWidth - 10 ) {
          resultsTextField.setText("You Win!");
        }
        else {
          resultsTextField.setText("Try again");
        }
      }
    }
  }
}