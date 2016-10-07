import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.Timer;

public class PaddleGame extends JFrame 
          implements ActionListener, ChangeListener
{
  private JTextField vxTextField;
  private JTextField vzTextField;

  private JLabel vxLabel;
  private JLabel vzLabel;
  private JLabel sliderLabel;

  private JSlider paddleSlider;

  private JButton startButton;
  private JButton resetButton;
  private JPanel drawingPanel;
  private GridBagConstraints gbc;

  //  These fields store ball data and the coefficient 
  //  of restitution.
  private double ballVx;
  private double ballVz;
  private double ballX;
  private double ballZ;
  private double paddleZ;
  private int paddleHeight;
  private double ballRadius; //  radius of ball

  //  These elements are used to control the execution
  //  speed of the game. Without them, the game would
  //  run too quickly.
  private GameUpdater gameUpdater;
  private Timer gameTimer;

  public PaddleGame() {

    //  Initialize ball location, velocity, and
    //  paddle location.
    ballVx = 100.0;
    ballVz = -71.0;
    ballX = 100.0;
    ballZ = 100.0;
    paddleZ = 100;
    paddleHeight = 40;
    ballRadius = 5.0;

    //  Create a Timer object that will be used
    //  to slow the action down and an ActionListener
    //  that the Timer will call. The timeDelay variable
    //  is the time delay in milliseconds.
    gameUpdater = new GameUpdater();
    int timeDelay = 50;
    gameTimer = new Timer(timeDelay, gameUpdater);

    //  Create JTextField objects to input the initial 
    //  velocity components of the ball.
    vxTextField = new JTextField("100.0",6);
    vzTextField = new JTextField("-80.0",6);

    //  Create some JLabels
    vxLabel = new JLabel("x-velocity, m/s");
    vzLabel = new JLabel("z-velocity, m/s");
    sliderLabel = new JLabel("Paddle location");

    //  Create a JSlider that will move the paddle up and down.
    paddleSlider = new JSlider(JSlider.VERTICAL, paddleHeight/2,
                               200 - paddleHeight/2, (int)paddleZ);
    paddleSlider.addChangeListener(this);

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

        //  Reset the ball location, velocity, and
        //  paddle location.
        ballVx = 100.0;
        ballVz = -80.0;
        ballX = 100.0;
        ballZ = 100.0;
        paddleZ = 100;
        paddleSlider.setValue((int)paddleZ);
  
        //  Update the display.
        updateDisplay();
      }  
    });

    //  Create a JTextArea that will display the results
    drawingPanel = new JPanel();
    drawingPanel.setPreferredSize(new Dimension(301, 201));

    //  Place components on a panel using a GridBagLayout
    JPanel northPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    northPanel.setLayout(gridBagLayout1);

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

    row = 0;
    col = 2;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(startButton, gbc);

    col = 3;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout1.setConstraints(resetButton, gbc);

    northPanel.add(vxLabel);
    northPanel.add(vxTextField);
    northPanel.add(vzLabel);
    northPanel.add(vzTextField);
    northPanel.add(startButton);
    northPanel.add(resetButton);

    //  Place components on a panel using a GridBagLayout
    JPanel westPanel = new JPanel();
    GridBagLayout gridBagLayout3 = new GridBagLayout();
    westPanel.setLayout(gridBagLayout3);

    row = 0;
    col = 0;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.EAST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(sliderLabel, gbc);

    col = 1;
    gbc = new GridBagConstraints(col, row, numCol, numRow,
                 0.0, 0.0, GridBagConstraints.WEST,
                 GridBagConstraints.NONE, insets, 0, 0);
    gridBagLayout3.setConstraints(paddleSlider, gbc);

    westPanel.add(sliderLabel);
    westPanel.add(paddleSlider);

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
    getContentPane().add(northPanel, BorderLayout.NORTH);
    getContentPane().add(eastPanel, BorderLayout.EAST);
    getContentPane().add(westPanel, BorderLayout.WEST);

    //  Add a title to the JFrame, size it, and make it visible.
    setTitle("Paddle Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100,100,500,350);
    setVisible(true);

    //  Update the GUI display
    updateDisplay(); 
  }

  //  The actionPerformed() method is called when 
  //  the "Start" button is pressed. 
  public void actionPerformed(ActionEvent event) {
    
    //  Get the initial quantities from the textfields.
    ballVx = Double.parseDouble(vxTextField.getText());
    ballVz = Double.parseDouble(vzTextField.getText());

    //  Update the display
    updateDisplay();

    //  Start the box sliding using a Timer object
    //  to slow down the action.
    gameTimer.start();
  }

  //  The stateChanged() method is called when 
  //  the the JSlider position is changed. 
  public void stateChanged(ChangeEvent event) {
    
    //  Set new paddle location based on position of JSlider.
    paddleZ = paddleSlider.getValue();

    //  Update the display
    updateDisplay();
  }

  //  This method redraws the GUI display.
  private void updateDisplay() {
    Graphics g = drawingPanel.getGraphics();
    int width = drawingPanel.getWidth() - 1;
    int height = drawingPanel.getHeight() - 1;

    g.clearRect(0, 0, width, height);
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height); 

    //  Draw outline of game area.
    g.setColor(Color.BLACK);
    g.drawLine(0, 0, width, 0);
    g.drawLine(width, 0, width, height);
    g.drawLine(width, height, 0, height);

    //  Update the position of the ball on the screen.
    int xPosition = (int)(ballX - ballRadius);
    int zPosition = (int)(height - ballRadius - ballZ);
    g.fillOval(xPosition, zPosition, 2*(int)(ballRadius), 
               2*(int)(ballRadius));

    //  Update the position of the paddle on the screen.
    zPosition = (int)(height - paddleZ);
    g.fillRect(10, zPosition - paddleHeight/2, 10, paddleHeight);
  }

  public static void main(String args[]) {
    PaddleGame gui = new PaddleGame();
  }

  //  This ActionListener is called by the Timer
  class GameUpdater implements ActionListener {
    public void actionPerformed(ActionEvent event) {

      //  Get dimensions of drawing area.
      Graphics g = drawingPanel.getGraphics();
      int width = drawingPanel.getWidth() - 1;
      int height = drawingPanel.getHeight() - 1;

      //  Determine if ball collides with right wall.
      //  If it does, change the x-velocity of the ball.
      if ( ballVx > 0.0 && ballX + ballRadius >= width ) {
        ballVx = -ballVx;
      }

      //  Determine if ball collides with the top wall.
      //  If it does, change the z-velocity of the ball.
      if ( ballVz > 0.0 && ballZ + ballRadius >= height ) {
        ballVz = -ballVz;
      }

      //  Determine if ball collides with the bottom wall.
      //  If it does, change the z-velocity of the ball.
      if ( ballVz < 0.0 && ballZ - ballRadius <= 0.0 ) {
        ballVz = -ballVz;
      }

      //  Determine if ball collides with paddle.
      //  If it does, change the x-velocity of the ball.
      if ( ballVx < 0.0 && ballX - ballRadius <= 20.0 ) {
        if ( ballZ - ballRadius >= paddleZ - paddleHeight/2 &&
             ballZ + ballRadius <= paddleZ + paddleHeight/2 ) {
          ballVx = -ballVx;
        }
      }

      //  If ball travels off the left edge of the game
      //  area, stop the simulation.
      if ( ballX <= 0.0 ) {
        gameTimer.stop();
      }

      //  Compute the new location of the ball. 
      double timeIncrement = 0.07;
      ballX = ballX + timeIncrement*ballVx;
      ballZ = ballZ + timeIncrement*ballVz;

      //  Update the display
      updateDisplay();

    }
  }
}