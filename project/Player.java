import java.awt.*;

public class Player {

    private int x, y;

    public Player() {

    }

    public void drawMe(Graphics g, int x, int y) {
        this.x = x;
        this.y = y;
        g.setColor(new Color(125, 90, 60));
        g.fillOval(x-40, y-20, 80, 40);
        g.setColor(new Color(45, 25, 15));
        g.fillOval(x-24, y-24, 48, 48);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}