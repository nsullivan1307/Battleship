import javax.swing.JFrame;
/**
 * BattleFrame is the JFrame for the game. It contains a reset method that returns the JFrame 
 * back to its initial state.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class BattleFrame extends JFrame
{
    public BattleFrame()
    {
        // sets the title of the JFrame to Battleship
        super("Nicholas Sullivan's Battleship Game");
        // The reset method sets up the contents of the JFrame
        reset();
    }
    public void reset()
    {
        // The program will terminate when the close button is pressed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Everything is removed from Content pane and the Primary panel is added to the JFrame
        Primary main = new Primary(this);
        getContentPane().removeAll();
        getContentPane().add(main);
        pack();
        setVisible(true);
        setResizable(false);
    }
}