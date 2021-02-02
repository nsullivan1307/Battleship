import javax.swing.*;
import java.awt.*;
/**
 * This is the general grid that is inherited for both the player and enemy grid.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class BattleGrid extends JPanel
{
    private final Position[][] grid;
    protected int xOff, yOff;
    public static final Dimension SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int LENGTH = 10, SIZE = (int) SCREEN.getWidth()/50;
    private final int[] numbers;
    private final char[] letters;
    private final Font font;
    protected Battleship[] ships;
    private int shipsSunk;
    private final Primary panel;
    protected JLabel hitStatusMessage;
    /**
     * Constructor for objects of class BattleGrid
     */
    public BattleGrid(int xOff, int yOff, Primary p)
    {
        // This is the square array for the grid
        grid = new Position[LENGTH][LENGTH];
        // The x and y offsets of the grid
        this.xOff = xOff;
        this.yOff = yOff;
        // The Primary panel
        this.panel = p;
        // Define the ships
        ships = new Battleship[5];
        ships[0] = new Battleship(Battleship.Type.AIRCRAFT_CARRIER, xOff, SIZE*(LENGTH+1)+yOff, this);
        ships[1] = new Battleship(Battleship.Type.BATTLESHIP, xOff, SIZE*(LENGTH+3)+yOff, this);
        ships[2] = new Battleship(Battleship.Type.SUBMARINE, xOff+6*SIZE, SIZE*(LENGTH+3)+yOff, this);
        ships[3] = new Battleship(Battleship.Type.CRUISER, xOff, SIZE*(LENGTH+5)+yOff, this);
        ships[4] = new Battleship(Battleship.Type.DESTROYER, xOff+5*SIZE, SIZE*(LENGTH+5)+yOff, this);
        // Creates each position in the grid
        for (int i = 0; i < LENGTH; i++)
        {
            for (int j = 0; j < LENGTH; j++)
            {
                grid[i][j] = new Position(i*SIZE+xOff, j*SIZE+yOff, this);
            }
        }
        // Sets the font for the status message and the number and letters beside the grid
        font = new Font("Arial", Font.PLAIN, (int)(SIZE*0.84));
        // Initiating the arrays for the numbers and letters beside the grid
        numbers = new int[LENGTH];
        letters = new char[LENGTH];
        // Initiating the values of the numbers and letters beside the grid
        for (int i = 0; i < LENGTH; i++)
        {
            numbers[i] = i+1;
            letters[i] = (char)(65+i);
        }
        // The number of ships that are sunk
        shipsSunk = 0;
        setBackground(Color.WHITE);
        //This will be the status message above the grid
        hitStatusMessage = new JLabel("");
        hitStatusMessage.setFont(font);
        hitStatusMessage.setForeground(Color.green);
        add(hitStatusMessage);

    }
    public int getYOff()
    {
        return yOff;
    }
    public Primary getPrimary()
    {
        return panel;
    }
    protected Battleship[] getShips()
    {
        return ships;
    }
    public Position getPosition(int x, int y)
    {
        return grid[x][y];
    }
    // This happens when a ship is sunk
    public void sink(Battleship ship)
    {
        // Adds to the number of ships sunk
        shipsSunk++;
        if (Primary.getState() == Primary.PLAYER_TURN) // If it is the player's turn
        {
            // Alerts the player that they have sunk a ship
            setMessage("You have sunk a " + ship.getName());
            // If 5 ships have been sunk, it tells the player that they have won
            if (shipsSunk == 5)
            {
                setMessage("You Won!");
                panel.endGame();
                this.getGraphics().fillRect(0, 0, SIZE*(LENGTH+3), yOff-SIZE);
            }
        }
        else // If it is the player's turn
        {
            // Alerts the player that the enemy has sunk a ship
            setMessage("The Enemy has sunk a " + ship.getName());
            // If 5 ships have been sunk, it tells the player that they have lost
            if (shipsSunk == 5)
            {
                setMessage("You lost!");
                panel.endGame();
                this.getGraphics().fillRect(0, 0, SIZE*(LENGTH+3), yOff-SIZE);
            }
        }
    }
    public void setMessage(String message)
    {
        hitStatusMessage.setText(message);
    }
    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        // Sets the page font
        page.setFont(font);
        for (int i = 0; i < LENGTH; i++)
        {
            // Draws the Letters and numbers
            page.setColor(Color.BLACK);
            page.fillRect(0, 0, SIZE*(LENGTH+3), yOff-SIZE);
            page.drawString("" + letters[i], xOff-SIZE, SIZE*(i+1)+yOff-15);
            page.drawString("" + numbers[i], SIZE*i+xOff, yOff-15);
            // Draws the grid
            for (int j = 0; j < LENGTH; j++)
            {
                grid[i][j].draw(page);
            }
        }

    }
}
