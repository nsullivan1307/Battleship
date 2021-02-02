import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
/**
 * Write a description of class Position here.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class Position implements ActionListener
{
    private final int x;
    private final int y;
    private boolean covered;
    // The state of the position is represented by a color.
    // It can either be hit, miss or neither (undecided)
    private Color state;
    public final Color WATER = new Color((int)(0.25*255), (int)(0.778*255), 255);
    public final Color UNDECIDED = Color.GRAY, HIT = Color.RED, MISS = Color.WHITE;
    public static final int OFFSET = BattleGrid.SIZE /3;
    private Battleship coverShip;
    private final BattleGrid grid;
    private final Timer time;
    public Position(int x, int y, BattleGrid grid)
    {
        // Sets the drawn x and y position and the grid to which it belongs
        this.x = x;
        this.y = y;
        this.grid = grid;
        // It is not covered by default and it has not been guessed
        covered = false;
        state = UNDECIDED;
        // Creates a timer so that the player can see a message if this position is hit
        time = new Timer(750, this);
    }
    public Color getState()
    {
        return state;
    }
    public void cover(Battleship ship)
    {
        covered = true;
        coverShip = ship;
    }
    // Returns if it is covered by a ship
    public boolean isCovered()
    {
        return covered;
    }
    public void miss()
    {
        state = MISS;
    }
    public void hit()
    {
        state = HIT;
    }
    // If the position is hit, it is told to the ship.
    public int reportHit()
    {
        grid.setMessage("HIT");
        // The timer is started so that the player can see that a ship has been hit
        time.start();
        // Returns 1 if it is just a hit, the length of the ship if the ship is sunk
        return coverShip.hit();
    }
    public void draw(Graphics page)
    {
        // If the game is in the placing ships state, draw the blue water and the black square
        // Always draw the peg circle. In other game stages, only the peg circle needs to be drawn.
        if (Primary.getState() == Primary.PLACING_SHIPS)
        {
            drawSquare(page);
        }
        drawHitStatus(page);
    }
    // Draws the square as part of the grid
    private void drawSquare(Graphics page)
    {
        page.setColor(WATER);
        page.fillRect(x, y, BattleGrid.SIZE, BattleGrid.SIZE);
        page.setColor(Color.BLACK);
        page.drawRect(x, y, BattleGrid.SIZE, BattleGrid.SIZE);
    }
    // Draws the circle that represents the hit or miss peg
    private void drawHitStatus(Graphics page)
    {
        page.setColor(state);
        page.fillOval(OFFSET+x, OFFSET+y, BattleGrid.SIZE-2*OFFSET, BattleGrid.SIZE-2*OFFSET);
    }
    // Changes the turn once the player has time to see the hit message above the grid
    public void actionPerformed(ActionEvent event)
    {
        grid.getPrimary().changeTurn();
        time.stop();
    }
}
