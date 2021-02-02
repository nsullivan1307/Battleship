import java.awt.*;
import java.awt.event.*;
/**
 *
 * Enemy grid is the grid that the enemy places their ships on. The player guesses which 
 * positions are occupied by a ship.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class EnemyGrid extends BattleGrid implements MouseListener
{
    public static final int xOff = 2, yOff = 3;
    private final Primary panel;
    /**
     * Constructor for objects of class PlayerGrid
     */
    public EnemyGrid(Primary panel)
    {
        super(xOff*SIZE, yOff*SIZE, panel);
        // Adds a mouseListener to this JPanel
        addMouseListener(this);
        this.panel = panel;
    }
    public Battleship[] getShips()
    {
        return ships;
    }
    public void mouseClicked(MouseEvent e)
    {
        // When the mouse is clicked in the JPanel
        // If it is the player's turn
        if (Primary.getState() == Primary.PLAYER_TURN)
        {
            // Gets the point that was clicked
            Point p = e.getPoint();
            // Makes sure that the mouse was clicked within the grid
            if (p.x < SIZE*(LENGTH+xOff) &&  p.x> SIZE*xOff && p.y < SIZE*(LENGTH+yOff) && p.y > SIZE*yOff)
            {
                // The position affected is determined by dividing the point x and y values by 
                // the grid square size, and then subtracting the xOff and yOff values.
                Position pos = getPosition((p.x /SIZE)-xOff, (p.y /SIZE)-yOff);
                // If the position has not been guessed yet
                if (pos.getState() == pos.UNDECIDED)
                {
                    // If it is covered by a ship
                    if (pos.isCovered())
                    {
                        // Set the position as hit, draw it, and report the hit to the ship 
                        // it is covered by
                        pos.hit();
                        pos.draw(this.getGraphics());
                        pos.reportHit();
                    }
                    else // If it is not covered by a ship
                    {
                        // Sets the position to missed, draws it and changes the turn to 
                        // the enemy turn
                        pos.miss();
                        pos.draw(this.getGraphics());
                        panel.changeTurn();
                    }
                }
            }
        }
    }

    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    // Paints the grid
    public void paintComponent(Graphics page)
    {
        super.paintComponent(page);
        page.setColor(Color.black);
        page.drawString("Enemy's Grid", SIZE*5, SIZE*(LENGTH+8+yOff));
    }
}