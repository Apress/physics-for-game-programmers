import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class SphereCollision extends JFrame implements ActionListener
{
  private JTextField vx1TextField;
  private JTextField mass1TextField;
  private JTextField vx2TextField;
  private JTextField mass2TextField;
  private JTextField eTextField;

  private JLabel vx1Label;
  private JLabel mass1Label;
  private JLabel vx2Label;
  private JLabel mass2Label;
  private JLabel eLabel;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  These fields store sphere data and the coefficient 
  //  of restitution.
  private double vx1;
  private double x1;
  private double mass1;
  private double vx2;
  private double x2;
  private double mass2;
  private double e;            //  coefficient of restitution
  private double sphereRadius; //  radius of spheres

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public SphereCollision() {

    //  Initialize sphere location, velocity, and
    //  coefficient of restitution.
    vx1 = 10.0;
    x1 = 5.0;
    mass1 = 10.0;
    vx2 = -5.0;
    x2 = 15.0;
    mass2 = 5.0;
    e = 0.9;
    sphereRadius = 2.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Create JTextField objects to input the velocity and
    //  mass of the sphere and the coefficient of restitution.
    vx1TextField = new JTextField("10.0",6);
    mass1TextField = new JTextField("10.0",6);
    vx2TextField = new JTextField("-5.0",6);
    mass2TextField = new JTextField("5.0",6);
    eTextField = new JTextField("0.9",6);

    //  Create some JLabels
    vx1Label = new JLabel("Sphere 1 velocity, m/s");
    mass1Label = new JLabel("Sphere 1 mass, kg");
    vx2Label = new JLabel("Sphere 2 velocity, m/s");
    mass2Label = new JLabel("Sphere 2 mass, kg");
    eLabel = new JLabel("Coefficient of restitution");

    //  Create a JButton that will start the balls moving
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

        //  Reset the sphere location, velocity, and
        //  coefficient of restitution.
        vx1 = 10.0;
        x1 = 5.0;
        mass1 = 10.0;
        vx2 = -5.0;
        x2 = 15.0;
        mass2 = 5.0;
        e = 0.9;

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(301, 201));

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
    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vx1Label, gbc);

    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vx1TextField, gbc);

    row = 1;
    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(mass1Label, gbc);

    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(mass1TextField, gbc);

    row = 2;
    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vx2Label, gbc);

    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(vx2TextField, gbc);

    row = 3;
    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(mass2Label, gbc);

    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(mass2TextField, gbc);

    row = 4;
    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(eLabel, gbc);

    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(eTextField, gbc);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(vx1Label);
    westPanel.add(vx1TextField);
    westPanel.add(mass1Label);
    westPanel.add(mass1TextField);
    westPanel.add(vx2Label);
    westPanel.add(vx2TextField);
    westPanel.add(mass2Label);
    westPanel.add(mass2TextField);
    westPanel.add(eLabel);
    westPanel.add(eTextField);
    westPanel.add(startButton);
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
    setTitle("Linear Collision Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,500);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    
    //  Get the initial quantities from the textfields.
    vx1 = Double.parseDouble(vx1TextField.getText());
    mass1 = Double.parseDouble(mass1TextField.getText());
    vx2 = Double.parseDouble(vx2TextField.getText());
    mass2 = Double.parseDouble(mass2TextField.getText());
    e = Double.parseDouble(eTextField.getText());

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

    //  Update the position of the spheres on the screen.
    g.setColor(Color.BLACK);
    int xPosition = (int)(10.0*(x1 - sphereRadius));
    g.drawOval(xPosition, 80, 2*(int)(10.0*sphereRadius), 
               2*(int)(10.0*sphereRadius));

    xPosition = (int)(10.0*(x2 - sphereRadius));
    g.drawOval(xPosition, 80, 2*(int)(10.0*sphereRadius), 
               2*(int)(10.0*sphereRadius));

  }

  public static void main(String args[]) {
    SphereCollision gui = new SphereCollision();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Determine if a collision occurs and if it
      //  does, change the velocties of the spheres.
      //  A collision occurs if the distance between the
      //  centers of the spheres is less than twice their
      //  radius
      double distance = Math.abs(x1 - x2);
      double tmp = 1.0/(mass1 + mass2);

      if ( distance <= 2.0*sphereRadius ) {
        double newVx1 = (mass1 - e*mass2)*vx1*tmp + 
                        (1.0 + e)*mass2*vx2*tmp;
        double newVx2 = (1.0 + e)*mass1*vx1*tmp +
                        (mass2 - e*mass1)*vx2*tmp;
        vx1 = newVx1;
        vx2 = newVx2;
      }

      //  Compute the new location of the spheres. 
      double timeIncrement = 0.07;
      x1 = x1 + timeIncrement*vx1;
      x2 = x2 + timeIncrement*vx2;

      //  Update the display
      updateDisplay();

      //  If either of the spheres hits the outer edge
      //  of the display, stop the simulation.
      Graphics g = drawingPanel.getGraphics();
      int width = drawingPanel.getWidth() - 1;
      int height = drawingPanel.getHeight() - 1;

      if ( 10.0*(x1 + sphereRadius) > width || 
           10.0*(x1 - sphereRadius) < 0.0 ||
           10.0*(x2 + sphereRadius) > width || 
           10.0*(x2 - sphereRadius) < 0.0 ) {
        gameTimer.stop();
      }
    }
  }
}