import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;
/**
 * InformationFrame creates a frame to display a message, such as help, within a scrollpane
 * at a font size that is readable, unlike JOptionPane on some computers.
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class InformationFrame extends JFrame
{
    private JPanel info;
    private JScrollPane textPlace;
    private JTextArea text;
    private Font legibleFont;
    private File file;
    /**
     * Constructor for objects of class InformationFrame
     */
    public InformationFrame()
    {
        // Sets up the JFrame, Panel, ScrollPane and TextArea within the ScrollPane.
        super("Info");
        info = new JPanel();
        text = new JTextArea(15, 30);
        text.setEditable(false);
        file = new File("helpInfo.txt");
        Scanner scan = new Scanner(System.in);
        try
        {
            scan = new Scanner(file);
        }
        catch (Exception ex){}
        String message = "";
        while (scan.hasNext())
        {
            message += scan.nextLine() + "\n";
        }
        text.setText(message);
        textPlace = new JScrollPane(text);
        textPlace.setPreferredSize(new Dimension(BattleGrid.SIZE*17, BattleGrid.SIZE*10));
        legibleFont = new Font("Arial", Font.PLAIN, (int)(BattleGrid.SIZE*0.5));
        text.setFont(legibleFont);
        info.add(textPlace);
        getContentPane().add(info);
        pack();
        setVisible(true);
    }
}
