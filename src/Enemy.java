import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;
/**
 * The Enemy...
 * AKA EVIL MASTERMIND...MUAHAHAHAHA
 * | |   ^   | |   ^   | |   ^   | |   ^   | |   ^   | |   ^
 * |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\
 * | | /   \ | | /   \ | | /   \ | | /   \ | | /   \ | | /   \
 *
 * | |   ^   | |   ^   | |   ^   | |   ^   | |   ^   | |   ^
 * |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\
 * | | /   \ | | /   \ | | /   \ | | /   \ | | /   \ | | /   \
 *
 * | |   ^   | |   ^   | |   ^   | |   ^   | |   ^   | |   ^
 * |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\
 * | | /   \ | | /   \ | | /   \ | | /   \ | | /   \ | | /   \
 *
 * | |   ^   | |   ^   | |   ^   | |   ^   | |   ^   | |   ^
 * |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\
 * | | /   \ | | /   \ | | /   \ | | /   \ | | /   \ | | /   \
 *
 * | |   ^   | |   ^   | |   ^   | |   ^   | |   ^   | |   ^
 * |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\  |-|  /_\
 * | | /   \ | | /   \ | | /   \ | | /   \ | | /   \ | | /   \
 *
 * @author (Nicholas Sullivan)
 * @version (May 2016)
 */
public class Enemy
{
    private final EnemyGrid e;
    private final PlayerGrid p;
    private final Random gen;
    // These arrayLists are for the logic of the AI
    private final ArrayList<Point> enemyShips;
    private final ArrayList<Point> playerShips;
    private final ArrayList<Point> hitPoints;
    private final ArrayList<Point> pointsToGuess;
    private final ArrayList<Point> highestPriority;
    private final Point[][] possiblePoints;
    private Point guessPoint;
    private int guess;
    private int numNotSunkHitPoints;
    private final Primary panel;
    public Enemy(Primary panel, EnemyGrid e, PlayerGrid p)
    {
        this.e = e;
        this.p = p;
        // Generator for randomly picking points when no obvious ones are available
        gen = new Random();
        // The points for picking the enemy ship locations
        enemyShips = new ArrayList<>();
        // The possibilities for the player ships
        playerShips = new ArrayList<>();
        // The points that have been hit
        hitPoints = new ArrayList<>();
        // The priority points to guess (around places known to be a hit)
        pointsToGuess = new ArrayList<>();
        // The highest priority points to guess (points along a line defined by two hit points)
        highestPriority = new ArrayList<>();
        // This array contains all of the possible points, for reference
        possiblePoints = new Point[BattleGrid.LENGTH][BattleGrid.LENGTH];
        // Adds every point to the possible player ship points
        for (int i = 0; i < BattleGrid.LENGTH; i++)
        {
            for (int j = 0; j < BattleGrid.LENGTH; j++)
            {
                possiblePoints[i][j] = new Point(i, j);
                playerShips.add(possiblePoints[i][j]);
            }
        }
        this.panel = panel;
        // The number points that have been hit but haven't sunk a ship
        numNotSunkHitPoints = 0;
    }
    // Deploys each of the enemy ships
    public void deployShips()
    {
        for (Battleship b : e.getShips())
        {
            deploySingleShip(b);
        }
    }
    // Deploys a single enemy ship
    public void deploySingleShip(Battleship ship)
    {
        // Randomly chooses the direction
        int dir = gen.nextInt(2);
        // row and col are the amounts be which the position increments to choose the ship both horizontally and vertically
        // Essentially they determine which direction the ship goes
        int row = 0, col = 0;
        // clears the possible locations for the enemy ships
        enemyShips.clear();
        if (dir == 0)
        {
            row = 1;
        }
        else
        {
            col = 1;
        }
        // Checks each possible point to see if it is possible for a ship to be there.
        for (int i = 0; i < BattleGrid.LENGTH; i++)
        {
            for (int j = 0; j < BattleGrid.LENGTH; j++)
            {
                boolean b = true;
                // For each position that the ship would occupy, test to see if it is already occupied or is out of bounds
                for (int k = 0; k < ship.getSize(); k++)
                {
                    if ((i + (row * k) < BattleGrid.LENGTH) && (j + (col * k) < BattleGrid.LENGTH))
                    {
                        // b is only remains true if b is true and the position is not covered
                        b = b && !e.getPosition(i + (row * k), j + (col * k)).isCovered();
                    }
                    else
                    {
                        // if it is out of bounds, then it is not a good possibility
                        b = false;
                    }
                }
                // Only if the position is valid is it added to the possible enemy ship positions
                if (b)
                {
                    enemyShips.add(new Point(i, j));
                }
            }
        }
        // Sets a random possibility as its position
        int num = gen.nextInt(enemyShips.size());
        for (int k = 0; k < ship.getSize(); k++)
        {
            e.getPosition(enemyShips.get(num).x + (row * k), enemyShips.get(num).y + (col * k)).cover(ship);
        }
    }
    // This makes the move for the computer
    public void makeMove()
    {
        // The number of possible moves left
        int len = playerShips.size();
        if (len > 0)
        {
            // If there are highest priority moves to make, guess a random point from that arrayList and remove that point
            if (highestPriority.size() > 0)
            {
                len = highestPriority.size();
                guess = gen.nextInt(len);
                guessPoint = possiblePoints[highestPriority.get(guess).x][highestPriority.get(guess).y];
                placeGuess(highestPriority);
                highestPriority.remove(guessPoint);
            }
            // If there are medium priority moves to make, guess a random point from that arrayList and remove that point
            else if (pointsToGuess.size() > 0)
            {
                len = pointsToGuess.size();
                guess = gen.nextInt(len);
                guessPoint = possiblePoints[pointsToGuess.get(guess).x][pointsToGuess.get(guess).y];
                placeGuess(pointsToGuess);
                pointsToGuess.remove(guessPoint);
            }
            // Else, just guess a random point from those remaining
            else
            {
                guess = gen.nextInt(len);
                guessPoint = possiblePoints[playerShips.get(guess).x][playerShips.get(guess).y];
                placeGuess(playerShips);
            }
            // Remove that point from the points remaining
            playerShips.remove(guessPoint);
        }
    }
    public void placeGuess(ArrayList<Point> pointSet)
    {
        // status is what the result of the guess is. 0 is a miss, 1 is a hit but not sunk, and if sunk, it is the length of that ship
        int status = p.guess(pointSet.get(guess).x, pointSet.get(guess).y);
        // A Hit
        if (status >= 1)
        {
            // Adds this point to the points that have been hit and increments the number of hits that have not sunk a ship
            hitPoints.add(guessPoint);
            numNotSunkHitPoints++;
            // If a ship has been sunk, remove the number of points that ship covers from the
            // number of hits that have not sunk a ship (Since they have now)
            if (status > 1)
            {
                numNotSunkHitPoints -= status;
            }
            // If any of the surrounding points has not been guessed yet, put that point in the medium priority list
            if (playerShips.contains(new Point(guessPoint.x, guessPoint.y+1)))
            {
                pointsToGuess.add(possiblePoints[guessPoint.x][guessPoint.y+1]);
            }
            if (playerShips.contains(new Point(guessPoint.x, guessPoint.y-1)))
            {
                pointsToGuess.add(possiblePoints[guessPoint.x][guessPoint.y-1]);
            }
            if (playerShips.contains(new Point(guessPoint.x+1, guessPoint.y)))
            {
                pointsToGuess.add(possiblePoints[guessPoint.x+1][guessPoint.y]);
            }
            if (playerShips.contains(new Point(guessPoint.x-1, guessPoint.y)))
            {
                pointsToGuess.add(possiblePoints[guessPoint.x-1][guessPoint.y]);
            }
            // If any of the surrounding points have been hit before, check if they have not been hit
            // and if they haven't, put them in the highest priority list
            if (hitPoints.contains(new Point(guessPoint.x, guessPoint.y+1)))
            {
                checkLine(guessPoint, new Point(guessPoint.x, guessPoint.y+1));
            }
            if (hitPoints.contains(new Point(guessPoint.x, guessPoint.y-1)))
            {
                checkLine(guessPoint, new Point(guessPoint.x, guessPoint.y-1));
            }
            if (hitPoints.contains(new Point(guessPoint.x+1, guessPoint.y)))
            {
                checkLine(guessPoint, new Point(guessPoint.x+1, guessPoint.y));
            }
            if (hitPoints.contains(new Point(guessPoint.x-1, guessPoint.y)))
            {
                checkLine(guessPoint, new Point(guessPoint.x-1, guessPoint.y));
            }
            // If there are no hits that have not contributed to sinking a ship, then clear the priority lists.
            // This relieves the AI of making unnecessary moves to check the positions around the hits
            if (numNotSunkHitPoints == 0)
            {
                highestPriority.clear();
                pointsToGuess.clear();
            }
        }
        // A Miss: Changes turn
        else
        {
            panel.changeTurn();
        }
    }
    // checkLine accepts two points and first verifies if they are adjacent,
    // and then if the points in its line have not been guessed, add them to the highest priority
    private void checkLine(Point p1, Point p2)
    {
        // Verifies they are adjacent
        if (Math.pow((p1.x-p2.x), 2) + Math.pow((p1.y-p2.y), 2) == 1)
        {
            // If the points in its line have not been guessed, add them to the highest priority
            if (playerShips.contains(new Point(2*p1.x-p2.x, 2*p1.y-p2.y)))
            {
                highestPriority.add(possiblePoints[2*p1.x-p2.x][2*p1.y-p2.y]);
            }
            if (playerShips.contains(new Point(2*p2.x-p1.x, 2*p2.y-p1.y)))
            {
                highestPriority.add(possiblePoints[2*p2.x-p1.x][2*p2.y-p1.y]);
            }
        }
    }
}
