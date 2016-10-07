import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class FreeKick extends JFrame implements ActionListener
{
  private JTextField vxTextField;
  private JTextField vyTextField;
  private JTextField vzTextField;
  private JTextField spinRateTextField;
  private JTextField rxTextField;
  private JTextField ryTextField;
  private JTextField rzTextField;

  private JLabel vxLabel;
  private JLabel vyLabel;
  private JLabel vzLabel;
  private JLabel spinRateLabel;
  private JLabel spinAxisLabel;
  private JLabel rxLabel;
  private JLabel ryLabel;
  private JLabel rzLabel;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  Declare a SoccerBall object
  private SoccerBall soccerBall;

  //  These fields are for the images used in the game.
  private ImageIcon playerIcon;
  private int playerWidth;
  private int playerHeight;
  private ImageIcon ballIcon;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public FreeKick() {

    //  Create a SoccerBall object representing the soccer ball.
    soccerBall = 
        new SoccerBall(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.43, 0.038, 1.2, 0.5, 0.0, 0.0,
                           0.0, 0.0, -1.0, 10.0, 0.11, 294.0);

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 20;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up image2 and determine their dimensions
    playerIcon = new ImageIcon("SoccerPlayerCartoon.gif");
    playerWidth = playerIcon.getIconWidth();
    playerHeight = playerIcon.getIconHeight();
    ballIcon = new ImageIcon("SoccerBallCartoon.jpg");

    //  Create JTextField objects to input the initial
    //  conditions.
    vxTextField = new JTextField("-28.0",5);
    vyTextField = new JTextField("10.0",5);
    vzTextField = new JTextField("4.0",5);
    spinRateTextField = new JTextField("10.0",5);
    rxTextField = new JTextField("0.0",5);
    ryTextField = new JTextField("0.0",5);
    rzTextField = new JTextField("-1.0",5);

    //  Create some JLabels
    vxLabel = new JLabel("Initial x-velocity (m/s)");
    vyLabel = new JLabel("Initial y-velocity (m/s)");
    vzLabel = new JLabel("Initial z-velocity (m/s)");
    spinRateLabel = new JLabel("Spin rate (rev/s)");
    spinAxisLabel = new JLabel("Spin Axes");
    rxLabel = new JLabel("rx");
    ryLabel = new JLabel("ry");
    rzLabel = new JLabel("rz");

    //  Create a JButton that will start the ball moving
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

        //  Reset the time, location, and velocity of ball;
        soccerBall.setS(0.0);     //  time
        soccerBall.setQ(0.0,0);   //  vx0
        soccerBall.setQ(23.2,1);  //  x0
        soccerBall.setQ(0.0,2);   //  vy0
        soccerBall.setQ(15.0,3);  //  y0
        soccerBall.setQ(0.0,4);   //  vz0
        soccerBall.setQ(0.0,5);   //  z0

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(301, 301));

    //  Place components on a panel using a GridBagLayout
    JPanel westPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    westPanel.setLayout(gridBagLayout1);

    int col = 0;
    int row = 0;
    int numCol = 1;
    int numRow = 1;
    Insets insets = new Insets(5, 3, 5, 3);
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
    gridBagLayout1.setConstraints(vyLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vyTextField, gbc);

    row = 2;
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

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(spinRateLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(spinRateTextField, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(spinAxisLabel, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rxLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rxTextField, gbc);

    row = 6;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(ryLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(ryTextField, gbc);

    row = 7;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rzLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(rzTextField, gbc);

    row = 8;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 9;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(vxLabel);
    westPanel.add(vxTextField);
    westPanel.add(vyLabel);
    westPanel.add(vyTextField);
    westPanel.add(vzLabel);
    westPanel.add(vzTextField);
    westPanel.add(spinRateLabel);
    westPanel.add(spinRateTextField);
    westPanel.add(spinAxisLabel);
    westPanel.add(rxLabel);
    westPanel.add(rxTextField);
    westPanel.add(ryLabel);
    westPanel.add(ryTextField);
    westPanel.add(rzLabel);
    westPanel.add(rzTextField);
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
    setTitle("Free Kick");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,650,400);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Fire" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Extract input values from textfields
    double vx0 = Double.parseDouble(vxTextField.getText());
    double vy0 = Double.parseDouble(vyTextField.getText());
    double vz0 = Double.parseDouble(vzTextField.getText());
    double spinRate = Double.parseDouble(spinRateTextField.getText());
    double rx = Double.parseDouble(rxTextField.getText());
    double ry = Double.parseDouble(ryTextField.getText());
    double rz = Double.parseDouble(rzTextField.getText());

    //  Calculate the angular velocity from the spin rate.
    double omega = spinRate*2.0*Math.PI;

    //  The ball starts at a spot 18 meters from and directly
    //  in front of the goal.
    double x0 = 23.2;
    double y0 = 15.0;
    double z0 = 0.0;

    //  Set the density to be sea level, the wind 
    //  velocity to zero, and temperature to be 294 K.
    double density = 1.2;
    double temperature = 294.0;
    double windVx = 0.0;
    double windVy = 0.0;

    //  Define some soccer ball variables. The cd value will be
    //  overridden in the getFunction method of the SoccerBall class.
    double ballMass = 0.43;
    double radius = 0.11;
    double area = Math.PI*radius*radius;
    double cd = 0.25;

    //  Create a SoccerBall object representing the soccer ball.
    soccerBall = new SoccerBall(x0, y0, z0, vx0, vy0, vz0, 
         0.0, ballMass, area, density, cd, windVx, windVy,
         rx, ry, rz, omega, radius, temperature);

    //  Update the display
    updateDisplay();

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
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    g.setColor(Color.BLACK);
    g.drawLine(0, 0, width, 0);
    g.drawLine(width, 0, width, height);
    g.drawLine(width, height, 0, height);
    g.drawLine(0, height, 0, 0);

    //  Draw goal box and endline
    g.drawLine(30, 60, 85, 60);
    g.drawLine(85, 60, 85, 243);
    g.drawLine(85, 243, 30, 243);
    g.drawLine(30, 0, 30, height);

    //  Draw goal 
    g.drawLine(30, 115, 10, 115);
    g.drawLine(10, 115, 10, 188);
    g.drawLine(10, 188, 30, 188);

    //  Draw defenders
    g.fillOval(75, 126, 10, 10);
    g.fillOval(75, 136, 10, 10);
    g.fillOval(75, 146, 10, 10);
    g.fillOval(75, 156, 10, 10);
    g.fillOval(75, 166, 10, 10);

    //  Draw soccer player
    ImageIcon playerIcon = new ImageIcon("SoccerPlayerCartoon.gif");
    g.drawImage(playerIcon.getImage(), 230, 
                120, 40, 40, drawingPanel);

    //  Update the position of the soccerBall
    //  on the screen.
    int xPosition = (int)(10.0*soccerBall.getX());
    int yPosition = (int)(height - 10.0*soccerBall.getY());
    g.drawImage(ballIcon.getImage(), xPosition-5, 
                yPosition-5, 10, 10, drawingPanel);

  }

  public static void main(String args[]) {
    FreeKick gui = new FreeKick();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the soccerBall. 
      double timeIncrement = 0.025;
      soccerBall.updateLocationAndVelocity(timeIncrement);

      //  Update the display
      updateDisplay();

      //  Access the Graphics object of the drawing panel.
      Graphics g = drawingPanel.getGraphics();

      //  See if the soccer ball hits the defenders
      if ( soccerBall.getX() <= 7.5 && soccerBall.getX() >= 6.5 ) {
        if ( soccerBall.getY() < 17.6 && soccerBall.getY() > 12.1 ) {
          g.drawString("Hit defenders", 80, 40); 

          //  Stop the simulation
          gameTimer.stop();
        }
      }

      //  When the soccerBall passes the end line, stop the 
      //  simulation and see if a goal was scored.
      if ( soccerBall.getX() <= 3.0 ) {
        if ( soccerBall.getY() < 11.3) {
          g.drawString("Wide Left", 80, 40); 
        }
        else if ( soccerBall.getY() > 18.5) {
          g.drawString("Wide Right", 80, 40); 
        }   
        else if ( soccerBall.getZ() > 2.44) {
          g.drawString("Over the Crossbar", 80, 40); 
        }   
        else {
          g.drawString("GOAL!  GOAL!", 80, 40); 
        } 

        //  Stop the simulation
        gameTimer.stop();
      }
    }
  }
}