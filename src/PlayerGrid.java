import java.awt.*;
/**
 * This extends the BattleGrid, and is the grid for the player.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class PlayerGrid extends BattleGrid
{
    public static int xOff = 1, yOff = 3;
    private Primary panel;
    /**
     * Constructor for objects of class PlayerGrid
     */
    public PlayerGrid(Primary p)
    {
        // Defines the xOff and yOff values for the grid
        super(xOff*SIZE, yOff*SIZE, p);
        // The panel
        this.panel = p;
        // Specifies that the battleships are for the player grid.
        for (Battleship s : ships)
        {
            s.playerState();
        }
        // Specifies the width and height of the panel and sets the message
        setPreferredSize(new Dimension(SIZE*(LENGTH+3), SIZE*(LENGTH+7+yOff)));
        setMessage("Place your Ships");
    }

    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        // readyStatus is the boolean that determines if the ships are ready
        boolean readyStatus = true;
        // readyStatus will only be true if all the ships are ready
        for (Battleship s : ships)
        {
            s.draw(page, ships);
            // if readyStatus is false, it will stay false.
            // if readyStatus is true, and the ship is not ready, it will become false
            // if readyStatus is true and the ship is ready, it will stay true
            readyStatus = readyStatus && s.isReady();
        }
        if (Primary.getState() == Primary.PLACING_SHIPS)
        {
            // This indirectly sets the "Ready!" button to be enabled or disabled
            panel.setReady(readyStatus);
            if (readyStatus == true)
            {
                // If all the ships are ready, tell the player to press the ready button
                setMessage("Press the \"Ready!\" button");
            }
            else
            {
                // If not, tell the player to place ships
                setMessage("Place your Ships");
            }
        }
        page.setColor(Color.black);
        page.drawString("Your Grid", SIZE*3, SIZE*(LENGTH+8+yOff));
    }
    // Guesses a position and returns whether it is hit or not
    public int guess(int x, int y)
    {
        // If the position is covered by a ship
        if (getPosition(x, y).isCovered())
        {
            // Set that position to be hit
            getPosition(x, y).hit();
            // Draw the hit mark with the current graphics for the JPanel
            getPosition(x, y).draw(this.getGraphics());
            // Reports the hit to the battleship in question
            return getPosition(x, y).reportHit();
        }
        else
        {
            // Set that position to miss and draw the miss mark
            getPosition(x, y).miss();
            getPosition(x, y).draw(this.getGraphics());
            return 0;
        }
    }
    public void setUnmovable()
    {
        // Sets each of the ships to become unmovable
        for (Battleship s : ships)
        {
            s.setUnmovable();
        }
        setMessage("");
    }
}
