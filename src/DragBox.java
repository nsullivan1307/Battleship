import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
/**
 * This is an abstract rectangle that can be 'dragged' on the panel. In order to drag,
 * you must click on the rectangle, put your mouse to where you want to put it, and then click
 * again to release the rectangle
 *
 * @author (Nicholas Sullivan)
 * @version (Jan 2016)
 */
public class DragBox
{
    // this is the x and y positions, the width and the height
    protected int x, y, w, h;
    // The panel that the DragBox is put into.
    protected JPanel p;
    // The Point where the mouse currently is (defined later)
    private Point point1;
    // x and y positions of the mouse, and the x and y positions relative to the top left corner
    private int xp, yp, xRel, yRel;
    protected boolean sel, movable;
    public DragBox()
    {
        sel = false;
        movable = true;
    }

    public void setPosition(int x1, int y1)
    {
        // Sets the position
        x = x1;
        y = y1;
    }
    public void setSize(Dimension d1)
    {
        // Sets the size of the rectangle
        w = (int)d1.getWidth();
        h = (int)d1.getHeight();
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getW()
    {
        return w;
    }
    public int getH()
    {
        return h;
    }
    public void addTo(JPanel panel)
    {
        // sets which panel it should be in.
        p = panel;
        // adds a mouse listener to the panel
        p.addMouseMotionListener(new PositionListener());
        p.addMouseListener(new ClickListener());
    }
    // Determines if one DragBox intersects this one
    public boolean intersects(DragBox d)
    {
        boolean b = (x < d.getX() + d.getW()) && (d.getX() < x + w);
        b = b && (y < d.getY() + d.getH()) && (d.getY() < y + h);
        return b;
    }
    private class ClickListener implements MouseListener
    {
        // When the mouse is moved
        public void mousePressed(MouseEvent event)
        {
            if (movable)
            {
                // get the mouse position
                point1 = event.getPoint();
                xp = point1.x;
                yp = point1.y;
                if (xp > x && xp < x + w && yp > y && yp < y + h)
                {
                    sel = true;
                    xRel = xp-x;
                    yRel = yp-y;
                }
                p.requestFocus();
            }
        }
        public void mouseClicked(MouseEvent event) {}
        public void mouseReleased(MouseEvent event)
        {
            if (movable)
            {
                sel = false;
                p.repaint();
            }
        }
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
    }
    // This is the mouse motion listener
    private class PositionListener implements MouseMotionListener
    {
        // When the mouse is moved
        public void mouseMoved(MouseEvent event){}
        public void mouseDragged(MouseEvent event)
        {
            if (sel)
            {
                // get the mouse position
                point1 = event.getPoint();
                xp = point1.x;
                yp = point1.y;
                // generate the x and y location from the x and y mouse position and the relative x and y
                x = xp - xRel;
                y = yp - yRel;
                // repaint the panel
                p.repaint();
            }
        }
    }
}
