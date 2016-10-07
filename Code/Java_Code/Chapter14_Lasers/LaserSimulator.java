import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class LaserSimulator extends JFrame implements ActionListener, 
               MouseListener, MouseMotionListener
{
  private JTextField radiusTextField;
  private JTextField powerTextField;
  private JTextField absorptionTextField;

  private JLabel radiusLabel;
  private JLabel powerLabel;
  private JLabel absorptionLabel;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private double elapsedTime;
  private double explosionTime;
  private double airplaneX;
  private int laserX;
  private int laserZ;

  //  Laser beam fields.
  double power;
  double radius;
  double absorption;
  double area;

  //  These fields are for the images used in the game.
  private ImageIcon airplaneIcon;
  private int airplaneIconWidth;
  private int airplaneIconHeight;
  private ImageIcon explosionIcon;
  private int explosionIconWidth;
  private int explosionIconHeight;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public LaserSimulator() {

    elapsedTime = 0.0;
    airplaneX = 0.0;
    laserX = 150;
    laserZ = 280;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. 
    gameUpdater = new GameUpdater();
    gameTimer = new Timer(100, gameUpdater);

    //  Set up some images and determine their dimensions
    airplaneIcon = new ImageIcon("airplaneCartoon.jpg");
    airplaneIconWidth = airplaneIcon.getIconWidth();
    airplaneIconHeight = airplaneIcon.getIconHeight();
    explosionIcon = new ImageIcon("explosionCartoon.jpg");
    explosionIconWidth = explosionIcon.getIconWidth();
    explosionIconHeight = explosionIcon.getIconHeight();

    //  Create JTextField objects to input the initial
    //  conditions.
    radiusTextField = new JTextField("35.0",6);
    powerTextField = new JTextField("7.5e+6",6);
    absorptionTextField = new JTextField("0.75",6);

    //  Create some JLabels
    radiusLabel = new JLabel("Beam radius, cm");
    powerLabel = new JLabel("Beam power, J/cm^2");
    absorptionLabel = new JLabel("Absorption coefficient");

    //  Create a JButton that will start the ball moving
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
        //  stop the timer and reset the time.
        gameTimer.stop();

        //  Reset values.
        elapsedTime = 0.0;
        airplaneX = 0.0;
        laserX = 150;
        laserZ = 280;

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JPanel that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(351, 301));
    drawingPanel.addMouseMotionListener(this);
    drawingPanel.addMouseListener(this);

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
    gridBagLayout1.setConstraints(powerLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(powerTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(radiusLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(radiusTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(absorptionLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(absorptionTextField, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(powerLabel);
    westPanel.add(powerTextField);
    westPanel.add(radiusLabel);
    westPanel.add(radiusTextField);
    westPanel.add(absorptionLabel);
    westPanel.add(absorptionTextField);
    westPanel.add(startButton);
    westPanel.add(resetButton);

    //  The drawing panel.
    JPanel northPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    northPanel.setLayout(gridBagLayout3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.CENTER,
                 GridBagConstraints.NONE, 
                 new Insets(10, 10, 10, 20), 0, 0);
    gridBagLayout3.setConstraints(drawingPanel, gbc);

    northPanel.add(drawingPanel);

    //  Add the JPanel objects to the content pane
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(westPanel, BorderLayout.WEST);
    getContentPane().add(northPanel, BorderLayout.NORTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Laser Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,450,550);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Get some initial quantities from the textfields.
    double power = Double.parseDouble(powerTextField.getText());
    double radius = Double.parseDouble(radiusTextField.getText());
    double absorption = Double.parseDouble(absorptionTextField.getText());

    //  Determine the beam area.
    double area = Math.PI*radius*radius;

    //  Compute beam contact time required to destroy airplane.
    explosionTime = 10000.0*area/(absorption*power);

    //  Start the airplane using a Timer object
    gameTimer.start();
  }

  //  These methods are used to update the position of the
  //  laser.
  public void mouseDragged(MouseEvent me) {
    laserX = me.getX();
    laserZ = me.getY();
  }

  public void mouseClicked(MouseEvent me) {
    laserX = me.getX();
    laserZ = me.getY();
  }

  //  These methods have to be included as part of the 
  //  listener interfaces even though they aren't used.
  public void mouseMoved(MouseEvent me) {}
  public void mouseEntered(MouseEvent me) {}
  public void mouseExited(MouseEvent me) {}
  public void mouseReleased(MouseEvent me) {}
  public void mousePressed(MouseEvent me) {}

  //  This method redraws the GUI display.
  private void updateDisplay() {
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, height-10, width, height-10);

    //  Draw the airplane.
    g.drawImage(airplaneIcon.getImage(), (int)airplaneX, 20, 
                airplaneIconWidth, airplaneIconHeight, drawingPanel);

    //  Draw the laser building.
    g.fillRect(140, height-30, 20, 20);

    //  Draw laser beam
    g.setColor(Color.RED);
    g.drawLine(150, height-20, laserX, laserZ);

    //  If the time that the laser has been on the airplane is
    //  greater than the necessary time to destroy the airplane, 
    //  draw the explosion icon.
    if ( elapsedTime > explosionTime ) {
      g.setColor(Color.BLACK);
      g.drawImage(explosionIcon.getImage(), (int)airplaneX - 10, 10, 
                explosionIconWidth, explosionIconHeight, drawingPanel);      
    }

  }

  public static void main(String args[]) {
    LaserSimulator gui = new LaserSimulator();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  set the time increment. 
      double timeIncrement = 0.1;

      //  If the laser is hitting the airplane increment
      //  the elapsedTime variable. If not, set the elapsedTime
      //  variable to zero.
      if ( laserX > airplaneX && laserX < airplaneX + airplaneIconWidth &&
           laserZ > 10 && laserZ < 10 + airplaneIconHeight ) {
        elapsedTime = elapsedTime + timeIncrement;
      }
      else {
        elapsedTime = 0.0;
      }

      //  The airplane cartoon moves across the screen at a 
      //  constant velocity of 20 pixels/second.
      airplaneX = airplaneX + 20.0*timeIncrement;

      //  Update the display
      updateDisplay();

      //  If the time that the laser has been on the airplane is
      //  greater than the necessary time to destroy the airplane, 
      //  stop the simulation.
      if ( elapsedTime > explosionTime ) {
        gameTimer.stop();      
      }

    }
  }
}