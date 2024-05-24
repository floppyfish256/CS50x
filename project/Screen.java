import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Screen extends JPanel implements KeyListener {
    //Window dimensions
    private int screenWidth = 800;
    private int screenHeight = 600;
    //Map dilation of 800x600 from origin at (0, 0)
    private int mapDilation = 4;
    //Translation of origin at (0, 0)
    private int centerX = 0;
    private int centerY = 0;
    //Player coordinates on screen
    private int playerX = screenWidth/2;
    private int playerY = screenHeight/2;
    //Movement from keys
    private double xMovement = 0;
    private double yMovement = 0;
    //Player facing direction
    private int playerRotation = 0;
    //Speed of player
    private double speed = 0;
    //Detects if player is on ship
    private boolean onShip = false;
    //Start screen
    private boolean startScreen = true;
    //End screen
    private boolean endScreen = false;
    //Ship rotation
    private int shipRotation = 0;
    //Line frequency
    private int lFreq = 40;
    //Valid movement
    private int validMovement;
    //Valid counter
    private int validCounter;
    //Colors
    private Color deepBlue = new Color(0, 60, 90);
    private Color waterBlue = new Color(0, 80, 120);
    private Color lineGray = new Color(128, 128, 128, 75);
    private Color noteGray = new Color(128, 128, 128, 150);
    private Color noteText = new Color(255, 255, 255);
    //Fonts
    private Font noteFont = new Font("PT Mono", Font.PLAIN, 30);
    private Font questFont = new Font("PT Mono", Font.BOLD, 20);
    private Font questSmall = new Font("PT Mono", Font.PLAIN, 15);

    //Instantiate player
    Player player = new Player();

    //Instantiate NPC
    NPC npc = new NPC();

    //Instantiate islands
    ArrayList<Island> islands = new ArrayList<Island>();

    //Instantiate ships
    ArrayList<Ship> ships = new ArrayList<Ship>();

    //Instantiate trees
    ArrayList<Tree> trees = new ArrayList<Tree>();

    //Instantiate quests
    ArrayList<Quest> quests = new ArrayList<Quest>();

    //Constructor
    public Screen() {
        //Attributes
		setLayout(null);
		addKeyListener(this);
        setFocusable(true);

        //Add islands
        islands.add(new Island(900, 700, 500, 8));
        islands.add(new Island(2500, 900, 400, 8));
        islands.add(new Island(1800, 1500, 300, 3));

        //Add ships
        ships.add(new Ship(islands.get(0).getX()+islands.get(0).getRadius(), islands.get(0).getY()));

        //Add trees
        for(int i = 0; i < islands.size(); i++) {
            for(int j = 0; j < islands.get(i).getRandX().length; j++) {
                trees.add(new Tree(centerX+islands.get(i).getRandX()[j], centerX+islands.get(i).getRandY()[j]));
            }
        }

        //Add quests
        quests.add(new Quest("Find the lost survivor", Color.BLACK));

        //Start position
        centerX = playerX-islands.get(0).getX();
        centerY = playerY-islands.get(0).getY();
    }

    //Window dimension
    public Dimension getPreferredSize() {
        return new Dimension(screenWidth, screenHeight);
	}

    //PaintComponent
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Water background
        g.setColor(deepBlue);
        g.fillRect(centerX-playerX, centerY-playerY, (mapDilation*screenWidth)+(2*playerX), (mapDilation*screenHeight)+(2*playerY));
        g.setColor(waterBlue);
        g.fillRect(centerX, centerY, mapDilation*screenWidth, mapDilation*screenHeight);

        //Islands
        for(int i = 0; i < islands.size(); i++) {
            islands.get(i).setPos(centerX+islands.get(i).getX(), centerY+islands.get(i).getY());
            islands.get(i).drawMe(g);
        }

        //Lines
        g.setColor(lineGray);
        for(int i = -playerX; i < mapDilation*screenWidth/lFreq+playerX; i++) {
            g.drawLine(centerX+i*lFreq, 0, centerX+i*lFreq, mapDilation*screenHeight);
        }
        for(int i = -playerY; i < mapDilation*screenHeight/lFreq+playerY; i++) {
            g.drawLine(0, centerY+i*lFreq, mapDilation*screenWidth, centerY+i*lFreq);
        }

        //Ships
        if(!onShip) {
            ships.get(0).setPos(centerX+ships.get(0).getX(), centerY+ships.get(0).getY());
        }

        //Rotation
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();

        //Ship rotation
        g2d.rotate(Math.toRadians(shipRotation), centerX+ships.get(0).getX(), centerY+ships.get(0).getY());
        if(!onShip) {
            ships.get(0).drawMe(g);
        }
        g2d.setTransform(old);

        //Player rotation
        g2d.rotate(Math.toRadians(playerRotation), screenWidth/2, screenHeight/2);
        if(onShip) {
            ships.get(0).drawMe(g);
        }
        player.drawMe(g, screenWidth/2, screenHeight/2);
        g2d.setTransform(old);

        //NPC
        npc.drawMe(g, centerX+islands.get(2).getX(), centerY+islands.get(2).getY());

        //Trees
        for(int i = 0; i < islands.size(); i++) {
            for(int j = 0; j < islands.get(i).getRandX().length; j++) {
                trees.get(j).drawMe(g, centerX+islands.get(i).getRandX()[j], centerY+islands.get(i).getRandY()[j]);
            }
        }

        //Interaction note
        g.setColor(noteGray);
        g.fillRoundRect(playerX-24, playerY+24+10, 48, 48, 10, 10);
        g.setColor(noteText);
        g.setFont(noteFont);
        g.drawString("F", playerX-9, playerY+48+19);

        //Quest box
        g.setColor(new Color(128, 128, 128, 85));
        g.fillRect(10, 10, 250, 100);
        g.setColor(Color.white);
        g.setFont(questFont);
        g.drawString("Quest", 30, 40);
        g.setFont(questSmall);
        for(int i = 0; i < quests.size(); i++) {
            g.drawString(quests.get(i).getText(), 30, i*25+70);
        }

        //Start sreen
        if(startScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.WHITE);
            g.setFont(questFont);
            g.drawString("Island Adventure", 310, 100);
            g.setFont(questSmall);
            g.drawString("You are on a rescue mission to find a lost survivor.", 180, 300);
            g.drawString("Use the arrow keys to move around, and F to interact.", 180, 400);
            g.drawString("Press space to continue...", 290, 500);
        }

        //End screen
        if(endScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 800, 600);
            g.setColor(Color.WHITE);
            g.drawString("You bring the survivor back to safety and he thanks you for your help.", 100, 300);
            g.drawString("At last, you feel joy for helping this man.", 100, 400);
            g.drawString("YOU WIN!", 100, 500);
        }
        
        //Repaint
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        //Left key
        if(e.getKeyCode()== 37) {
            xMovement=-1;
        }
        //Up key
        else if(e.getKeyCode() == 38) {
            yMovement=-1;
        }
        //Right key
        else if(e.getKeyCode() == 39) {
            xMovement=1;
        }
        //Down key
        else if(e.getKeyCode() == 40) {
            yMovement=1;
        }
        else if(e.getKeyCode() == 32) {
            startScreen = false;
        }
        else if(e.getKeyCode() == 192) {
            endScreen = true;
        }
        else {
            return;
        }
        setPlayerRotation();
    }

    public void keyReleased(KeyEvent e) {
        //Left key
        if(e.getKeyCode()== 37) {
            xMovement=0;
        }
        //Up key
        if(e.getKeyCode() == 38) {
            yMovement=0;
        }
        //Right key
        if(e.getKeyCode() == 39) {
            xMovement=0;
        }
        //Down key
        if(e.getKeyCode() == 40) {
            yMovement=0;
        }
        //F key
        if(e.getKeyCode() == 70) {
            interact();
        }
        //Retain direction
        if(!(xMovement == 0 && yMovement == 0)) {
            setPlayerRotation();
        }
    }
    
    public void keyTyped(KeyEvent e) {
    }

    //Press F to activate, pick up, use, get on, etc.
    public void interact() {
        //Getting on the ship
        if(!onShip && Point2D.distance(ships.get(0).getCX(), ships.get(0).getCY(), playerX, playerY) <= 100) {
            centerX = playerX-ships.get(0).getX();
            centerY = playerY-ships.get(0).getY();
            ships.get(0).setPos(playerX, playerY);
            onShip = true;
        }
        //Interacting with NPC
        else if(!onShip && Point2D.distance(npc.getX(), npc.getY(), playerX, playerY) <= 100) {
            endScreen = true;
        }

        //Getting off the ship
        else if(onShip) {
            validCounter = 0;
            for(int i = 0; i < islands.size(); i++) {
                if(Point2D.distance(islands.get(i).getCX(), islands.get(i).getCY(), playerX, playerY) <= islands.get(i).getRadius()+20) {
                    validCounter++;
                }
            }
            if(validCounter == 1) {
                ships.get(0).setOriPos(playerX-centerX, playerY-centerY);
                shipRotation = playerRotation;
                onShip = false;
            }
        }
    }

    //Setting the player rotation corresponding to arrow keys
    public void setPlayerRotation() {
        int x = (int)Math.asin((double)xMovement);
        int y = -(int)Math.asin((double)yMovement);
        playerRotation = (int)(theta(0, 0, x, y));
    }

    //Derive angle from points
    public double theta(int cx, int cy, int x, int y) {
        double angle = Math.toDegrees(Math.atan2(cy-y, x+cx)) + 90;
        return (angle+360)%360;
    }

    //Checks to see if the player can move
    public boolean canMove(double speed) {
        //Multiply the movement and speed to check for valid position movement
        if(!onShip) {
            int islandValidMovement = 0;
            int treeValidMovement = 0;
            boolean inRangeNPC = false;
            for(int i = 0; i < islands.size(); i++) {
                if(Point2D.distance(islands.get(i).getX(), islands.get(i).getY(), -centerX+playerX+xMovement*speed, -centerY+playerY+yMovement*speed) <= islands.get(i).getRadius()+20) {
                    islandValidMovement++;
                    break;
                }
            }
            if(!onShip && Point2D.distance(npc.getX(), npc.getY(), playerX+xMovement*speed, playerY+yMovement*speed) <= 80) {
                inRangeNPC = true;
            }
            for(int i = 0; i < trees.size(); i++) {
                //Bug Fix: Stuck under tree on spawn
                if(Point2D.distance(trees.get(i).getX(), trees.get(i).getY(), -centerX+playerX+xMovement*speed, -centerY+playerY+yMovement*speed) <= trees.get(i).getTrunkRadius()+15) {
                    centerX-=20;
                    centerY-=20;
                }
                if(Point2D.distance(trees.get(i).getX(), trees.get(i).getY(), -centerX+playerX+xMovement*speed, -centerY+playerY+yMovement*speed) >= trees.get(i).getTrunkRadius()+22) {
                    treeValidMovement++;
                }
            }
            if(islandValidMovement == 1 && treeValidMovement == trees.size() && !inRangeNPC) {
                return true;
            }
        }
        else if(onShip) {
            validMovement = 0;
            for(int i = 0; i < islands.size(); i++) {
                if(Point2D.distance(islands.get(i).getX(), islands.get(i).getY(), -centerX+playerX+xMovement*speed, -centerY+playerY+yMovement*speed) >= islands.get(i).getRadius()) {
                    validMovement++;
                }
            }
            if(validMovement == islands.size()) {
                return true;
            }
        }
        return false;
    }

    //Animates the game
    public void animate() {
        while(true) {
            //Testing

            //Speed
            speed = 4*Math.cos(Math.toRadians(playerRotation%90));
            if(canMove(speed) && !startScreen) {
                centerX -= Math.round(xMovement*speed);
                centerY -= Math.round(yMovement*speed);
            }

            //Note detection
            if(!onShip && Point2D.distance(ships.get(0).getCX(), ships.get(0).getCY(), playerX, playerY) <= 100
            || !onShip && Point2D.distance(npc.getX(), npc.getY(), playerX, playerY) <= 100) {
                noteGray = new Color(128, 128, 128, 150);
                noteText = new Color(255, 255, 255);
            }
            else {
                noteGray = new Color(128, 128, 128, 0);
                noteText = new Color(0, 0, 0, 0);
            }

            //Bounds
            if(centerX > playerX) {
                centerX = playerX;
            }
            if(centerY > playerY) {
                centerY = playerY;
            }
            if(centerX <= mapDilation*-screenWidth + playerX) {
                centerX = mapDilation*-screenWidth + playerX;
            }
            if(centerY <= mapDilation*-screenHeight + playerY) {
                centerY = mapDilation*-screenHeight + playerY;
            }

            try {
			    Thread.sleep(10);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
        }
    }
}