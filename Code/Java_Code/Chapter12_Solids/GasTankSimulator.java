import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.Timer;

public class GasTankSimulator extends JFrame implements ActionListener
{
  private JTextField thicknessTextField;
  private JTextField flameTempTextField;
  private JTextField timeTextField;
  private JTextField outerTempTextField;
  private JTextField innerTempTextField;

  private JLabel thicknessLabel;
  private JLabel flameTempLabel;
  private JLabel timeLabel;
  private JLabel outerTempLabel;
  private JLabel innerTempLabel;
  private JLabel materialLabel;

  private JComboBox materialComboBox;

  private JButton fireButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  private double time;
  private double innerWallTemp;

  //  Declare a GasTank object
  private GasTank tank;

  //  These fields are for the images used in the game.
  private ImageIcon soldierIcon;
  private int soldierIconWidth;
  private int soldierIconHeight;
  private ImageIcon flameIcon;
  private int flameIconWidth;
  private int flameIconHeight;
  private ImageIcon explosionIcon;
  private int explosionIconWidth;
  private int explosionIconHeight;

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public GasTankSimulator() {

    time = 0.0;
    innerWallTemp = 300.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. 
    gameUpdater = new GameUpdater();
    gameTimer = new Timer(100, gameUpdater);

    //  Set up some images and determine their dimensions
    soldierIcon = new ImageIcon("soldierCartoon.jpg");
    soldierIconWidth = soldierIcon.getIconWidth();
    soldierIconHeight = soldierIcon.getIconHeight();
    flameIcon = new ImageIcon("flameCartoon.jpg");
    flameIconWidth = flameIcon.getIconWidth();
    flameIconHeight = flameIcon.getIconHeight();
    explosionIcon = new ImageIcon("explosionCartoon.jpg");
    explosionIconWidth = explosionIcon.getIconWidth();
    explosionIconHeight = explosionIcon.getIconHeight();

    //  Create JTextField objects to input the initial
    //  conditions.
    thicknessTextField = new JTextField("0.02",6);
    flameTempTextField = new JTextField("1000.0",6);

    timeTextField = new JTextField("0.0",8);
    timeTextField.setEditable(false);

    outerTempTextField = new JTextField("300.0",6);
    outerTempTextField.setEditable(false);

    innerTempTextField = new JTextField("300.0",6);
    innerTempTextField.setEditable(false);

    //  Create some JLabels
    thicknessLabel = new JLabel("Tank thickness, m");
    flameTempLabel = new JLabel("Flame temperature, K");
    timeLabel = new JLabel("Elapsed time, s");
    outerTempLabel = new JLabel("Outer wall temp, K");
    innerTempLabel = new JLabel("Inner wall temp, K");
    materialLabel = new JLabel("Tank material");

    //  Create a JComboBox to choose the rocket engine type.
    materialComboBox = new JComboBox();
    materialComboBox.addItem("Aluminum");
    materialComboBox.addItem("Concrete");

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
        //  stop the timer and reset the time.
        gameTimer.stop();

        //  Reset the time and inner wall temperature values.
        time = 0.0;
        innerWallTemp = 300.0;

        //  Reset the textfields
        timeTextField.setText("0.0");
        outerTempTextField.setText("300.0");
        innerTempTextField.setText("300.0");

        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JPanel that will display the results
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
    gridBagLayout1.setConstraints(flameTempLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(flameTempTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(thicknessLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(thicknessTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(materialLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(materialComboBox, gbc);

    row = 3;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(fireButton, gbc);

    row = 4;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    westPanel.add(flameTempLabel);
    westPanel.add(flameTempTextField);
    westPanel.add(thicknessLabel);
    westPanel.add(thicknessTextField);
    westPanel.add(materialLabel);
    westPanel.add(materialComboBox);
    westPanel.add(fireButton);
    westPanel.add(resetButton);

    //  Place components on a panel using a GridBagLayout
    JPanel eastPanel = new JPanel();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    eastPanel.setLayout(gridBagLayout2);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(timeLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(timeTextField, gbc);

    row = 1;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(outerTempLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(outerTempTextField, gbc);

    row = 2;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(innerTempLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout2.setConstraints(innerTempTextField, gbc);

    eastPanel.add(timeLabel);
    eastPanel.add(timeTextField);
    eastPanel.add(outerTempLabel);
    eastPanel.add(outerTempTextField);
    eastPanel.add(innerTempLabel);
    eastPanel.add(innerTempTextField);

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
    getContentPane().add(eastPanel, BorderLayout.EAST);
    getContentPane().add(northPanel, BorderLayout.NORTH);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Gas Tank Simulator");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,450,500);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Fire" button is pressed. 
  public void actionPerformed(ActionEvent event) {

    //  Get some initial quantities from the textfields.
    double flameTemp = Double.parseDouble(flameTempTextField.getText());
    double thickness = Double.parseDouble(thicknessTextField.getText());

    //  Set the initial temperature.
    double initialT = 300.0;

    //  Set the diffusivity according to the material selected.
    double diffusivity;
    String material = (String)materialComboBox.getSelectedItem();
    if ( material.equals("Aluminum") ) {
      diffusivity = 9.975e-5;
    }
    else { 
      diffusivity = 6.6e-7;   //  concrete
    }

    //  Create a GasTank object.
    tank = new GasTank(thickness, diffusivity, initialT, flameTemp);

    //  Set the display for the outer wall temperature textfield
    outerTempTextField.setText(""+flameTemp);

    //  Update the display
    updateDisplay();

    //  Fire the flamethrower using a Timer object
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
    g.drawLine(0, height-10, width, height-10);

    //  Draw the soldier.
    int soldierZ = height - 10 - soldierIconHeight;
    g.drawImage(soldierIcon.getImage(), 20, soldierZ, 
                soldierIconWidth, soldierIconHeight, drawingPanel);

    //  If the flamethrower is on, draw the flame
    if ( time > 0.0 ) {
      g.drawImage(flameIcon.getImage(), 80, height - 77, 
                flameIconWidth, flameIconHeight, drawingPanel);
    }

    //  Draw the gas tank.
    g.drawOval(150, height-60, 50, 50);

    //  If the inner wall of the tank has reached the ignition
    //  temperature of gasoline, stop the simulation and draw
    //  the explosion icon.
    if ( innerWallTemp > 550.0 ) {
      g.drawImage(explosionIcon.getImage(), 100, height - 140, 
                explosionIconWidth, explosionIconHeight, drawingPanel);      
    }

  }

  public static void main(String args[]) {
    GasTankSimulator gui = new GasTankSimulator();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Compute the new inner wall temperature. 
      double timeIncrement = 0.1;
      time = time + timeIncrement;
      innerWallTemp = tank.getTemperature(tank.getThickness(),time);

      //  Update the display
      updateDisplay();

      //  Update the output data textfields
      timeTextField.setText(""+(float)time);
      innerTempTextField.setText(""+(int)innerWallTemp);

      //  If the inner wall temperature exceeds the ignition 
      //  temperature of gasoline, stop the simulation.
      if ( innerWallTemp > 550.0 ) {
        gameTimer.stop();      
      }

    }
  }
}