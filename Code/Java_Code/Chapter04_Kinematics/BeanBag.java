import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;
import javax.swing.Timer;

public class BeanBag extends JFrame implements ActionListener
{
  private JTextField vxTextField;
  private JTextField vzTextField;

  private JLabel vxLabel;
  private JLabel vzLabel;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private double z;     //  altitude of beanbag
  private double z0;    //  initial altitude of beanbag
  private double vz0;   //  initial vertical velocity
  private double x;     //  horizontal location
  private double x0;    //  initial horizontal location
  private double vx0;   //  initial horizontal velocity
  private double time;      

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  //  These fields are for the images used in the game.
  private ImageIcon playerIcon;
  private ImageIcon backgroundIcon;

  public BeanBag() {

    //  Initialize the beanbag parameters.
    z = 1.7;
    z0 = 1.7;
    vz0 = 0.0;
    x = 0.5;
    x0 = 0.5;
    vx0 = 0.0;
    time = 0.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up images
    playerIcon = new ImageIcon("BeanbagTosser.jpg");
    backgroundIcon = new ImageIcon("Background.jpg");

    //  Create JTextField objects to display the outcome.
    vxTextField = new JTextField("2.5",8);
    vzTextField = new JTextField("2.0",8);

    //  Create some JLabels
    vxLabel = new JLabel("Initial horizontal velocity, m/s");
    vzLabel = new JLabel("Initial vertical velocity, m/s");

    //  Create a JButton that will start the box moving
    fireButton = new JButton("Fire");
    fireButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    fireButton.setPreferredSize(new Dimension(60,35));
    fireButton.addActionListener(this);

    //  Create a JButton that will update the drawing area.
    resetButton = new JButton("Reset");
    resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
    resetButton.setPreferredSize(new Dimension(60,35));
    resetButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        //  stop the timer.
        gameTimer.stop();

        //  Reset the box and ball location and time;
        z = 1.7;
        z0 = 1.7;
        vz0 = 0.0;
        x = 0.5;
        x0 = 0.5;
        vx0 = 0.0;
        time = 0.0;

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
    gridBagLayout1.setConstraints(vxLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vxTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vzLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vzTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(vxLabel);
    westPanel.add(vxTextField);
    westPanel.add(vzLabel);
    westPanel.add(vzTextField);
    westPanel.add(fireButton);
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
    setTitle("Beanbag Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,600,300);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    
    //  Get the initial velocities from the textfield
    vx0 = Double.parseDouble(vxTextField.getText());
    vz0 = Double.parseDouble(vzTextField.getText());

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

    //  Draw the beanbag tosser and background.
    g.drawImage(backgroundIcon.getImage(), 1, 1, 
                  width-2, 123, drawingPanel);
    int zLocation = 125 - 67;
    g.drawImage(playerIcon.getImage(), 7, zLocation, 
                  51, 67, drawingPanel);

    g.setColor(Color.BLACK);
    g.drawLine(0, 125, width, 125);
    g.drawLine(150, 125, 150, height);
    g.drawLine(175, 125, 175, height);
    g.drawLine(200, 125, 200, height);
    g.drawLine(225, 125, 225, height);
    g.drawString("10", 155, 143);
    g.drawString("20", 180, 143);
    g.drawString("50", 205, 143);
    g.drawString("0", 230, 143);

    //  Update the position of the beanbag
    //  on the screen.
    int xPosition = (int)(100.0*x);
    double deltaZ = z - 1.25;
    int zPosition = (int)(125 - 100.0*deltaZ);
    g.fillOval(xPosition, zPosition, 10, 10);

  }

  public static void main(String args[]) {
    BeanBag gui = new BeanBag();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the beanbag. 
      double timeIncrement = 0.05;
      time += timeIncrement; 

      //  There is no force in the x-direction, so the
      //  new x location is the initial x location plus
      //  the product of the horizontal velocity and time.
      x = x0 + vx0*time;

      //  The z-location is influenced by the acceleration
      //  due to gravity.
      double g = -9.81;
      z = z0 + vz0*time + 0.5*g*time*time;

      //  Update the display
      updateDisplay();

      //  If the beanbag hits the ground, stop 
      //  the simulation.
      if ( z <= 1.4 ) {
        gameTimer.stop();
      }
    }
  }
}