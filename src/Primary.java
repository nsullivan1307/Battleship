import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Primary is the JPanel that contains the Enemy Grid, the Player Grid and the side panel
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class Primary extends JPanel implements ActionListener
{

    private PlayerGrid playerGrid;
    private EnemyGrid enemyGrid;
    private SidePanel sp;
    private JPanel gamePanel;
    // These are the state values of the game
    private static int gameState;
    public static final int PLACING_SHIPS = 0, ENEMY_SHIPS = 1;
    public static final int ENEMY_TURN = 2, PLAYER_TURN = 3, END_GAME = 4;
    private Enemy AI;
    private BattleFrame frame;
    private Timer time;
    public Primary(BattleFrame frame)
    {
        this.frame = frame;
        // Sets the initial game state to player placing ships
        gameState = PLACING_SHIPS;
        // Sets the Layout to Border Layout
        setLayout(new BorderLayout());
        // The gamePanel contains both grids, the playe rand enemy grids
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 2));
        playerGrid = new PlayerGrid(this);
        enemyGrid = new EnemyGrid(this);
        gamePanel.add(playerGrid);
        gamePanel.add(enemyGrid);
        add(gamePanel, BorderLayout.CENTER);
        // AI is the Artificial intelligence
        AI = new Enemy(this, enemyGrid, playerGrid);
        // sp is the Side Panel that has the buttons, which is placed at the top.
        sp = new SidePanel(this);
        add(sp, BorderLayout.NORTH);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(2*BattleGrid.SIZE*(BattleGrid.LENGTH+3), BattleGrid.SIZE*(BattleGrid.LENGTH+9+PlayerGrid.yOff)));
        // Create timer for displaying message
        time = new Timer(1000, this);
    }
    public BattleFrame getGame()
    {
        return frame;
    }
    public static int getState()
    {
        return gameState;
    }
    // Ends the game and draws the gray foreground
    public void endGame()
    {
        gameState = END_GAME;
        drawGray();
    }
    public void setReady(boolean b)
    {
        sp.setReady(b);
    }
    // Deploys ships for the AI
    public void deployEnemyShips()
    {
        gameState = ENEMY_SHIPS;
        AI.deployShips();
    }
    // Draws the gray foreground at the end of the game
    public void drawGray()
    {
        Graphics page = this.getGraphics();
        page.setColor(new Color(50, 50, 50, 150));
        page.fillRect(0, 0, 3*BattleGrid.SIZE*(BattleGrid.LENGTH+3), BattleGrid.SIZE*(BattleGrid.LENGTH+7+enemyGrid.getYOff()));
    }
    public void shipsUnmovable()
    {
        playerGrid.setUnmovable();
    }
    // Sets the turn to either player turn or enemy turn and sets the appropriate messages
    public void playerTurn()
    {
        gameState = PLAYER_TURN;
        playerGrid.setMessage("");
        enemyGrid.setMessage("It's your turn. Fire!");
    }
    public void enemyTurn()
    {
        gameState = ENEMY_TURN;
        enemyGrid.setMessage("");
        playerGrid.setMessage("The Enemy is Firing!");
        // Waits while player reads the message that the enemy is firing before making the enemy move
        time.start();
    }
    // Changes the turn from whatever turn it currently is
    public void changeTurn()
    {
        if (Primary.getState() == Primary.ENEMY_TURN)
        {
            playerTurn();
        }
        else if (Primary.getState() == Primary.PLAYER_TURN)
        {
            enemyTurn();
        }
    }
    // When the player has had time to see the message, the enemy moves
    public void actionPerformed(ActionEvent event)
    {
        time.stop();
        AI.makeMove();
    }
}
