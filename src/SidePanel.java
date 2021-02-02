import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
/**
 * Write a description of class SidePanel here.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class SidePanel extends JPanel implements ActionListener
{
    private final JButton goButton;
    private final JButton reset;
    private final JButton help;
    private final Primary p;
    public SidePanel(Primary p)
    {
        this.p = p;
        // Sets the font for the buttons
        Font f = new Font("Arial", Font.PLAIN, BattleGrid.SIZE / 2);
        // This button is the button that sets the ships unmovable and put the game into the guessing stage
        goButton = new JButton("Ready!");
        goButton.setEnabled(Primary.getState() == Primary.PLACING_SHIPS);
        goButton.addActionListener(this);
        goButton.setFont(f);
        // This button makes a new game
        reset = new JButton("New Game");
        reset.addActionListener(this);
        reset.setFont(f);
        // This button opens an information frame with help info for the user
        help = new JButton("?");
        help.addActionListener(this);
        help.setFont(f);
        add(goButton);
        add(reset);
        add(help);
        setBackground(Color.WHITE);
    }
    public void actionPerformed(ActionEvent e)
    {
        // Finds which source this event came from
        JButton source = (JButton)e.getSource();
        // If it was the "Ready" button, set the button to be disabled, deploy the enemy ships,
        // set the player ships unmovable and set the turn to player turn
        if (source == goButton)
        {
            goButton.setEnabled(false);
            p.deployEnemyShips();
            p.shipsUnmovable();
            p.playerTurn();
        }
        // If it was the reset button, reset the game
        if (source == reset)
        {
            p.getGame().reset();
        }
        // If the source was the help button, open the help window
        if (source == help)
        {
            openHelp();
        }
    }
    // Opens the information frame to the help message
    public void openHelp()
    {
        new InformationFrame();
    }
    // Sets the "Ready" button enabled when the ships are properly positioned
    public void setReady(boolean b)
    {
        goButton.setEnabled(b);
    }
}
