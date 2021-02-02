import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Write a description of class Battleship here.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class Battleship extends DragBox implements KeyListener
{
    // The state of the Battleship (Whether it is validly placed or not)
    private Color state;
    private final Color READY = Color.GREEN, OVERLAP = Color.RED, OUT = Color.GRAY;
    private int length;
    private BattleGrid b;
    private boolean sunk;
    private Type ship;
    private int numberHit = 0;
    private boolean visible;
    // This enumerated type defines the name and lengths of the different types of ships
    public enum Type
    {
        AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
        BATTLESHIP(4, "Battleship"),
        SUBMARINE(3, "Submarine"),
        CRUISER(3, "Cruiser"),
        DESTROYER(2, "Destroyer");
        private int size;
        private String name;
        Type(int size, String name)
        {
            this.size = size;
            this.name = name;
        }
        public int getSize()
        {
            return size;
        }
        public String getName()
        {
            return name;
        }
    }
    public Battleship(Type ship, int x, int y, BattleGrid grid)
    {
        // Defines the type, length and position of the ship
        this.ship = ship;
        length = ship.getSize();
        setPosition(x, y);
        setSize(new Dimension(BattleGrid.SIZE*length, BattleGrid.SIZE));
        b = grid;
        // Sets the default state to out of the grid.
        state = OUT;
        // Default: Enemy grid states
        // sunk: whether the ship has been sunk
        sunk = false;
        // visible: whether the ship is visible
        visible = false;
    }
    // In the player grid state, it is visible, and there are key listeners added to flip the ship orientation
    public void playerState()
    {
        visible = true;
        b.addKeyListener(this);
        b.setFocusable(true);
        addTo(b);
    }
    public int getSize()
    {
        return length;
    }
    public String getName()
    {
        return ship.getName();
    }
    // If the ship has been hit, increase the number of points of this ship that are hit
    // If the ship is sunk, return the length of that ship, otherwise, return 1.
    public int hit()
    {
        numberHit++;
        if (numberHit == length)
        {
            sunk = true;
            b.sink(this);
            return length;
        }
        return 1;
    }
    public boolean isReady()
    {
        return (state == READY);
    }
    // Tests whether the battleship is within the grid
    public boolean isInGrid()
    {
        return (x < BattleGrid.SIZE*(BattleGrid.LENGTH+1.5)-w
                && x >= BattleGrid.SIZE*0.5
                && y < BattleGrid.SIZE*(BattleGrid.LENGTH+3.5)-h
                && y >= BattleGrid.SIZE*2.5);
    }
    // Sets the ship to unmovable when it is in the guessing stage of the game
    // Tells each position it is covering to "covered" state.
    public void setUnmovable()
    {
        movable = false;
        if (w == BattleGrid.SIZE)
        {
            for (int i = 0; i < length; i++)
            {
                b.getPosition(((int)x/BattleGrid.SIZE)-1, ((int)y/BattleGrid.SIZE)+i-3).cover(this);
            }
        }
        else
        {
            for (int i = 0; i < length; i++)
            {
                b.getPosition(((int)x/BattleGrid.SIZE)+i-1, ((int)y/BattleGrid.SIZE)-3).cover(this);
            }
        }
    }
    // Draws the battleship appropriately depending on whether it is visible, movable, in the grid,
    // intersecting another ship
    public void draw(Graphics page, Battleship[] ships)
    {
        // Only draw it if it is visible
        if (visible)
        {
            // If it is movable, then define which state it is in.
            if (movable)
            {
                // If it is in the Grid, then firgure out if it is intersecting another ship
                // Otherwise, set the state to out of bounds
                if (isInGrid())
                {
                    boolean intersecting = false;
                    for (Battleship bat : ships)
                    {
                        if (bat != this)
                        {
                            intersecting = intersecting || intersects(bat);
                        }
                    }
                    // If the ship is intersecting with any of the other ships, set the state to overlap
                    if (intersecting)
                    {
                        state = OVERLAP;
                    }
                    // Otherwise, set the state to ready and reposition the ship to fit in the grid
                    else
                    {
                        state = READY;
                        if (!sel)
                        {
                            x = BattleGrid.SIZE*((int)(x+(BattleGrid.SIZE/2))/BattleGrid.SIZE);
                            y = BattleGrid.SIZE*((int)(y+(BattleGrid.SIZE/2))/BattleGrid.SIZE);
                        }
                    }
                }
                else
                {
                    state = OUT;
                }
            }
            // Draw the ship as an elipse in its appropriate colour and
            // draw the peg holes in the correct direction
            page.setColor(state);
            page.fillOval(x, y, w, h);
            page.setColor(Color.GRAY);
            if (h == BattleGrid.SIZE)
            {
                for (int i = 0; i < length; i++)
                {
                    page.fillOval(
                            x+i*BattleGrid.SIZE+Position.OFFSET,
                            y+Position.OFFSET,
                            BattleGrid.SIZE-2*Position.OFFSET,
                            BattleGrid.SIZE-2*Position.OFFSET);
                }
            }
            else
            {
                for (int i = 0; i < length; i++)
                {
                    page.fillOval(
                            x+Position.OFFSET,
                            y+i*BattleGrid.SIZE+Position.OFFSET,
                            BattleGrid.SIZE-2*Position.OFFSET,
                            BattleGrid.SIZE-2*Position.OFFSET);
                }
            }
        }
    }
    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    // If a key is pressed and the ship is being dragged (sel), change the orientation of the ship
    public void keyTyped(KeyEvent e)
    {
        if (sel)
        {
            int temp = h;
            h = w;
            w = temp;
            p.repaint();
        }
    }
}
