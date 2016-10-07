import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class GolfGame2 extends JFrame implements ActionListener
{
  private JTextField vxTextField;
  private JTextField vyTextField;
  private JTextField vzTextField;
  private JTextField distanceTextField;
  private JTextField massTextField;
  private JTextField areaTextField;
  private JTextField cdTextField;
  private JTextField densityTextField;

  private JLabel vxLabel;
  private JLabel vyLabel;
  private JLabel vzLabel;
  private JLabel distanceLabel;
  private JLabel axesLabel;
  private JLabel massLabel;
  private JLabel areaLabel;
  private JLabel cdLabel;
  private JLabel densityLabel;

  private JComboBox axesComboBox;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  The golf ball is a DragProjectile
  private DragProjectile golfball;

  //  The player can control the distance to the hole.
  private double distanceToHole;

  //  These fields are for the images used in the game.
  private ImageIcon golferIcon;
  private ImageIcon flagIcon;
  private int golferWidth;
  private int golferHeight;
  private int flagWidth; 
  private int flagHeight;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public GolfGame2() {

    //  Create a DragProjectile object representing
    //  the golf ball.
    golfball = 
        new DragProjectile(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                           0.0459, 0.001432, 0.5, 1.225);

    //  Initialize the distanceToHole field.
    distanceToHole = 200.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Set up some images and determine their dimensions
    golferIcon = new ImageIcon("Golfer.jpg");
    golferWidth = golferIcon.getIconWidth();
    golferHeight = golferIcon.getIconHeight();

    flagIcon = new ImageIcon("Hole_Cartoon.jpg");
    flagWidth = flagIcon.getIconWidth();
    flagHeight = flagIcon.getIconHeight();

    //  Create JTextField objects to input the initial
    //  golfball velocities and distance to hole.
    vxTextField = new JTextField("31.0",6);
    vyTextField = new JTextField("0.0",6);
    vzTextField = new JTextField("35.0",6);
    distanceTextField = new JTextField("200.0",6);
    massTextField    = new JTextField("0.0459",6);
    areaTextField    = new JTextField("0.001432",6);
    cdTextField      = new JTextField("0.25",6);
    densityTextField = new JTextField("1.225",6);

    //  Create some JLabels
    vxLabel = new JLabel("Initial x-velocity, m/s");
    vyLabel = new JLabel("Initial y-velocity, m/s");
    vzLabel = new JLabel("Initial z-velocity, m/s");
    distanceLabel = new JLabel("Distance to hole, m");
    axesLabel = new JLabel("View axes");
    massLabel = new JLabel("mass (kg)");
    areaLabel = new JLabel("area (m^2)");
    cdLabel = new JLabel("drag coefficient");
    densityLabel = new JLabel("density (kg/m^3)");

    //  Create a JComboBox to choose the coordinate axes that 
    //  will be displayed.
    axesComboBox = new JComboBox();
    axesComboBox.addItem("XZ");
    axesComboBox.addItem("XY");

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
        golfball.setS(0.0);     //  time
        golfball.setQ(0.0,0);   //  vx0
        golfball.setQ(0.0,1);   //  x0
        golfball.setQ(0.0,2);   //  vy0
        golfball.setQ(0.0,3);   //  y0
        golfball.setQ(0.0,4);   //  vz0
        golfball.setQ(0.0,5);   //  z0

        //  Reset the distance to hole.
        distanceToHole = Double.parseDouble(distanceTextField.getText());
 
        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(501, 201));

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
    gridBagLayout1.setConstraints(distanceLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(distanceTextField, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(axesLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(axesComboBox, gbc);

    row = 5;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 6;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    row = 0;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(massLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(massTextField, gbc);

    row = 1;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(areaLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(areaTextField, gbc);

    row = 2;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(cdLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(cdTextField, gbc);

    row = 3;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(densityLabel, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(densityTextField, gbc);

    westPanel.add(vxLabel);
    westPanel.add(vxTextField);
    westPanel.add(vyLabel);
    westPanel.add(vyTextField);
    westPanel.add(vzLabel);
    westPanel.add(vzTextField);
    westPanel.add(distanceLabel);
    westPanel.add(distanceTextField);
    westPanel.add(axesLabel);
    westPanel.add(axesComboBox);
    westPanel.add(massLabel);
    westPanel.add(massTextField);
    westPanel.add(areaLabel);
    westPanel.add(areaTextField);
    westPanel.add(cdLabel);
    westPanel.add(cdTextField);
    westPanel.add(densityLabel);
    westPanel.add(densityTextField);
    westPanel.add(fireButton);
    westPanel.add(resetButton);

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
    getContentPane().add(southPanel, BorderLayout.SOUTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Golf Game version 2");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,600,550);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Fire" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    
    //  Get the initial quantities from the textfields.
    double vx0 = Double.parseDouble(vxTextField.getText());
    double vy0 = Double.parseDouble(vyTextField.getText());
    double vz0 = Double.parseDouble(vzTextField.getText());
    double mass = Double.parseDouble(massTextField.getText());
    double area = Double.parseDouble(areaTextField.getText());
    double cd = Double.parseDouble(cdTextField.getText());
    double density = Double.parseDouble(densityTextField.getText());
    distanceToHole = Double.parseDouble(distanceTextField.getText());

    //  Create a DragProjectile object representing the golf ball.
    golfball = new DragProjectile(0.0, 0.0, 0.0,
         vx0, vy0, vz0, 0.0, mass, area, density, cd);

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

    //  Draw picture based on whether the XZ or 
    //  XY axes are selected.
    String axes = (String)axesComboBox.getSelectedItem();
    if ( axes.equals("XZ") ) {

      //  Draw the golfer.
      int zLocation = height - 50;
      g.drawImage(golferIcon.getImage(), 0, zLocation, 34, 50, drawingPanel);

      //  Draw the flag
      zLocation = height - 62;
      g.drawImage(flagIcon.getImage(), (int)(2.0*distanceToHole), 
                  zLocation, 55, 62, drawingPanel);

      //  Update the position of the golfball
      //  on the screen.
      g.setColor(Color.BLACK);
      int xPosition = (int)(2.0*golfball.getX() + 14);
      int zPosition = (int)(height - 5 - 2.0*golfball.getZ());
      g.fillOval(xPosition, zPosition, 5, 5);
    }
    else {
      //  Draw location of green.
      g.setColor(Color.BLACK);
      g.drawOval((int)(2.0*distanceToHole - 20), 80, 40, 40);
      g.fillOval((int)(2.0*distanceToHole - 4), 96, 8, 8);

      //  Update the position of the golfball
      //  on the screen.
      int xPosition = (int)(2.0*golfball.getX());
      int yPosition = (int)(100 - 2 - 2.0*golfball.getY());
      g.fillOval(xPosition, yPosition, 5, 5);
    }

  }

  public static void main(String args[]) {
    GolfGame2 gui = new GolfGame2();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Update the time and compute the new position
      //  of the golfball. 
      double timeIncrement = 0.07;
      golfball.updateLocationAndVelocity(timeIncrement);

      //  Update the display
      updateDisplay();

      //  Access the Graphics object of the drawing panel.
      Graphics g = drawingPanel.getGraphics();

      //  When the golfball hits the ground, stop the simulation
      //  and see where ball has landed.
      if ( golfball.getZ() <= 0.0 ) {

System.out.println("time="+(float)golfball.getTime()+
"  x="+(float)golfball.getX()+
"  y="+(float)golfball.getY()+"  z="+(float)golfball.getZ());

        //  Stop the simulation
        gameTimer.stop();

        //  Determine if ball is on the green.
        if ( golfball.getX() > distanceToHole - 10.0 &&
             golfball.getX() < distanceToHole + 10.0 &&
             golfball.getY() < 10.0) {
          g.drawString("You're on the green", 100, 30);

        }
        else {
          drawingPanel.getGraphics().drawString("You missed", 100, 30);
        }
      }
    }
  }
}