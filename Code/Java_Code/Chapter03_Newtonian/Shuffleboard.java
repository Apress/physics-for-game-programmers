import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class Shuffleboard extends JFrame implements ActionListener
{
  private JTextField muTextField;
  private JTextField massTextField;
  private JTextField velocityTextField;

  private JLabel muLabel;
  private JLabel massLabel;
  private JLabel velocityLabel;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private double mu;
  private double mass;
  private double initialVelocity;
  private double xLocation;
  private double time;

  private static final double G = 9.81;

  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public Shuffleboard() {

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Create JTextField objects to input the initial
    //  conditions.
    muTextField = new JTextField("0.5",10);
    massTextField = new JTextField("1.0",10);
    velocityTextField = new JTextField("4.5",10);

    //  Create some JLabels
    muLabel = new JLabel("Friction coefficient");
    massLabel = new JLabel("Disk mass, kg");
    velocityLabel = new JLabel("Initial velocity, m/s");

    //  Create a JButton that will start the disk moving
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
        gameTimer.stop();

        //  Reset the disk location
        xLocation = 0.0;

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(301, 151));

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
    gridBagLayout1.setConstraints(muLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(muTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(massLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(massTextField, gbc);

    row = 3;
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

    westPanel.add(muLabel);
    westPanel.add(muTextField);
    westPanel.add(massLabel);
    westPanel.add(massTextField);
    westPanel.add(velocityLabel);
    westPanel.add(velocityTextField);

    //  Place components on a panel using a GridBagLayout
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(startButton, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(resetButton, gbc);

    eastPanel.add(startButton);
    eastPanel.add(resetButton);

    //  The drawing panel.
    JPanel southPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    southPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout2.setConstraints(drawingPanel, gbc);

    southPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(eastPanel, BorderLayout.EAST);
    getContentPane().add(southPanel, BorderLayout.SOUTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Shuffleboard Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,450,350);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Extract initial data from the textfields.
    mu = Double.parseDouble(muTextField.getText());
    mass = Double.parseDouble(massTextField.getText());
    initialVelocity = Double.parseDouble(velocityTextField.getText());

    //  Set the time and the initial x-location of the disk
    time = 0.0;
    xLocation = 0.0;

    //  Start the disk sliding using a Timer object
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
    g.drawLine(0, 125, width, 125);
    g.drawLine(200, 125, 200, height);
    g.drawLine(225, 125, 225, height);
    g.drawLine(250, 125, 250, height);
    g.drawLine(275, 125, 275, height);
    g.drawString("10", 205, 143);
    g.drawString("20", 230, 143);
    g.drawString("50", 255, 143);
    g.drawString("0", 280, 143);

    //  Update the position of the disk on the screen.
    //  The drawing area is 300 pixels wide corresponding
    //  to a game length of 3 meters.
    int x = (int)(xLocation*100);
    g.fillRect(x, 115, 10, 10);

  }

  public static void main(String args[]) {
    Shuffleboard gui = new Shuffleboard();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the disk. 
      time += 0.05; 

      //  Compute the current velocity of the disk.
      double velocity = initialVelocity - mu*G*time;

      //  Update the position of the disk.
      xLocation = initialVelocity*time - 0.5*mu*G*time*time;

      //  Update the display
      updateDisplay();

      //  If the disk stops moving or if it reaches
      //  the end of the board, stop the simulation
      if ( velocity <= 0.0 || xLocation > 2.9) {
        gameTimer.stop();
      }

    }
  }
}